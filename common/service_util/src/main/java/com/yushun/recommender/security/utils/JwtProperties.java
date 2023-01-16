package com.yushun.recommender.security.utils;

import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * JWT Rule
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-12
 */

@Configuration
public class JwtProperties {
    private String secretKey = "Qpuur990415#zys";
//    private long validityInMs = 3600_000;
    private long validityInMs = 5_000;

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public long getValidityInMs() {
        return validityInMs;
    }

    public void setValidityInMs(long validityInMs) {
        this.validityInMs = validityInMs;
    }
}
