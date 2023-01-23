package com.yushun.recommender.model.base;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * MongoDB Base Model
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-22
 */

public class BaseMongoEntity implements Serializable {
    @MongoId
    private String id;

    private Date createTime;

    private Date updateTime;

    // 0: not delete, 1: deleted
    private Integer isDeleted;

    @Transient
    private Map<String,Object> param = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }
}