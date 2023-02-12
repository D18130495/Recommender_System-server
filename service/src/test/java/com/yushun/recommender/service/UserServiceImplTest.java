package com.yushun.recommender.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yushun.recommender.RecommenderApplication;
import com.yushun.recommender.model.common.User;
import com.yushun.recommender.security.result.Result;
import com.yushun.recommender.security.user.UserRepository;
import com.yushun.recommender.security.utils.JwtTokenProvider;
import com.yushun.recommender.vo.user.user.UserDetailReturnVo;
import com.yushun.recommender.vo.user.user.UserGoogleLoginVo;
import com.yushun.recommender.vo.user.user.UserSystemLoginVo;
import com.yushun.recommender.vo.user.user.UserSystemRegisterVo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;

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

    /**
     * test google login
     */
    @Order(1)
    @Test(timeout = 30000)
    @Transactional
    public void googleLogin_successfulLogin_success() {
        UserGoogleLoginVo userGoogleLoginVo = new UserGoogleLoginVo();
        userGoogleLoginVo.setAvatar("https://lh3.googleusercontent.com/a/AEdFTp64EvN_8F_UkmI59xLWoRdo_Xrceo77kZcdONsR=s96-c");
        userGoogleLoginVo.setEmail("990415zys@gmail.com");
        userGoogleLoginVo.setUsername("Yushun Zeng");

        Result result = userService.googleLogin(userGoogleLoginVo);

        Assert.assertEquals("Successfully login", result.getMessage());
    }

    @Order(2)
    @Test(timeout = 30000)
    @Transactional
    public void googleLogin_registerNewUser_success() {
        UserGoogleLoginVo userGoogleLoginVo = new UserGoogleLoginVo();
        userGoogleLoginVo.setAvatar("");
        userGoogleLoginVo.setEmail("test@test.com");
        userGoogleLoginVo.setUsername("Test");

        Result result = userService.googleLogin(userGoogleLoginVo);

        Assert.assertEquals("Successfully login", result.getMessage());
    }

    @Order(3)
    @Test(timeout = 30000)
    @Transactional
    public void googleLogin_accountRegisterBySystem_fail() {
        UserGoogleLoginVo userGoogleLoginVo = new UserGoogleLoginVo();
        userGoogleLoginVo.setAvatar("url=http://63.35.221.13:9000/avatar/imageprocessing_1675431077207.jpeg");
        userGoogleLoginVo.setEmail("990415zys@gmail.co");
        userGoogleLoginVo.setUsername("Yuhong He");

        Result result = userService.googleLogin(userGoogleLoginVo);

        Assert.assertEquals("Email address already been used for register with System", result.getMessage());
    }

    /**
     * test system login
     */
    @Order(4)
    @Test(timeout = 30000)
    @Transactional
    public void systemLogin_emailFormatWrong_fail() {
        UserSystemLoginVo userSystemLoginVo = new UserSystemLoginVo();
        userSystemLoginVo.setEmail("990415zysgmail.com");
        userSystemLoginVo.setPassword("123123");

        Result result = userService.userSystemLogin(userSystemLoginVo);

        Assert.assertEquals("Incorrect email format", result.getMessage());
    }

    @Order(5)
    @Test(timeout = 30000)
    @Transactional
    public void systemLogin_passwordFormatWrong_fail() {
        UserSystemLoginVo userSystemLoginVo = new UserSystemLoginVo();
        userSystemLoginVo.setEmail("d18130495@mytudublin.ie");
        userSystemLoginVo.setPassword("123123");

        Result result = userService.userSystemLogin(userSystemLoginVo);

        Assert.assertEquals("Incorrect password format", result.getMessage());
    }

    @Order(6)
    @Test(timeout = 30000)
    @Transactional
    public void systemLogin_userNotFind_fail() {
        UserSystemLoginVo userSystemLoginVo = new UserSystemLoginVo();
        userSystemLoginVo.setEmail("990415zys@gmail.c");
        userSystemLoginVo.setPassword("Qpuur990415#zys");

        Result result = userService.userSystemLogin(userSystemLoginVo);

        Assert.assertEquals("Email has not been registered", result.getMessage());
    }

    @Order(7)
    @Test(timeout = 30000)
    @Transactional
    public void systemLogin_userRegisterByGoogle_fail() {
        UserSystemLoginVo userSystemLoginVo = new UserSystemLoginVo();
        userSystemLoginVo.setEmail("990415zys@gmail.com");
        userSystemLoginVo.setPassword("Qpuur990415#zys");

        Result result = userService.userSystemLogin(userSystemLoginVo);

        Assert.assertEquals("Email registered with Google", result.getMessage());
    }

    @Order(8)
    @Test(timeout = 30000)
    @Transactional
    public void systemLogin_incorrectPassword_fail() {
        UserSystemLoginVo userSystemLoginVo = new UserSystemLoginVo();
        userSystemLoginVo.setEmail("d18130495@mytudublin.ie");
        userSystemLoginVo.setPassword("Qpuur990415#zy");

        Result result = userService.userSystemLogin(userSystemLoginVo);

        Assert.assertEquals("Incorrect password", result.getMessage());
    }

    @Order(9)
    @Test(timeout = 30000)
    @Transactional
    public void systemLogin_validUser_success() {
        UserSystemLoginVo userSystemLoginVo = new UserSystemLoginVo();
        userSystemLoginVo.setEmail("d18130495@mytudublin.ie");
        userSystemLoginVo.setPassword("Qpuur990415#zys");

        Result result = userService.userSystemLogin(userSystemLoginVo);

        Assert.assertEquals("Successfully login", result.getMessage());
    }

    /**
     * test system register
     */
    @Order(10)
    @Test(timeout = 30000)
    @Transactional
    public void userSystemRegister_validInformation_success() {
        UserSystemRegisterVo userSystemRegisterVo = new UserSystemRegisterVo();
        userSystemRegisterVo.setEmail("d18130495@mytudublin.com");
        userSystemRegisterVo.setPassword("Qpuur990415#zys");
        userSystemRegisterVo.setUsername("Test");
        userSystemRegisterVo.setVerification("123456");

        Result result = userService.userSystemRegister(userSystemRegisterVo);

        Assert.assertEquals("Successfully registered, will automatically login with in 3 second", result.getMessage());
    }

    @Order(11)
    @Test(timeout = 30000)
    @Transactional
    public void userSystemRegister_emailFormatWrong_fail() {
        UserSystemRegisterVo userSystemRegisterVo = new UserSystemRegisterVo();
        userSystemRegisterVo.setEmail("990415zysgmail.com");

        Result result = userService.userSystemRegister(userSystemRegisterVo);

        Assert.assertEquals("Incorrect email format", result.getMessage());
    }

    @Order(12)
    @Test(timeout = 30000)
    @Transactional
    public void userSystemRegister_passwordFormatWrong_fail() {
        UserSystemRegisterVo userSystemRegisterVo = new UserSystemRegisterVo();
        userSystemRegisterVo.setEmail("990415zys@gmail.com");
        userSystemRegisterVo.setPassword("qpuur990415#zys");

        Result result = userService.userSystemRegister(userSystemRegisterVo);
        Assert.assertEquals("Incorrect password format", result.getMessage());

        userSystemRegisterVo.setPassword("Qpuur#zys");
        Result result2 = userService.userSystemRegister(userSystemRegisterVo);
        Assert.assertEquals("Incorrect password format", result.getMessage());

        userSystemRegisterVo.setPassword("Qpuur990415zys");
        Result result3 = userService.userSystemRegister(userSystemRegisterVo);
        Assert.assertEquals("Incorrect password format", result.getMessage());
    }

    @Order(13)
    @Test(timeout = 30000)
    @Transactional
    public void userSystemRegister_userAlreadyRegisteredWithSystem_fail() {
        UserSystemRegisterVo userSystemRegisterVo = new UserSystemRegisterVo();
        userSystemRegisterVo.setEmail("990415zys@gmail.co");
        userSystemRegisterVo.setPassword("Qpuur990415#zys");

        Result result = userService.userSystemRegister(userSystemRegisterVo);

        Assert.assertEquals("Email address already been used for register with System", result.getMessage());
    }

    @Order(14)
    @Test(timeout = 30000)
    @Transactional
    public void userSystemRegister_userAlreadyRegisteredWithGoogle_fail() {
        UserSystemRegisterVo userSystemRegisterVo = new UserSystemRegisterVo();
        userSystemRegisterVo.setEmail("990415zys@gmail.com");
        userSystemRegisterVo.setPassword("Qpuur990415#zys");

        Result result = userService.userSystemRegister(userSystemRegisterVo);

        Assert.assertEquals("Email address already been used for register with Google", result.getMessage());
    }

    @Order(15)
    @Test(timeout = 30000)
    @Transactional
    public void userSystemRegister_verificationCodeIncorrect_fail() {
        UserSystemRegisterVo userSystemRegisterVo = new UserSystemRegisterVo();
        userSystemRegisterVo.setEmail("d18130495@mytudublin.com");
        userSystemRegisterVo.setPassword("Qpuur990415#zys");
        userSystemRegisterVo.setVerification("12345");

        Result result = userService.userSystemRegister(userSystemRegisterVo);

        Assert.assertEquals("Verification code is incorrect", result.getMessage());
    }

    @Order(16)
    @Test(timeout = 30000)
    @Transactional
    public void userSystemRegister_verificationCodeExpired_fail() {
        UserSystemRegisterVo userSystemRegisterVo = new UserSystemRegisterVo();
        userSystemRegisterVo.setEmail("d18130495@mytudublin.co");
        userSystemRegisterVo.setPassword("Qpuur990415#zys");
        userSystemRegisterVo.setVerification("123456");

        Result result = userService.userSystemRegister(userSystemRegisterVo);

        Assert.assertEquals("Verification is expired", result.getMessage());
    }

    /**
     * test get user detail by token
     */
    @Order(17)
    @Test(timeout = 30000)
    @Transactional
    public void getUserDetailByToken_googleValidToken_success() {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("990415zys@gmail.com", "990415zys@gmail.com"));

        String newToken = jwtTokenProvider.createToken("990415zys@gmail.com",
                userRepository.findByUsername("990415zys@gmail.com")
                        .orElseThrow(() -> new UsernameNotFoundException("User " + "990415zys@gmail.com" + "not found")).getRoles()
        );

        Result result = userService.getUserDetailByToken(newToken);

        Assert.assertEquals("Successfully refresh login time", result.getMessage());
    }

    @Order(18)
    @Test(timeout = 30000)
    @Transactional
    public void getUserDetailByToken_systemValidToken_success() {
        QueryWrapper userWrapper = new QueryWrapper();
        userWrapper.eq("email", "990415zys@gmail.co");

        User findUser = userService.getOne(userWrapper);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("990415zys@gmail.co", findUser.getPassword()));

        String newToken = jwtTokenProvider.createToken("990415zys@gmail.co",
                userRepository.findByUsername("990415zys@gmail.co")
                        .orElseThrow(() -> new UsernameNotFoundException("User " + "990415zys@gmail.co" + "not found")).getRoles()
        );

        Result result = userService.getUserDetailByToken(newToken);

        Assert.assertEquals("Successfully refresh login time", result.getMessage());
    }

    @Order(19)
    @Test(timeout = 30000)
    @Transactional
    public void getUserDetailByToken_userNotFound_fail() {
        // add user to spring security
        userRepository.addGoogleUser(new User("990415zys@gmail.c",
                "990415zys@gmail.c"));

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("990415zys@gmail.c", "990415zys@gmail.c"));

        String newToken = jwtTokenProvider.createToken("990415zys@gmail.c",
                userRepository.findByUsername("990415zys@gmail.c")
                        .orElseThrow(() -> new UsernameNotFoundException("User " + "990415zys@gmail.c" + "not found")).getRoles()
        );

        Result result = userService.getUserDetailByToken(newToken);

        Assert.assertEquals("Can not find user", result.getMessage());
    }

    @Order(20)
    @Test(timeout = 30000)
    @Transactional
    public void getUserDetailByToken_invalidToken_exception() {
        Result result = userService.getUserDetailByToken("1234567890");

        Assert.assertEquals("Invalid token", result.getMessage());
//        assertThrows(JwtAuthenticationException.class, () -> userService.getUserDetailByToken("1234567890"));
    }

    /**
     * test get user detail by email
     */
    @Order(21)
    @Test(timeout = 30000)
    @Transactional
    public void getUserDetailByEmail_userFind_UserDetailReturnVo() {
        UserDetailReturnVo userDetailByEmail = userService.getUserDetailByEmail("990415zys@gmail.com");

        Assert.assertEquals(userDetailByEmail.getUsername(), "Yushun Zeng");
    }

    @Order(22)
    @Test(timeout = 30000)
    @Transactional
    public void getUserDetailByEmail_userNotFind_null() {
        UserDetailReturnVo userDetailByEmail = userService.getUserDetailByEmail("990415zys@gmail.c");

        Assert.assertNull(userDetailByEmail);
    }
}
