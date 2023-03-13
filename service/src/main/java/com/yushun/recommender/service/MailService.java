package com.yushun.recommender.service;

import com.yushun.recommender.security.result.Result;

import javax.mail.MessagingException;

public interface MailService {
    Result sendUserSystemRegisterVerificationCode(String email) throws MessagingException;

    Result sendUserResetPassword(String email);

    Result sendChangePasswordVerificationCode(String email);
}
