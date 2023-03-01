package com.yushun.recommender.service.impl;

import com.yushun.recommender.model.common.mongoEntity.book.Book;
import com.yushun.recommender.model.common.mongoEntity.movie.Movie;
import com.yushun.recommender.model.common.mongoEntity.movie.MovieActor;
import com.yushun.recommender.model.common.mongoEntity.search.Count;
import com.yushun.recommender.service.SearchService;
import com.yushun.recommender.vo.user.search.FuzzySearchReturnVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * Search Service Implementation
 * </p>
 *
 * @author yushun zeng
 * @since 2023-2-20
 */

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public HashMap<String, Object> fuzzySearchMovieAndBookByTitleOrYear(String substring, String type) {
        // fuzzy search return vo list
        List<FuzzySearchReturnVo> fuzzySearchReturnVoList = new ArrayList<>();

        // find movie
        Criteria criteriaMovie = new Criteria();
        criteriaMovie.and(type).regex(".*?" + substring + ".*", "i");

        Aggregation aggregationMovie = Aggregation.newAggregation(
                Aggregation.match(criteriaMovie), Aggregation.sample(5)
        );

        AggregationResults<Movie> movieResults = mongoTemplate.aggregate(aggregationMovie, Movie.class, Movie.class);

        List<Movie> movieList = movieResults.getMappedResults();

        // form movie search return vo
        for(Movie movie:movieList) {
            FuzzySearchReturnVo fuzzySearchReturnVo = new FuzzySearchReturnVo();
            fuzzySearchReturnVo.setMovieId(movie.getMovieId());
            fuzzySearchReturnVo.setYear(movie.getYear());
            fuzzySearchReturnVo.setTitle(movie.getTitle());
            fuzzySearchReturnVo.setImage(movie.getMovieImage());

            if(movie.getDirector().getDirectorName() != null) {
                fuzzySearchReturnVo.setDirector(movie.getDirector().getDirectorName());
            }else {
                fuzzySearchReturnVo.setDirector("Currently not available");
            }

            StringBuilder actorString = new StringBuilder();

            if(movie.getActor() != null) {
                for(MovieActor movieActor:movie.getActor()) {
                    actorString.append(movieActor.getActorName()).append(", ");
                }

                fuzzySearchReturnVo.setActorList(actorString.substring(0, actorString.length() - 2));
            }else {
                fuzzySearchReturnVo.setActorList("Currently not available");
            }

            fuzzySearchReturnVo.setType("movie");

            fuzzySearchReturnVoList.add(fuzzySearchReturnVo);
        }

        // find book
        Criteria criteriaBook = new Criteria();
        criteriaBook.and(type).regex(".*?" + substring + ".*", "i");

        Aggregation aggregationBook = Aggregation.newAggregation(
                Aggregation.match(criteriaMovie), Aggregation.sample(5)
        );

        AggregationResults<Book> bookResults = mongoTemplate.aggregate(aggregationBook, Book.class, Book.class);

        List<Book> bookList = bookResults.getMappedResults();

        // form book search return vo
        for(Book book:bookList) {
            FuzzySearchReturnVo fuzzySearchReturnVo = new FuzzySearchReturnVo();
            fuzzySearchReturnVo.setIsbn(book.getISBN());
            fuzzySearchReturnVo.setTitle(book.getTitle());

            if (book.getBookImage() != null) {
                fuzzySearchReturnVo.setImage(book.getBookImage());
            } else {
                fuzzySearchReturnVo.setImage(book.getBookImageL());
            }

            fuzzySearchReturnVo.setYear(book.getYear());
            fuzzySearchReturnVo.setAuthor(book.getAuthor());
            fuzzySearchReturnVo.setType("book");

            fuzzySearchReturnVoList.add(fuzzySearchReturnVo);
        }

        Aggregation aggregationMovieCount = Aggregation.newAggregation(
                Aggregation.match(criteriaMovie),
                Aggregation.group().count().as("count")
        );

        AggregationResults<Count> movieCount = mongoTemplate.aggregate(aggregationMovieCount, "movie", Count.class);

        Aggregation aggregationBookCount = Aggregation.newAggregation(
                Aggregation.match(criteriaBook),
                Aggregation.group().count().as("count")
        );

        AggregationResults<Count> bookCount = mongoTemplate.aggregate(aggregationBookCount, "book", Count.class);

        int movieAndBookSum = 0;

        if(movieCount.getMappedResults().size() != 0) {
            movieAndBookSum += Integer.parseInt(movieCount.getMappedResults().get(0).getCount());
        }

        if(bookCount.getMappedResults().size() != 0) {
            movieAndBookSum += Integer.parseInt(bookCount.getMappedResults().get(0).getCount());
        }

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("fuzzySearchReturnVoList", fuzzySearchReturnVoList);
        resultMap.put("count", movieAndBookSum);

        return resultMap;
    }
}
