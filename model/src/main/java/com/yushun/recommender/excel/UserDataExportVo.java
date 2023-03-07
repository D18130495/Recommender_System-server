package com.yushun.recommender.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * <p>
 * User Data Export Vo
 * </p>
 *
 * @author yushun zeng
 * @since 2023-3-7
 */
public class UserDataExportVo {
    @Excel(name = "username", width = 10)
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
