package com.yushun.recommender.controller.user;

import com.yushun.recommender.model.common.mongoEntity.book.Book;
import com.yushun.recommender.model.common.mongoEntity.movie.Movie;
import com.yushun.recommender.security.result.Result;
import com.yushun.recommender.service.RecommendationService;
import com.yushun.recommender.vo.user.book.BookReturnVo;
import com.yushun.recommender.vo.user.movie.MovieReturnVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * User Interface Recommendation Controller
 * </p>
 *
 * @author yushun zeng
 * @since 2023-2-3
 */

@CrossOrigin
@RestController
@RequestMapping("/recommendation")
public class UserInterfaceRecommendationController {
    @Autowired
    private RecommendationService recommendationService;

    @GetMapping("/getMoviesLikeThis")
    public Result getMoviesLikeThis(@PathParam("movieId") Integer movieId) {

        return null;
    }

    @GetMapping("/getBooksLikeThis")
    public Result getBooksLikeThis(@PathParam("isbn") String isbn) {
        List<Book> booksLikeThis = recommendationService.getBooksLikeThis(isbn);

        // result variable
        List<BookReturnVo> bookReturnList;

        if(booksLikeThis != null) {
            // form result
            bookReturnList = booksLikeThis.stream().map(this::formBookResult).collect(Collectors.toList());
        }else {
            bookReturnList = null;
        }

        return Result.ok(bookReturnList);
    }

    @GetMapping("/getRecommendMovieListByItemCF")
    public Result getRecommendMovieListByItemCF(@PathParam("email") String email) {
        List<Movie> recommendedMovie = new ArrayList<>();

        recommendedMovie = recommendationService.getMovieRecommendationData_byMovie_itemCF(email);

        // result variable
        List<MovieReturnVo> movieReturnList;

        if(recommendedMovie != null) {
            // form result
            movieReturnList = recommendedMovie.stream().map(this::formMovieResult).collect(Collectors.toList());
        }else {
            movieReturnList = null;
        }

        return Result.ok(movieReturnList);
    }

    @GetMapping("/getRecommendMovieListByUserCF")
    public Result getRecommendMovieListByUserCF(@PathParam("email") String email, @PathParam("type") String type) {
        List<Movie> recommendedMovie = new ArrayList<>();

        if(type.equals("movie") && email != null) {
            recommendedMovie = recommendationService.getMovieRecommendationData_byMovie_userCF(email, type);
        }else if(type.equals("book") && email != null) {
            recommendedMovie = recommendationService.getMovieRecommendationData_byBook_userCF(email, type);
        }else {
            return Result.fail().message("Bad operation");
        }

        // result variable
        List<MovieReturnVo> movieReturnList;

        if(recommendedMovie != null) {
            // form result
            movieReturnList = recommendedMovie.stream().map(this::formMovieResult).collect(Collectors.toList());
        }else {
            movieReturnList = null;
        }

        return Result.ok(movieReturnList);
    }

    // ok
    @GetMapping("/getRecommendBookListByItemCF")
    public Result getRecommendBookListByItemCF(@PathParam("email") String email) {
        List<Book> recommendedBook = new ArrayList<>();

        recommendedBook = recommendationService.getBookRecommendationData_byBook_itemCF(email);

        // result variable
        List<BookReturnVo> bookReturnList;

        if(recommendedBook != null) {
            // form result
            bookReturnList = recommendedBook.stream().map(this::formBookResult).collect(Collectors.toList());
        }else {
            bookReturnList = null;
        }

        return Result.ok(bookReturnList);
    }

    @GetMapping("/getRecommendBookListByUserCF")
    public Result getRecommendBookListByUserCF(@PathParam("email") String email, @PathParam("type") String type) {
        List<Book> recommendedBook = new ArrayList<>();

        if(type.equals("book")) {
            recommendedBook = recommendationService.getBookRecommendationData_byBook_userCF(email, type);
        }else if(type.equals("movie")) {
            recommendedBook = recommendationService.getBookRecommendationData_byMovie_userCF(email, type);
        }

        // result variable
        List<BookReturnVo> bookReturnList;

        if(recommendedBook != null) {
            // form result
            bookReturnList = recommendedBook.stream().map(this::formBookResult).collect(Collectors.toList());
        }else {
            bookReturnList = null;
        }

        return Result.ok(bookReturnList);
    }

    public MovieReturnVo formMovieResult(Movie movie) {
        MovieReturnVo movieReturnVo = new MovieReturnVo();

        BeanUtils.copyProperties(movie, movieReturnVo);

        // parse genres
        if(!movie.getGenres().isEmpty()) {
            String[] genreList = movie.getGenres().split("[ | ]");
            movieReturnVo.setGenres(new ArrayList<>(Arrays.asList(genreList)));
        }

        movieReturnVo.setRate(Float.parseFloat((String)movie.getParam().get("rate")));

        return movieReturnVo;
    }

    public BookReturnVo formBookResult(Book book) {
        BookReturnVo bookReturnVo = new BookReturnVo();

        BeanUtils.copyProperties(book, bookReturnVo);

        bookReturnVo.setRate(Float.parseFloat((String)book.getParam().get("rate")));

        return bookReturnVo;
    }
}
