package com.yushun.recommender.model.common.mongoEntity.movie;

/**
 * <p>
 * Movie Rate Model
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-22
 */

public class MovieRate {
    private Integer userId;

    private Integer rating;

    private String tags;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
