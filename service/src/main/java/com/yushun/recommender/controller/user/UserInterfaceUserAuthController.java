package com.yushun.recommender.controller.user;

import com.yushun.recommender.security.result.Result;

import com.yushun.recommender.service.UserService;
import com.yushun.recommender.vo.user.user.UserGoogleLoginVo;
import com.yushun.recommender.vo.user.user.UserSystemLoginVo;
import com.yushun.recommender.vo.user.user.UserSystemRegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * User Authentication Controller
 * </p>
 *
 * @author yushun zeng
 * @since 2022-12-30
 */

@CrossOrigin
@RestController
@RequestMapping("/authentication")
public class UserInterfaceUserAuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/userSystemRegister")
    public Result userSystemRegister(@RequestBody UserSystemRegisterVo userSystemRegisterVo) {
        return userService.userSystemRegister(userSystemRegisterVo);
    }

    @PostMapping("/userSystemLogin")
    public Result userSystemLogin(@RequestBody UserSystemLoginVo userSystemLoginVo) {
       return userService.userSystemLogin(userSystemLoginVo);
    }

    @PostMapping("/googleLogin")
    public Result googleLogin(@RequestBody UserGoogleLoginVo userGoogleLoginVo) {
        return userService.googleLogin(userGoogleLoginVo);
    }

    @GetMapping("/tokenLoginRefresh")
    public Result tokenLoginRefresh(@RequestParam("token") String token) {
       return userService.tokenLoginRefresh(token);
    }
}
