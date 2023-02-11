package com.yushun.recommender.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yushun.recommender.email.EmailSender;
import com.yushun.recommender.model.common.User;
import com.yushun.recommender.security.result.Result;
import com.yushun.recommender.security.utils.EmailChecker;
import com.yushun.recommender.security.utils.RandomUtil;
import com.yushun.recommender.service.MailService;
import com.yushun.recommender.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Email Service Implementation
 * </p>
 *
 * @author yushun zeng
 * @since 2023-2-11
 */

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private UserService userService;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Result sendUserSystemRegisterVerificationCode(String email) {
        // check email
        boolean isValidEmail = EmailChecker.check(email);

        if(!isValidEmail) return Result.fail().message("Incorrect email format");

        // find if the email is already registered
        QueryWrapper userWrapper = new QueryWrapper();
        userWrapper.eq("email", email);

        User findUser = userService.getOne(userWrapper);

        if(findUser == null) {
            String code = redisTemplate.opsForValue().get(email);

            if(code != null) {
                return Result.fail().message("Please check you email, the Verification Code is still valid");
            }

            // generate random Verification Code
            String newCode = RandomUtil.getSixBitRandom();

            // send email
            boolean isSend = emailSender.sendMail(email, newCode);

            if(isSend) {
                redisTemplate.opsForValue().set(email, newCode, 5, TimeUnit.MINUTES);

                return Result.ok().message("Successfully send Verification Code");
            }else {
                return Result.fail().message("Email send failed");
            }
        }else {
            return Result.fail().message("This email has already been registered");
        }
    }
}
