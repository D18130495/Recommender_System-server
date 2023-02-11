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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RecommenderApplication.class)
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MailServiceImplTest {
    @Autowired
    private MailService mailService;

    @Autowired
    private UserService userService;

    /**
     * test register send email
     */
    @Order(1)
    @Test(timeout = 30000)
    @Transactional
    public void sendUserSystemRegisterVerificationCode_sendCode_success() {
        Result result = mailService.sendUserSystemRegisterVerificationCode("d18130495@mytudublin.ie");

        Assert.assertEquals("Successfully send Verification Code", result.getMessage());
    }

    @Order(2)
    @Test(timeout = 30000)
    @Transactional
    public void sendUserSystemRegisterVerificationCode_invalidEmail_fail() {
        Result result = mailService.sendUserSystemRegisterVerificationCode("123");

        Assert.assertEquals("Incorrect email format", result.getMessage());
    }

    @Order(3)
    @Test(timeout = 30000)
    @Transactional
    public void sendUserSystemRegisterVerificationCode_codeStillValid_fail() {
        Result result = mailService.sendUserSystemRegisterVerificationCode("d18130495@mytudublin.ie");

        Assert.assertEquals("Please check you email, the Verification Code is still valid", result.getMessage());
    }

    @Order(4)
    @Test(timeout = 30000)
    @Transactional
    public void sendUserSystemRegisterVerificationCode_emailAlreadyRegistered_fail() {
        Result result = mailService.sendUserSystemRegisterVerificationCode("990415zys@gmail.com");

        Assert.assertEquals("This email has already been registered", result.getMessage());
    }
}
