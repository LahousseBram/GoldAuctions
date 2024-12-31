package com.qjewels.qjewels.service.implementation;

import com.qjewels.qjewels.model.User;
import com.qjewels.qjewels.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TokenService {

    @Autowired
    private IUserRepository iUserRepository;

    public boolean verifyToken(String token) {
        Optional<User> user = iUserRepository.findByEmailVerificationToken(token);
        if (user.isPresent() && user.get().getEmailVerificationTokenExpiry().isAfter(LocalDateTime.now())) {
            User verifiedUser = user.get();
            verifiedUser.setEmailVerified(true);
            verifiedUser.setEmailVerificationToken(null);
            iUserRepository.save(verifiedUser);
            return true;
        }
        return false;
    }

}
