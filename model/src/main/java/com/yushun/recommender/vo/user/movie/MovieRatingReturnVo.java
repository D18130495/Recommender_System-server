package com.yushun.recommender.vo.user.movie;

/**
 * <p>
 * Movie Rating Return Vo
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-27
 */

public class MovieRatingReturnVo {
    private Integer movieId;

    private String email;

    private Float rating;

    public MovieRatingReturnVo() {
    }

    public MovieRatingReturnVo(Integer movieId, String email, Float rating) {
        this.movieId = movieId;
        this.email = email;
        this.rating = rating;
    }

    public Integer getMovieId() {
        return movieId;
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

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }
}
