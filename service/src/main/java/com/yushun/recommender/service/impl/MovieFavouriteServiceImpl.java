package com.yushun.recommender.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yushun.recommender.mapper.MovieFavouriteMapper;
import com.yushun.recommender.model.user.MovieFavourite;
import com.yushun.recommender.service.MovieFavouriteService;
import com.yushun.recommender.vo.user.movie.MovieFavouriteReturnVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * Movie Favourite Service Impl
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-28
 */

@Service
public class MovieFavouriteServiceImpl extends ServiceImpl<MovieFavouriteMapper, MovieFavourite> implements MovieFavouriteService {
    @Override
    public MovieFavouriteReturnVo getUserMovieFavourite(Integer movieId, String email) {
        // find favourite
        QueryWrapper movieFavouriteWrapper = new QueryWrapper();
        movieFavouriteWrapper.eq("movieId", movieId);
        movieFavouriteWrapper.eq("email", email);

        MovieFavourite findMovieFavourite = baseMapper.selectOne(movieFavouriteWrapper);

        // return favourite if exist
        if(findMovieFavourite == null) {
            return null;
        }else {
            MovieFavouriteReturnVo movieFavouriteReturnVo = new MovieFavouriteReturnVo();
            BeanUtils.copyProperties(findMovieFavourite, movieFavouriteReturnVo);

            if (movieFavouriteReturnVo.getFavourite().equals("T")) {
                movieFavouriteReturnVo.setFavourite("3");
            } else if (movieFavouriteReturnVo.getFavourite().equals("F")) {
                movieFavouriteReturnVo.setFavourite("1");
            } else {
                movieFavouriteReturnVo.setFavourite("2");
            }

            return movieFavouriteReturnVo;
        }
    }

    @Override
    public String likeOrUnlikeMovie(MovieFavourite movieFavourite) {
        // find favourite movie
        QueryWrapper movieFavouriteWrapper = new QueryWrapper();
        movieFavouriteWrapper.eq("movieId", movieFavourite.getMovieId());
        movieFavouriteWrapper.eq("email", movieFavourite.getEmail());

        MovieFavourite findMovieFavourite = baseMapper.selectOne(movieFavouriteWrapper);

        if (findMovieFavourite == null) { // add new record
            switch (movieFavourite.getFavourite()) {
                case "1": { // do not like
                    MovieFavourite newMovieFavourite = addNewMovieFavourite(movieFavourite, "F");

                    int insert = baseMapper.insert(newMovieFavourite);

                    return insert > 0 ? "Don't like" : "Error";
                }
                case "2": { // normal
                    MovieFavourite newMovieFavourite = addNewMovieFavourite(movieFavourite, "N");

                    int insert = baseMapper.insert(newMovieFavourite);

                    return insert > 0 ? "Normal" : "Error";
                }
                case "3": { // like
                    MovieFavourite newMovieFavourite = addNewMovieFavourite(movieFavourite, "T");

                    int insert = baseMapper.insert(newMovieFavourite);

                    return insert > 0 ? "Favourite" : "Error";
                }
            }
        } else {
            switch (movieFavourite.getFavourite()) {
                case "1": { // do not like
                    findMovieFavourite.setFavourite("F");

                    int update = baseMapper.update(findMovieFavourite, movieFavouriteWrapper);

                    return update > 0 ? "Don't like" : "Error";
                }
                case "2": { // normal
                    findMovieFavourite.setFavourite("N");

                    int update = baseMapper.update(findMovieFavourite, movieFavouriteWrapper);

                    return update > 0 ? "Normal" : "Error";
                }
                case "3": { // like
                    findMovieFavourite.setFavourite("T");

                    int update = baseMapper.update(findMovieFavourite, movieFavouriteWrapper);

                    return update > 0 ? "Favourite" : "Error";
                }
            }
        }

        return "Error";
    }

    public MovieFavourite addNewMovieFavourite(MovieFavourite movieFavourite, String favourite) {
        MovieFavourite newMovieFavourite = new MovieFavourite();

        BeanUtils.copyProperties(movieFavourite, newMovieFavourite);
        newMovieFavourite.setFavourite(favourite);
        newMovieFavourite.setCreateTime(new Date());
        newMovieFavourite.setUpdateTime(new Date());
        newMovieFavourite.setIsDeleted(0);

        return newMovieFavourite;
    }
}
