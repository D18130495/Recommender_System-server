package com.yushun.recommender.service;

import com.yushun.recommender.model.common.mongoEntity.book.Book;
import com.yushun.recommender.vo.user.book.BookLikeListReturnVo;
import com.yushun.recommender.vo.user.book.BookRatingListReturnVo;

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
    List<Book> getRandomBookList();

    Book getBookByISBN(String isbn);

    Integer getUserLikeAndRatingBookCount(String email);

    List<BookLikeListReturnVo> getUserBookLikeList(String email);

    List<BookRatingListReturnVo> getUserBookRatingList(String email);
}
