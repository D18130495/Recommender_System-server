package com.yushun.recommender.algorithm.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yushun.recommender.algorithm.ItemCF;
import com.yushun.recommender.algorithm.UserRatingItemVo;
import com.yushun.recommender.model.user.BookFavourite;
import com.yushun.recommender.model.user.BookRating;
import com.yushun.recommender.service.BookFavouriteService;
import com.yushun.recommender.service.BookRatingService;
import com.yushun.recommender.service.MovieFavouriteService;
import com.yushun.recommender.service.MovieRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * Item Similarity Calculation Task
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-29
 */

@Component
public class ItemCFTask {
    @Autowired
    private MovieFavouriteService movieFavouriteService;

    @Autowired
    private MovieRatingService movieRatingService;

    @Autowired
    private BookFavouriteService bookFavouriteService;

    @Autowired
    private BookRatingService bookRatingService;

//    @Scheduled(cron ="*/6 * * * * ?") // 6 second
//    @Scheduled(cron ="0 0/1 * * * ?") // 1 min
//    @Scheduled(cron ="0 0 1 * * ?") // every day at 1 am
    public void itemSimilarity() throws IOException {
        List<UserRatingItemVo> systemUserBookList = this.getSystemUserBookList();

        ItemCF.generateSimilarityItemTxt(systemUserBookList, "book");
        System.out.println("Task finished");
    }

    public List<UserRatingItemVo> getSystemUserMovieList() {
        // all the user favourite items set in the system
        Set<UserRatingItemVo> itemSet = new HashSet<>();

        return new ArrayList<>(itemSet);
    }

    public List<UserRatingItemVo> getSystemUserBookList() {
        // all the user favourite items set in the system
        Set<UserRatingItemVo> itemSet = new HashSet<>();

        // find user favourite list
        QueryWrapper bookFavouriteWrapper = new QueryWrapper();

        List<BookFavourite> bookFavouriteList = bookFavouriteService.list(bookFavouriteWrapper);

        if(bookFavouriteList != null) {
            for(BookFavourite book:bookFavouriteList) {
                UserRatingItemVo userRatingItemVo = new UserRatingItemVo();
                userRatingItemVo.setUserId(book.getEmail());
                userRatingItemVo.setItemId(book.getIsbn());

                if(book.getFavourite().equals("T")) {
                    userRatingItemVo.setRate("4.0");
                }else if(book.getFavourite().equals("F")) {
                    userRatingItemVo.setRate("-4.0");
                }else {
                    continue;
                }

                itemSet.add(userRatingItemVo);
            }
        }

        // find user rating list
        List<BookRating> bookRatingList = bookRatingService.list();

        if(bookRatingList != null) {
            for(BookRating book:bookRatingList) {
                UserRatingItemVo userRatingItemVo =new UserRatingItemVo();
                userRatingItemVo.setUserId(book.getEmail());
                userRatingItemVo.setItemId(book.getIsbn());
                userRatingItemVo.setRate(book.getRating().toString());

                itemSet.add(userRatingItemVo);
            }
        }

        return new ArrayList<>(itemSet);
    }
}
