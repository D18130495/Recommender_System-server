package com.yushun.recommender.service;

import com.yushun.recommender.security.result.Result;

public interface MailService {
    Result sendUserSystemRegisterVerificationCode(String email);

    Result sendUserResetPassword(String email);

    Result sendChangePasswordVerificationCode(String email);
}
