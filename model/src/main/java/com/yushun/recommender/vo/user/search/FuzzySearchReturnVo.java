package com.yushun.recommender.vo.user.search;

import com.yushun.recommender.model.common.mongoEntity.movie.MovieActor;

import java.util.List;

/**
 * <p>
 * Fuzzy Search Result Return Vo
 * </p>
 *
 * @author yushun zeng
 * @since 2023-2-20
 */

public class FuzzySearchReturnVo {
    private Integer movieId;

    private String isbn;

    private String title;

    private String image;

    private String year;

    private String director;

    private String actorList;

    private String author;

    private String type;

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActorList() {
        return actorList;
    }

    public void setActorList(String actorList) {
        this.actorList = actorList;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
