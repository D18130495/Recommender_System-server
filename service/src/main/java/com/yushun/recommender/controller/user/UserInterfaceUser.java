package com.yushun.recommender.controller.user;

import com.yushun.recommender.common.result.Result;

import com.yushun.recommender.model.user.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * welcome controller
 * </p>
 *
 * @author yushun zeng
 * @since 2022-12-30
 */

@CrossOrigin
@RestController
@RequestMapping("/authentication")
public class UserInterfaceUser {
    @GetMapping("/login")
    public Result login(User user) {
        return Result.ok("123").message("Successfully login");
    }
}
