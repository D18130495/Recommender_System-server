package com.yushun.recommender.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yushun.recommender.algorithm.FindUserLikedItem;
import com.yushun.recommender.algorithm.UserCF;
import com.yushun.recommender.algorithm.UserRatingItemVo;
import com.yushun.recommender.model.common.mongoEntity.book.Book;
import com.yushun.recommender.model.common.mongoEntity.book.BookRate;
import com.yushun.recommender.model.common.mongoEntity.movie.Movie;
import com.yushun.recommender.model.common.mongoEntity.movie.MovieRate;
import com.yushun.recommender.model.user.BookFavourite;
import com.yushun.recommender.model.user.BookRating;
import com.yushun.recommender.model.user.MovieFavourite;
import com.yushun.recommender.model.user.MovieRating;
import com.yushun.recommender.repository.BookRepository;
import com.yushun.recommender.repository.MovieRepository;
import com.yushun.recommender.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

/**
 * <p>
 * Recommendation Service Impl
 * </p>
 *
 * @author yushun zeng
 * @since 2023-2-4
 */

@Service
public class RecommendationServiceImpl implements RecommendationService {
    @Autowired
    private MovieFavouriteService movieFavouriteService;

    @Autowired
    private MovieRatingService movieRatingService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private BookFavouriteService bookFavouriteService;

    @Autowired
    private BookRatingService bookRatingService;

    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<Movie> getMovieRecommendationData_byMovie_userCF(String email, String type) {
        // TODO
        // all the user in the system

        // user rating set
        Set<UserRatingItemVo> itemSet = new HashSet<>();

        // find user favourite list
        QueryWrapper movieFavouriteWrapper = new QueryWrapper();
        movieFavouriteWrapper.eq("email", email);
        movieFavouriteWrapper.eq("favourite", "T");

        List<MovieFavourite> movieFavouriteList = movieFavouriteService.list(movieFavouriteWrapper);

        if(movieFavouriteList != null) {
            for(MovieFavourite movie:movieFavouriteList) {
                UserRatingItemVo userRatingItemVo = new UserRatingItemVo();
                userRatingItemVo.setUserId(email);
                userRatingItemVo.setItemId(movie.getMovieId().toString());
                userRatingItemVo.setRate("4.0");

                itemSet.add(userRatingItemVo);
            }
        }

        // find user rating list
        QueryWrapper movieRatingWrapper = new QueryWrapper();
        movieRatingWrapper.eq("email", email);

        List<MovieRating> movieRatingList = movieRatingService.list(movieRatingWrapper);

        if(movieRatingList != null) {
            for(MovieRating movie:movieRatingList) {
                UserRatingItemVo userRatingItemVo =new UserRatingItemVo();
                userRatingItemVo.setUserId(email);
                userRatingItemVo.setItemId(movie.getMovieId().toString());
                userRatingItemVo.setRate(movie.getRating().toString());

                itemSet.add(userRatingItemVo);
            }
        }

        List<UserRatingItemVo> itemList = new ArrayList<>(itemSet);

        try {
            UserCF userCF = new UserCF();
            List<String> simUserItemListResult = userCF.simUserItemListResult(email, itemList, type);
            List<Movie> recommendationResultList = new ArrayList<>();

            // get movies by movieId
//            System.out.println(simUserItemListResult);

            for(int i = 0; i < 6; i++) {
                String movieId = simUserItemListResult.remove(new Random().nextInt(simUserItemListResult.size()));

                Movie movie = movieRepository.findByMovieId(Integer.parseInt(movieId));

                if(movie == null) {
                    i = i - 1;

                    continue;
                }

                List<MovieRate> rateList = movie.getRate();
                float total = 0;

                for(MovieRate movieRate: rateList) {
                    total = total + movieRate.getRating();
                }

                DecimalFormat decimalFormat =new DecimalFormat("#.0");
                movie.getParam().put("rate", decimalFormat.format(total / movie.getRate().size()));

                recommendationResultList.add(movie);
            }

            return recommendationResultList;
        }catch (Exception ignored) {}

        return null;
    }

    @Override
    public List<Movie> getMovieRecommendationData_byBook_userCF(String email, String type) {
        // TODO
        // all the user in the system

        // user rating set
        Set<UserRatingItemVo> itemSet = new HashSet<>();

        // find user favourite list
        QueryWrapper bookFavouriteWrapper = new QueryWrapper();
        bookFavouriteWrapper.eq("email", email);
        bookFavouriteWrapper.eq("favourite", "T");

        List<BookFavourite> bookFavouriteList = bookFavouriteService.list(bookFavouriteWrapper);

        if(bookFavouriteList != null) {
            for(BookFavourite book:bookFavouriteList) {
                UserRatingItemVo userRatingItemVo = new UserRatingItemVo();
                userRatingItemVo.setUserId(email);
                userRatingItemVo.setItemId(book.getIsbn());
                userRatingItemVo.setRate("4.0");

                itemSet.add(userRatingItemVo);
            }
        }

        // find user rating list
        QueryWrapper bookRatingWrapper = new QueryWrapper();
        bookRatingWrapper.eq("email", email);

        List<BookRating> bookRatingList = bookRatingService.list(bookRatingWrapper);

        if(bookRatingList != null) {
            for(BookRating book:bookRatingList) {
                UserRatingItemVo userRatingItemVo =new UserRatingItemVo();
                userRatingItemVo.setUserId(email);
                userRatingItemVo.setItemId(book.getIsbn());
                userRatingItemVo.setRate(book.getRating().toString());

                itemSet.add(userRatingItemVo);
            }
        }

        List<UserRatingItemVo> itemList = new ArrayList<>(itemSet);

        try {
            UserCF userCF = new UserCF();
            List<String> simUserListResult = userCF.simUserListResult(email, itemList, type);
            List<Movie> recommendationResultList = new ArrayList<>();

            // sim user list find movie recommendation,  sim user get by book
//            System.out.println(simUserListResult);

            FindUserLikedItem findUserLikedItem = new FindUserLikedItem();
            List<String> movieRecommendationList = findUserLikedItem.simUserRatedItemList(simUserListResult, "movie");

            // sim movie recommendation
//            System.out.println(movieRecommendationList);
            for(int i = 0; i < 6; i++) {
                Integer movieId = Integer.parseInt(movieRecommendationList.remove(new Random().nextInt(movieRecommendationList.size())));

                Movie movie = movieRepository.findByMovieId(movieId);

                if(movie == null) {
                    i = i - 1;

                    continue;
                }

                List<MovieRate> rateList = movie.getRate();
                float total = 0;

                for(MovieRate movieRate: rateList) {
                    total = total + movieRate.getRating();
                }

                DecimalFormat decimalFormat =new DecimalFormat("#.0");
                movie.getParam().put("rate", decimalFormat.format(total / movie.getRate().size()));

                recommendationResultList.add(movie);
            }

            return recommendationResultList;
        }catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<Book> getBookRecommendationData_byBook_userCF(String email, String type) {
        // TODO
        // all the user in the system

        // user rating set
        Set<UserRatingItemVo> itemSet = new HashSet<>();

        // find user favourite list
        QueryWrapper bookFavouriteWrapper = new QueryWrapper();
        bookFavouriteWrapper.eq("email", email);
        bookFavouriteWrapper.eq("favourite", "T");

        List<BookFavourite> bookFavouriteList = bookFavouriteService.list(bookFavouriteWrapper);

        if(bookFavouriteList != null) {
            for(BookFavourite book:bookFavouriteList) {
                UserRatingItemVo userRatingItemVo = new UserRatingItemVo();
                userRatingItemVo.setUserId(email);
                userRatingItemVo.setItemId(book.getIsbn());
                userRatingItemVo.setRate("4.0");

                itemSet.add(userRatingItemVo);
            }
        }

        // find user rating list
        QueryWrapper bookRatingWrapper = new QueryWrapper();
        bookRatingWrapper.eq("email", email);

        List<BookRating> bookRatingList = bookRatingService.list(bookRatingWrapper);

        if(bookRatingList != null) {
            for(BookRating book:bookRatingList) {
                UserRatingItemVo userRatingItemVo =new UserRatingItemVo();
                userRatingItemVo.setUserId(email);
                userRatingItemVo.setItemId(book.getIsbn());
                userRatingItemVo.setRate(book.getRating().toString());

                itemSet.add(userRatingItemVo);
            }
        }

        List<UserRatingItemVo> itemList = new ArrayList<>(itemSet);

        try {
            UserCF userCF = new UserCF();
            List<String> simUserItemListResult = userCF.simUserItemListResult(email, itemList, type);
            List<Book> recommendationResultList = new ArrayList<>();

            // get books isbn by book
//            System.out.println(simUserItemListResult);

            for(int i = 0; i < 6; i++) {
                String isbn = simUserItemListResult.remove(new Random().nextInt(simUserItemListResult.size()));

                Book book = bookRepository.findByISBN(isbn.substring(1));

                if(book == null) {
                    i = i - 1;

                    continue;
                }

                List<BookRate> rateList = book.getRate();
                float total = 0;

                for(BookRate bookRate: rateList) {
                    total = total + bookRate.getRating();
                }

                DecimalFormat decimalFormat =new DecimalFormat("#.0");
                book.getParam().put("rate", decimalFormat.format(total / book.getRate().size()));

                recommendationResultList.add(book);
            }

            return recommendationResultList;
        }catch (Exception ignored) {}

        return null;
    }

    @Override
    public List<Book> getBookRecommendationData_byMovie_userCF(String email, String type) {
        // TODO
        // all the user in the system

        // user rating set
        Set<UserRatingItemVo> itemSet = new HashSet<>();

        // find user favourite list
        QueryWrapper movieFavouriteWrapper = new QueryWrapper();
        movieFavouriteWrapper.eq("email", email);
        movieFavouriteWrapper.eq("favourite", "T");

        List<MovieFavourite> movieFavouriteList = movieFavouriteService.list(movieFavouriteWrapper);

        if(movieFavouriteList != null) {
            for(MovieFavourite movie:movieFavouriteList) {
                UserRatingItemVo userRatingItemVo = new UserRatingItemVo();
                userRatingItemVo.setUserId(email);
                userRatingItemVo.setItemId(movie.getMovieId().toString());
                userRatingItemVo.setRate("4");

                itemSet.add(userRatingItemVo);
            }
        }

        // find user rating list
        QueryWrapper movieRatingWrapper = new QueryWrapper();
        movieRatingWrapper.eq("email", email);

        List<MovieRating> movieRatingList = movieRatingService.list(movieRatingWrapper);

        if(movieRatingList != null) {
            for(MovieRating movie:movieRatingList) {
                UserRatingItemVo userRatingItemVo =new UserRatingItemVo();
                userRatingItemVo.setUserId(email);
                userRatingItemVo.setItemId(movie.getMovieId().toString());
                userRatingItemVo.setRate(movie.getRating().toString());

                itemSet.add(userRatingItemVo);
            }
        }

        List<UserRatingItemVo> itemList = new ArrayList<>(itemSet);

        try {
            UserCF userCF = new UserCF();
            List<String> simUserListResult = userCF.simUserListResult(email, itemList, type);
            List<Book> recommendationResultList = new ArrayList<>();

            // sim user list find book recommendation,  sim user get by movie
//            System.out.println(simUserListResult);

            FindUserLikedItem findUserLikedItem = new FindUserLikedItem();
            List<String> bookRecommendationList = findUserLikedItem.simUserRatedItemList(simUserListResult, "book");

            // sim book recommendation
//            System.out.println(bookRecommendationList);

            for(int i = 0; i < 6; i++) {
                String isbn = bookRecommendationList.remove(new Random().nextInt(bookRecommendationList.size()));

                Book book = bookRepository.findByISBN(isbn.substring(1));

                if(book == null) {
                    i = i - 1;

                    continue;
                }

                List<BookRate> rateList = book.getRate();
                float total = 0;

                for(BookRate bookRate: rateList) {
                    total = total + bookRate.getRating();
                }

                DecimalFormat decimalFormat =new DecimalFormat("#.0");
                book.getParam().put("rate", decimalFormat.format(total / book.getRate().size()));

                recommendationResultList.add(book);
            }

            return recommendationResultList;
        }catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
