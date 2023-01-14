package com.yushun.recommender.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yushun.recommender.security.helper.JwtHelper;
import com.yushun.recommender.security.result.Result;

import com.yushun.recommender.model.common.User;
import com.yushun.recommender.security.user.UserRepository;
import com.yushun.recommender.security.utils.JwtTokenProvider;
import com.yushun.recommender.service.user.UserInterfaceUserService;
import com.yushun.recommender.vo.user.user.UserGoogleLoginVo;
import com.yushun.recommender.vo.user.user.UserReturnVo;
import com.yushun.recommender.vo.user.user.UserSystemRegisterVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;

/**
 * <p>
 * Welcome Controller
 * </p>
 *
 * @author yushun zeng
 * @since 2022-12-30
 */

@CrossOrigin
@RestController
@RequestMapping("/authentication")
public class UserInterfaceUserController {
    @Autowired
    private UserInterfaceUserService userInterfaceUserService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/userSystemRegister")
    public Result userSystemRegister(UserSystemRegisterVo userSystemRegisterVo) {
        System.out.println(userSystemRegisterVo);

        return Result.fail().message("Successfully register");
    }

    @PostMapping("/googleLogin")
    public Result googleLogin(UserGoogleLoginVo userGoogleLoginVo) {
        // generate token
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userGoogleLoginVo.getEmail(), userGoogleLoginVo.getEmail()));

        String token = jwtTokenProvider.createToken(userGoogleLoginVo.getEmail(),
                userRepository.findByUsername(userGoogleLoginVo.getEmail())
                        .orElseThrow(() -> new UsernameNotFoundException("User " + userGoogleLoginVo.getEmail() + "not found")).getRoles()
        );

        // form return data
        UserReturnVo userReturnVo = new UserReturnVo();
        BeanUtils.copyProperties(userGoogleLoginVo, userReturnVo);
        userReturnVo.setToken(token);

        // find if the user is first time sign in
        QueryWrapper userWrapper = new QueryWrapper();
        userWrapper.eq("email", userGoogleLoginVo.getEmail());

        User findUser = userInterfaceUserService.getOne(userWrapper);

        if(findUser == null) {
            // create new google user
            User storeUser = new User();
            BeanUtils.copyProperties(userGoogleLoginVo, storeUser);
            storeUser.setPolicy("F");
            storeUser.setType("G");
            storeUser.setRoles(Collections.singletonList("ROLE_USER"));
            storeUser.setCreateTime(new Date());
            storeUser.setUpdateTime(new Date());
            storeUser.setIsDeleted(0);

            boolean save = userInterfaceUserService.save(storeUser);

            if(!save) return Result.fail().message("Login failed");
        }

        return Result.ok(userReturnVo).message("Successfully login");
    }
}
