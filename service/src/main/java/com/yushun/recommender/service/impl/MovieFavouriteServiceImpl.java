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
    public MovieFavouriteReturnVo getMovieFavourite(Integer movieId, String email) {
        // find favourite
        QueryWrapper movieFavouriteWrapper = new QueryWrapper();
        movieFavouriteWrapper.eq("movieId", movieId);
        movieFavouriteWrapper.eq("email", email);

        MovieFavourite findMovieFavourite = baseMapper.selectOne(movieFavouriteWrapper);

        // return rating if exist
        if(findMovieFavourite == null) {
            return null;
        }else {
            MovieFavouriteReturnVo movieFavouriteReturnVo = new MovieFavouriteReturnVo();
            BeanUtils.copyProperties(findMovieFavourite, movieFavouriteReturnVo);

            if(movieFavouriteReturnVo.getFavourite().equals("T")) {
                movieFavouriteReturnVo.setFavourite("1");
            }else {
                movieFavouriteReturnVo.setFavourite("0");
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

        // add new rating or update rating
        if(findMovieFavourite == null && movieFavourite.getFavourite().equals("0")) {
            MovieFavourite newMovieFavourite = new MovieFavourite();

            BeanUtils.copyProperties(movieFavourite, newMovieFavourite);
            newMovieFavourite.setFavourite("T");
            newMovieFavourite.setCreateTime(new Date());
            newMovieFavourite.setUpdateTime(new Date());
            newMovieFavourite.setIsDeleted(0);

            int insert = baseMapper.insert(newMovieFavourite);

            return insert > 0 ? "Liked" : "Error";
        }else if(findMovieFavourite == null && movieFavourite.getFavourite().equals("1")) {
            return "Bad operation";
        }else if(findMovieFavourite != null) {
            if(findMovieFavourite.getFavourite().equals("F") && movieFavourite.getFavourite().equals("0")) {
                findMovieFavourite.setFavourite("T");
                findMovieFavourite.setUpdateTime(new Date());

                int update = baseMapper.update(findMovieFavourite, movieFavouriteWrapper);

                return update > 0? "Liked":"Error";
            }else if(findMovieFavourite.getFavourite().equals("T") && movieFavourite.getFavourite().equals("1")) {
                findMovieFavourite.setFavourite("F");
                findMovieFavourite.setUpdateTime(new Date());

                int update = baseMapper.update(findMovieFavourite, movieFavouriteWrapper);

                return update > 0? "Unliked":"Error";
            }else {
                return "Bad operation";
            }
        }

        return "Bad operation";
    }
}
