package com.yushun.recommender.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * <p>
 * Book Favourite Export Vo
 * </p>
 *
 * @author yushun zeng
 * @since 2023-3-12
 */
public class BookFavouriteEntity implements Serializable {
    @Excel(name = "Book Title", width = 30, isImportField = "true_st")
    private String bookTitle;

    @Excel(name = "Favourite Status", width = 15, isImportField = "true_st")
    private String favouriteStatus;

    public BookFavouriteEntity() {
    }

    public BookFavouriteEntity(String bookTitle, String favouriteStatus) {
        this.bookTitle = bookTitle;
        this.favouriteStatus = favouriteStatus;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getFavouriteStatus() {
        return favouriteStatus;
    }

    public void setFavouriteStatus(String favouriteStatus) {
        this.favouriteStatus = favouriteStatus;
    }
}