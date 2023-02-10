package com.yushun.recommender.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yushun.recommender.mapper.UserMapper;
import com.yushun.recommender.model.common.User;
import com.yushun.recommender.security.exception.JwtAuthenticationException;
import com.yushun.recommender.security.result.Result;
import com.yushun.recommender.security.user.UserRepository;
import com.yushun.recommender.security.utils.EmailChecker;
import com.yushun.recommender.security.utils.JwtTokenProvider;
import com.yushun.recommender.security.utils.PasswordChecker;
import com.yushun.recommender.service.UserService;
import com.yushun.recommender.vo.user.user.UserDetailReturnVo;
import com.yushun.recommender.vo.user.user.UserGoogleLoginVo;
import com.yushun.recommender.vo.user.user.UserReturnVo;
import com.yushun.recommender.vo.user.user.UserSystemLoginVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;

/**
 * <p>
 * User Interface User Service Impl
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-5
 */

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Result googleLogin(UserGoogleLoginVo userGoogleLoginVo) {
        // form return data
        UserReturnVo userReturnVo = new UserReturnVo();
        BeanUtils.copyProperties(userGoogleLoginVo, userReturnVo);

        // find if the user is sign up with Google or first time sign in
        QueryWrapper userWrapper = new QueryWrapper();
        userWrapper.eq("email", userGoogleLoginVo.getEmail());

        User findUser = baseMapper.selectOne(userWrapper);

        if(findUser == null) {
            // create new google user
            User storeGoogleUser = new User();
            BeanUtils.copyProperties(userGoogleLoginVo, storeGoogleUser);
            storeGoogleUser.setPolicy("U");
            storeGoogleUser.setType("G");
            storeGoogleUser.setRoles(Collections.singletonList("ROLE_USER"));
            storeGoogleUser.setCreateTime(new Date());
            storeGoogleUser.setUpdateTime(new Date());
            storeGoogleUser.setIsDeleted(0);

            int save = baseMapper.insert(storeGoogleUser);

            if(save < 1) return Result.fail().message("Login failed with server error");
        }else if(findUser.getType().equals("S")) {
            return Result.fail(userReturnVo).message("Email address already been used for register with System");
        }

        // add user to spring security
        userRepository.addGoogleUser(new User(userGoogleLoginVo.getUsername(),
                userGoogleLoginVo.getEmail()));

        // generate token
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userGoogleLoginVo.getEmail(), userGoogleLoginVo.getEmail()));

        String token = jwtTokenProvider.createToken(userGoogleLoginVo.getEmail(),
                userRepository.findByUsername(userGoogleLoginVo.getEmail())
                        .orElseThrow(() -> new UsernameNotFoundException("User " + userGoogleLoginVo.getEmail() + "not found")).getRoles()
        );

        userReturnVo.setToken(token);

        if(findUser == null) {
            userReturnVo.setPolicy("U");
        }else {
            userReturnVo.setPolicy(findUser.getPolicy());
        }

        userReturnVo.setType("G");

        return Result.ok(userReturnVo).message("Successfully login");
    }

    @Override
    public Result userSystemLogin(UserSystemLoginVo userSystemLoginVo) {
        // check email
        boolean isValidEmail = EmailChecker.check(userSystemLoginVo.getEmail());

        if(!isValidEmail) return Result.fail().message("Incorrect email format");

        // check password
//        boolean isValidPassword = PasswordChecker.check(userSystemLoginVo.getPassword());
//
//        if(!isValidPassword) return Result.fail().message("Incorrect password format");

        // find user
        QueryWrapper userWrapper = new QueryWrapper();
        userWrapper.eq("email", userSystemLoginVo.getEmail());

        User findUser = baseMapper.selectOne(userWrapper);

        if(findUser == null) {
            return Result.fail().message("Email has not been registered");
        }else if(findUser.getType().equals("G")) {
            return Result.fail().message("Email registered with Google");
        }

        // compare the password
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean isMatch = bcryptPasswordEncoder.matches(userSystemLoginVo.getPassword(), findUser.getPassword());

        if(!isMatch) {
            return Result.fail().message("Incorrect password");
        }else {
            // form return data
            UserReturnVo userReturnVo = new UserReturnVo();
            BeanUtils.copyProperties(userSystemLoginVo, userReturnVo);
            userReturnVo.setUsername(findUser.getUsername());
            userReturnVo.setAvatar(findUser.getAvatar());

            // generate token
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userSystemLoginVo.getEmail(), findUser.getPassword()));

            String token = jwtTokenProvider.createToken(userSystemLoginVo.getEmail(),
                    userRepository.findByUsername(userSystemLoginVo.getEmail())
                            .orElseThrow(() -> new UsernameNotFoundException("User " + userSystemLoginVo.getEmail() + "not found")).getRoles()
            );

            userReturnVo.setToken(token);
            userReturnVo.setPolicy(findUser.getPolicy());
            userReturnVo.setType(findUser.getType());

            return Result.ok(userReturnVo).message("Successfully login");
        }
    }

    @Override
    public Result getUserDetailByToken(String token) {
        try{
            // check if the token valid
            jwtTokenProvider.validateToken(token);

            // get user email
            String userEmail = jwtTokenProvider.getUsername(token);

            // find user
            QueryWrapper userWrapper = new QueryWrapper();
            userWrapper.eq("email", userEmail);

            User findUser = baseMapper.selectOne(userWrapper);

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
            userReturnVo.setPolicy(findUser.getPolicy());
            userReturnVo.setType(findUser.getType());

            return Result.ok(userReturnVo).message("Successfully refresh login time");
        }catch(JwtAuthenticationException e) {
            return Result.invalidToken().message("Invalid token");
        }
    }

    @Override
    public UserDetailReturnVo getUserDetailByEmail(String email) {
        // find user
        QueryWrapper userWrapper = new QueryWrapper();
        userWrapper.eq("email", email);

        User findUser = baseMapper.selectOne(userWrapper);

        if(findUser == null) {
            return null;
        }

        // form return data
        UserDetailReturnVo userDetailReturnVo = new UserDetailReturnVo();
        BeanUtils.copyProperties(findUser, userDetailReturnVo);

        return userDetailReturnVo;
    }
}
