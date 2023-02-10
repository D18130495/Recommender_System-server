package com.yushun.recommender.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yushun.recommender.security.exception.JwtAuthenticationException;
import com.yushun.recommender.security.result.Result;

import com.yushun.recommender.model.common.User;
import com.yushun.recommender.security.user.UserRepository;
import com.yushun.recommender.security.utils.JwtTokenProvider;
import com.yushun.recommender.service.UserService;
import com.yushun.recommender.vo.user.user.UserGoogleLoginVo;
import com.yushun.recommender.vo.user.user.UserReturnVo;
import com.yushun.recommender.vo.user.user.UserSystemLoginVo;
import com.yushun.recommender.vo.user.user.UserSystemRegisterVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;

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

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/userSystemRegister")
    public Result userSystemRegister(UserSystemRegisterVo userSystemRegisterVo) {
        // find if the email is first time be used to sign up
        QueryWrapper userWrapper = new QueryWrapper();
        userWrapper.eq("email", userSystemRegisterVo.getEmail());

        User findUser = userService.getOne(userWrapper);

        if(findUser == null) {
            BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
            String cryptPassword = bcryptPasswordEncoder.encode(userSystemRegisterVo.getPassword());

            // form return data
            UserReturnVo userReturnVo = new UserReturnVo();
            BeanUtils.copyProperties(userSystemRegisterVo, userReturnVo);

            // create new system user
            User storeSystemUser = new User();
            BeanUtils.copyProperties(userSystemRegisterVo, storeSystemUser);
            storeSystemUser.setPassword(cryptPassword);
            storeSystemUser.setPolicy("U");
            storeSystemUser.setType("S");
            storeSystemUser.setRoles(Collections.singletonList("ROLE_USER"));
            storeSystemUser.setCreateTime(new Date());
            storeSystemUser.setUpdateTime(new Date());
            storeSystemUser.setIsDeleted(0);

            boolean save = userService.save(storeSystemUser);

            if(!save) return Result.fail().message("Register failed with server error");

            // add user for spring security
            userRepository.addSystemUser(new User(userSystemRegisterVo.getUsername(),
                    cryptPassword,
                    userSystemRegisterVo.getEmail()));

            // generate token
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userSystemRegisterVo.getEmail(), cryptPassword));

            String token = jwtTokenProvider.createToken(userSystemRegisterVo.getEmail(),
                    userRepository.findByUsername(userSystemRegisterVo.getEmail())
                            .orElseThrow(() -> new UsernameNotFoundException("User " + userSystemRegisterVo.getEmail() + "not found")).getRoles()
            );

            userReturnVo.setToken(token);
            userReturnVo.setPolicy("U");
            userReturnVo.setType("S");

            return Result.ok(userReturnVo).message("Successfully registered, will automatically login with in 3 second");
        }else if(findUser.getType().equals("S")) {
            return Result.fail().message("Email address already been used for register with System");
        }else {
            return Result.fail().message("Email address already been used for register with Google");
        }
    }

    @PostMapping("/userSystemLogin")
    public Result userSystemLogin(UserSystemLoginVo userSystemLoginVo) {
       return userService.userSystemLogin(userSystemLoginVo);
    }

    @PostMapping("/googleLogin")
    public Result googleLogin(UserGoogleLoginVo userGoogleLoginVo) {
        return userService.googleLogin(userGoogleLoginVo);
    }

    @GetMapping("/tokenLoginRefresh")
    public Result getUserDetailByToken(@RequestParam("token") String token) {
       return userService.getUserDetailByToken(token);
    }
}
