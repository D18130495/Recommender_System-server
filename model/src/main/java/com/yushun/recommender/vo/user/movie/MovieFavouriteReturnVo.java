package com.yushun.recommender.vo.user.movie;

/**
 * <p>
 * Movie Favourite Return Vo
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-28
 */

public class MovieFavouriteReturnVo {
    private Integer movieId;

    private String email;

    private String favourite;

    public Integer getMovieId() {
        return movieId;
    }

    public MovieFavouriteReturnVo() {
    }

    public MovieFavouriteReturnVo(Integer movieId, String email, String favourite) {
        this.movieId = movieId;
        this.email = email;
        this.favourite = favourite;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
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
