package com.yushun.recommender.service;

import com.yushun.recommender.RecommenderApplication;
import com.yushun.recommender.model.common.mongoEntity.book.Book;
import com.yushun.recommender.model.user.BookFavourite;
import com.yushun.recommender.model.user.BookRating;
import com.yushun.recommender.vo.user.book.BookLikeListReturnVo;
import com.yushun.recommender.vo.user.book.BookRatingListReturnVo;
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
public class BookServiceImplTest {
    @Autowired
    private BookService bookService;

    @Autowired
    private BookFavouriteService bookFavouriteService;

    @Autowired
    private BookRatingService bookRatingService;

    /**
     * get random book list(size 6)
     */
    @Order(1)
    @Test(timeout = 30000)
    @Transactional
    public void getRandomBookList_findBookList_success() {
        List<Book> randomBookList = bookService.getRandomBookList();

        Assert.assertEquals(6, randomBookList.size());
    }

    /**
     * get book by ISBN
     */
    @Order(2)
    @Test(timeout = 30000)
    @Transactional
    public void getBookByISBN_findBook_book() {
        Book bookByISBN = bookService.getBookByISBN("0345439252");

        Assert.assertNotNull(bookByISBN);
    }

    @Order(3)
    @Test(timeout = 30000)
    @Transactional
    public void getBookByISBN_bookNotFind_null() {
        Book bookByISBN = bookService.getBookByISBN("031224499");

        Assert.assertNull(bookByISBN);
    }

    /**
     * get user like and rating book count
     */
    @Order(4)
    @Test(timeout = 30000)
    @Transactional
    public void getUserLikeAndRatingBookCount_userNotFind_minus() {
        Integer userLikeAndRatingBookCount = bookService.getUserLikeAndRatingBookCount("d18130495@mytudublin.com");

        Assert.assertEquals(new Integer(-1), userLikeAndRatingBookCount);
    }

    @Order(5)
    @Test(timeout = 30000)
    @Transactional
    public void getUserLikeAndRatingBookCount_userNotLikeOrRateBook_zero() {
        Integer userLikeAndRatingBookCount = bookService.getUserLikeAndRatingBookCount("d18130495@mytudublin.ie");

        Assert.assertEquals(new Integer(0), userLikeAndRatingBookCount);
    }

    @Order(6)
    @Test(timeout = 30000)
    @Transactional
    public void getUserLikeAndRatingBookCount_userOnlyRateBook_rateListSize() {
        BookFavourite bookFavourite = new BookFavourite();
        bookFavourite.setEmail("d18130495@mytudublin.ie");
        bookFavourite.setIsbn("0345439252");
        bookFavourite.setFavourite("3");

        bookFavouriteService.likeOrUnlikeBook(bookFavourite);

        Integer userLikeAndRatingBookCount = bookService.getUserLikeAndRatingBookCount("d18130495@mytudublin.ie");

        Assert.assertEquals(new Integer(1), userLikeAndRatingBookCount);
    }

    @Order(7)
    @Test(timeout = 30000)
    @Transactional
    public void getUserLikeAndRatingBookCount_userOnlyLikeBook_rateListSize() {
        BookRating bookRating = new BookRating();
        bookRating.setEmail("d18130495@mytudublin.ie");
        bookRating.setIsbn("0345439252");
        bookRating.setRating(3F);

        bookRatingService.addOrUpdateUserBookRating(bookRating);

        Integer userLikeAndRatingBookCount = bookService.getUserLikeAndRatingBookCount("d18130495@mytudublin.ie");

        Assert.assertEquals(new Integer(1), userLikeAndRatingBookCount);
    }

    @Order(8)
    @Test(timeout = 30000)
    @Transactional
    public void getUserLikeAndRatingBookCount_userLikeAndRateSameBook_likeAndRateListSize() {
        BookFavourite bookFavourite = new BookFavourite();
        bookFavourite.setEmail("d18130495@mytudublin.ie");
        bookFavourite.setIsbn("0345439252");
        bookFavourite.setFavourite("3");

        bookFavouriteService.likeOrUnlikeBook(bookFavourite);

        BookRating bookRating = new BookRating();
        bookRating.setEmail("d18130495@mytudublin.ie");
        bookRating.setIsbn("0345439252");
        bookRating.setRating(3F);

        bookRatingService.addOrUpdateUserBookRating(bookRating);

        Integer userLikeAndRatingBookCount = bookService.getUserLikeAndRatingBookCount("d18130495@mytudublin.ie");

        Assert.assertEquals(new Integer(1), userLikeAndRatingBookCount);
    }

    @Order(8)
    @Test(timeout = 30000)
    @Transactional
    public void getUserLikeAndRatingBookCount_userLikeAndRateDifferentBook_likeAndRateListSize() {
        BookFavourite bookFavourite = new BookFavourite();
        bookFavourite.setEmail("d18130495@mytudublin.ie");
        bookFavourite.setIsbn("0345439252");
        bookFavourite.setFavourite("3");

        bookFavouriteService.likeOrUnlikeBook(bookFavourite);

        BookRating bookRating = new BookRating();
        bookRating.setEmail("d18130495@mytudublin.ie");
        bookRating.setIsbn("0380787814");
        bookRating.setRating(3F);

        bookRatingService.addOrUpdateUserBookRating(bookRating);

        Integer userLikeAndRatingBookCount = bookService.getUserLikeAndRatingBookCount("d18130495@mytudublin.ie");

        Assert.assertEquals(new Integer(2), userLikeAndRatingBookCount);
    }

    /**
     * get user book like list
     */
    @Order(9)
    @Test(timeout = 30000)
    @Transactional
    public void getUserBookLikeList_userNotFind_null() {
        List<BookLikeListReturnVo> userBookLikeList = bookService.getUserBookLikeList("d18130495@mytudublin.com");

        Assert.assertNull(userBookLikeList);
    }

    @Order(10)
    @Test(timeout = 30000)
    @Transactional
    public void getUserBookLikeList_userNotLike_null() {
        List<BookLikeListReturnVo> userBookLikeList = bookService.getUserBookLikeList("d18130495@mytudublin.ie");

        Assert.assertNull(userBookLikeList);
    }

    @Order(11)
    @Test(timeout = 30000)
    @Transactional
    public void getUserBookLikeList_userNotRateThisBook_rateIsNull() {
        BookFavourite bookFavourite = new BookFavourite();
        bookFavourite.setEmail("d18130495@mytudublin.ie");
        bookFavourite.setIsbn("0345439252");
        bookFavourite.setFavourite("3");

        bookFavouriteService.likeOrUnlikeBook(bookFavourite);

        List<BookLikeListReturnVo> userBookLikeList = bookService.getUserBookLikeList("d18130495@mytudublin.ie");

        Assert.assertNull(userBookLikeList.get(0).getRating());
    }

    @Order(12)
    @Test(timeout = 30000)
    @Transactional
    public void getUserBookLikeList_userRateThisBook_rateIsNotNull() {
        BookFavourite bookFavourite = new BookFavourite();
        bookFavourite.setEmail("d18130495@mytudublin.ie");
        bookFavourite.setIsbn("0345439252");
        bookFavourite.setFavourite("3");

        bookFavouriteService.likeOrUnlikeBook(bookFavourite);

        BookRating bookRating = new BookRating();
        bookRating.setEmail("d18130495@mytudublin.ie");
        bookRating.setIsbn("0345439252");
        bookRating.setRating(3F);

        bookRatingService.addOrUpdateUserBookRating(bookRating);

        List<BookLikeListReturnVo> userBookLikeList = bookService.getUserBookLikeList("d18130495@mytudublin.ie");

        Assert.assertEquals(new Float(3), userBookLikeList.get(0).getRating());
    }

    /**
     * get user book rating list
     */
    @Order(13)
    @Test(timeout = 30000)
    @Transactional
    public void getUserBookRatingList_userNotFind_null() {
        List<BookRatingListReturnVo> userBookRatingList = bookService.getUserBookRatingList("d18130495@mytudublin.com");

        Assert.assertNull(userBookRatingList);
    }

    @Order(14)
    @Test(timeout = 30000)
    @Transactional
    public void getUserBookRatingList_userNotRateBook_null() {
        List<BookRatingListReturnVo> userBookRatingList = bookService.getUserBookRatingList("d18130495@mytudublin.ie");

        Assert.assertNull(userBookRatingList);
    }

    @Order(15)
    @Test(timeout = 30000)
    @Transactional
    public void getUserBookRatingList_userRateBook_bookRateListIsNotNull() {
        BookRating bookRating = new BookRating();
        bookRating.setEmail("d18130495@mytudublin.ie");
        bookRating.setIsbn("0345439252");
        bookRating.setRating(3F);

        bookRatingService.addOrUpdateUserBookRating(bookRating);

        List<BookRatingListReturnVo> userBookRatingList = bookService.getUserBookRatingList("d18130495@mytudublin.ie");

        Assert.assertNotNull(userBookRatingList);
    }
}
