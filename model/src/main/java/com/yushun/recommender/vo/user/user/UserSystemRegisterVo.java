package com.yushun.recommender.vo.user.user;

import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * User Sign Up Vo
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-5
 */

public class UserSystemRegisterVo {
    @TableField("username")
    private String username;

    @Override
    public String toString() {
        return "UserSystemRegisterVo{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @TableField("email")
    private String email;

    @TableField("password")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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