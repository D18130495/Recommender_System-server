package com.yushun.recommender.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yushun.recommender.algorithm.*;
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

    //
    @Override
    public List<Movie> getMovieRecommendationData_byMovie_itemCF(String email) {
        List<UserRatingItemVo> systemUserMovieList = this.getSystemUserMovieList(email);

        try {
            List<String> movie = ItemCF.simItemResult(email, systemUserMovieList, "movie");

            return this.getRandomMovieRecommendationList(movie);
        }catch (Exception e) {
            System.out.println("getMovieRecommendationData_byMovie_itemCF " + e);
        }

        return null;
    }

    // Done
    @Override
    public List<Book> getBookRecommendationData_byBook_itemCF(String email) {
        List<UserRatingItemVo> systemUserBookList = this.getSystemUserBookList(email);

        try {
            List<String> book = ItemCF.simItemResult(email, systemUserBookList, "book");

            return this.getRandomBookRecommendationList(book);
        }catch (Exception e) {
            System.out.println("getBookRecommendationData_byBook_itemCF " + e);
        }

        return null;
    }

    // Done
    @Override
    public List<Movie> getMovieRecommendationData_byMovie_userCF(String email, String type) {
        List<UserRatingItemVo> systemUserMovieList = this.getSystemUserMovieList(email);

        try {
            List<String> simUserItemListResult = UserCF.simUserItemListResult(email, systemUserMovieList, type);

            return this.getRandomMovieRecommendationList(simUserItemListResult);
        }catch (Exception e) {
            System.out.println("getMovieRecommendationData_byMovie_userCF " + e);
        }

        return null;
    }

    // Done
    @Override
    public List<Movie> getMovieRecommendationData_byBook_userCF(String email, String type) {
        List<UserRatingItemVo> systemUserBookList = this.getSystemUserBookList(email);

        try {
            List<String> simUserListResult = UserCF.simUserList(email, systemUserBookList, type);

            List<UserRatingItemVo> systemUserMovieList = getSystemUserMovieList(email);

            List<String> movieRecommendationList = FindUserLikedItem.simUserRatedItemList(simUserListResult, systemUserMovieList, "movie");

            return this.getRandomMovieRecommendationList(movieRecommendationList);
        }catch (Exception e) {
            System.out.println("getMovieRecommendationData_byBook_userCF " + e);
        }

        return null;
    }

    // Done
    @Override
    public List<Book> getBookRecommendationData_byBook_userCF(String email, String type) {
        List<UserRatingItemVo> systemUserBookList = this.getSystemUserBookList(email);

        try {
            List<String> simUserItemListResult = UserCF.simUserItemListResult(email, systemUserBookList, type);

            return this.getRandomBookRecommendationList(simUserItemListResult);
        }catch (Exception e) {
            System.out.println("getBookRecommendationData_byBook_userCF " + e);
        }

        return null;
    }

    // Done
    @Override
    public List<Book> getBookRecommendationData_byMovie_userCF(String email, String type) {
        List<UserRatingItemVo> systemUserMovieList = this.getSystemUserMovieList(email);

        try {
            List<String> simUserListResult = UserCF.simUserList(email, systemUserMovieList, type);

            List<UserRatingItemVo> systemUserBookList = getSystemUserBookList(email);

            List<String> bookRecommendationList = FindUserLikedItem.simUserRatedItemList(simUserListResult, systemUserBookList, "book");

            return this.getRandomBookRecommendationList(bookRecommendationList);
        }catch (Exception e) {
            System.out.println("getBookRecommendationData_byMovie_userCF " + e);
        }

        return null;
    }

    // Done
    @Override
    public List<Movie> getMoviesLikeThis(String movieId) {
        try(BufferedReader bufferedReader = CFUtils.readSimMovies()) {
            String line;
            String[] SplitLine;

            List<Movie> moviesLikeThis = new ArrayList<>();

            // random number 1 - 12
            int[] array = randomArray();

            while((line = bufferedReader.readLine()) != null) {
                SplitLine = line.split(" ");

                if(SplitLine[0].equals(movieId)) {
                    int j = 0;

                    for(int i = 0; i < 6; i++) {
                        Movie simMovie = movieRepository.findByMovieId(Integer.parseInt(SplitLine[array[j]]));

                        j = j + 1;

                        // most possible not have error
                        if(simMovie == null) {
                            i = i - 1;
                            continue;
                        }

                        float total = 0;

                        for(MovieRate movieRate: simMovie.getRate()) {
                            total = total + movieRate.getRating();
                        }

                        // find system user rating
                        QueryWrapper movieRateQueryWrapper = new QueryWrapper();
                        movieRateQueryWrapper.eq("movieId", Integer.parseInt(SplitLine[array[j]]));

                        List<MovieRating> systemMovieRatingList = movieRatingService.list(movieRateQueryWrapper);

                        if(systemMovieRatingList.size() != 0) {
                            for(MovieRating systemMovieRating:systemMovieRatingList) {
                                total = total + systemMovieRating.getRating();
                            }
                        }

                        DecimalFormat decimalFormat = new DecimalFormat("#.0");
                        simMovie.getParam().put("rate", decimalFormat.format(total / (simMovie.getRate().size() + systemMovieRatingList.size())));

                        moviesLikeThis.add(simMovie);
                    }
                }
            }

            return moviesLikeThis;
        }catch (Exception e) {
            System.out.println("getMoviesLikeThis " + e);
        }

        return null;
    }

    // Done
    @Override
    public List<Book> getBooksLikeThis(String isbn) {
        try(BufferedReader bufferedReader = CFUtils.readSimBooks()) {
            String line;
            String[] SplitLine;

            List<Book> booksLikeThis = new ArrayList<>();

            // random number 1 - 22
            int[] array = randomArray();

            while((line = bufferedReader.readLine()) != null) {
                SplitLine = line.split(" ");

                if(SplitLine[0].equals("Y" + isbn)) {
                    int j = 0;

                    for(int i = 0; i < 6; i++) {
                        Book simBook = bookRepository.findByISBN(SplitLine[array[j]].substring(1));

                        j = j + 1;

                        // most possible not have error
                        if(simBook == null) {
                            i = i - 1;
                            continue;
                        }

                        float total = 0;

                        for(BookRate bookRate: simBook.getRate()) {
                            total = total + bookRate.getRating();
                        }

                        // find system user rating
                        QueryWrapper bookRateQueryWrapper = new QueryWrapper();
                        bookRateQueryWrapper.eq("ISBN", SplitLine[array[j]].substring(1));

                        List<BookRating> systemBookRatingList = bookRatingService.list(bookRateQueryWrapper);

                        if(systemBookRatingList.size() != 0) {
                            for(BookRating systemBookRating:systemBookRatingList) {
                                total = total + systemBookRating.getRating();
                            }
                        }

                        DecimalFormat decimalFormat = new DecimalFormat("#.0");
                        simBook.getParam().put("rate", decimalFormat.format(total / (simBook.getRate().size() + systemBookRatingList.size())));

                        booksLikeThis.add(simBook);
                    }
                }
            }

            return booksLikeThis;
        }catch (Exception e) {
            System.out.println("getBooksLikeThis " + e);
        }

        return null;
    }

    // Done
    public int[] randomArray() {
        Random random = new Random();
        int[] array = new int[22];

        for(int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(22) + 1;

            for(int j = 0; j < i; j++){
                if(array[i] == array[j]) {
                    i--;
                    break;
                }
            }
        }

        return array;
    }

    // Done
    public List<UserRatingItemVo> getSystemUserMovieList(String email) {
        // all the user in the system
        // user rating set
        Set<UserRatingItemVo> itemSet = new HashSet<>();

        // find user favourite list
        QueryWrapper movieFavouriteWrapper = new QueryWrapper();
        movieFavouriteWrapper.eq("email", email);
        movieFavouriteWrapper.in("favourite", "T", "F");

        List<MovieFavourite> movieFavouriteList = movieFavouriteService.list(movieFavouriteWrapper);

        if(movieFavouriteList != null) {
            for(MovieFavourite movie:movieFavouriteList) {
                UserRatingItemVo userRatingItemVo = new UserRatingItemVo();
                userRatingItemVo.setUserId(email);
                userRatingItemVo.setItemId(movie.getMovieId().toString());

                if(movie.getFavourite().equals("T")) {
                    userRatingItemVo.setRate("4");
                }else {
                    userRatingItemVo.setRate("-4");
                }

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

    // Done
    public List<UserRatingItemVo> getSystemUserBookList(String email) {
        // all the user in the system
        // user rating set
        Set<UserRatingItemVo> itemSet = new HashSet<>();

        // find user favourite list
        QueryWrapper bookFavouriteWrapper = new QueryWrapper();
        bookFavouriteWrapper.eq("email", email);
        bookFavouriteWrapper.in("favourite", "T", "F");

        List<BookFavourite> bookFavouriteList = bookFavouriteService.list(bookFavouriteWrapper);

        if(bookFavouriteList != null) {
            for(BookFavourite book:bookFavouriteList) {
                UserRatingItemVo userRatingItemVo = new UserRatingItemVo();
                userRatingItemVo.setUserId(email);
                userRatingItemVo.setItemId("Y" + book.getIsbn());

                if(book.getFavourite().equals("T")) {
                    userRatingItemVo.setRate("4");
                }else {
                    userRatingItemVo.setRate("-4");
                }

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
                userRatingItemVo.setItemId("Y" + book.getIsbn());
                userRatingItemVo.setRate(book.getRating().toString());

                itemSet.add(userRatingItemVo);
            }
        }

        return new ArrayList<>(itemSet);
    }

    // Done
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

            // find system user rating
            QueryWrapper movieRateQueryWrapper = new QueryWrapper();
            movieRateQueryWrapper.eq("movieId", movieId);

            List<MovieRating> systemMovieRatingList = movieRatingService.list(movieRateQueryWrapper);

            if(systemMovieRatingList.size() != 0) {
                for(MovieRating systemMovieRating:systemMovieRatingList) {
                    total = total + systemMovieRating.getRating();
                }
            }

            DecimalFormat decimalFormat = new DecimalFormat("#.0");
            movie.getParam().put("rate", decimalFormat.format(total / (movie.getRate().size() + systemMovieRatingList.size())));

            recommendationResultList.add(movie);
        }

        return recommendationResultList;
    }

    // Done
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

            // find system user rating
            QueryWrapper bookRateQueryWrapper = new QueryWrapper();
            bookRateQueryWrapper.eq("ISBN", isbn.substring(1));

            List<BookRating> systemBookRatingList = bookRatingService.list(bookRateQueryWrapper);

            if(systemBookRatingList.size() != 0) {
                for(BookRating systemBookRating:systemBookRatingList) {
                    total = total + systemBookRating.getRating();
                }
            }

            DecimalFormat decimalFormat =new DecimalFormat("#.0");
            book.getParam().put("rate", decimalFormat.format(total / (book.getRate().size()) + systemBookRatingList.size()));

            recommendationResultList.add(book);
        }

        return recommendationResultList;
    }
}
