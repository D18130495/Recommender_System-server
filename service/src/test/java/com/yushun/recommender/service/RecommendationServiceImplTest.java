package com.yushun.recommender.service;

import com.yushun.recommender.RecommenderApplication;
import com.yushun.recommender.model.common.mongoEntity.book.Book;
import com.yushun.recommender.model.common.mongoEntity.movie.Movie;
import com.yushun.recommender.model.user.BookFavourite;
import com.yushun.recommender.model.user.MovieFavourite;
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
public class RecommendationServiceImplTest {
    @Autowired
    private RecommendationService recommendationService;

    /**
     * test movie like this
     */
    @Order(1)
    @Test(timeout = 30000)
    @Transactional
    public void getMovieLikeThis_findMovieList_movieList() {
        List<Movie> moviesLikeThis = recommendationService.getMoviesLikeThis("1");

        Assert.assertEquals(6, moviesLikeThis.size());
    }

    /**
     * test book like this
     */
    @Order(2)
    @Test(timeout = 30000)
    @Transactional
    public void getBooksLikeThis_findBookList_bookList() {
        List<Book> booksLikeThis = recommendationService.getBooksLikeThis("0060923717");

        Assert.assertEquals(6, booksLikeThis.size());
    }

    /**
     * test item-based CF
     */
    @Order(3)
    @Test(timeout = 30000)
    @Transactional
    public void getMovieRecommendationData_byMovie_itemCF_movieList() {
        MovieFavourite movieFavourite = new MovieFavourite();
        movieFavourite.setEmail("d18130495@mytudublin.ie");
        movieFavourite.setMovieId(4);
        movieFavourite.setFavourite("3");

        List<Movie> moviesLikeThis = recommendationService.getMovieRecommendationData_byMovie_itemCF("d18130495@mytudublin.ie");

        Assert.assertEquals(6, moviesLikeThis.size());
    }

    @Order(4)
    @Test(timeout = 30000)
    @Transactional
    public void getBookRecommendationData_byBook_itemCF_bookList() {
        BookFavourite bookFavourite = new BookFavourite();
        bookFavourite.setEmail("d18130495@mytudublin.ie");
        bookFavourite.setIsbn("0451197004");

        List<Book> booksLikeThis = recommendationService.getBookRecommendationData_byBook_itemCF("d18130495@mytudublin.ie");

        Assert.assertEquals(6, booksLikeThis.size());
    }

    /**
     * test user-based CF
     */
    @Order(5)
    @Test(timeout = 30000)
    @Transactional
    public void getMovieRecommendationData_byMovie_userCF_movieList() {
        List<Movie> moviesLikeThis = recommendationService.getMovieRecommendationData_byMovie_userCF("d18130495@mytudublin.ie", "movie");

        Assert.assertEquals(6, moviesLikeThis.size());
    }

    @Order(6)
    @Test(timeout = 30000)
    @Transactional
    public void getMovieRecommendationData_byBook_userCF_movieList() {
        List<Movie> moviesLikeThis = recommendationService.getMovieRecommendationData_byBook_userCF("d18130495@mytudublin.ie", "book");

        Assert.assertEquals(6, moviesLikeThis.size());
    }

    @Order(7)
    @Test(timeout = 30000)
    @Transactional
    public void getBookRecommendationData_byBook_userCF_bookList() {
        List<Book> booksLikeThis = recommendationService.getBookRecommendationData_byBook_userCF("d18130495@mytudublin.ie", "book");

        Assert.assertEquals(6, booksLikeThis.size());
    }

    @Order(8)
    @Test(timeout = 30000)
    @Transactional
    public void getBookRecommendationData_byMovie_userCF_bookList() {
        List<Book> booksLikeThis = recommendationService.getBookRecommendationData_byMovie_userCF("d18130495@mytudublin.ie", "movie");

        Assert.assertEquals(6, booksLikeThis.size());
    }
}
