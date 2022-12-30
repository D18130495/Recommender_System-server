package com.yushun.recommender.model.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yushun.recommender.model.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * user
 * </p>
 *
 * @author yushun zeng
 * @since 2022-12-30
 */

@TableName("ui_user")
public class User extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "username")
    @TableField("username")
    private String username;

    @ApiModelProperty(value = "password")
    @JsonIgnore
    @TableField("password")
    private String password;

    @ApiModelProperty(value = "email")
    @TableField("email")
    private String email;

    @ApiModelProperty(value = "avatar")
    @TableField("avatar")
    private String avatar;

    @ApiModelProperty(value = "T represent accept, F represent reject")
    @TableField("policy")
    private String policy;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
