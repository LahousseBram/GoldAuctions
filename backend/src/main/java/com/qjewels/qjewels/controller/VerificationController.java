package com.qjewels.qjewels.controller;


import com.qjewels.qjewels.model.User;
import com.qjewels.qjewels.request.AuthenticationRequest;
import com.qjewels.qjewels.request.AuthenticationResponse;
import com.qjewels.qjewels.service.implementation.TokenService;
import com.qjewels.qjewels.service.implementation.UserService;
import com.qjewels.qjewels.utils.JWTUtil;
import com.qjewels.qjewels.utils.exceptions.QJewelsException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class VerificationController {

    private final TokenService tokenService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public VerificationController(TokenService tokenService, UserService userService, AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam("token") String token) {
        boolean isVerified = tokenService.verifyToken(token);

        if (isVerified) {
            return ResponseEntity.ok("Your email has been successfully verified!");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired verification token.");
        }
    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest){

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new QJewelsException("Incorrect E-mail or password");
        }


        User user = userService.loadUserProfile(authenticationRequest.getEmail());


        String jwt = jwtUtil.generateToken(user.getEmail(), user.getUserRole().name(),user.getUserId(),3600);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

}
