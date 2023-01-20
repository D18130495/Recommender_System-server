package com.yushun.recommender.minio.vo;

/**
 * <p>
 * Minio Object
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-20
 */

public class MinioObject {
    private String objectName;
    private Long size;

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
