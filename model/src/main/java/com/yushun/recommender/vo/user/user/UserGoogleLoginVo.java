package com.yushun.recommender.vo.user.user;

import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * User Google Login Vo
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-5
 */

public class UserGoogleLoginVo {
    @TableField("email")
    private String email;

    @TableField("username")
    private String username;

    @TableField("avatar")
    private String avatar;

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
}
