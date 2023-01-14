package com.yushun.recommender.controller.user;

import com.yushun.recommender.security.result.Result;
import com.yushun.recommender.service.user.UserInterfaceUserService;
import com.yushun.recommender.vo.user.user.UserSystemRegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserInterfaceUserDetailController {
    @Autowired
    private UserInterfaceUserService userInterfaceUserService;

    @GetMapping("/detail")
    public Result systemRegister(String email) {
        // TODO
        System.out.println(email);

        return Result.ok().message("Successfully register");
    }
}
