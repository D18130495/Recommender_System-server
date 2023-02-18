package com.yushun.recommender.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yushun.recommender.model.common.User;
import com.yushun.recommender.model.common.mongoEntity.movie.Movie;
import com.yushun.recommender.model.common.mongoEntity.movie.MovieRate;
import com.yushun.recommender.model.user.MovieFavourite;
import com.yushun.recommender.model.user.MovieRating;
import com.yushun.recommender.repository.MovieRepository;
import com.yushun.recommender.service.MovieFavouriteService;
import com.yushun.recommender.service.MovieRatingService;
import com.yushun.recommender.service.MovieService;
import com.yushun.recommender.service.UserService;
import com.yushun.recommender.vo.user.movie.MovieLikeListReturnVo;
import com.yushun.recommender.vo.user.movie.MovieRatingListReturnVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Movie Service Impl
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-22
 */

@Service
public class MovieServiceImpl implements MovieService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MovieFavouriteService movieFavouriteService;

    @Autowired
    private MovieRatingService movieRatingService;

    @Override
    public List<Movie> getRandomMovie() {
        // aggregate and find results
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.sample(6)
        );

        AggregationResults<Movie> results = mongoTemplate.aggregate(aggregation, Movie.class, Movie.class);
        List<Movie> movieList = results.getMappedResults();

        // calculate average rate score
        for(Movie movie:movieList) {
            List<MovieRate> rateList = movie.getRate();
            float total = 0;

            for(MovieRate movieRate: rateList) {
                total = total + movieRate.getRating();
            }

            // find system user rating
            QueryWrapper movieRateQueryWrapper = new QueryWrapper();
            movieRateQueryWrapper.eq("movieId", movie.getMovieId());

            List<MovieRating> systemMovieRatingList = movieRatingService.list(movieRateQueryWrapper);

            if(systemMovieRatingList.size() != 0) {
                for(MovieRating systemMovieRating:systemMovieRatingList) {
                    total = total + systemMovieRating.getRating();
                }
            }

            DecimalFormat decimalFormat =new DecimalFormat("#.0");
            movie.getParam().put("rate", decimalFormat.format(total / (movie.getRate().size() + systemMovieRatingList.size())));
        }

        return movieList;
    }

    @Override
    public Movie getMovieByMovieId(Integer movieId) {
        // find book
        Movie movie = movieRepository.findByMovieId(movieId);

        if(movie == null) {
            return null;
        }else {
            // calculate average rate value
            float total = 0;

            for(MovieRate movieRate: movie.getRate()) {
                total = total + movieRate.getRating();
            }

            // find system user rating
            QueryWrapper movieRateQueryWrapper = new QueryWrapper();
            movieRateQueryWrapper.eq("movieId", movie.getMovieId());

            List<MovieRating> systemMovieRatingList = movieRatingService.list(movieRateQueryWrapper);

            if(systemMovieRatingList.size() != 0) {
                for(MovieRating systemMovieRating:systemMovieRatingList) {
                    total = total + systemMovieRating.getRating();
                }
            }

            DecimalFormat decimalFormat =new DecimalFormat("#.0");
            movie.getParam().put("rate", decimalFormat.format(total / (movie.getRate().size() + systemMovieRatingList.size())));
        }

        return movie;
    }

    @Override
    public Integer getUserLikeAndRatingMovieCount(String email) {
        int movieCount = 0;

        // find if user is existed
        QueryWrapper userWrapper = new QueryWrapper();
        userWrapper.eq("email", email);

        User findUser = userService.getOne(userWrapper);

        if(findUser == null) return -1;

        // find user movie like list
        QueryWrapper movieLikeListWrapper = new QueryWrapper();
        movieLikeListWrapper.eq("email", email);
        movieLikeListWrapper.eq("favourite", "T");

        List<MovieFavourite> movieFavouriteList = movieFavouriteService.list(movieLikeListWrapper);

        // find user movie rating list
        QueryWrapper movieRatingListWrapper = new QueryWrapper();
        movieRatingListWrapper.eq("email", email);

        List<MovieRating> movieRatingList = movieRatingService.list(movieRatingListWrapper);

        if(movieFavouriteList.size() == 0 && movieRatingList.size() == 0) {
            return 0;
        }else if(movieFavouriteList.size() == 0) {
            return movieRatingList.size();
        }else if(movieRatingList.size() == 0) {
            return movieFavouriteList.size();
        }else {
            movieCount = movieFavouriteList.size() + movieRatingList.size();

            for(MovieRating movieRating:movieRatingList) {
                for(MovieFavourite movieFavourite:movieFavouriteList) {
                    if(movieRating.getMovieId().equals(movieFavourite.getMovieId())) {
                        movieCount -= 1;
                    }
                }
            }

            return movieCount;
        }
    }

    @Override
    public List<MovieLikeListReturnVo> getUserMovieLikeList(String email) {
        // find if user is existed
        QueryWrapper userWrapper = new QueryWrapper();
        userWrapper.eq("email", email);

        User findUser = userService.getOne(userWrapper);

        if(findUser == null) return null;

        // find user movie like list movieId
        QueryWrapper movieLikeListWrapper = new QueryWrapper();
        movieLikeListWrapper.eq("email", email);
        movieLikeListWrapper.in("favourite", "T", "F");

        List<MovieFavourite> findMovieLikeList = movieFavouriteService.list(movieLikeListWrapper);

        if(findMovieLikeList.size() != 0) {
            // return list
            ArrayList<MovieLikeListReturnVo> movieLikeListReturnVoList = new ArrayList<>();

            // find movie detail
            for(MovieFavourite movieFavourite: findMovieLikeList) {
                Movie movie = movieRepository.findByMovieId(movieFavourite.getMovieId());

                // form movie detail
                if(movie != null) {
                    MovieLikeListReturnVo movieLikeListReturnVo = new MovieLikeListReturnVo();
                    movieLikeListReturnVo.setMovieId(movieFavourite.getMovieId());
                    movieLikeListReturnVo.setTitle(movie.getTitle());
                    movieLikeListReturnVo.setGenres(movie.getGenres());
                    movieLikeListReturnVo.setDirector(movie.getDirector());
                    movieLikeListReturnVo.setActor(movie.getActor());

                    // form rating data
                    // find user rating for this movie
                    QueryWrapper movieRatingWrapper = new QueryWrapper();
                    movieRatingWrapper.eq("email", email);
                    movieRatingWrapper.eq("movieId", movie.getMovieId());

                    MovieRating findMovieRating = movieRatingService.getOne(movieRatingWrapper);

                    if(findMovieRating != null) movieLikeListReturnVo.setRating(findMovieRating.getRating());

                    movieLikeListReturnVo.setFavourite(movieFavourite.getFavourite());
                    movieLikeListReturnVo.setUpdateDate(movieFavourite.getUpdateTime());

                    movieLikeListReturnVoList.add(movieLikeListReturnVo);
                }
            }

            return movieLikeListReturnVoList;
        }else {
            return null;
        }
    }

    @Override
    public List<MovieRatingListReturnVo> getUserMovieRatingList(String email) {
        // find if user is existed
        QueryWrapper userWrapper = new QueryWrapper();
        userWrapper.eq("email", email);

        User findUser = userService.getOne(userWrapper);

        if(findUser == null) return null;

        // find user rated movie list
        QueryWrapper movieRatingListWrapper = new QueryWrapper();
        movieRatingListWrapper.eq("email", email);

        List<MovieRating> findMovieRatingList = movieRatingService.list(movieRatingListWrapper);

        if(findMovieRatingList.size() != 0) {
            // return list
            ArrayList<MovieRatingListReturnVo> movieRatingListReturnVoList = new ArrayList<>();

            // find movie detail
            for(MovieRating movieRating: findMovieRatingList) {
                Movie movie = movieRepository.findByMovieId(movieRating.getMovieId());

                // form movie detail
                if(movie != null) {
                    MovieRatingListReturnVo movieRatingListReturnVo = new MovieRatingListReturnVo();
                    movieRatingListReturnVo.setMovieId(movieRating.getMovieId());
                    movieRatingListReturnVo.setTitle(movie.getTitle());
                    movieRatingListReturnVo.setRating(movieRating.getRating());
                    movieRatingListReturnVo.setDirector(movie.getDirector());
                    movieRatingListReturnVo.setActor(movie.getActor());
                    movieRatingListReturnVo.setUpdateDate(movieRating.getUpdateTime());

                    movieRatingListReturnVoList.add(movieRatingListReturnVo);
                }
            }

            return movieRatingListReturnVoList;
        }else {
            return null;
        }
    }

//    @Override
//    public List<Movie> getRandomMovie() {
//        Criteria criteria = new Criteria().andOperator(Criteria.where("movieId").is(1));
//        Query query = new Query();
//        query.addCriteria(criteria);
//
//        Movie movie = mongoTemplate.findOne(query, Movie.class, "movie");
//
//        System.out.println(movie);
//        System.out.println(movie.getDirector());
//
//        return null;
//    }
}
