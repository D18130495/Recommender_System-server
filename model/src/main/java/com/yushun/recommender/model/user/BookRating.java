package com.yushun.recommender.model.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yushun.recommender.model.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * Book Rating Model
 * </p>
 *
 * @author yushun zeng
 * @since 2023-2-1
 */

@TableName(value = "ui_book_rating")
public class BookRating extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "isbn")
    @TableField("isbn")
    private String isbn;

    @ApiModelProperty(value = "email")
    @TableField("email")
    private String email;

    @ApiModelProperty(value = "rating")
    @TableField("rating")
    private Float rating;

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
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
