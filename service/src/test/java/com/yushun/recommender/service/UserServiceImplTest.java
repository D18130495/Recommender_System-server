package com.yushun.recommender.service;

import com.yushun.recommender.RecommenderApplication;
import com.yushun.recommender.model.common.User;
import com.yushun.recommender.security.result.Result;
import com.yushun.recommender.security.user.UserRepository;
import com.yushun.recommender.security.utils.JwtTokenProvider;
import com.yushun.recommender.vo.user.user.UserDetailReturnVo;
import com.yushun.recommender.vo.user.user.UserUpdatePasswordVo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RecommenderApplication.class)
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceImplTest {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * test get user detail by token
     */
    @Order(1)
    @Test(timeout = 30000)
    @Transactional
    public void getUserDetailByToken_validUser_success() {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("990415zys@gmail.com", "990415zys@gmail.com"));

        String newToken = jwtTokenProvider.createToken("990415zys@gmail.com",
                userRepository.findByUsername("990415zys@gmail.com")
                        .orElseThrow(() -> new UsernameNotFoundException("User " + "990415zys@gmail.com" + "not found")).getRoles()
        );

        Result result = userService.getUserDetailByToken(newToken);

        Assert.assertEquals("Successfully get user detail", result.getMessage());
    }

    @Order(2)
    @Test(timeout = 30000)
    @Transactional
    public void getUserDetailByToken_userNotFind_fail() {
        userRepository.addGoogleUser(new User("d18130495@mytudublin.com",
                "d18130495@mytudublin.com"));

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("d18130495@mytudublin.com", "d18130495@mytudublin.com"));

        String newToken = jwtTokenProvider.createToken("d18130495@mytudublin.com",
                userRepository.findByUsername("d18130495@mytudublin.com")
                        .orElseThrow(() -> new UsernameNotFoundException("User " + "d18130495@mytudublin.com" + "not found")).getRoles()
        );

        Result result = userService.getUserDetailByToken(newToken);

        Assert.assertEquals("Can not find user", result.getMessage());
    }

    /**
     * test get user detail by email
     */
    @Order(3)
    @Test(timeout = 30000)
    @Transactional
    public void getUserDetailByEmail_validUser_UserDetailReturnVo() {
        UserDetailReturnVo userDetailByEmail = userService.getUserDetailByEmail("990415zys@gmail.com");

        Assert.assertEquals(userDetailByEmail.getUsername(), "Yushun Zeng");
    }

    @Order(4)
    @Test(timeout = 30000)
    @Transactional
    public void getUserDetailByEmail_userNotFind_null() {
        UserDetailReturnVo userDetailByEmail = userService.getUserDetailByEmail("990415zys@gmail.c");

        Assert.assertNull(userDetailByEmail);
    }

    /**
     * test update user detail
     */
    @Order(5)
    @Test(timeout = 30000)
    @Transactional
    public void updateUserDetail_validUserPolicyTrue_success() {
        User user = new User();
        user.setUsername("Ziyi");
        user.setEmail("d18130495@mytudublin.ie");
        user.setPolicy("true");

        Result result = userService.updateUserDetail(user);

        Assert.assertEquals("Successfully updated user", result.getMessage());
    }

    @Order(6)
    @Test(timeout = 30000)
    @Transactional
    public void updateUserDetail_validUserPolicyFalse_success() {
        User user = new User();
        user.setUsername("Ziyi");
        user.setEmail("d18130495@mytudublin.ie");
        user.setPolicy("false");

        Result result = userService.updateUserDetail(user);

        Assert.assertEquals("Successfully updated user", result.getMessage());
    }

    @Order(7)
    @Test(timeout = 30000)
    @Transactional
    public void updateUserDetail_userNotFind_fail() {
        User user = new User();
        user.setUsername("Ziyi");
        user.setEmail("d18130495@mytudublin.com");
        user.setPolicy("false");

        Result result = userService.updateUserDetail(user);

        Assert.assertEquals("Can not find user", result.getMessage());
    }

    /**
     * test update user avatar
     */
    @Order(8)
    @Test(timeout = 30000)
    @Transactional
    public void updateUserAvatar_validUser_success() {

//        Result result = userService.updateUserAvatar();
//
//        Assert.assertEquals("Successfully updated avatar", result.getMessage());
    }

    @Order(9)
    @Test(timeout = 30000)
    @Transactional
    public void updateUserAvatar_userNotFind_fail() {

//        Result result = userService.updateUserAvatar();
//
//        Assert.assertEquals("Can not find user", result.getMessage());
    }

    /**
     * test update system user password
     */

    @Order(10)
    @Test(timeout = 30000)
    @Transactional
    public void updateSystemUserPassword_validUser_success() {
        redisTemplate.opsForValue().set("d18130495@mytudublin.ie changePassword", "123456");

        UserUpdatePasswordVo userUpdatePasswordVo = new UserUpdatePasswordVo();
        userUpdatePasswordVo.setEmail("d18130495@mytudublin.ie");
        userUpdatePasswordVo.setOldPassword("Qpuur990415#zys");
        userUpdatePasswordVo.setNewPassword("Qpuur990415#zy");
        userUpdatePasswordVo.setVerification("123456");

        Result result = userService.updateSystemUserPassword(userUpdatePasswordVo);

        Assert.assertEquals("Successfully updated password", result.getMessage());
    }

    @Order(11)
    @Test(timeout = 30000)
    @Transactional
    public void updateSystemUserPassword_invalidEmail_fail() {
        UserUpdatePasswordVo userUpdatePasswordVo = new UserUpdatePasswordVo();
        userUpdatePasswordVo.setEmail("d18130495mytudublin.ie");

        Result result = userService.updateSystemUserPassword(userUpdatePasswordVo);

        Assert.assertEquals("Incorrect email format", result.getMessage());
    }

    @Order(12)
    @Test(timeout = 30000)
    @Transactional
    public void updateSystemUserPassword_invalidNewPassword_fail() {
        UserUpdatePasswordVo userUpdatePasswordVo = new UserUpdatePasswordVo();
        userUpdatePasswordVo.setEmail("d18130495@mytudublin.ie");
        userUpdatePasswordVo.setNewPassword("Qpuur990415zys");

        Result result = userService.updateSystemUserPassword(userUpdatePasswordVo);

        Assert.assertEquals("Incorrect new password format", result.getMessage());
    }

    @Order(13)
    @Test(timeout = 30000)
    @Transactional
    public void updateSystemUserPassword_userNotFind_fail() {
        UserUpdatePasswordVo userUpdatePasswordVo = new UserUpdatePasswordVo();
        userUpdatePasswordVo.setEmail("d1813049@mytudublin.ie");
        userUpdatePasswordVo.setNewPassword("Qpuur990415#zys");

        Result result = userService.updateSystemUserPassword(userUpdatePasswordVo);

        Assert.assertEquals("Can not find user", result.getMessage());
    }

    @Order(14)
    @Test(timeout = 30000)
    @Transactional
    public void updateSystemUserPassword_registerWithGoogle_fail() {
        UserUpdatePasswordVo userUpdatePasswordVo = new UserUpdatePasswordVo();
        userUpdatePasswordVo.setEmail("990415zys@gmail.com");
        userUpdatePasswordVo.setNewPassword("Qpuur990415#zys");

        Result result = userService.updateSystemUserPassword(userUpdatePasswordVo);

        Assert.assertEquals("Email registered with Google", result.getMessage());
    }

    @Order(15)
    @Test(timeout = 30000)
    @Transactional
    public void updateSystemUserPassword_incorrectVerificationCode_fail() {
        redisTemplate.opsForValue().set("d18130495@mytudublin.ie changePassword", "123456");

        UserUpdatePasswordVo userUpdatePasswordVo = new UserUpdatePasswordVo();
        userUpdatePasswordVo.setEmail("d18130495@mytudublin.ie");
        userUpdatePasswordVo.setNewPassword("Qpuur990415#zy");
        userUpdatePasswordVo.setVerification("111111");

        Result result = userService.updateSystemUserPassword(userUpdatePasswordVo);

        redisTemplate.delete("d18130495@mytudublin.ie changePassword");

        Assert.assertEquals("Verification code is incorrect", result.getMessage());
    }

    @Order(16)
    @Test(timeout = 30000)
    @Transactional
    public void updateSystemUserPassword_verificationCodeExpired_fail() {
        UserUpdatePasswordVo userUpdatePasswordVo = new UserUpdatePasswordVo();
        userUpdatePasswordVo.setEmail("d18130495@mytudublin.ie");
        userUpdatePasswordVo.setNewPassword("Qpuur990415#zys");
        userUpdatePasswordVo.setVerification("123456");

        Result result = userService.updateSystemUserPassword(userUpdatePasswordVo);

        Assert.assertEquals("Verification is expired", result.getMessage());
    }

    @Order(17)
    @Test(timeout = 30000)
    @Transactional
    public void updateSystemUserPassword_incorrectOldPassword_fail() {
        redisTemplate.opsForValue().set("d18130495@mytudublin.ie changePassword", "123456");

        UserUpdatePasswordVo userUpdatePasswordVo = new UserUpdatePasswordVo();
        userUpdatePasswordVo.setEmail("d18130495@mytudublin.ie");
        userUpdatePasswordVo.setOldPassword("Qpuur990415#z");
        userUpdatePasswordVo.setNewPassword("Qpuur990415#zy");
        userUpdatePasswordVo.setVerification("123456");

        Result result = userService.updateSystemUserPassword(userUpdatePasswordVo);

        redisTemplate.delete("d18130495@mytudublin.ie changePassword");

        Assert.assertEquals("Incorrect old password", result.getMessage());
    }
}
