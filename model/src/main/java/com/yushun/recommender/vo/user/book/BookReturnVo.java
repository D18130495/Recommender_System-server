package com.yushun.recommender.vo.user.book;

/**
 * <p>
 * Book Result Return Vo
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-31
 */

public class BookReturnVo {
    private String ISBN;

    private String title;

    private String author;

    private String year;

    private String publisher;

    private String bookImage;

    private String bookImageS;

    private String bookImageM;

    private String bookImageL;

    private String bookLink;

    private String bookDescription;

    private float rate;

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
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

    public String getBookImage() {
        return bookImage;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }

    public String getBookImageS() {
        return bookImageS;
    }

    public void setBookImageS(String bookImageS) {
        this.bookImageS = bookImageS;
    }

    public String getBookImageM() {
        return bookImageM;
    }

    public void setBookImageM(String bookImageM) {
        this.bookImageM = bookImageM;
    }

    public String getBookImageL() {
        return bookImageL;
    }

    public void setBookImageL(String bookImageL) {
        this.bookImageL = bookImageL;
    }

    public String getBookLink() {
        return bookLink;
    }

    public void setBookLink(String bookLink) {
        this.bookLink = bookLink;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
