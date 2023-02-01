package com.yushun.recommender.vo.user.book;

import java.util.Date;

/**
 * <p>
 * Book Like List Return Vo
 * </p>
 *
 * @author yushun zeng
 * @since 2023-2-1
 */

public class BookLikeListReturnVo {
    private String isbn;

    private String title;

    private String author;

    private String year;

    private String publisher;

    private Float rating;

    private Date updateDate;

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
