package com.yushun.recommender.vo.user.movie;

import com.yushun.recommender.model.common.mongoEntity.movie.MovieActor;
import com.yushun.recommender.model.common.mongoEntity.movie.MovieDirector;

import java.util.List;

/**
 * <p>
 * Movie Result Return Vo
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-23
 */

public class MovieReturnVo {
    private Integer movieId;

    private Integer imdbId;

    private Integer tmdbId;

    private String title;

    private String year;

    private String url;

    private List<String> genres;

    private String movieImage;

    private String description;

    private MovieDirector director;

    private List<MovieActor> actor;

    private float rate;

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public Integer getImdbId() {
        return imdbId;
    }

    public void setImdbId(Integer imdbId) {
        this.imdbId = imdbId;
    }

    public Integer getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(Integer tmdbId) {
        this.tmdbId = tmdbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getMovieImage() {
        return movieImage;
    }

    public void setMovieImage(String movieImage) {
        this.movieImage = movieImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
