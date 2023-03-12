package com.yushun.recommender.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.util.Map;

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

    @Excel(name = "list", width = 10)
    private Map<Integer, String> map;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Map<Integer, String> getMap() {
        return map;
    }

    public void setMap(Map<Integer, String> map) {
        this.map = map;
    }
}
