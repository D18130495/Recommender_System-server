package com.yushun.recommender.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yushun.recommender.algorithm.CFUtils;
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

import java.io.BufferedReader;
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
    public List<Movie> getMovieRecommendationData_byMovie_itemCF(String email) {
        List<UserRatingItemVo> systemUserMovieList = this.getSystemUserMovieList(email);

        try {
//            ItemCF.simUserItemListResult(email, systemUserMovieList, "movie");
        }catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    @Override
    public List<Book> getBookRecommendationData_byBook_itemCF(String email) {
        return null;
    }

    @Override
    public List<Movie> getMovieRecommendationData_byMovie_userCF(String email, String type) {
        List<UserRatingItemVo> systemUserMovieList = this.getSystemUserMovieList(email);

        try {
            List<String> simUserItemListResult = UserCF.simUserItemListResult(email, systemUserMovieList, type);

            // get movies by movieId
//            System.out.println(simUserItemListResult);

            return this.getRandomMovieRecommendationList(simUserItemListResult);
        }catch (Exception ignored) {}

        return null;
    }

    @Override
    public List<Movie> getMovieRecommendationData_byBook_userCF(String email, String type) {
        List<UserRatingItemVo> systemUserBookList = this.getSystemUserBookList(email);

        try {
            UserCF userCF = new UserCF();
            List<String> simUserListResult = userCF.simUserListResult(email, systemUserBookList, type);

            // sim user list find movie recommendation,  sim user get by book
//            System.out.println(simUserListResult);

            List<String> movieRecommendationList = FindUserLikedItem.simUserRatedItemList(simUserListResult, "movie");

            // sim movie recommendation
//            System.out.println(movieRecommendationList);

            return this.getRandomMovieRecommendationList(movieRecommendationList);
        }catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<Book> getBookRecommendationData_byBook_userCF(String email, String type) {
        List<UserRatingItemVo> systemUserBookList = this.getSystemUserBookList(email);

        try {
            List<String> simUserItemListResult = UserCF.simUserItemListResult(email, systemUserBookList, type);

            // get books isbn by book
//            System.out.println(simUserItemListResult);

            return this.getRandomBookRecommendationList(simUserItemListResult);
        }catch (Exception ignored) {}

        return null;
    }

    @Override
    public List<Book> getBookRecommendationData_byMovie_userCF(String email, String type) {
        List<UserRatingItemVo> systemUserMovieList = this.getSystemUserMovieList(email);

        try {
            UserCF userCF = new UserCF();
            List<String> simUserListResult = userCF.simUserListResult(email, systemUserMovieList, type);

            // sim user list find book recommendation,  sim user get by movie
//            System.out.println(simUserListResult);

            List<String> bookRecommendationList = FindUserLikedItem.simUserRatedItemList(simUserListResult, "book");

            // sim book recommendation
//            System.out.println(bookRecommendationList);

            return this.getRandomBookRecommendationList(bookRecommendationList);
        }catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<Book> getMoviesLikeThis(String movieId) {
        return null;
    }

    @Override
    public List<Book> getBooksLikeThis(String isbn) {
        try {
            BufferedReader bufferedReader = CFUtils.readSimBooks();

            String line;
            String[] SplitLine;

            List<Book> booksLikeThis = new ArrayList<>();

            Random ran = new Random();
            int[] arr = new int[12];

            for(int i = 0; i < arr.length; i++) {
                arr[i] = ran.nextInt(30) + 1;

                for(int j = 0; j < i; j++){
                    if(arr[i] == arr[j]) {
                        i--;
                        break;
                    }
                }
            }

            while((line = bufferedReader.readLine()) != null) {
                SplitLine = line.split(" ");

                if(SplitLine[0].equals("y" + isbn)) {
                    int j = 0;

                    for(int i = 0; i < 6; i++) {
                        Book simBook = bookRepository.findByISBN(SplitLine[arr[j]].substring(1));

                        j = j + 1;

                        if(simBook == null) {
                            i = i - 1;
                            continue;
                        }

                        float total = 0;

                        for(BookRate bookRate: simBook.getRate()) {
                            total = total + bookRate.getRating();
                        }

                        DecimalFormat decimalFormat =new DecimalFormat("#.0");
                        simBook.getParam().put("rate", decimalFormat.format(total / simBook.getRate().size()));

                        booksLikeThis.add(simBook);
                    }
                }
            }

            return booksLikeThis;
        }catch (Exception e) {}

        return null;
    }

    public List<UserRatingItemVo> getSystemUserMovieList(String email) {
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

        return new ArrayList<>(itemSet);
    }

    public List<UserRatingItemVo> getSystemUserBookList(String email) {
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

        return new ArrayList<>(itemSet);
    }

    public List<Movie> getRandomMovieRecommendationList(List<String> movieRecommendationList) {
        List<Movie> recommendationResultList = new ArrayList<>();

        for(int i = 0; i < 6; i++) {
            Integer movieId = Integer.parseInt(movieRecommendationList.remove(new Random().nextInt(movieRecommendationList.size())));

            Movie movie = movieRepository.findByMovieId(movieId);

            if(movie == null) {
                i = i - 1;

                continue;
            }

            float total = 0;

            for(MovieRate movieRate: movie.getRate()) {
                total = total + movieRate.getRating();
            }

            DecimalFormat decimalFormat =new DecimalFormat("#.0");
            movie.getParam().put("rate", decimalFormat.format(total / movie.getRate().size()));

            recommendationResultList.add(movie);
        }

        return recommendationResultList;
    }

    public List<Book> getRandomBookRecommendationList(List<String> bookRecommendationList) {
        List<Book> recommendationResultList = new ArrayList<>();

        for(int i = 0; i < 6; i++) {
            String isbn = bookRecommendationList.remove(new Random().nextInt(bookRecommendationList.size()));

            Book book = bookRepository.findByISBN(isbn.substring(1));

            if(book == null) {
                i = i - 1;

                continue;
            }

            float total = 0;

            for(BookRate bookRate: book.getRate()) {
                total = total + bookRate.getRating();
            }

            DecimalFormat decimalFormat =new DecimalFormat("#.0");
            book.getParam().put("rate", decimalFormat.format(total / book.getRate().size()));

            recommendationResultList.add(book);
        }

        return recommendationResultList;
    }
}
