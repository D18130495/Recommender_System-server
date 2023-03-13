package com.yushun.recommender.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Movie Rating Export Vo
 * </p>
 *
 * @author yushun zeng
 * @since 2023-3-12
 */
public class MovieRatingEntity implements Serializable {
    @Excel(name = "Movie Title", width = 30, isImportField = "true_st")
    private String movieTitle;

    @Excel(name = "Rating", width = 10, isImportField = "true_st")
    private String movieRating;

    public MovieRatingEntity() {
    }

    public MovieRatingEntity(String movieTitle, String movieRating) {
        this.movieTitle = movieTitle;
        this.movieRating = movieRating;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieRating() {
        return movieRating;
    }

    public void setMovieRating(String movieRating) {
        this.movieRating = movieRating;
    }
}