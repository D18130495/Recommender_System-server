package com.yushun.recommender.service;

import com.yushun.recommender.security.result.Result;

import javax.mail.MessagingException;

public interface MailService {
    Result sendUserSystemRegisterVerificationCode(String email) throws MessagingException;

    Result sendUserResetPassword(String email) throws MessagingException;

    Result sendChangePasswordVerificationCode(String email) throws MessagingException;
}
