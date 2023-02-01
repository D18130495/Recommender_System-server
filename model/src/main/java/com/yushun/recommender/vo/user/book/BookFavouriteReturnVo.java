package com.yushun.recommender.vo.user.book;

/**
 * <p>
 * Book Favourite Return Vo
 * </p>
 *
 * @author yushun zeng
 * @since 2023-2-1
 */

public class BookFavouriteReturnVo {
    private String isbn;

    private String email;

    private String favourite;

    public BookFavouriteReturnVo() {
    }

    public BookFavouriteReturnVo(String isbn, String email, String favourite) {
        this.isbn = isbn;
        this.email = email;
        this.favourite = favourite;
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

    public String getFavourite() {
        return favourite;
    }

    public void setFavourite(String favourite) {
        this.favourite = favourite;
    }
}
