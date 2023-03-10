package com.yushun.recommender.model.common.mongoEntity.movie;

import com.yushun.recommender.model.base.BaseMongoEntity;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * <p>
 * MongoDB Movie Model
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-22
 */

@Document("movie")
public class Movie extends BaseMongoEntity {
    private static final long serialVersionUID = 1L;

    private Integer movieId;

    private Integer imdbId;

    private Integer tmdbId;

    private String title;

    private String year;

    private String imdbUrl;

    private String tmdbUrl;

    private String genres;

    private String movieImage;

    private String moviePhoto;

    private String movieVideo;

    private String description;

    private MovieDirector director;

    private List<MovieActor> actor;

    private List<MovieRate> rate;

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

    public String getImdbUrl() {
        return imdbUrl;
    }

    public void setImdbUrl(String imdbUrl) {
        this.imdbUrl = imdbUrl;
    }

    public String getTmdbUrl() {
        return tmdbUrl;
    }

    public void setTmdbUrl(String tmdbUrl) {
        this.tmdbUrl = tmdbUrl;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getMovieImage() {
        return movieImage;
    }

    public void setMovieImage(String movieImage) {
        this.movieImage = movieImage;
    }

    public String getMoviePhoto() {
        return moviePhoto;
    }

    public void setMoviePhoto(String moviePhoto) {
        this.moviePhoto = moviePhoto;
    }

    public String getMovieVideo() {
        return movieVideo;
    }

    public void setMovieVideo(String movieVideo) {
        this.movieVideo = movieVideo;
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

    public List<MovieRate> getRate() {
        return rate;
    }

    public void setRate(List<MovieRate> rate) {
        this.rate = rate;
    }
}
