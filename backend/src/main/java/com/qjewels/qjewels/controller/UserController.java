package com.qjewels.qjewels.controller;

import com.qjewels.qjewels.dto.*;
import com.qjewels.qjewels.model.User;
import com.qjewels.qjewels.service.implementation.UserService;
import com.qjewels.qjewels.utils.JWTUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final JWTUtil jwtUtil;

    public UserController(UserService userService, JWTUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public UserDTO registerUser(@Valid @RequestBody UserRegistrationDTO user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        Optional<LoginResponseDTO> loginResponseOpt = userService.loginUser(loginDTO.email(), loginDTO.password());

        if (loginResponseOpt.isPresent()) {
            LoginResponseDTO loginResponse = loginResponseOpt.get();

            Cookie accessTokenCookie = new Cookie("access_token", loginResponse.accessToken());
            accessTokenCookie.setHttpOnly(true);
            accessTokenCookie.setPath("/");
            accessTokenCookie.setMaxAge(3600);

            Cookie refreshTokenCookie = new Cookie("refresh_token", loginResponse.refreshToken());
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setPath("/");
            refreshTokenCookie.setMaxAge(30 * 24 * 60 * 60);

            response.addCookie(accessTokenCookie);
            response.addCookie(refreshTokenCookie);

            return ResponseEntity.ok(loginResponse);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/users/email")
    public Optional<UserProfileDTO> get(@RequestBody String email) {
        return userService.get(email);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(@CookieValue(name = "refresh_token") String refreshToken, HttpServletResponse response) {

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtUtil.getSignInKey())
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();

            String email = claims.getSubject();


            userService.logoutUser(email);

            clearCookies(response);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
        }
    }

    private void clearCookies(HttpServletResponse response) {
        Cookie accessTokenCookie = new Cookie("access_token", null);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(0);

        Cookie refreshTokenCookie = new Cookie("refresh_token", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }

    @PostMapping("/reset-password")
    public void resetPassword(@RequestBody String email) {
        userService.resetPassword(email);
    }

    @PostMapping("/change-password")
    public void changePassword(@RequestBody LoginDTO loginDTO) {
        userService.changePassword(loginDTO);
    }

    @PostMapping("/reset-password-confirm")
    public ResponseEntity<String> resetPasswordConfirm(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        String token = resetPasswordDTO.getToken();
        String newPassword = resetPasswordDTO.getNewPassword();

        Optional<User> user = userService.findByPasswordResetToken(token);
        if (user.isPresent() && !userService.isResetTokenExpired(user.get())) {
            userService.updatePassword(user.get(), newPassword);
            userService.clearResetToken(user.get());
            return ResponseEntity.ok("Password has been reset.");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired token.");
        }
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/profile/{profileId}")
    public ResponseEntity<UserProfileDTO> getUserProfileById(@PathVariable Long profileId) {
        Optional<UserProfileDTO> userProfile = userService.getUserProfileById(profileId);
        return userProfile.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/users/profile")
    public ResponseEntity<UserProfileDTO> getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Optional<UserProfileDTO> userProfile = userService.get(email);
        return userProfile.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/update")
    public ResponseEntity<UserProfileDTO> updateUser(@Valid @RequestBody UserProfileDTO userProfileDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        // Only allow users to update their own profile or admin to update any profile
        if (!email.equals(userProfileDto.getEmail()) && !authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        UserProfileDTO updatedProfile = userService.updateUser(userProfileDto);
        return ResponseEntity.ok(updatedProfile);
    }

    @DeleteMapping("/delete")
    public void deleteUser(@RequestBody String email) {
        userService.deleteUser(email);
    }

    @GetMapping("/send-verification-email")
    public void sendVerificationEmail(@RequestBody VerificationEmailCTO verificationEmailCTO) {
        userService.sendVerificationEmail(verificationEmailCTO);
    }

    @GetMapping("/send-password-reset-email")
    public void sendPasswordResetEmail(@RequestBody VerificationEmailCTO verificationEmailCTO) {
        userService.sendPasswordResetEmail(verificationEmailCTO);
    }

    @PostMapping("/send-contact-email")
    public void sendContactEmail(@RequestBody ContactEmailDTO contactEmailDTO) {
        userService.sendContactEmail(contactEmailDTO);
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshToken(
            @CookieValue(name = "refresh_token") String refreshToken,
            HttpServletResponse response
    ) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtUtil.getSignInKey())
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();

            Date expiration = claims.getExpiration();

            if (expiration.before(new Date())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token expired");
            }

            String email = claims.getSubject();

            User user = userService.loadUserProfile(email);

            if (!refreshToken.equals(user.getRefreshToken())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
            }

            String newAccessToken = jwtUtil.generateToken(user.getEmail(), user.getUserRole().name(),user.getUserId(), 3600);

            Cookie jwtCookie = new Cookie("access_token", newAccessToken);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(3600);
            response.addCookie(jwtCookie);

            return ResponseEntity.ok(newAccessToken);

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token expired");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }
}