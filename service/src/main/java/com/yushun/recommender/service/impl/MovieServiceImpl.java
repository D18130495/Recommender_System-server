package com.yushun.recommender.service.impl;

import com.yushun.recommender.model.common.mongoEntity.movie.Movie;
import com.yushun.recommender.model.common.mongoEntity.movie.MovieRate;
import com.yushun.recommender.repository.MovieRepository;
import com.yushun.recommender.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;

/**
 * <p>
 * Movie Service Impl
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-22
 */

@Service
public class MovieServiceImpl implements MovieService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public List<Movie> getRandomMovie() {
        // aggregate and find results
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.sample(5)
        );

        AggregationResults<Movie> results = mongoTemplate.aggregate(aggregation, Movie.class, Movie.class);
        List<Movie> movieList = results.getMappedResults();

        // calculate average rate score
        for(Movie movie:movieList) {
            List<MovieRate> rateList = movie.getRate();
            float total = 0;

            for(MovieRate movieRate: rateList) {
                total = total + movieRate.getRating();
            }

            DecimalFormat decimalFormat =new DecimalFormat("#.0");
            movie.getParam().put("rate", decimalFormat.format(total / movie.getRate().size()));
        }

        return movieList;
    }

//    @Override
//    public List<Movie> getRandomMovie() {
//        Criteria criteria = new Criteria().andOperator(Criteria.where("movieId").is(1));
//        Query query = new Query();
//        query.addCriteria(criteria);
//
//        Movie movie = mongoTemplate.findOne(query, Movie.class, "movie");
//
//        System.out.println(movie);
//        System.out.println(movie.getDirector());
//
//        return null;
//    }
}
