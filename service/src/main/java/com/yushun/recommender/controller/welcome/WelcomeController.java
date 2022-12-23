package com.yushun.recommender.controller.welcome;

import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * welcome controller
 * </p>
 *
 * @author yushun zeng
 * @since 2022-12-23
 */

@CrossOrigin
@RestController
@RequestMapping("/")
public class WelcomeController {
    @GetMapping("")
    public String welcome() {
        String welcome = "Welcome to yushun's recommender system back-end.";

        return welcome;
    }
}
