package com.yushun.recommender.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yushun.recommender.model.user.MovieFavourite;
import com.yushun.recommender.vo.user.movie.MovieFavouriteReturnVo;

/**
 * <p>
 * Movie Favourite Service
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-28
 */

public interface MovieFavouriteService extends IService<MovieFavourite> {
    MovieFavouriteReturnVo getUserMovieFavourite(Integer movieId, String email);

    String likeOrUnlikeMovie(MovieFavourite movieFavourite);
}
