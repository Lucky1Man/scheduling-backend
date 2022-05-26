package com.online.scheduling.registration.implementations;

import com.online.scheduling.email.IEmailService;
import com.online.scheduling.exceptions.InvalidConfirmationTokenException;
import com.online.scheduling.exceptions.UnableEmailException;
import com.online.scheduling.registration.models.RegistrationRequest;
import com.online.scheduling.exceptions.RegistrationInvalidDataException;
import com.online.scheduling.registration.interfaces.IRegistrationRequestService;
import com.online.scheduling.registration.interfaces.IRegistrationRequestValidator;

import com.online.scheduling.registration.tokens.ConfirmationToken;
import com.online.scheduling.registration.tokens.ConfirmationTokenService;
import com.online.scheduling.user.User;
import com.online.scheduling.exceptions.EmailAlreadyRegisteredException;
import com.online.scheduling.user.implementations.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service("RegistrationRequestService")
public class RegistrationRequestServiceImpl implements IRegistrationRequestService {
    private final IRegistrationRequestValidator requestValidator;
    private final RegistrationRequestConfig registrationRequestConfig;
    private final UserService userService;
    private final PasswordEncoder encoder;
    private final IEmailService emailSender;
    private final ConfirmationTokenService confirmationTokenService;
    @Autowired
    public RegistrationRequestServiceImpl(
            @Qualifier("RegistrationValidator")
            IRegistrationRequestValidator requestValidator,
            @Qualifier("RegistrationConfig")
            RegistrationRequestConfig registrationRequestConfig,
            @Qualifier("UserService")
            UserService userService,
            @Qualifier("UsedPasswordEncoder")
            PasswordEncoder encoder,
            @Qualifier("EmailService")
            IEmailService emailSender,
            ConfirmationTokenService confirmationTokenService) {
        this.requestValidator = requestValidator;
        this.registrationRequestConfig = registrationRequestConfig;
        this.userService = userService;
        this.encoder = encoder;
        this.emailSender = emailSender;
        this.confirmationTokenService = confirmationTokenService;
    }

    @Override
    public void register(RegistrationRequest request)
            throws RegistrationInvalidDataException, EmailAlreadyRegisteredException {
        if(requestValidator.validateAll(request, registrationRequestConfig.getValidators())) {

            String clientEmail = request.getEmail();
            User newUser = new User(
                    request.getFirstName(),
                    request.getLastName(),
                    clientEmail,
                    encoder.encode(request.getPassword())
            );
            userService.saveUser(newUser);
            try {
                emailSender.send(
                        clientEmail,
                        registrationRequestConfig.getConfirmationEmailSubject(),
                        buildEmail(
                                clientEmail,
                                registrationRequestConfig.getConfirmationLink() + createToken(newUser),
                                registrationRequestConfig.getConfirmationTokenExpirationTime().toString()
                        )
                );
            } catch (MessagingException e) {
                throw new UnableEmailException(e.getMessage());
            }
        }
    }

    @Override
    public void confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token)
                .orElseThrow(() -> new InvalidConfirmationTokenException(String.format("Token: %s not found", token)));

        if(confirmationToken.getConfirmedAt() != null){
            throw new InvalidConfirmationTokenException(String.format("Token: %s already confirmed", token));
        }

        if(confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new InvalidConfirmationTokenException(String.format("Token: %s expired", token));
        }
        confirmationTokenService.setConfirmedAt(token);

        userService.enableAppUser(confirmationToken.getUser().getEmail());
    }

    public String createToken(User user){
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(registrationRequestConfig.getConfirmationTokenExpirationTime()),
                user
                );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }

    private String buildEmail(String clientEmail, String link, String expirationTime) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + clientEmail + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in " + expirationTime +" minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }
}
