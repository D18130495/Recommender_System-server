package com.yushun.recommender.vo.user.user;

import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * System User Update Password Vo
 * </p>
 *
 * @author yushun zeng
 * @since 2023-2-14
 */

public class UserUpdatePasswordVo {
    @TableField("email")
    private String email;

    private String oldPassword;

    private String newPassword;

    private String verification;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }
}
