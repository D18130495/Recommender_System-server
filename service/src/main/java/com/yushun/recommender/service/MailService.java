package com.yushun.recommender.service;

import com.yushun.recommender.security.result.Result;

public interface MailService {
    Result sendUserSystemRegisterVerificationCode(String email);
}
