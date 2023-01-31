package com.yushun.recommender.model.common.mongoEntity.book;

/**
 * <p>
 * Book Rate Model
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-31
 */

public class BookRate {
    private Integer userId;

    private Float rating;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }
}
