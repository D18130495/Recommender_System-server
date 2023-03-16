package com.yushun.recommender.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yushun.recommender.mapper.MovieRatingMapper;
import com.yushun.recommender.model.common.User;
import com.yushun.recommender.model.common.mongoEntity.book.Book;
import com.yushun.recommender.model.common.mongoEntity.movie.Movie;
import com.yushun.recommender.model.user.MovieRating;
import com.yushun.recommender.service.MovieRatingService;
import com.yushun.recommender.service.MovieService;
import com.yushun.recommender.service.UserService;
import com.yushun.recommender.vo.user.movie.MovieRatingReturnVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * Movie Rating Service Impl
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-27
 */

@Service
public class MovieRatingServiceImpl extends ServiceImpl<MovieRatingMapper, MovieRating> implements MovieRatingService {
    @Autowired
    private UserService userService;

    @Autowired
    private MovieService movieService;

    @Override
    public MovieRatingReturnVo getUserMovieRating(Integer movieId, String email) {
        // find if user is existed
        QueryWrapper userWrapper = new QueryWrapper();
        userWrapper.eq("email", email);

        User findUser = userService.getOne(userWrapper);

        if(findUser == null) return null;

        // find if the movie exist
        Movie movieByMovieId = movieService.getMovieByMovieId(movieId);

        if(movieByMovieId == null) return null;

        // find rating
        QueryWrapper movieRatingWrapper = new QueryWrapper();
        movieRatingWrapper.eq("movieId", movieId);
        movieRatingWrapper.eq("email", email);

        MovieRating findMovieRating = baseMapper.selectOne(movieRatingWrapper);

        // return rating if exist
        if(findMovieRating == null) {
            return null;
        }else {
            MovieRatingReturnVo movieRatingReturnVo = new MovieRatingReturnVo();
            BeanUtils.copyProperties(findMovieRating, movieRatingReturnVo);

            return movieRatingReturnVo;
        }
    }

    @Override
    public String addOrUpdateUserMovieRating(MovieRating movieRating) {
        // find if user is existed
        QueryWrapper userWrapper = new QueryWrapper();
        userWrapper.eq("email",movieRating.getEmail());

        User findUser = userService.getOne(userWrapper);

        if(findUser == null) return "User not find";

        // find if the book exist
        Movie movieByMovieId = movieService.getMovieByMovieId(movieRating.getMovieId());

        if(movieByMovieId == null) return "Book not find";

        // find rating
        QueryWrapper movieRatingWrapper = new QueryWrapper();
        movieRatingWrapper.eq("movieId", movieRating.getMovieId());
        movieRatingWrapper.eq("email", movieRating.getEmail());

        MovieRating findMovieRating = baseMapper.selectOne(movieRatingWrapper);

        // add new rating or update rating
        if(findMovieRating == null) {
            MovieRating newMovieRating = new MovieRating();

            BeanUtils.copyProperties(movieRating, newMovieRating);
            newMovieRating.setCreateTime(new Date());
            newMovieRating.setUpdateTime(new Date());
            newMovieRating.setIsDeleted(0);

            int insert = baseMapper.insert(newMovieRating);

            return insert > 0? "Successfully rating this movie":"Error";
        }else {
            findMovieRating.setRating(movieRating.getRating());
            findMovieRating.setUpdateTime(new Date());

            int update = baseMapper.update(findMovieRating, movieRatingWrapper);

            return update > 0? "Successfully updated rating":"Error";
        }
    }
}
