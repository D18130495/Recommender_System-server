package com.yushun.recommender.service;

import com.yushun.recommender.RecommenderApplication;
import com.yushun.recommender.model.user.BookFavourite;
import com.yushun.recommender.vo.user.book.BookFavouriteReturnVo;
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
public class BookFavouriteServiceImplTest {
    @Autowired
    private BookFavouriteService bookFavouriteService;

    /**
     * get user book like status
     */
    @Order(1)
    @Test(timeout = 30000)
    @Transactional
    public void getUserBookFavourite_userNotFind_null() {
        BookFavouriteReturnVo userBookFavourite = bookFavouriteService.getUserBookFavourite("0451197003", "d18130495@mytudublin.com");

        Assert.assertNull(userBookFavourite);
    }

    @Order(2)
    @Test(timeout = 30000)
    @Transactional
    public void getUserBookFavourite_bookNotFind_null() {
        BookFavouriteReturnVo userBookFavourite = bookFavouriteService.getUserBookFavourite("0451197004", "990415zys@gmail.com");

        Assert.assertNull(userBookFavourite);
    }

    @Order(3)
    @Test(timeout = 30000)
    @Transactional
    public void getUserBookFavourite_userNotFavouriteThisBook_null() {
        BookFavouriteReturnVo userBookFavourite = bookFavouriteService.getUserBookFavourite("0001714600", "d18130495@mytudublin.ie");

        Assert.assertNull(userBookFavourite);
    }

    @Order(4)
    @Test(timeout = 30000)
    @Transactional
    public void getUserBookFavourite_findBookFavourite_doNotLike() {
        BookFavouriteReturnVo userBookFavourite = bookFavouriteService.getUserBookFavourite("0545935202", "d18130495@mytudublin.ie");

        Assert.assertEquals("1", userBookFavourite.getFavourite());
    }

    @Order(5)
    @Test(timeout = 30000)
    @Transactional
    public void getUserBookFavourite_findBookFavourite_normal() {
        BookFavouriteReturnVo userBookFavourite = bookFavouriteService.getUserBookFavourite("0439226481", "d18130495@mytudublin.ie");

        Assert.assertEquals("2", userBookFavourite.getFavourite());
    }

    @Order(6)
    @Test(timeout = 30000)
    @Transactional
    public void getUserBookFavourite_findBookFavourite_favourite() {
        BookFavouriteReturnVo userBookFavourite = bookFavouriteService.getUserBookFavourite("8865437162", "d18130495@mytudublin.ie");

        Assert.assertEquals("3", userBookFavourite.getFavourite());
    }

    /**
     * change book favourite status
     */
    @Order(7)
    @Test(timeout = 30000)
    @Transactional
    public void likeOrUnlikeBook_userNotFind_userNotFind() {
        BookFavourite bookFavourite = new BookFavourite();
        bookFavourite.setEmail("d18130495@mytudublin.com");

        Assert.assertEquals("User not find", bookFavouriteService.likeOrUnlikeBook(bookFavourite));
    }

    @Order(8)
    @Test(timeout = 30000)
    @Transactional
    public void likeOrUnlikeBook_bookNotFind_bookNotFind() {
        BookFavourite bookFavourite = new BookFavourite();
        bookFavourite.setEmail("d18130495@mytudublin.ie");
        bookFavourite.setIsbn("0451197004");

        Assert.assertEquals("Book not find", bookFavouriteService.likeOrUnlikeBook(bookFavourite));
    }

    @Order(9)
    @Test(timeout = 30000)
    @Transactional
    public void likeOrUnlikeBook_addNewFavouriteCase1_doNotLike() {
        BookFavourite bookFavourite = new BookFavourite();
        bookFavourite.setEmail("d18130495@mytudublin.ie");
        bookFavourite.setIsbn("1897093705");
        bookFavourite.setFavourite("1");

        Assert.assertEquals("Don't like", bookFavouriteService.likeOrUnlikeBook(bookFavourite));
    }

    @Order(10)
    @Test(timeout = 30000)
    @Transactional
    public void likeOrUnlikeBook_addNewFavouriteCase2_normal() {
        BookFavourite bookFavourite = new BookFavourite();
        bookFavourite.setEmail("d18130495@mytudublin.ie");
        bookFavourite.setIsbn("1897093705");
        bookFavourite.setFavourite("2");

        Assert.assertEquals("Normal", bookFavouriteService.likeOrUnlikeBook(bookFavourite));
    }

    @Order(11)
    @Test(timeout = 30000)
    @Transactional
    public void likeOrUnlikeBook_addNewFavouriteCase3_favourite() {
        BookFavourite bookFavourite = new BookFavourite();
        bookFavourite.setEmail("d18130495@mytudublin.ie");
        bookFavourite.setIsbn("1897093705");
        bookFavourite.setFavourite("3");

        Assert.assertEquals("Favourite", bookFavouriteService.likeOrUnlikeBook(bookFavourite));
    }

    @Order(12)
    @Test(timeout = 30000)
    @Transactional
    public void likeOrUnlikeBook_updateFavouriteCase1_favourite() {
        BookFavourite bookFavourite = new BookFavourite();
        bookFavourite.setEmail("d18130495@mytudublin.ie");
        bookFavourite.setIsbn("8865437162");
        bookFavourite.setFavourite("1");

        Assert.assertEquals("Don't like", bookFavouriteService.likeOrUnlikeBook(bookFavourite));
    }

    @Order(13)
    @Test(timeout = 30000)
    @Transactional
    public void likeOrUnlikeBook_updateFavouriteCase2_favourite() {
        BookFavourite bookFavourite = new BookFavourite();
        bookFavourite.setEmail("d18130495@mytudublin.ie");
        bookFavourite.setIsbn("8865437162");
        bookFavourite.setFavourite("2");

        Assert.assertEquals("Normal", bookFavouriteService.likeOrUnlikeBook(bookFavourite));
    }

    @Order(14)
    @Test(timeout = 30000)
    @Transactional
    public void likeOrUnlikeBook_updateFavouriteCase3_favourite() {
        BookFavourite bookFavourite = new BookFavourite();
        bookFavourite.setEmail("d18130495@mytudublin.ie");
        bookFavourite.setIsbn("8865437162");
        bookFavourite.setFavourite("3");

        Assert.assertEquals("Favourite", bookFavouriteService.likeOrUnlikeBook(bookFavourite));
    }

    @Order(15)
    @Test(timeout = 30000)
    @Transactional
    public void likeOrUnlikeBook_defaultCase_error() {
        BookFavourite bookFavourite = new BookFavourite();
        bookFavourite.setEmail("d18130495@mytudublin.ie");
        bookFavourite.setIsbn("8865437162");
        bookFavourite.setFavourite("4");

        Assert.assertEquals("Error", bookFavouriteService.likeOrUnlikeBook(bookFavourite));
    }
}
