package com.yushun.recommender.excel;

import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * Result Export Vo
 * </p>
 *
 * @author yushun zeng
 * @since 2023-3-12
 */
@ExcelTarget("resultEntity")
public class ResultEntity implements Serializable {
    @ExcelEntity(id = "userDetail")
    private UserEntity userDetail;

    @ExcelCollection(name = "movieFavourite", orderNum = "1")
    private List<MovieFavouriteEntity> movieFavourite;

    @ExcelCollection(name = "movieRating", orderNum = "2")
    private List<MovieRatingEntity> movieRating;

    @ExcelCollection(name = "bookFavourite", orderNum = "3")
    private List<BookFavouriteEntity> bookFavourite;

    @ExcelCollection(name = "bookRating", orderNum = "4")
    private List<BookRatingEntity> bookRating;

    public UserEntity getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserEntity userDetail) {
        this.userDetail = userDetail;
    }

    public List<MovieFavouriteEntity> getMovieFavourite() {
        return movieFavourite;
    }

    public void setMovieFavourite(List<MovieFavouriteEntity> movieFavourite) {
        this.movieFavourite = movieFavourite;
    }

    public List<MovieRatingEntity> getMovieRating() {
        return movieRating;
    }

    public void setMovieRating(List<MovieRatingEntity> movieRating) {
        this.movieRating = movieRating;
    }

    public List<BookFavouriteEntity> getBookFavourite() {
        return bookFavourite;
    }

    public void setBookFavourite(List<BookFavouriteEntity> bookFavourite) {
        this.bookFavourite = bookFavourite;
    }

    public List<BookRatingEntity> getBookRating() {
        return bookRating;
    }

    public void setBookRating(List<BookRatingEntity> bookRating) {
        this.bookRating = bookRating;
    }
}