package com.yushun.recommender.controller.user;

import com.yushun.recommender.security.result.Result;
import com.yushun.recommender.security.user.UserRepository;
import com.yushun.recommender.security.utils.JwtTokenProvider;
import com.yushun.recommender.service.user.UserInterfaceUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/user/detail")
public class UserInterfaceUserDetailController {
    @Autowired
    private UserInterfaceUserService userInterfaceUserService;

    @GetMapping("/")
    public Result test(String email) {
        // TODO
        System.out.println(email);

        return Result.ok().message("");
    }
}
