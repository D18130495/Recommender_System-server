package com.yushun.recommender.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * <p>
 * JWT Authentication Exception
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-12
 */

public class JwtAuthenticationException extends AuthenticationException {
    private static final long serialVersionUID = -761503632186596342L;

    public JwtAuthenticationException(String e) {
        super(e);
    }
}
