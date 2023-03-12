package com.yushun.recommender.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * <p>
 * Movie Favourite Export Vo
 * </p>
 *
 * @author yushun zeng
 * @since 2023-3-12
 */
public class MovieFavouriteEntity implements Serializable {
    @Excel(name = "Movie Title", width = 30, isImportField = "true_st")
    private String movieTitle;

    @Excel(name = "Favourite Status", width = 15, isImportField = "true_st")
    private String favouriteStatus;

    public MovieFavouriteEntity() {
    }

    public MovieFavouriteEntity(String movieTitle, String favouriteStatus) {
        this.movieTitle = movieTitle;
        this.favouriteStatus = favouriteStatus;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getFavouriteStatus() {
        return favouriteStatus;
    }

    public void setFavouriteStatus(String favouriteStatus) {
        this.favouriteStatus = favouriteStatus;
    }
}