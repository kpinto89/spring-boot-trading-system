package com.example.trading.security;

import com.example.trading.infrastructure.persistence.entity.UserEntity;
import com.example.trading.infrastructure.persistence.entity.WalletEntity;
import com.example.trading.infrastructure.persistence.repository.JpaUserRepository;
import com.example.trading.infrastructure.persistence.repository.JpaWalletRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JpaUserRepository userRepo;
    private final JpaWalletRepository walletRepo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtProvider jwtProvider;

    public record RegisterRequest(@Email @NotBlank String email, @NotBlank String password, @NotBlank String fullName) {}
    public record LoginRequest(@Email @NotBlank String email, @NotBlank String password) {}
    public record TokenResponse(String token) {}

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        if (userRepo.findByEmail(req.email()).isPresent()) {
            return ResponseEntity.badRequest().body("email already registered");
        }
        UUID userId = UUID.randomUUID();
        Instant now = Instant.now();

        UserEntity u = UserEntity.builder()
                .id(userId)
                .email(req.email())
                .passwordHash(encoder.encode(req.password()))
                .fullName(req.fullName())
                .createdAt(now)
                .build();
        userRepo.save(u);

        // Seed wallet with demo balance
        WalletEntity w = WalletEntity.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .currency("USD")
                .balance(new BigDecimal("100000.00"))
                .updatedAt(now)
                .build();
        walletRepo.save(w);

        String token = jwtProvider.createToken(userId, u.getEmail());
        return ResponseEntity.ok(new TokenResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.email(), req.password()));
        UserEntity u = userRepo.findByEmail(req.email()).orElseThrow();
        String token = jwtProvider.createToken(u.getId(), u.getEmail());
        return ResponseEntity.ok(new TokenResponse(token));
    }
}
