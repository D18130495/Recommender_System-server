package com.yushun.recommender.service;

import com.yushun.recommender.RecommenderApplication;
import com.yushun.recommender.model.common.mongoEntity.movie.Movie;
import com.yushun.recommender.model.user.MovieFavourite;
import com.yushun.recommender.model.user.MovieRating;
import com.yushun.recommender.vo.user.movie.MovieLikeListReturnVo;
import com.yushun.recommender.vo.user.movie.MovieRatingListReturnVo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RecommenderApplication.class)
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MovieServiceImplTest {
    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieFavouriteService movieFavouriteService;

    @Autowired
    private MovieRatingService movieRatingService;

    /**
     * get random movie list(size 6)
     */
    @Order(1)
    @Test(timeout = 30000)
    @Transactional
    public void getRandomMovie_validMovie_success() {
        List<Movie> randomMovieList = movieService.getRandomMovie();

        Assert.assertEquals(6, randomMovieList.size());
    }

    /**
     * get movie by movie ID
     */
    @Order(2)
    @Test(timeout = 30000)
    @Transactional
    public void getMovieByMovieId_findMovie_movie() {
        Movie movieByMovieId = movieService.getMovieByMovieId(1);

        Assert.assertNotNull(movieByMovieId);
    }

    @Order(3)
    @Test(timeout = 30000)
    @Transactional
    public void getMovieByMovieId_movieNotFind_null() {
        Movie movieByMovieId = movieService.getMovieByMovieId(10000);

        Assert.assertNull(movieByMovieId);
    }

    /**
     * get user like and rating movie count
     */
    @Order(4)
    @Test(timeout = 30000)
    @Transactional
    public void getUserLikeAndRatingMovieCount_userNotFind_minus() {
        Integer userLikeAndRatingMovieCount = movieService.getUserLikeAndRatingMovieCount("d18130495@mytudublin.com");

        Assert.assertEquals(new Integer(-1), userLikeAndRatingMovieCount);
    }

    @Order(5)
    @Test(timeout = 30000)
    @Transactional
    public void getUserLikeAndRatingMovieCount_userNotLikeOrRateMovie_zero() {
        Integer userLikeAndRatingMovieCount = movieService.getUserLikeAndRatingMovieCount("d18130495@mytudublin.ie");

        Assert.assertEquals(new Integer(0), userLikeAndRatingMovieCount);
    }

    @Order(6)
    @Test(timeout = 30000)
    @Transactional
    public void getUserLikeAndRatingMovieCount_userOnlyListMovie_rateListSize() {
        MovieFavourite movieFavourite = new MovieFavourite();
        movieFavourite.setEmail("d18130495@mytudublin.ie");
        movieFavourite.setMovieId(1);
        movieFavourite.setFavourite("3");

        movieFavouriteService.likeOrUnlikeMovie(movieFavourite);

        Integer userLikeAndRatingMovieCount = movieService.getUserLikeAndRatingMovieCount("d18130495@mytudublin.ie");

        Assert.assertEquals(new Integer(1), userLikeAndRatingMovieCount);
    }

    @Order(7)
    @Test(timeout = 30000)
    @Transactional
    public void getUserLikeAndRatingMovieCount_userOnlyRateMovie_rateListSize() {
        MovieRating movieRating = new MovieRating();
        movieRating.setEmail("d18130495@mytudublin.ie");
        movieRating.setMovieId(1);
        movieRating.setRating(3F);

        movieRatingService.addOrUpdateUserMovieRating(movieRating);

        Integer userLikeAndRatingMovieCount = movieService.getUserLikeAndRatingMovieCount("d18130495@mytudublin.ie");

        Assert.assertEquals(new Integer(1), userLikeAndRatingMovieCount);
    }

    @Order(8)
    @Test(timeout = 30000)
    @Transactional
    public void getUserLikeAndRatingMovieCount_userLikeAndRateSameMovie_likeAndRateListSize() {
        MovieFavourite movieFavourite = new MovieFavourite();
        movieFavourite.setEmail("d18130495@mytudublin.ie");
        movieFavourite.setMovieId(1);
        movieFavourite.setFavourite("3");

        movieFavouriteService.likeOrUnlikeMovie(movieFavourite);

        MovieRating movieRating = new MovieRating();
        movieRating.setEmail("d18130495@mytudublin.ie");
        movieRating.setMovieId(1);
        movieRating.setRating(3F);

        movieRatingService.addOrUpdateUserMovieRating(movieRating);

        Integer userLikeAndRatingMovieCount = movieService.getUserLikeAndRatingMovieCount("d18130495@mytudublin.ie");

        Assert.assertEquals(new Integer(1), userLikeAndRatingMovieCount);
    }

    @Order(9)
    @Test(timeout = 30000)
    @Transactional
    public void getUserLikeAndRatingMovieCount_userLikeAndRateDifferentMovie_likeAndRateListSize() {
        MovieFavourite movieFavourite = new MovieFavourite();
        movieFavourite.setEmail("d18130495@mytudublin.ie");
        movieFavourite.setMovieId(1);
        movieFavourite.setFavourite("3");

        movieFavouriteService.likeOrUnlikeMovie(movieFavourite);

        MovieRating movieRating = new MovieRating();
        movieRating.setEmail("d18130495@mytudublin.ie");
        movieRating.setMovieId(2);
        movieRating.setRating(3F);

        movieRatingService.addOrUpdateUserMovieRating(movieRating);

        Integer userLikeAndRatingMovieCount = movieService.getUserLikeAndRatingMovieCount("d18130495@mytudublin.ie");

        Assert.assertEquals(new Integer(2), userLikeAndRatingMovieCount);
    }

    /**
     * get user movie like list
     */
    @Order(10)
    @Test(timeout = 30000)
    @Transactional
    public void getUserMovieLikeList_userNotFind_null() {
        List<MovieLikeListReturnVo> userMovieLikeList = movieService.getUserMovieLikeList("d18130495@mytudublin.com");

        Assert.assertNull(userMovieLikeList);
    }

    @Order(11)
    @Test(timeout = 30000)
    @Transactional
    public void getUserMovieLikeList_userNotLikeOrUnlike_null() {
        List<MovieLikeListReturnVo> userMovieLikeList = movieService.getUserMovieLikeList("d18130495@mytudublin.ie");

        Assert.assertNull(userMovieLikeList);
    }

    @Order(12)
    @Test(timeout = 30000)
    @Transactional
    public void getUserMovieLikeList_userNotRateThisMovie_rateIsNull() {
        MovieFavourite movieFavourite = new MovieFavourite();
        movieFavourite.setEmail("d18130495@mytudublin.ie");
        movieFavourite.setMovieId(1);
        movieFavourite.setFavourite("3");

        movieFavouriteService.likeOrUnlikeMovie(movieFavourite);

        List<MovieLikeListReturnVo> userMovieLikeList = movieService.getUserMovieLikeList("d18130495@mytudublin.ie");

        Assert.assertNull(userMovieLikeList.get(0).getRating());
    }

    @Order(13)
    @Test(timeout = 30000)
    @Transactional
    public void getUserMovieLikeList_userRateThisMovie_rateIsNotNull() {
        MovieFavourite movieFavourite = new MovieFavourite();
        movieFavourite.setEmail("d18130495@mytudublin.ie");
        movieFavourite.setMovieId(1);
        movieFavourite.setFavourite("3");

        movieFavouriteService.likeOrUnlikeMovie(movieFavourite);

        MovieRating movieRating = new MovieRating();
        movieRating.setEmail("d18130495@mytudublin.ie");
        movieRating.setMovieId(1);
        movieRating.setRating(3F);

        movieRatingService.addOrUpdateUserMovieRating(movieRating);

        List<MovieLikeListReturnVo> userMovieLikeList = movieService.getUserMovieLikeList("d18130495@mytudublin.ie");

        Assert.assertEquals(new Float(3), userMovieLikeList.get(0).getRating());
    }

    /**
     * get user movie rating list
     */
    @Order(14)
    @Test(timeout = 30000)
    @Transactional
    public void getUserMovieRatingList_userNotFind_null() {
        List<MovieRatingListReturnVo> userMovieRatingList = movieService.getUserMovieRatingList("d18130495@mytudublin.com");

        Assert.assertNull(userMovieRatingList);
    }

    @Order(15)
    @Test(timeout = 30000)
    @Transactional
    public void getUserMovieRatingList_userNotRateMovie_null() {
        List<MovieRatingListReturnVo> userMovieRatingList = movieService.getUserMovieRatingList("d18130495@mytudublin.ie");

        Assert.assertNull(userMovieRatingList);
    }

    @Order(16)
    @Test(timeout = 30000)
    @Transactional
    public void getUserBookRatingList_userRateBook_bookRateListIsNotNull() {
        MovieRating movieRating = new MovieRating();
        movieRating.setEmail("d18130495@mytudublin.ie");
        movieRating.setMovieId(1);
        movieRating.setRating(3F);

        movieRatingService.addOrUpdateUserMovieRating(movieRating);

        List<MovieRatingListReturnVo> userMovieRatingList = movieService.getUserMovieRatingList("d18130495@mytudublin.ie");

        Assert.assertNotNull(userMovieRatingList);
    }
}
