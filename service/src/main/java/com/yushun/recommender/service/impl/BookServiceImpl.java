package com.yushun.recommender.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yushun.recommender.model.common.User;
import com.yushun.recommender.model.common.mongoEntity.book.Book;
import com.yushun.recommender.model.common.mongoEntity.book.BookRate;
import com.yushun.recommender.model.common.mongoEntity.movie.Movie;
import com.yushun.recommender.model.user.BookFavourite;
import com.yushun.recommender.model.user.BookRating;
import com.yushun.recommender.model.user.MovieFavourite;
import com.yushun.recommender.model.user.MovieRating;
import com.yushun.recommender.repository.BookRepository;
import com.yushun.recommender.service.BookFavouriteService;
import com.yushun.recommender.service.BookRatingService;
import com.yushun.recommender.service.BookService;
import com.yushun.recommender.service.UserService;
import com.yushun.recommender.vo.user.book.BookLikeListReturnVo;
import com.yushun.recommender.vo.user.book.BookRatingListReturnVo;
import com.yushun.recommender.vo.user.movie.MovieLikeListReturnVo;
import com.yushun.recommender.vo.user.movie.MovieRatingListReturnVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Book Service Impl
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-31
 */

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BookFavouriteService bookFavouriteService;

    @Autowired
    private BookRatingService bookRatingService;

    @Override
    public List<Book> getRandomBookList() {
        // aggregate and find results
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.sample(6)
        );

        AggregationResults<Book> results = mongoTemplate.aggregate(aggregation, Book.class, Book.class);
        List<Book> bookList = results.getMappedResults();

        // calculate average rate score
        for(Book book:bookList) {
            List<BookRate> rateList = book.getRate();
            float total = 0;

            for(BookRate bookRate: rateList) {
                total = total + bookRate.getRating();
            }

            // find system user rating
            QueryWrapper bookRateQueryWrapper = new QueryWrapper();
            bookRateQueryWrapper.eq("isbn", book.getISBN());

            List<BookRating> systemBookRatingList = bookRatingService.list(bookRateQueryWrapper);

            if(systemBookRatingList.size() != 0) {
                for(BookRating systemBookRating:systemBookRatingList) {
                    total = total + systemBookRating.getRating();
                }
            }

            DecimalFormat decimalFormat =new DecimalFormat("#.0");
            book.getParam().put("rate", decimalFormat.format(total / (book.getRate().size() + systemBookRatingList.size())));
        }

        return bookList;
    }

    @Override
    public Book getBookByISBN(String isbn) {
        // find book by isbn
        Book book = bookRepository.findByISBN(isbn);

        if(book == null) {
            return null;
        }else {
            // calculate average rate value
            float total = 0;

            for(BookRate bookRate: book.getRate()) {
                total = total + bookRate.getRating();
            }

            // find system user rating
            QueryWrapper bookRateQueryWrapper = new QueryWrapper();
            bookRateQueryWrapper.eq("isbn", book.getISBN());

            List<BookRating> systemBookRatingList = bookRatingService.list(bookRateQueryWrapper);

            if(systemBookRatingList.size() != 0) {
                for(BookRating systemBookRating:systemBookRatingList) {
                    total = total + systemBookRating.getRating();
                }
            }

            DecimalFormat decimalFormat =new DecimalFormat("#.0");
            book.getParam().put("rate", decimalFormat.format(total / (book.getRate().size() + systemBookRatingList.size())));
        }

        return book;
    }

    @Override
    public Integer getUserLikeAndRatingBookCount(String email) {
        int bookCount = 0;

        // find if user is existed
        QueryWrapper userWrapper = new QueryWrapper();
        userWrapper.eq("email", email);

        User findUser = userService.getOne(userWrapper);

        if(findUser == null) return -1;

        // find user book like list
        QueryWrapper bookLikeListWrapper = new QueryWrapper();
        bookLikeListWrapper.eq("email", email);
        bookLikeListWrapper.eq("favourite", "T");

        List<BookFavourite> bookFavouriteList = bookFavouriteService.list(bookLikeListWrapper);
        System.out.println(bookFavouriteList);
        // find user book rating list
        QueryWrapper bookRatingListWrapper = new QueryWrapper();
        bookRatingListWrapper.eq("email", email);

        List<BookRating> bookRatingList = bookRatingService.list(bookRatingListWrapper);

        if(bookFavouriteList.size() == 0 && bookRatingList.size() == 0) {
            return 0;
        }else if(bookFavouriteList.size() == 0) {
            return bookRatingList.size();
        }else if(bookRatingList.size() == 0) {
            return bookFavouriteList.size();
        }else {
            bookCount = bookFavouriteList.size() + bookRatingList.size();

            for(BookRating bookRating:bookRatingList) {
                for(BookFavourite bookFavourite:bookFavouriteList) {
                    if(bookRating.getIsbn().equals(bookFavourite.getIsbn())) {
                        bookCount -= 1;
                    }
                }
            }

            return bookCount;
        }
    }

    @Override
    public List<BookLikeListReturnVo> getUserBookLikeList(String email) {
        // find if user is existed
        QueryWrapper userWrapper = new QueryWrapper();
        userWrapper.eq("email", email);

        User findUser = userService.getOne(userWrapper);

        if(findUser == null) return null;

        // find user book like list
        QueryWrapper bookLikeListWrapper = new QueryWrapper();
        bookLikeListWrapper.eq("email", email);
        bookLikeListWrapper.eq("favourite", "T");

        List<BookFavourite> findBookLikeList = bookFavouriteService.list(bookLikeListWrapper);

        if(findBookLikeList.size() != 0) {
            // return list
            ArrayList<BookLikeListReturnVo> bookLikeListReturnVoList = new ArrayList<>();

            // find book detail
            for(BookFavourite bookFavourite: findBookLikeList) {
                Book book = bookRepository.findByISBN(bookFavourite.getIsbn());

                // form book detail
                if(book != null) {
                    BookLikeListReturnVo bookLikeListReturnVo = new BookLikeListReturnVo();
                    bookLikeListReturnVo.setIsbn(book.getISBN());
                    bookLikeListReturnVo.setTitle(book.getTitle());
                    bookLikeListReturnVo.setAuthor(book.getAuthor());
                    bookLikeListReturnVo.setYear(book.getYear());
                    bookLikeListReturnVo.setPublisher(book.getPublisher());

                    // form rating data
                    // find user rating for this book
                    QueryWrapper bookRatingWrapper = new QueryWrapper();
                    bookRatingWrapper.eq("email", email);
                    bookRatingWrapper.eq("isbn", book.getISBN());

                    BookRating findBookRating = bookRatingService.getOne(bookRatingWrapper);

                    if(findBookRating != null) bookLikeListReturnVo.setRating(findBookRating.getRating());

                    bookLikeListReturnVo.setUpdateDate(bookFavourite.getUpdateTime());

                    bookLikeListReturnVoList.add(bookLikeListReturnVo);
                }
            }

            return bookLikeListReturnVoList;
        }else {
            return null;
        }
    }

    @Override
    public List<BookRatingListReturnVo> getUserBookRatingList(String email) {
        // find if user is existed
        QueryWrapper userWrapper = new QueryWrapper();
        userWrapper.eq("email", email);

        User findUser = userService.getOne(userWrapper);

        if(findUser == null) return null;

        // find user rated book list
        QueryWrapper bookRatingListWrapper = new QueryWrapper();
        bookRatingListWrapper.eq("email", email);

        List<BookRating> findBookRatingList = bookRatingService.list(bookRatingListWrapper);

        if(findBookRatingList.size() != 0) {
            // return list
            ArrayList<BookRatingListReturnVo> bookRatingListReturnVoList = new ArrayList<>();

            // find movie detail
            for(BookRating bookRating: findBookRatingList) {
                Book book = bookRepository.findByISBN(bookRating.getIsbn());

                // form book detail
                if(book != null) {
                    BookRatingListReturnVo bookRatingListReturnVo = new BookRatingListReturnVo();
                    bookRatingListReturnVo.setIsbn(bookRating.getIsbn());
                    bookRatingListReturnVo.setTitle(book.getTitle());
                    bookRatingListReturnVo.setRating(bookRating.getRating());
                    bookRatingListReturnVo.setAuthor(book.getAuthor());
                    bookRatingListReturnVo.setPublisher(book.getPublisher());
                    bookRatingListReturnVo.setUpdateDate(bookRating.getUpdateTime());

                    bookRatingListReturnVoList.add(bookRatingListReturnVo);
                }
            }

            return bookRatingListReturnVoList;
        }else {
            return null;
        }
    }
}
