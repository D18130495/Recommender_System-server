package com.yushun.recommender.model.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yushun.recommender.model.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * Movie Rating Model
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-27
 */

@TableName(value = "ui_movie_rating")
public class MovieRating extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "movieId")
    @TableField("movieId")
    private Integer movieId;

    @ApiModelProperty(value = "email")
    @TableField("email")
    private String email;

    @ApiModelProperty(value = "rating")
    @TableField("rating")
    private Float rating;

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }
}
