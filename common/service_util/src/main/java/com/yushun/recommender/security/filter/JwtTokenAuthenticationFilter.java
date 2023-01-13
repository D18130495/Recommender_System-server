package com.yushun.recommender.security.filter;

import com.yushun.recommender.security.exception.JwtAuthenticationException;
import com.yushun.recommender.security.utils.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * JWT Token Authentication Filter
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-12
 */

public class JwtTokenAuthenticationFilter extends GenericFilterBean {
    private JwtTokenProvider jwtToken;

    public JwtTokenAuthenticationFilter(JwtTokenProvider jwtToken) {
        this.jwtToken = jwtToken;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        try {
            String token = jwtToken.resolveToken(request);

            if(token != null && jwtToken.validateToken(token)) {
                Authentication auth = jwtToken.getAuthentication(token);

                if(auth != null) {
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }catch(JwtAuthenticationException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Invalid token");
            response.getWriter().flush();

            return;
        }

        filterChain.doFilter(req, res);
    }
}