package com.yushun.recommender.service;

import com.yushun.recommender.RecommenderApplication;
import com.yushun.recommender.model.user.MovieFavourite;
import com.yushun.recommender.vo.user.movie.MovieFavouriteReturnVo;
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
public class MovieFavouriteServiceImplTest {
    @Autowired
    private MovieFavouriteService movieFavouriteService;

    /**
     * get user movie like status
     */
    @Order(1)
    @Test(timeout = 30000)
    @Transactional
    public void getUserMovieFavourite_userNotFind_null() {
        MovieFavouriteReturnVo userMovieFavourite = movieFavouriteService.getUserMovieFavourite(1, "d18130495@mytudublin.com");

        Assert.assertNull(userMovieFavourite);
    }

    @Order(2)
    @Test(timeout = 30000)
    @Transactional
    public void getUserMovieFavourite_movieNotFind_null() {
        MovieFavouriteReturnVo userMovieFavourite = movieFavouriteService.getUserMovieFavourite(10000, "990415zys@gmail.com");

        Assert.assertNull(userMovieFavourite);
    }

    @Order(3)
    @Test(timeout = 30000)
    @Transactional
    public void getUserMovieFavourite_userNotFavouriteThisBook_null() {
        MovieFavouriteReturnVo userMovieFavourite = movieFavouriteService.getUserMovieFavourite(1, "d18130495@mytudublin.ie");

        Assert.assertNull(userMovieFavourite);
    }

    @Order(4)
    @Test(timeout = 30000)
    @Transactional
    public void getUserMovieFavourite_findMovieFavourite_doNotLike() {
        MovieFavouriteReturnVo userMovieFavourite = movieFavouriteService.getUserMovieFavourite(1544, "990415zys@gmail.com");

        Assert.assertEquals("1", userMovieFavourite.getFavourite());
    }

    @Order(5)
    @Test(timeout = 30000)
    @Transactional
    public void getUserMovieFavourite_findMovieFavourite_normal() {
        MovieFavouriteReturnVo userMovieFavourite = movieFavouriteService.getUserMovieFavourite(1792, "990415zys@gmail.com");

        Assert.assertEquals("2", userMovieFavourite.getFavourite());
    }

    @Order(6)
    @Test(timeout = 30000)
    @Transactional
    public void getUserMovieFavourite_findMovieFavourite_favourite() {
        MovieFavouriteReturnVo userMovieFavourite = movieFavouriteService.getUserMovieFavourite(1, "990415zys@gmail.com");

        Assert.assertEquals("3", userMovieFavourite.getFavourite());
    }

    /**
     * change movie favourite status
     */
    @Order(7)
    @Test(timeout = 30000)
    @Transactional
    public void likeOrUnlikeMovie_userNotFind_userNotFind() {
        MovieFavourite movieFavourite = new MovieFavourite();
        movieFavourite.setEmail("d18130495@mytudublin.com");

        Assert.assertEquals("User not find", movieFavouriteService.likeOrUnlikeMovie(movieFavourite));
    }

    @Order(8)
    @Test(timeout = 30000)
    @Transactional
    public void likeOrUnlikeBook_movieNotFind_movieNotFind() {
        MovieFavourite movieFavourite = new MovieFavourite();
        movieFavourite.setEmail("d18130495@mytudublin.ie");
        movieFavourite.setMovieId(10000);

        Assert.assertEquals("Movie not find", movieFavouriteService.likeOrUnlikeMovie(movieFavourite));
    }

    @Order(9)
    @Test(timeout = 30000)
    @Transactional
    public void likeOrUnlikeMovie_addNewFavouriteCase1_doNotLike() {
        MovieFavourite movieFavourite = new MovieFavourite();
        movieFavourite.setEmail("d18130495@mytudublin.ie");
        movieFavourite.setMovieId(1);
        movieFavourite.setFavourite("1");

        Assert.assertEquals("Don't like", movieFavouriteService.likeOrUnlikeMovie(movieFavourite));
    }

    @Order(10)
    @Test(timeout = 30000)
    @Transactional
    public void likeOrUnlikeMovie_addNewFavouriteCase2_normal() {
        MovieFavourite movieFavourite = new MovieFavourite();
        movieFavourite.setEmail("d18130495@mytudublin.ie");
        movieFavourite.setMovieId(1);
        movieFavourite.setFavourite("2");

        Assert.assertEquals("Normal", movieFavouriteService.likeOrUnlikeMovie(movieFavourite));
    }

    @Order(11)
    @Test(timeout = 30000)
    @Transactional
    public void likeOrUnlikeMovie_addNewFavouriteCase3_favourite() {
        MovieFavourite movieFavourite = new MovieFavourite();
        movieFavourite.setEmail("d18130495@mytudublin.ie");
        movieFavourite.setMovieId(1);
        movieFavourite.setFavourite("3");

        Assert.assertEquals("Favourite", movieFavouriteService.likeOrUnlikeMovie(movieFavourite));
    }

    @Order(12)
    @Test(timeout = 30000)
    @Transactional
    public void likeOrUnlikeMovie_updateFavouriteCase1_favourite() {
        MovieFavourite movieFavourite = new MovieFavourite();
        movieFavourite.setEmail("990415zys@gmail.com");
        movieFavourite.setMovieId(1);
        movieFavourite.setFavourite("1");

        Assert.assertEquals("Don't like", movieFavouriteService.likeOrUnlikeMovie(movieFavourite));
    }

    @Order(13)
    @Test(timeout = 30000)
    @Transactional
    public void likeOrUnlikeMovie_updateFavouriteCase2_favourite() {
        MovieFavourite movieFavourite = new MovieFavourite();
        movieFavourite.setEmail("990415zys@gmail.com");
        movieFavourite.setMovieId(1);
        movieFavourite.setFavourite("2");

        Assert.assertEquals("Normal", movieFavouriteService.likeOrUnlikeMovie(movieFavourite));
    }

    @Order(14)
    @Test(timeout = 30000)
    @Transactional
    public void likeOrUnlikeBook_updateFavouriteCase3_favourite() {
        MovieFavourite movieFavourite = new MovieFavourite();
        movieFavourite.setEmail("990415zys@gmail.com");
        movieFavourite.setMovieId(1);
        movieFavourite.setFavourite("3");

        Assert.assertEquals("Favourite", movieFavouriteService.likeOrUnlikeMovie(movieFavourite));
    }

    @Order(15)
    @Test(timeout = 30000)
    @Transactional
    public void likeOrUnlikeBook_defaultCase_error() {
        MovieFavourite movieFavourite = new MovieFavourite();
        movieFavourite.setEmail("990415zys@gmail.com");
        movieFavourite.setMovieId(1);
        movieFavourite.setFavourite("4");

        Assert.assertEquals("Error", movieFavouriteService.likeOrUnlikeMovie(movieFavourite));
    }
}
