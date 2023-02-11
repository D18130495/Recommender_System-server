package com.yushun.recommender.service;

import com.yushun.recommender.RecommenderApplication;
import com.yushun.recommender.model.common.mongoEntity.movie.Movie;
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

    /**
     * test
     */
    @Order(1)
    @Test(timeout = 30000)
    @Transactional
    public void getRandomMovie_validMovie_success() {
        List<Movie> randomMovieList = movieService.getRandomMovie();
        Assert.assertNotNull(randomMovieList);
    }

    @Order(2)
    @Test(timeout = 30000)
    @Transactional
    public void movie_getMovieByMovieId_success() {
        Movie getMovieByMovieId = movieService.getMovieByMovieId(1);
        Assert.assertNotNull(getMovieByMovieId);
    }

    @Order(3)
    @Test(timeout = 30000)
    @Transactional
    public void movie_getMovieByMovieId_null() {
        Movie getMovieByMovieId = movieService.getMovieByMovieId(10000);
        Assert.assertNull(getMovieByMovieId);
    }
}
