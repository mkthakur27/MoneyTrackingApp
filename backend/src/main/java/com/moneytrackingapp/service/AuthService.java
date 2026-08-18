package com.moneytrackingapp.service;

import com.moneytrackingapp.dto.AuthRequest;
import com.moneytrackingapp.dto.AuthResponse;
import com.moneytrackingapp.exception.DuplicateEmailException;
import com.moneytrackingapp.model.User;
import com.moneytrackingapp.repository.UserRepository;
import com.moneytrackingapp.security.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse signup(AuthRequest request) {
        String email = normalize(request.getEmail());
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("An account with that email already exists");
        }
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user = userRepository.save(user);
        return toResponse(user);
    }

    public AuthResponse login(AuthRequest request) {
        String email = normalize(request.getEmail());
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid email or password");
        }
        return toResponse(user);
    }

    private AuthResponse toResponse(User user) {
        String token = jwtService.generateToken(user.getId(), user.getEmail());
        return new AuthResponse(token, user.getId(), user.getEmail());
    }

    private String normalize(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }
}
