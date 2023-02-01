package com.yushun.recommender.vo.user.book;

/**
 * <p>
 * Book Rating Return Vo
 * </p>
 *
 * @author yushun zeng
 * @since 2023-2-1
 */

public class BookRatingReturnVo {
    private String isbn;

    private String email;

    private Float rating;

    public BookRatingReturnVo() {
    }

    public BookRatingReturnVo(String isbn, String email, Float rating) {
        this.isbn = isbn;
        this.email = email;
        this.rating = rating;
    }

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
