package com.yushun.recommender.security.config;

import com.yushun.recommender.security.filter.JwtTokenAuthenticationFilter;
import com.yushun.recommender.security.utils.JwtAuthenticationEntryPoint;
import com.yushun.recommender.security.utils.JwtTokenProvider;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * <p>
 * JWT Security Configuration
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-12
 */

public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private JwtTokenProvider jwtTokenProvider;

    public JwtSecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        JwtTokenAuthenticationFilter customFilter = new JwtTokenAuthenticationFilter(jwtTokenProvider);

        http.exceptionHandling()
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .and()
                .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
