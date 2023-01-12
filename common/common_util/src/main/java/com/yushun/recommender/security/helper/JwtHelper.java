package com.yushun.recommender.security.helper;

import io.jsonwebtoken.*;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * <p>
 * JWT Generator
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-4
 */

public class JwtHelper {
    private static long tokenExpiration = 24 * 60 * 60 * 1000;
    private static String tokenSignKey = "Qpuur990415#zys";

    public static String createToken(String email, String username) {
        String token = Jwts.builder()
                .setSubject("Recommender System")
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .claim("email", email)
                .claim("userName", username)
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                .compressWith(CompressionCodecs.GZIP)
                .compact();

        return token;
    }

    public static String getEmail(String token) {
        if(StringUtils.isEmpty(token)) return "";

        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        String email = (String)claims.get("email");

        return email;
    }

    public static String getUsername(String token) {
        if(StringUtils.isEmpty(token)) return "";

        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        String username = (String)claims.get("username");

        return username;
    }

    public static void main(String[] args) {
        String token = JwtHelper.createToken("990415zys@gmail.com", "Yushun Zeng");

        System.out.println(token);
        System.out.println(JwtHelper.getEmail(token));
        System.out.println(JwtHelper.getUsername(token));
    }
}