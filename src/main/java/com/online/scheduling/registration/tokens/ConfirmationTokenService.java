package com.online.scheduling.registration.tokens;

import com.online.scheduling.exceptions.InvalidConfirmationTokenException;
import com.online.scheduling.registration.models.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    public ConfirmationTokenService(ConfirmationTokenRepository confirmationTokenRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    public ConfirmationToken saveConfirmationToken(ConfirmationToken token){
        return confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token){
        return confirmationTokenRepository.findByToken(token);
    }

    @Transactional
    public void setConfirmedAt(String token) {
        ConfirmationToken byToken = confirmationTokenRepository.findByToken(token).get();
        byToken.setConfirmedAt(LocalDateTime.now());
    }
    public List<ConfirmationToken> findByUserId(Long id){
        return confirmationTokenRepository.findByUser_Id(id);
    }
}
