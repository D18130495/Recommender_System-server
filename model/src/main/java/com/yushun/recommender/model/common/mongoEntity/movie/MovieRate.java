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

    private Float rating;

    private String tags;

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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
