package com.avinash.danumalk.token;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TokenCleanupService {
    private final TokenRepository tokenRepository;

    @Scheduled(fixedRate = 600000) // Cleanup every 1 minute (adjust as needed)
    public void cleanupRevokedTokens() {
        // Perform cleanup logic here
        tokenRepository.deleteRevokedTokens();
    }

}
