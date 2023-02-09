//package com.yushun.recommender.algorithm.task;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.yushun.recommender.algorithm.ItemCF;
//import com.yushun.recommender.algorithm.UserRatingItemVo;
//import com.yushun.recommender.model.user.MovieFavourite;
//import com.yushun.recommender.model.user.MovieRating;
//import com.yushun.recommender.service.MovieFavouriteService;
//import com.yushun.recommender.service.MovieRatingService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
///**
// * <p>
// * Item Similarity Calculation Task
// * </p>
// *
// * @author yushun zeng
// * @since 2023-1-29
// */
//
//@Component
//public class ItemCFTask {
//    @Autowired
//    private MovieFavouriteService movieFavouriteService;
//
//    @Autowired
//    private MovieRatingService movieRatingService;
//
////    @Scheduled(cron ="*/6 * * * * ?") // 1 min
//    @Scheduled(cron ="0 0/1 * * * ?") // 1 min
////    @Scheduled(cron ="0 0 1 * * ?") // every day at 1 am
//    public void itemSimilarity() throws IOException {
//        List<UserRatingItemVo> systemUserMovieList = this.getSystemUserMovieList();
//
//        ItemCF.generateSimilarityItemTxt();
//        System.out.println("ok");
//    }
//
//    public List<UserRatingItemVo> getSystemUserMovieList() {
//        // TODO
//        // all the user in the system
//
//        // user rating set
//        Set<UserRatingItemVo> itemSet = new HashSet<>();
//
//        // find user favourite list
//        QueryWrapper movieFavouriteWrapper = new QueryWrapper();
//        movieFavouriteWrapper.eq("favourite", "T");
//        movieFavouriteWrapper.eq("favourite", "F");
//
//        List<MovieFavourite> movieFavouriteList = movieFavouriteService.list(movieFavouriteWrapper);
//
//        if(movieFavouriteList != null) {
//            for(MovieFavourite movie:movieFavouriteList) {
//                UserRatingItemVo userRatingItemVo = new UserRatingItemVo();
//                userRatingItemVo.setUserId(email);
//                userRatingItemVo.setItemId(movie.getMovieId().toString());
//                userRatingItemVo.setRate("4.0");
//
//                itemSet.add(userRatingItemVo);
//            }
//        }
//
//        // find user rating list
//        QueryWrapper movieRatingWrapper = new QueryWrapper();
//        movieRatingWrapper.eq("email", email);
//
//        List<MovieRating> movieRatingList = movieRatingService.list(movieRatingWrapper);
//
//        if(movieRatingList != null) {
//            for(MovieRating movie:movieRatingList) {
//                UserRatingItemVo userRatingItemVo =new UserRatingItemVo();
//                userRatingItemVo.setUserId(email);
//                userRatingItemVo.setItemId(movie.getMovieId().toString());
//                userRatingItemVo.setRate(movie.getRating().toString());
//
//                itemSet.add(userRatingItemVo);
//            }
//        }
//
//        return new ArrayList<>(itemSet);
//    }
//}
