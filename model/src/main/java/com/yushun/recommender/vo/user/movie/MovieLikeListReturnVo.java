package com.yushun.recommender.vo.user.movie;

import com.yushun.recommender.model.common.mongoEntity.movie.MovieActor;
import com.yushun.recommender.model.common.mongoEntity.movie.MovieDirector;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * Movie Like List Return Vo
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-30
 */

public class MovieLikeListReturnVo {
    private Integer movieId;

    private String title;

    private String genres;

    private MovieDirector director;

    private List<MovieActor> actor;

    private Float rating;

    private Date updateDate;

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public MovieDirector getDirector() {
        return director;
    }

    public void setDirector(MovieDirector director) {
        this.director = director;
    }

    public List<MovieActor> getActor() {
        return actor;
    }

    public void setActor(List<MovieActor> actor) {
        this.actor = actor;
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
