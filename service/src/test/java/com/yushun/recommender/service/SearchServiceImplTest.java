package com.yushun.recommender.service;

import com.yushun.recommender.RecommenderApplication;
import com.yushun.recommender.vo.user.search.FuzzySearchReturnVo;
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

import java.util.HashMap;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RecommenderApplication.class)
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SearchServiceImplTest {
    @Autowired
    private SearchService searchService;

    /**
     * fuzzy search movie and book by title
     */
    @Order(1)
    @Test(timeout = 30000)
    @Transactional
    public void fuzzySearchMovieAndBookByTitleOrYear_noMovieAndBookResultFind_countZero() {
        HashMap<String, Object> stringObjectHashMap = searchService.fuzzySearchMovieAndBookByTitleOrYear("The 123", "title");

        Assert.assertEquals(0, stringObjectHashMap.get("count"));
    }

    @Order(2)
    @Test(timeout = 30000)
    @Transactional
    public void fuzzySearchMovieAndBookByTitleOrYear_bookDoNotHaveBookImage_bookImageL() {
        HashMap<String, Object> stringObjectHashMap = searchService.fuzzySearchMovieAndBookByTitleOrYear("Politically Correct Bedtime Stories: Modern Tales for Our Life and Times", "title");

        Assert.assertNotEquals(0, stringObjectHashMap.get("count"));
    }

    @Order(3)
    @Test(timeout = 30000)
    @Transactional
    public void fuzzySearchMovieAndBookByTitleOrYear_movieDoNotHaveActor_currentlyNotAvailable() {
        HashMap<String, Object> stringObjectHashMap = searchService.fuzzySearchMovieAndBookByTitleOrYear("THE ADVENTURES OF SHERLOCK HOLMES AND DR. WATSON: THE HOUND OF THE BASKERVILLES", "title");

        Assert.assertEquals("Currently not available", ((List<FuzzySearchReturnVo>)stringObjectHashMap.get("fuzzySearchReturnVoList")).get(0).getActorList());
    }

    @Order(4)
    @Test(timeout = 30000)
    @Transactional
    public void fuzzySearchMovieAndBookByTitleOrYear_movieDoNotHaveDirector_currentlyNotAvailable() {
        HashMap<String, Object> stringObjectHashMap = searchService.fuzzySearchMovieAndBookByTitleOrYear("THE ADVENTURES OF SHERLOCK HOLMES AND DR. WATSON: THE HOUND OF THE BASKERVILLES", "title");

        Assert.assertEquals("Currently not available", ((List<FuzzySearchReturnVo>)stringObjectHashMap.get("fuzzySearchReturnVoList")).get(0).getDirector());
    }

    @Order(5)
    @Test(timeout = 30000)
    @Transactional
    public void fuzzySearchMovieAndBookByTitleOrYear_movieAndBookResultFind_countNotZero() {
        HashMap<String, Object> stringObjectHashMap = searchService.fuzzySearchMovieAndBookByTitleOrYear("The", "title");

        Assert.assertNotEquals(0, stringObjectHashMap.get("count"));
    }
}
