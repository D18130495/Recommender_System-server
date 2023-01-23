package com.yushun.recommender.service;

import com.yushun.recommender.model.common.mongoEntity.movie.Movie;

import java.util.List;

/**
 * <p>
 * Movie Service
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-22
 */

public interface MovieService {
    List<Movie> getRandomMovie();
}
