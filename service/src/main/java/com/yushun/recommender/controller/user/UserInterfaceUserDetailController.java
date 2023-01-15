package com.yushun.recommender.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yushun.recommender.model.common.User;
import com.yushun.recommender.security.result.Result;
import com.yushun.recommender.security.user.UserRepository;
import com.yushun.recommender.security.utils.JwtTokenProvider;
import com.yushun.recommender.service.user.UserInterfaceUserService;
import com.yushun.recommender.vo.user.user.UserReturnVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/user/detail")
public class UserInterfaceUserDetailController {
    @Autowired
    private UserInterfaceUserService userInterfaceUserService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public Result test(String email) {
        // TODO
        System.out.println(email);

        return Result.ok().message("");
    }

    @GetMapping("/tokenLoginRefresh")
    public Result getUserDetailByToken(String token) {
        // check if the token valid
        boolean isValidToken = jwtTokenProvider.validateToken(token);

        if(isValidToken) {
            String userEmail = jwtTokenProvider.getUsername(token);

            // find user
            QueryWrapper userWrapper = new QueryWrapper();
            userWrapper.eq("email", userEmail);

            User findUser = userInterfaceUserService.getOne(userWrapper);

            if(findUser == null) {
                return Result.fail().message("Can not find user");
            }

            // form return data
            UserReturnVo userReturnVo = new UserReturnVo();
            BeanUtils.copyProperties(findUser, userReturnVo);

            // generate token
            if(findUser.getType().equals("G")) {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userEmail, userEmail));
            }else {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userEmail, findUser.getPassword()));
            }

            String newToken = jwtTokenProvider.createToken(userEmail,
                    userRepository.findByUsername(userEmail)
                            .orElseThrow(() -> new UsernameNotFoundException("User " + userEmail + "not found")).getRoles()
            );

            userReturnVo.setToken(newToken);

            return Result.ok(userReturnVo).message("Successfully refresh login time");
        }

        return Result.fail().message("Invalid token");
    }
}
