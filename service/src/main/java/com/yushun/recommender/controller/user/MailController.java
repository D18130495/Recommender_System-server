package com.yushun.recommender.controller.user;

import com.yushun.recommender.security.result.Result;
import com.yushun.recommender.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/sendUserResetPassword")
    public Result sendUserResetPassword(@RequestParam("email") String email) {
        return mailService.sendUserResetPassword(email);
    }

    @GetMapping("/sendChangePasswordVerificationCode")
    public Result sendChangePasswordVerificationCode(@RequestParam("email") String email) {
        return mailService.sendChangePasswordVerificationCode(email);
    }
}
