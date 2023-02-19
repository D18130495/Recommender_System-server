package com.yushun.recommender.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yushun.recommender.model.user.BookRating;
import com.yushun.recommender.vo.user.book.BookRatingReturnVo;

/**
 * <p>
 * Book Rating Service
 * </p>
 *
 * @author yushun zeng
 * @since 2023-2-1
 */

public interface BookRatingService extends IService<BookRating> {
    BookRatingReturnVo getUserBookRating(String isbn, String email);

    String addOrUpdateUserBookRating(BookRating bookRating);
}
