package com.yushun.recommender.service;

import com.yushun.recommender.model.common.mongoEntity.movie.Movie;
import com.yushun.recommender.vo.user.movie.MovieLikeListReturnVo;
import com.yushun.recommender.vo.user.movie.MovieRatingListReturnVo;

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

    List<MovieLikeListReturnVo> getMovieLikeList(String email);

    List<MovieRatingListReturnVo> getMovieRatingList(String email);
}
