package com.yushun.recommender.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * <p>
 * Book Rating Export Vo
 * </p>
 *
 * @author yushun zeng
 * @since 2023-3-12
 */
public class BookRatingEntity implements Serializable {
    @Excel(name = "Book Title", width = 30, isImportField = "true_st")
    private String bookTitle;

    @Excel(name = "Rating", width = 10, isImportField = "true_st")
    private String bookRating;

    public BookRatingEntity() {
    }

    public BookRatingEntity(String bookTitle, String bookRating) {
        this.bookTitle = bookTitle;
        this.bookRating = bookRating;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookRating() {
        return bookRating;
    }

    public void setBookRating(String bookRating) {
        this.bookRating = bookRating;
    }
}