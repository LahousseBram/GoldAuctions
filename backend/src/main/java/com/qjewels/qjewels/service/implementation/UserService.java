package com.qjewels.qjewels.service.implementation;

import com.qjewels.qjewels.dto.*;
import com.qjewels.qjewels.model.User;
import com.qjewels.qjewels.model.UserProfile;
import com.qjewels.qjewels.model.enums.Role;
import com.qjewels.qjewels.repository.IUserProfileRepository;
import com.qjewels.qjewels.repository.IUserRepository;
import com.qjewels.qjewels.utils.JWTUtil;
import com.qjewels.qjewels.utils.exceptions.QJewelsException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserService implements UserDetailsService {
    private final IUserRepository iUserRepository;
    private final IUserProfileRepository iUserProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private EmailService emailService;
    private final JWTUtil jwtTokenService;


    @Autowired
    public UserService(IUserRepository iUserRepository, IUserProfileRepository iUserProfileRepository, PasswordEncoder passwordEncoder, EmailService emailService, JWTUtil jwtTokenService) {
        this.iUserRepository = iUserRepository;
        this.iUserProfileRepository = iUserProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.jwtTokenService = jwtTokenService;
    }

    public UserDTO registerUser(UserRegistrationDTO userRegistrationDTO) {
        Optional<User> existingUser = iUserRepository.findByEmail(userRegistrationDTO.email());
        if (existingUser.isPresent()) {
            throw new QJewelsException("Email is already in use.");
        }

        User user = new User();
        user.setName(userRegistrationDTO.firstName() + " " + userRegistrationDTO.lastName());
        user.setEmail(userRegistrationDTO.email());
        user.setPasswordHash(passwordEncoder.encode(userRegistrationDTO.password()));
        user.isTermsAccepted(true);

        user.setUserRole(Role.USER);

        String verificationToken = UUID.randomUUID().toString();
        user.setEmailVerificationToken(verificationToken);
        user.setEmailVerificationTokenExpiry(LocalDateTime.now().plusDays(1));

        iUserRepository.save(user);

        UserProfile userProfile = getUserProfile(userRegistrationDTO, user);
        iUserProfileRepository.save(userProfile);

        emailService.sendVerificationEmail(user.getEmail(), verificationToken);

        return new UserDTO(user.getName(), user.getEmail(), user.getEmailVerificationToken());
    }

    public Optional<UserProfileDTO> getUserProfileById(Long profileId) {
        Optional<UserProfile> userProfile = iUserProfileRepository.findById(profileId);
        return userProfile.map(UserProfileDTO::new);
    }

    @NotNull
    private static UserProfile getUserProfile(UserRegistrationDTO userRegistrationDTO, User user) {
        UserProfile userProfile = new UserProfile();
        userProfile.setStreet(userRegistrationDTO.street());
        userProfile.setCity(userRegistrationDTO.city());
        userProfile.setPostalCode(userRegistrationDTO.postalCode());
        userProfile.setCountry(userRegistrationDTO.country());
        userProfile.setPhoneNumber(userRegistrationDTO.phoneNumber());
        userProfile.setUser(user);
        userProfile.setEmail(userRegistrationDTO.email());
        userProfile.setName(user.getName());
        return userProfile;
    }

    @Transactional
    public Optional<LoginResponseDTO> loginUser(String email, String password) {
        Optional<User> userOptional = iUserRepository.findByEmail(email);

        System.out.println(passwordEncoder.matches(password, userOptional.get().getPasswordHash()));
        if (userOptional.isPresent() && passwordEncoder.matches(password, userOptional.get().getPasswordHash())) {
            User user = userOptional.get();

            String accessToken = jwtTokenService.generateToken(user.getEmail(), user.getUserRole().name(), user.getUserId(), 3600);
            String refreshToken = jwtTokenService.generateToken(user.getEmail(), user.getUserRole().name(), user.getUserId(), 30 * 24 * 60 * 60);

            user.setRefreshToken(refreshToken);
            iUserRepository.save(user);

            return Optional.of(new LoginResponseDTO(accessToken, refreshToken));
        } else {
            throw new QJewelsException("Invalid email or password.");
        }
    }

    public void logoutUser(String email) {
        Optional<User> user = iUserRepository.findByEmail(email);
        if (user.isPresent()) {
            User userToUpdate = user.get();
            userToUpdate.setRefreshToken(null);
            iUserRepository.save(userToUpdate);
        } else {
            throw new QJewelsException("User not found.");
        }
    }

    public void resetPassword(String email) {
        if (email.startsWith("\"") && email.endsWith("\"")) {
            email = email.substring(1, email.length() - 1);
        }
        Optional<User> user = iUserRepository.findByEmail(email);
        if (user.isPresent()) {
            String resetToken = UUID.randomUUID().toString();
            user.get().setEmailVerificationToken(resetToken);
            user.get().setEmailVerificationTokenExpiry(LocalDateTime.now().plusDays(1));
            iUserRepository.save(user.get());

            emailService.sendPasswordResetEmail(user.get().getEmail(), resetToken);
        }
    }

    public void changePassword(LoginDTO loginDTO) {
        Optional<User> user = iUserRepository.findByEmail(loginDTO.email());
        if (user.isPresent()) {
            user.get().setPasswordHash(passwordEncoder.encode(loginDTO.password()));
            iUserRepository.save(user.get());
        }
    }

    public void updateUser(User user) {
        iUserRepository.save(user);
    }

    public void deleteUser(String email) {
        Optional<User> user = iUserRepository.findByEmail(email);
        user.ifPresent(iUserRepository::delete);
    }

    public void sendVerificationEmail(VerificationEmailCTO verificationEmailCTO) {
        emailService.sendVerificationEmail(verificationEmailCTO.email(), verificationEmailCTO.token());
    }

    public void sendPasswordResetEmail(VerificationEmailCTO verificationEmailCTO) {
        emailService.sendPasswordResetEmail(verificationEmailCTO.email(), verificationEmailCTO.token());
    }

    public User loadUserProfile(String email) {
        if (email == null) {
            throw new QJewelsException("Email is null");
        }
        User user = iUserRepository.findByEmail(email)
                .orElseThrow(() -> new QJewelsException("User not found"));

        if (!user.isEmailVerified()) {
            throw new QJewelsException("Email is not verified. Please verify your email first.");
        }

        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = iUserRepository.findByEmail(username);
        if (user.isPresent()) {
            return new org.springframework.security.core.userdetails.User(user.get().getEmail(), user.get().getPasswordHash(), user.get().getAuthorities());
        } else {
            throw new QJewelsException("User not found.");
        }
    }

    public Optional<UserProfileDTO> get(String email) {
        Optional<UserProfile> user = iUserProfileRepository.findByEmail(email);
        return user.map(UserProfileDTO::new);
    }

    public Optional<User> findByPasswordResetToken(String token) {
        return iUserRepository.findByEmailVerificationToken(token);
    }

    public boolean isResetTokenExpired(User user) {
        return user.getEmailVerificationTokenExpiry().isBefore(LocalDateTime.now());
    }

    public void updatePassword(User user, String newPassword) {
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        iUserRepository.save(user);
    }

    public void clearResetToken(User user) {
        user.setEmailVerificationToken(null);
        user.setEmailVerificationTokenExpiry(null);
        iUserRepository.save(user);
    }

    public void sendContactEmail(ContactEmailDTO contactEmailDTO) {
        emailService.sendContactEmail("howeststudent75@gmail.com", contactEmailDTO.getEmail(), contactEmailDTO.getMessage(), contactEmailDTO.getName());
    }

    public UserProfileDTO updateUser(UserProfileDTO userProfileDto) {
        User existingUser = iUserRepository.findByEmail(userProfileDto.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
    
        // Update user basic info
        existingUser.setName(userProfileDto.getName());
        existingUser.setEmail(userProfileDto.getEmail());
        
        // Update or create profile information
        UserProfile profile = existingUser.getUserProfile();
        if (profile == null) {
            profile = new UserProfile();
            profile.setUser(existingUser);
        }
        
        profile.setName(userProfileDto.getName());
        profile.setPhoneNumber(userProfileDto.getPhoneNumber());
        profile.setStreet(userProfileDto.getStreet());
        profile.setCity(userProfileDto.getCity());
        profile.setPostalCode(userProfileDto.getPostalCode());
        profile.setCountry(userProfileDto.getCountry());
        profile.setEmail(userProfileDto.getEmail());
        profile.setName(existingUser.getName());
        
        iUserRepository.save(existingUser);
        iUserProfileRepository.save(profile);
        
        return new UserProfileDTO(profile);
    }
}