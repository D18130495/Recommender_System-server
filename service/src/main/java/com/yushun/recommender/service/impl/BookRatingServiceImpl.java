package com.yushun.recommender.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yushun.recommender.mapper.BookRatingMapper;
import com.yushun.recommender.model.user.BookRating;
import com.yushun.recommender.model.user.MovieRating;
import com.yushun.recommender.service.BookRatingService;
import com.yushun.recommender.vo.user.book.BookRatingReturnVo;
import com.yushun.recommender.vo.user.movie.MovieRatingReturnVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * Book Rating Service Impl
 * </p>
 *
 * @author yushun zeng
 * @since 2023-2-1
 */

@Service
public class BookRatingServiceImpl extends ServiceImpl<BookRatingMapper, BookRating> implements BookRatingService {
    @Override
    public BookRatingReturnVo getUserBookRating(String isbn, String email) {
        // find rating
        QueryWrapper bookRatingWrapper = new QueryWrapper();
        bookRatingWrapper.eq("isbn", isbn);
        bookRatingWrapper.eq("email", email);

        BookRating findBookRating = baseMapper.selectOne(bookRatingWrapper);

        // return rating if exist
        if(findBookRating == null) {
            return null;
        }else {
            BookRatingReturnVo bookRatingReturnVo = new BookRatingReturnVo();
            BeanUtils.copyProperties(findBookRating, bookRatingReturnVo);

            return bookRatingReturnVo;
        }
    }

    @Override
    public boolean addOrUpdateUserBookRating(BookRating bookRating) {
        // find rating
        QueryWrapper bookRatingWrapper = new QueryWrapper();
        bookRatingWrapper.eq("isbn", bookRating.getIsbn());
        bookRatingWrapper.eq("email", bookRating.getEmail());

        BookRating findBookRating = baseMapper.selectOne(bookRatingWrapper);

        // add new rating or update rating
        if(findBookRating == null) {
            BookRating newBookRating = new BookRating();

            BeanUtils.copyProperties(bookRating, newBookRating);
            newBookRating.setCreateTime(new Date());
            newBookRating.setUpdateTime(new Date());
            newBookRating.setIsDeleted(0);

            int insert = baseMapper.insert(newBookRating);

            return insert > 0;
        }else {
            findBookRating.setRating(bookRating.getRating());
            findBookRating.setUpdateTime(new Date());

            int update = baseMapper.update(findBookRating, bookRatingWrapper);

            return update > 0;
        }
    }
}
