package com.yushun.recommender.repository;

import com.yushun.recommender.model.common.mongoEntity.book.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * <p>
 * Book Repository
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-31
 */

public interface BookRepository extends MongoRepository<Book, String> {
}
