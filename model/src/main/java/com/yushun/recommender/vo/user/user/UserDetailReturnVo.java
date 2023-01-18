package com.yushun.recommender.vo.user.user;

import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * User Detail Result Return Vo
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-16
 */

public class UserDetailReturnVo {
    @TableField("email")
    private String email;

    @TableField("username")
    private String username;

    @TableField("avatar")
    private String avatar;

    @TableField("policy")
    private String policy;

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

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }
}
