package com.online.scheduling.security.authentication;

import com.online.scheduling.security.authentication.models.MyUserDetails;
import com.online.scheduling.user.User;
import com.online.scheduling.user.implementations.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service("MyUserDetailsService")
public class MyUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Autowired
    public MyUserDetailsService(
            @Qualifier("UserService")
            UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        User user = userService.getUserByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException(String.format("Email %s is not registered", email)));
        return MyUserDetails.builder()
                    .emailAsKey(email)
                    .password(user.getPassword())
                    .enabled(user.isEnabled())
                    .locked(user.isLocked())
                    .authorities(Set.of(new SimpleGrantedAuthority("ROLE_USER")))
                    .build();
    }
}
