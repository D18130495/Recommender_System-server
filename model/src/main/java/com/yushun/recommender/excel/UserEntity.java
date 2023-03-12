package com.yushun.recommender.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * <p>
 * User Export Vo
 * </p>
 *
 * @author yushun zeng
 * @since 2023-3-12
 */
public class UserEntity implements Serializable {
    @Excel(name = "Username", width = 15, isImportField = "true_st", needMerge = true)
    private String username;

    @Excel(name = "Avatar", width = 30, isImportField = "true_st", needMerge = true)
    private String avatar;

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