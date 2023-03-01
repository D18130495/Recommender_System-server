package com.yushun.recommender.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yushun.recommender.mapper.UserMapper;
import com.yushun.recommender.minio.utils.MinioUtils;
import com.yushun.recommender.model.common.User;
import com.yushun.recommender.security.result.Result;
import com.yushun.recommender.security.user.UserRepository;
import com.yushun.recommender.security.utils.EmailChecker;
import com.yushun.recommender.security.utils.JwtTokenProvider;
import com.yushun.recommender.security.utils.PasswordChecker;
import com.yushun.recommender.service.UserService;
import com.yushun.recommender.vo.user.user.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private MinioUtils minioUtils;

    @Value("${minio.endpoint}")
    private String address;

    @Value("${minio.bucketName}")
    private String bucketName;

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

        return Result.ok(userReturnVo).message("Welcome back " + findUser.getUsername());
    }

    @Override
    public Result userSystemLogin(UserSystemLoginVo userSystemLoginVo) {
        // check email
        boolean isValidEmail = EmailChecker.check(userSystemLoginVo.getEmail());

        if(!isValidEmail) return Result.fail().message("Incorrect email format");

        // check password
        boolean isValidPassword = PasswordChecker.check(userSystemLoginVo.getPassword());

        if(!isValidPassword) return Result.fail().message("Incorrect password format");

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

            return Result.ok(userReturnVo).message("Welcome back " + findUser.getUsername());
        }
    }

    @Override
    public Result userSystemRegister(UserSystemRegisterVo userSystemRegisterVo) {
        // check email
        boolean isValidEmail = EmailChecker.check(userSystemRegisterVo.getEmail());

        if(!isValidEmail) return Result.fail().message("Incorrect email format");

        // check password
        boolean isValidPassword = PasswordChecker.check(userSystemRegisterVo.getPassword());

        if(!isValidPassword) return Result.fail().message("Incorrect password format");

        // find if the email is first time be used to sign up
        QueryWrapper userWrapper = new QueryWrapper();
        userWrapper.eq("email", userSystemRegisterVo.getEmail());

        User findUser = baseMapper.selectOne(userWrapper);

        if(findUser == null) {
            // get verification code
            String code = redisTemplate.opsForValue().get(userSystemRegisterVo.getEmail());

            if(code != null) {
                if(!code.equals(userSystemRegisterVo.getVerification())) {
                    return Result.fail().message("Verification code is incorrect");
                }
            }else {
                return Result.fail().message("Verification is expired");
            }

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

            int save = baseMapper.insert(storeSystemUser);

            if(save < 1) return Result.fail().message("Register failed with server error");

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

            redisTemplate.delete(userSystemRegisterVo.getEmail());

            return Result.ok(userReturnVo).message("Successfully registered, will automatically login with in 3 second");
        }else if(findUser.getType().equals("S")) {
            return Result.fail().message("Email address already been used for register with System");
        }else {
            return Result.fail().message("Email address already been used for register with Google");
        }
    }

    @Override
    public Result tokenLoginRefresh(String token) {
        // get user email
        String userEmail = getUserEmailByToken(token);

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
    }

    /*
    *  User Detail Methods
    * */
    @Override
    public Result getUserDetailByToken(String token) {
        // get user email
        String userEmail = getUserEmailByToken(token);

        // find user
        QueryWrapper userWrapper = new QueryWrapper();
        userWrapper.eq("email", userEmail);

        User findUser = baseMapper.selectOne(userWrapper);

        if(findUser == null) {
            return Result.fail().message("Can not find user");
        }

        // form return data
        UserDetailReturnVo userDetailReturnVo = new UserDetailReturnVo();
        BeanUtils.copyProperties(findUser, userDetailReturnVo);

        return Result.ok(userDetailReturnVo).message("Successfully get user detail");
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

    @Override
    public Result updateUserDetail(User user) {
        // find user
        QueryWrapper userWrapper = new QueryWrapper();
        userWrapper.eq("email", user.getEmail());

        User findUser = baseMapper.selectOne(userWrapper);

        if(findUser == null) {
            return Result.fail().message("Can not find user");
        }

        findUser.setUsername(user.getUsername());

        if(user.getPolicy().equals("true")) {
            findUser.setPolicy("T");
        }else {
            findUser.setPolicy("F");
        }

        findUser.setUpdateTime(new Date());

        int isUpdate = baseMapper.update(findUser, userWrapper);

        if(isUpdate < 1) return Result.fail().message("Failed to update user");

        return Result.ok(findUser).message("Successfully updated user");
    }

    @Override
    public Result updateUserAvatar(MultipartFile file, HttpServletRequest request) {
        // find user email
        String token = jwtTokenProvider.resolveToken(request);

        String userEmail = getUserEmailByToken(token);

        // find user
        QueryWrapper userWrapper = new QueryWrapper();
        userWrapper.eq("email", userEmail);

        User findUser = baseMapper.selectOne(userWrapper);

        if(findUser == null) {
            return Result.fail().message("Can not find user");
        }

        // upload file to minio
        List<String> upload = minioUtils.upload(new MultipartFile[]{file});

        findUser.setAvatar("//images.weserv.nl/?url=" + address+"/"+bucketName+"/"+upload.get(0));

        findUser.setUpdateTime(new Date());

        int isUpdate = baseMapper.update(findUser, userWrapper);

        if(isUpdate < 1) return Result.fail().message("Failed to update avatar");

        UserDetailReturnVo userDetailReturnVo = new UserDetailReturnVo();
        BeanUtils.copyProperties(findUser, userDetailReturnVo);

        return Result.ok(userDetailReturnVo).message("Successfully updated avatar");
    }

    @Override
    public Result updateSystemUserPassword(UserUpdatePasswordVo userUpdatePasswordVo) {
        // check email
        boolean isValidEmail = EmailChecker.check(userUpdatePasswordVo.getEmail());

        if(!isValidEmail) return Result.fail().message("Incorrect email format");

        // check password
        boolean isValidPassword = PasswordChecker.check(userUpdatePasswordVo.getNewPassword());

        if(!isValidPassword) return Result.fail().message("Incorrect new password format");

        // find user
        QueryWrapper userWrapper = new QueryWrapper();
        userWrapper.eq("email", userUpdatePasswordVo.getEmail());

        User findUser = baseMapper.selectOne(userWrapper);

        if(findUser == null) {
            return Result.fail().message("Can not find user");
        }else if(findUser.getType().equals("G")) {
            return Result.fail().message("Email registered with Google");
        }

        // get verification code
        String code = redisTemplate.opsForValue().get(userUpdatePasswordVo.getEmail() + " changePassword");

        if(code != null) {
            if(!code.equals(userUpdatePasswordVo.getVerification())) {
                return Result.fail().message("Verification code is incorrect");
            }
        }else {
            return Result.fail().message("Verification is expired");
        }

        // compare the password
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean isMatch = bcryptPasswordEncoder.matches(userUpdatePasswordVo.getOldPassword(), findUser.getPassword());

        if(!isMatch) {
            return Result.fail().message("Incorrect old password");
        }else {
            // update user password
            if(updateUserPassword(findUser.getUsername(), userUpdatePasswordVo.getNewPassword(), userUpdatePasswordVo.getEmail())) {
                redisTemplate.delete(userUpdatePasswordVo.getEmail() + " changePassword");

                return Result.ok().message("Successfully updated password");
            }else {
                redisTemplate.delete(userUpdatePasswordVo.getEmail() + " changePassword");

                return Result.fail().message("Server updated password error");
            }
        }
    }

    @Override
    public boolean updateUserPassword(String username, String password, String email) {
        QueryWrapper userWrapper = new QueryWrapper();
        userWrapper.eq("email", email);

        User findUser = baseMapper.selectOne(userWrapper);

        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodePassword = bcryptPasswordEncoder.encode(password);

        findUser.setPassword(encodePassword);
        findUser.setUpdateTime(new Date());

        int update = baseMapper.update(findUser, userWrapper);

        if(update < 1) return false;

        // add new password to the spring security
        userRepository.addSystemUser(new User(username,
                encodePassword,
                email));

        return true;
    }

    public String getUserEmailByToken(String token) {
        return jwtTokenProvider.getUsername(token);
    }
}
