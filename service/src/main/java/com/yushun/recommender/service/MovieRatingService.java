package com.yushun.recommender.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yushun.recommender.model.user.MovieRating;
import com.yushun.recommender.vo.user.movie.MovieRatingReturnVo;

/**
 * <p>
 * Movie Rating Service
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-27
 */

public interface MovieRatingService extends IService<MovieRating> {
    MovieRatingReturnVo getUserMovieRating(Integer movieId, String email);

    String addOrUpdateUserMovieRating(MovieRating movieRating);
}
