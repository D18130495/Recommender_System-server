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

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RecommenderApplication.class)
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookFavouriteServiceImplTest {
    @Autowired
    private BookFavouriteService bookFavouriteService;

    /**
     * get random book list(size 6)
     */
    @Order(1)
    @Test(timeout = 30000)
    @Transactional
    public void getUserBookFavourite_findBookFavourite_doNotLike() {
        BookFavouriteReturnVo userBookFavourite = bookFavouriteService.getUserBookFavourite("0451197003", "990415zys@gmail.com");

        Assert.assertEquals("1", userBookFavourite.getFavourite());
    }

    @Order(2)
    @Test(timeout = 30000)
    @Transactional
    public void getUserBookFavourite_findBookFavourite_normal() {
        BookFavouriteReturnVo userBookFavourite = bookFavouriteService.getUserBookFavourite("0872203948", "990415zys@gmail.com");

        Assert.assertEquals("2", userBookFavourite.getFavourite());
    }

    @Order(3)
    @Test(timeout = 30000)
    @Transactional
    public void getUserBookFavourite_findBookFavourite_favourite() {
        BookFavouriteReturnVo userBookFavourite = bookFavouriteService.getUserBookFavourite("067174139X", "990415zys@gmail.com");

        Assert.assertEquals("3", userBookFavourite.getFavourite());
    }

    @Order(4)
    @Test(timeout = 30000)
    @Transactional
    public void getUserBookFavourite_userNotFind_null() {
        BookFavouriteReturnVo userBookFavourite = bookFavouriteService.getUserBookFavourite("0451197003", "d18130495@mytudublin.com");

        Assert.assertNull(userBookFavourite);
    }

    @Order(5)
    @Test(timeout = 30000)
    @Transactional
    public void getUserBookFavourite_bookNotFind_null() {
        BookFavouriteReturnVo userBookFavourite = bookFavouriteService.getUserBookFavourite("0451197004", "990415zys@gmail.com");

        Assert.assertNull(userBookFavourite);
    }

    @Order(6)
    @Test(timeout = 30000)
    @Transactional
    public void getUserBookFavourite_userNotFavouriteThisBook_null() {
        BookFavouriteReturnVo userBookFavourite = bookFavouriteService.getUserBookFavourite("0001714600", "990415zys@gmail.com");

        Assert.assertNull(userBookFavourite);
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
    public void likeOrUnlikeBook_userNotFind_bookNotFind() {
        BookFavourite bookFavourite = new BookFavourite();
        bookFavourite.setEmail("990415zys@gmail.com");
        bookFavourite.setIsbn("0451197004");

        Assert.assertEquals("Book not find", bookFavouriteService.likeOrUnlikeBook(bookFavourite));
    }

    @Order(9)
    @Test(timeout = 30000)
    @Transactional
    public void likeOrUnlikeBook_addNewFavouriteCase1_doNotLike() {
        BookFavourite bookFavourite = new BookFavourite();
        bookFavourite.setEmail("d18130495@mytudublin.ie");
        bookFavourite.setIsbn("0451197003");
        bookFavourite.setFavourite("1");

        Assert.assertEquals("Don't like", bookFavouriteService.likeOrUnlikeBook(bookFavourite));
    }

    @Order(10)
    @Test(timeout = 30000)
    @Transactional
    public void likeOrUnlikeBook_addNewFavouriteCase2_normal() {
        BookFavourite bookFavourite = new BookFavourite();
        bookFavourite.setEmail("d18130495@mytudublin.ie");
        bookFavourite.setIsbn("0451197003");
        bookFavourite.setFavourite("2");

        Assert.assertEquals("Normal", bookFavouriteService.likeOrUnlikeBook(bookFavourite));
    }

    @Order(11)
    @Test(timeout = 30000)
    @Transactional
    public void likeOrUnlikeBook_addNewFavouriteCase3_favourite() {
        BookFavourite bookFavourite = new BookFavourite();
        bookFavourite.setEmail("d18130495@mytudublin.ie");
        bookFavourite.setIsbn("0451197003");
        bookFavourite.setFavourite("3");

        Assert.assertEquals("Favourite", bookFavouriteService.likeOrUnlikeBook(bookFavourite));
    }

    @Order(12)
    @Test(timeout = 30000)
    @Transactional
    public void likeOrUnlikeBook_updateFavouriteCase1_favourite() {
        BookFavourite bookFavourite = new BookFavourite();
        bookFavourite.setEmail("990415zys@gmail.com");
        bookFavourite.setIsbn("0451197003");
        bookFavourite.setFavourite("1");

        Assert.assertEquals("Don't like", bookFavouriteService.likeOrUnlikeBook(bookFavourite));
    }

    @Order(13)
    @Test(timeout = 30000)
    @Transactional
    public void likeOrUnlikeBook_updateFavouriteCase2_favourite() {
        BookFavourite bookFavourite = new BookFavourite();
        bookFavourite.setEmail("990415zys@gmail.com");
        bookFavourite.setIsbn("0451197003");
        bookFavourite.setFavourite("2");

        Assert.assertEquals("Normal", bookFavouriteService.likeOrUnlikeBook(bookFavourite));
    }

    @Order(14)
    @Test(timeout = 30000)
    @Transactional
    public void likeOrUnlikeBook_updateFavouriteCase3_favourite() {
        BookFavourite bookFavourite = new BookFavourite();
        bookFavourite.setEmail("990415zys@gmail.com");
        bookFavourite.setIsbn("0451197003");
        bookFavourite.setFavourite("3");

        Assert.assertEquals("Favourite", bookFavouriteService.likeOrUnlikeBook(bookFavourite));
    }

    @Order(15)
    @Test(timeout = 30000)
    @Transactional
    public void likeOrUnlikeBook_defaultCase_error() {
        BookFavourite bookFavourite = new BookFavourite();
        bookFavourite.setEmail("990415zys@gmail.com");
        bookFavourite.setIsbn("0451197003");
        bookFavourite.setFavourite("4");

        Assert.assertEquals("Error", bookFavouriteService.likeOrUnlikeBook(bookFavourite));
    }
}
