package com.yushun.recommender.vo.user.user;

import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * User Result Return Vo
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-4
 */

public class UserReturnVo {
    @TableField("email")
    private String email;

    @TableField("username")
    private String username;

    @TableField("avatar")
    private String avatar;

    @TableField("token")
    private String token;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
