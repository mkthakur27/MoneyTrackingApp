package com.moneytrackingapp.controller;

import com.moneytrackingapp.dto.AuthRequest;
import com.moneytrackingapp.dto.AuthResponse;
import com.moneytrackingapp.security.AppUserPrincipal;
import com.moneytrackingapp.security.CurrentUser;
import com.moneytrackingapp.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Auth", description = "Sign up, log in, and inspect the current user")
public class AuthController {
    private final AuthService authService;
    private final CurrentUser currentUser;

    public AuthController(AuthService authService, CurrentUser currentUser) {
        this.authService = authService;
        this.currentUser = currentUser;
    }

    @PostMapping("/signup")
    @Operation(summary = "Create an account")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account created"),
            @ApiResponse(responseCode = "400", description = "Invalid email or password"),
            @ApiResponse(responseCode = "409", description = "Email already registered")
    })
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Log in")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logged in"),
            @ApiResponse(responseCode = "401", description = "Invalid email or password")
    })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    @Operation(summary = "Get the current user")
    @ApiResponse(responseCode = "200", description = "Current user returned")
    public ResponseEntity<AuthResponse> me() {
        AppUserPrincipal principal = currentUser.requirePrincipal();
        return ResponseEntity.ok(new AuthResponse(null, principal.getId(), principal.getEmail()));
    }
}
