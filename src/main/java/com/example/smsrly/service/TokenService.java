package com.example.smsrly.service;


import com.example.smsrly.auth.AuthenticationResponse;
import com.example.smsrly.entity.Token;
import com.example.smsrly.entity.User;
import com.example.smsrly.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private static final long ONE_DAY_IN_MILLISECONDS = 86400000; // Number of milliseconds in a day

    public AuthenticationResponse generateTokens(User user) {
        String refreshToken = jwtService.generateRefreshToken(user);
        expireAllAccessTokens(user);
        String accessToken = saveAccessToken(user);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse generateAccessToken(User user, String refreshToken) {
        expireAllAccessTokens(user);
        String accessToken = saveAccessToken(user);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private String saveAccessToken(User user) {
        String generateToken = jwtService.generateToken(user);
        Token token = Token.builder()
                .token(generateToken)
                .isExpired(false)
                .user(user)
                .expireDate(LocalDateTime.now().plusDays(1))
                .build();
        tokenRepository.save(token);
        return generateToken;
    }

    private void expireAllAccessTokens(User user) {
        List<Token> userValidToken = tokenRepository.findAllValidTokens(user.getId());
        if (userValidToken.isEmpty()) return;
        userValidToken.forEach(token -> token.setExpired(true));
    }

    public Boolean isAccessTokenExists(String token) {
        return tokenRepository.findByToken(token).isPresent();
    }

    @Scheduled(fixedRate = ONE_DAY_IN_MILLISECONDS)
    private void deleteExpiredToken() {
        List<Token> expiredTokens = tokenRepository.findAllExpiredTokenByTime(LocalDateTime.now());
        expiredTokens.forEach(tokenRepository::delete);
    }
}
