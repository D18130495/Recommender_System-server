package com.yushun.recommender.repository;

import com.yushun.recommender.model.common.MongoEntity.Movie.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * <p>
 * Movie Repository
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-22
 */

public interface MovieRepository extends MongoRepository<Movie, String> {
    Movie findByMovieId(Integer movieId);
}
