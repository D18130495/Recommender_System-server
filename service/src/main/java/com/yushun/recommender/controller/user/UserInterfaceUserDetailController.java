package com.yushun.recommender.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yushun.recommender.model.common.User;
import com.yushun.recommender.security.result.Result;
import com.yushun.recommender.security.utils.JwtTokenProvider;
import com.yushun.recommender.service.user.UserInterfaceUserService;
import com.yushun.recommender.vo.user.user.UserDetailReturnVo;
import com.yushun.recommender.vo.user.user.UserReturnVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserInterfaceUserDetailController {
    @Autowired
    private UserInterfaceUserService userInterfaceUserService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @GetMapping("/getUserDetailByToken")
    public Result getUserDetailByToken(String token) {
        String userEmail = jwtTokenProvider.getUsername(token);

        // find user
        QueryWrapper userWrapper = new QueryWrapper();
        userWrapper.eq("email", userEmail);

        User findUser = userInterfaceUserService.getOne(userWrapper);

        if(findUser == null) {
            return Result.fail().message("Can not find user");
        }

        // form return data
        UserDetailReturnVo userDetailReturnVo = new UserDetailReturnVo();
        BeanUtils.copyProperties(findUser, userDetailReturnVo);

        return Result.ok(userDetailReturnVo).message("");
    }


    @GetMapping("/getUserDetailByEmail")
    public Result getUserDetailByEmail(String email) {
        // TODO
        System.out.println(email);

        return Result.ok().message("");
    }
}
