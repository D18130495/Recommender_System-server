package com.yushun.recommender.service.impl;

import com.yushun.recommender.model.common.mongoEntity.book.Book;
import com.yushun.recommender.model.common.mongoEntity.book.BookRate;
import com.yushun.recommender.model.common.mongoEntity.movie.Movie;
import com.yushun.recommender.model.common.mongoEntity.movie.MovieRate;
import com.yushun.recommender.repository.MovieRepository;
import com.yushun.recommender.service.BookService;
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
    private MovieRepository movieRepository;

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
}
