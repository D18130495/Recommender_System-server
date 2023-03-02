package com.yushun.recommender.service;

import com.yushun.recommender.RecommenderApplication;
import com.yushun.recommender.model.user.BookRating;
import com.yushun.recommender.vo.user.book.BookRatingReturnVo;
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
public class BookRatingServiceImplTest {
    @Autowired
    private BookRatingService bookRatingService;

    /**
     * get user book rate status
     */
    @Order(1)
    @Test(timeout = 30000)
    @Transactional
    public void getUserBookRating_userNotFind_null() {
        BookRatingReturnVo userBookRating = bookRatingService.getUserBookRating("0451197003", "d18130495@mytudublin.com");

        Assert.assertNull(userBookRating);
    }

    @Order(2)
    @Test(timeout = 30000)
    @Transactional
    public void getUserBookRating_bookNotFind_null() {
        BookRatingReturnVo userBookRating = bookRatingService.getUserBookRating("0451197004", "990415zys@gmail.com");

        Assert.assertNull(userBookRating);
    }

    @Order(3)
    @Test(timeout = 30000)
    @Transactional
    public void getUserBookRating_userNotRatingThisBook_null() {
        BookRatingReturnVo userBookRating = bookRatingService.getUserBookRating("0001714600", "d18130495@mytudublin.ie");

        Assert.assertNull(userBookRating);
    }

    @Order(4)
    @Test(timeout = 30000)
    @Transactional
    public void getUserBookRating_findBookRating_bookRatingReturnVo() {
        BookRatingReturnVo userBookRating = bookRatingService.getUserBookRating("0805076557", "d18130495@mytudublin.ie");

        Assert.assertNotNull(userBookRating.getRating());
    }

    /**
     * change book rating status
     */
    @Order(5)
    @Test(timeout = 30000)
    @Transactional
    public void addOrUpdateUserBookRating_userNotFind_userNotFind() {
        BookRating bookRating = new BookRating();
        bookRating.setEmail("d18130495@mytudublin.com");

        String addOrUpdateState = bookRatingService.addOrUpdateUserBookRating(bookRating);

        Assert.assertEquals("User not find", addOrUpdateState);
    }

    @Order(6)
    @Test(timeout = 30000)
    @Transactional
    public void addOrUpdateUserBookRating_bookNotFind_bookNotFind() {
        BookRating bookRating = new BookRating();
        bookRating.setEmail("d18130495@mytudublin.ie");
        bookRating.setIsbn("0451197004");

        String addOrUpdateState = bookRatingService.addOrUpdateUserBookRating(bookRating);

        Assert.assertEquals("Book not find", addOrUpdateState);
    }

    @Order(7)
    @Test(timeout = 30000)
    @Transactional
    public void addOrUpdateUserBookRating_addNewRating_successfullyRatingThisBook() {
        BookRating bookRating = new BookRating();
        bookRating.setEmail("d18130495@mytudublin.ie");
        bookRating.setIsbn("8865437162");

        String addOrUpdateState = bookRatingService.addOrUpdateUserBookRating(bookRating);

        Assert.assertEquals("Successfully rating this book", addOrUpdateState);
    }

    @Order(8)
    @Test(timeout = 30000)
    @Transactional
    public void addOrUpdateUserBookRating_updateRating_successfullyUpdatedRating() {
        BookRating bookRating = new BookRating();
        bookRating.setEmail("d18130495@mytudublin.ie");
        bookRating.setIsbn("0805076557");
        bookRating.setRating(5F);

        String addOrUpdateState = bookRatingService.addOrUpdateUserBookRating(bookRating);

        Assert.assertEquals("Successfully updated rating", addOrUpdateState);
    }
}
