package com.yushun.recommender.service;

import com.yushun.recommender.model.common.mongoEntity.book.Book;
import com.yushun.recommender.model.common.mongoEntity.movie.Movie;

import java.util.List;

/**
 * <p>
 * Recommendation Service
 * </p>
 *
 * @author yushun zeng
 * @since 2023-2-4
 */

public interface RecommendationService {
    List<Movie> getMovieRecommendationData_byMovie_itemCF(String email);

    List<Book> getBookRecommendationData_byBook_itemCF(String email);

    List<Movie> getMovieRecommendationData_byMovie_userCF(String email, String type);

    List<Movie> getMovieRecommendationData_byBook_userCF(String email, String type);

    List<Book> getBookRecommendationData_byBook_userCF(String email, String type);

    List<Book> getBookRecommendationData_byMovie_userCF(String email, String type);
}
