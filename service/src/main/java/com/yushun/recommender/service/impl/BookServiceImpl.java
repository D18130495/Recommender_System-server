package com.yushun.recommender.service.impl;

import com.yushun.recommender.model.common.mongoEntity.book.Book;
import com.yushun.recommender.model.common.mongoEntity.book.BookRate;
import com.yushun.recommender.repository.BookRepository;
import com.yushun.recommender.service.BookService;
import com.yushun.recommender.vo.user.book.BookLikeListReturnVo;
import com.yushun.recommender.vo.user.book.BookRatingListReturnVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
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

    @Override
    public List<Book> getRandomBook() {
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

            DecimalFormat decimalFormat =new DecimalFormat("#.0");
            book.getParam().put("rate", decimalFormat.format(total / book.getRate().size()));
        }

        return bookList;
    }

    @Override
    public Book getBookByISBN(String isbn) {
        // find book
        Book book = bookRepository.findByISBN(isbn);

        if(book == null) {
            return null;
        }else {
            // calculate average rate value
            float total = 0;

            for(BookRate bookRate: book.getRate()) {
                total = total + bookRate.getRating();
            }

            DecimalFormat decimalFormat =new DecimalFormat("#.0");
            book.getParam().put("rate", decimalFormat.format(total / book.getRate().size()));
        }

        return book;
    }

    @Override
    public List<BookLikeListReturnVo> getUserBookLikeList(String email) {
        return null;
    }

    @Override
    public List<BookRatingListReturnVo> getUserBookRatingList(String email) {
        return null;
    }
}
