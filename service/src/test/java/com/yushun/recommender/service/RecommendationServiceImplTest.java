package com.yushun.recommender.service;

import com.yushun.recommender.RecommenderApplication;
import com.yushun.recommender.model.common.mongoEntity.book.Book;
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

    @Test(timeout = 30000)
    @Transactional
    public void bookRecommendationData_byBook_itemCF_findBookList_bookList() {
        List<Book> bookRecommendationData_byBook_itemCF = recommendationService.getBookRecommendationData_byBook_itemCF("990415zys@gmail.com");

        Assert.assertEquals(6, bookRecommendationData_byBook_itemCF.size());
    }

    @Test(timeout = 30000)
    @Transactional
    public void getMovieLikeThis_findBookList_bookList() {
        List<Book> booksLikeThis = recommendationService.getBooksLikeThis("0060923717");

        Assert.assertEquals(6, booksLikeThis.size());
    }
}
