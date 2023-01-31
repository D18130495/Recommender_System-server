package com.yushun.recommender.model.common.mongoEntity.book;

import com.yushun.recommender.model.base.BaseMongoEntity;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * <p>
 * MongoDB Book Model
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-31
 */

@Document("book")
public class Book extends BaseMongoEntity {
    private static final long serialVersionUID = 1L;

    private String ISBN;

    private String title;

    private String author;

    private String year;

    private String publisher;

    private String bookImageS;

    private String bookImageM;

    private String bookImageL;

    private List<BookRate> rate;

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

    public List<BookRate> getRate() {
        return rate;
    }

    public void setRate(List<BookRate> rate) {
        this.rate = rate;
    }
}
