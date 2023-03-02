package com.yushun.recommender.service;

import com.yushun.recommender.RecommenderApplication;
import com.yushun.recommender.model.user.BookRating;
import com.yushun.recommender.model.user.MovieRating;
import com.yushun.recommender.vo.user.book.BookRatingReturnVo;
import com.yushun.recommender.vo.user.movie.MovieRatingReturnVo;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RecommenderApplication.class)
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MovieRatingServiceImplTest {
    @Autowired
    private MovieRatingService movieRatingService;

    /**
     * get user Movie rate status
     */

    @Order(1)
    @Test(timeout = 30000)
    @Transactional
    public void getUserMovieRating_userNotFind_null() {
        MovieRatingReturnVo userMovieRating = movieRatingService.getUserMovieRating(1, "d18130495@mytudublin.com");

        Assert.assertNull(userMovieRating);
    }

    @Order(2)
    @Test(timeout = 30000)
    @Transactional
    public void getUserBookRating_bookNotFind_null() {
        MovieRatingReturnVo userMovieRating = movieRatingService.getUserMovieRating(10000, "990415zys@gmail.com");

        Assert.assertNull(userMovieRating);
    }

    @Order(3)
    @Test(timeout = 30000)
    @Transactional
    public void getUserMovieRating_userNotRatingThisMovie_null() {
        MovieRatingReturnVo userMovieRating = movieRatingService.getUserMovieRating(2, "990415zys@gmail.com");

        Assert.assertNull(userMovieRating);
    }

    @Order(4)
    @Test(timeout = 30000)
    @Transactional
    public void getUserMovieRating_findMovieRating_movieRatingReturnVo() {
        MovieRatingReturnVo userMovieRating = movieRatingService.getUserMovieRating(1, "d18130495@mytudublin.ie");

        Assert.assertNotNull(userMovieRating.getRating());
    }

    /**
     * change movie rating status
     */
    @Order(5)
    @Test(timeout = 30000)
    @Transactional
    public void addOrUpdateUserMovieRating_userNotFind_userNotFind() {
        MovieRating movieRating = new MovieRating();
        movieRating.setEmail("d18130495@mytudublin.com");

        String addOrUpdateState = movieRatingService.addOrUpdateUserMovieRating(movieRating);

        Assert.assertEquals("User not find", addOrUpdateState);
    }

    @Order(6)
    @Test(timeout = 30000)
    @Transactional
    public void addOrUpdateUserMovieRating_movieNotFind_movieNotFind() {
        MovieRating movieRating = new MovieRating();
        movieRating.setEmail("d18130495@mytudublin.ie");
        movieRating.setMovieId(10000);

        String addOrUpdateState = movieRatingService.addOrUpdateUserMovieRating(movieRating);

        Assert.assertEquals("Book not find", addOrUpdateState);
    }

    @Order(7)
    @Test(timeout = 30000)
    @Transactional
    public void addOrUpdateUserMovieRating_addNewRating_successfullyRatingThisMovie() {
        MovieRating movieRating = new MovieRating();
        movieRating.setEmail("d18130495@mytudublin.ie");
        movieRating.setMovieId(2);

        String addOrUpdateState = movieRatingService.addOrUpdateUserMovieRating(movieRating);

        Assert.assertEquals("Successfully rating this movie", addOrUpdateState);
    }

    @Order(8)
    @Test(timeout = 30000)
    @Transactional
    public void addOrUpdateUserMovieRating_updateRating_successfullyUpdatedRating() {
        MovieRating movieRating = new MovieRating();
        movieRating.setEmail("d18130495@mytudublin.ie");
        movieRating.setMovieId(1);
        movieRating.setRating(5F);

        String addOrUpdateState = movieRatingService.addOrUpdateUserMovieRating(movieRating);

        Assert.assertEquals("Successfully updated rating", addOrUpdateState);
    }
}
