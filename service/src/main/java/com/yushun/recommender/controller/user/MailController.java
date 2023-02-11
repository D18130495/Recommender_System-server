package com.yushun.recommender.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yushun.recommender.model.common.User;
import com.yushun.recommender.security.result.Result;
import com.yushun.recommender.security.utils.EmailChecker;
import com.yushun.recommender.security.utils.RandomUtil;
import com.yushun.recommender.service.MailService;
import com.yushun.recommender.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Email Controller
 * </p>
 *
 * @author yushun zeng
 * @since 2023-2-11
 */

@CrossOrigin
@RestController
@RequestMapping("/email")
public class MailController {
    @Autowired
    private MailService mailService;

    @GetMapping("/sendUserSystemRegisterVerificationCode")
    public Result sendUserSystemRegisterVerificationCode(@RequestParam("email") String email) {
        return mailService.sendUserSystemRegisterVerificationCode(email);
    }
}
