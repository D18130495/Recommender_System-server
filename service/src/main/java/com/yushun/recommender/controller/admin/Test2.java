package com.yushun.recommender.controller.admin;

import org.springframework.web.bind.annotation.GetMapping;
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
public class Test2 {
    @GetMapping("/admin")
    public String admin() {
        return "you are admin";
    }

    @GetMapping("/user")
    public String user() {
        return "you are user";
    }
}