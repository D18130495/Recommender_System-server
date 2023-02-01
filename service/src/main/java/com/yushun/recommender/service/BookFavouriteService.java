package com.yushun.recommender.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yushun.recommender.model.user.BookFavourite;
import com.yushun.recommender.vo.user.book.BookFavouriteReturnVo;

/**
 * <p>
 * Book Favourite Service
 * </p>
 *
 * @author yushun zeng
 * @since 2023-2-1
 */

public interface BookFavouriteService extends IService<BookFavourite> {
    BookFavouriteReturnVo getUserBookFavourite(String isbn, String email);

    String likeOrUnlikeBook(BookFavourite bookFavourite);
}
