package com.yushun.recommender.controller.admin;

import com.yushun.recommender.security.users.UserRepository;
import com.yushun.recommender.security.utils.JwtTokenProvider;
import com.yushun.recommender.vo.admin.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * Spring Security Test
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-12
 */

@RestController
@RequestMapping("/auth")
public class Test {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository userRepository;



    @PostMapping("/login")
    public String login(AuthRequest request) {
        String username = request.getUsername();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, request.getPassword()));

        String token = jwtTokenProvider.createToken(username,
                userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username " + username + "not found")).getRoles()
        );

        return token;

    }
}