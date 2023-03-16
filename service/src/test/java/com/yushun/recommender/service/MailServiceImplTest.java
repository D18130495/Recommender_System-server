package com.yushun.recommender.service;

import com.yushun.recommender.RecommenderApplication;
import com.yushun.recommender.security.result.Result;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RecommenderApplication.class)
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MailServiceImplTest {
    @Autowired
    private MailService mailService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * test register send email
     */
    @Order(1)
    @Test(timeout = 30000)
    @Transactional
    public void sendUserSystemRegisterVerificationCode_sendCode_success() throws MessagingException {
        Result result = mailService.sendUserSystemRegisterVerificationCode("d18130495@codeTest.com");

        Assert.assertEquals("Successfully send Verification Code", result.getMessage());
    }

    @Order(2)
    @Test(timeout = 30000)
    @Transactional
    public void sendUserSystemRegisterVerificationCode_invalidEmail_fail() throws MessagingException {
        Result result = mailService.sendUserSystemRegisterVerificationCode("123");

        Assert.assertEquals("Incorrect email format", result.getMessage());
    }

    @Order(3)
    @Test(timeout = 30000)
    @Transactional
    public void sendUserSystemRegisterVerificationCode_codeStillValid_fail() throws MessagingException {
        Result result = mailService.sendUserSystemRegisterVerificationCode("d18130495@codeTest.com");

        Assert.assertEquals("Please check your email, the Verification Code is still valid", result.getMessage());

        redisTemplate.delete("d18130495@codeTest.com");
    }

    @Order(4)
    @Test(timeout = 30000)
    @Transactional
    public void sendUserSystemRegisterVerificationCode_emailAlreadyRegistered_fail() throws MessagingException {
        Result result = mailService.sendUserSystemRegisterVerificationCode("990415zys@gmail.com");

        Assert.assertEquals("This email has already been registered", result.getMessage());
    }

    /**
     * test reset password email
     */
    @Order(5)
    @Test(timeout = 30000)
    @Transactional
    public void sendUserResetPassword_sendPassword_success() throws MessagingException {
        Result result = mailService.sendUserResetPassword("990415zys@gmail.co");

        Assert.assertEquals("Successfully send new password", result.getMessage());
    }

    @Order(6)
    @Test(timeout = 30000)
    @Transactional
    public void sendUserResetPassword_passwordAlreadySend_fail() throws MessagingException {
        redisTemplate.opsForValue().set("990415zys@gmail.co password", "123456");

        Result result = mailService.sendUserResetPassword("990415zys@gmail.co");

        Assert.assertEquals("Please check your email, the new password already send", result.getMessage());

        redisTemplate.delete("990415zys@gmail.co password");
    }

    @Order(7)
    @Test(timeout = 30000)
    @Transactional
    public void sendUserResetPassword_invalidEmail_fail() throws MessagingException {
        Result result = mailService.sendUserResetPassword("d18130495mytudublin.ie");

        Assert.assertEquals("Incorrect email format", result.getMessage());
    }


    @Order(8)
    @Test(timeout = 30000)
    @Transactional
    public void sendUserResetPassword_emailNotRegistered_fail() throws MessagingException {
        Result result = mailService.sendUserResetPassword("990415zys@gmail.c");

        Assert.assertEquals("This email has not been registered", result.getMessage());
    }

    @Order(9)
    @Test(timeout = 30000)
    @Transactional
    public void sendUserResetPassword_emailRegisteredWithGoogle_fail() throws MessagingException {
        Result result = mailService.sendUserResetPassword("990415zys@gmail.com");

        Assert.assertEquals("This email registered with Google", result.getMessage());
    }

    /**
     * test change password email
     */
    @Order(10)
    @Test(timeout = 30000)
    @Transactional
    public void sendChangePasswordVerificationCode_sendCode_success() throws MessagingException {
        Result result = mailService.sendChangePasswordVerificationCode("990415zys@gmail.co");

        Assert.assertEquals("Successfully send Verification Code", result.getMessage());

        Result result2 = mailService.sendChangePasswordVerificationCode("990415zys@gmail.co");

        Assert.assertEquals("Please check your email, the Verification Code is still valid", result2.getMessage());

        redisTemplate.delete("990415zys@gmail.co changePassword");
    }

    @Order(11)
    @Test(timeout = 30000)
    @Transactional
    public void sendChangePasswordVerificationCode_invalidEmail_fail() throws MessagingException {
        Result result = mailService.sendChangePasswordVerificationCode("d18130495mytudublin.ie");

        Assert.assertEquals("Incorrect email format", result.getMessage());
    }

    @Order(12)
    @Test(timeout = 30000)
    @Transactional
    public void sendChangePasswordVerificationCode_emailNotRegistered_fail() throws MessagingException {
        Result result = mailService.sendChangePasswordVerificationCode("990415zys@gmail.c");

        Assert.assertEquals("This email has not been registered", result.getMessage());
    }

    @Order(13)
    @Test(timeout = 30000)
    @Transactional
    public void sendChangePasswordVerificationCode_emailRegisteredWithGoogle_fail() throws MessagingException {
        Result result = mailService.sendChangePasswordVerificationCode("990415zys@gmail.com");

        Assert.assertEquals("This email registered with Google", result.getMessage());
    }
}
