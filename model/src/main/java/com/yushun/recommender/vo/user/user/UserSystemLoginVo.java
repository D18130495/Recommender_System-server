package com.yushun.recommender.vo.user.user;

import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * User System Login Vo
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-14
 */

public class UserSystemLoginVo {
    @TableField("email")
    private String email;

    @TableField("password")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
