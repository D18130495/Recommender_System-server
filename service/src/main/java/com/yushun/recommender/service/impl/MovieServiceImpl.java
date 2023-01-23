package com.yushun.recommender.service.impl;

import com.yushun.recommender.model.common.MongoEntity.Movie.Movie;
import com.yushun.recommender.repository.MovieRepository;
import com.yushun.recommender.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

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
