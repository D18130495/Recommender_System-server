package com.yushun.recommender.service;

import com.yushun.recommender.model.common.mongoEntity.book.Book;

import java.util.List;

/**
 * <p>
 * Book Service
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-31
 */

public interface BookService {
    List<Book> getRandomBook();

    Book getBookByISBN(String isbn);
}
