package com.yushun.recommender.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yushun.recommender.mapper.BookFavouriteMapper;
import com.yushun.recommender.model.user.BookFavourite;
import com.yushun.recommender.service.BookFavouriteService;
import com.yushun.recommender.vo.user.book.BookFavouriteReturnVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * Book Favourite Service Impl
 * </p>
 *
 * @author yushun zeng
 * @since 2023-2-1
 */

@Service
public class BookFavouriteServiceImpl extends ServiceImpl<BookFavouriteMapper, BookFavourite> implements BookFavouriteService {
    @Override
    public BookFavouriteReturnVo getUserBookFavourite(String isbn, String email) {
        // find favourite
        QueryWrapper bookFavouriteWrapper = new QueryWrapper();
        bookFavouriteWrapper.eq("isbn", isbn);
        bookFavouriteWrapper.eq("email", email);

        BookFavourite findBookFavourite = baseMapper.selectOne(bookFavouriteWrapper);

        // return favourite if exist
        if(findBookFavourite == null) {
            return null;
        }else {
            BookFavouriteReturnVo bookFavouriteReturnVo = new BookFavouriteReturnVo();
            BeanUtils.copyProperties(findBookFavourite, bookFavouriteReturnVo);

            if(bookFavouriteReturnVo.getFavourite().equals("T")) {
                bookFavouriteReturnVo.setFavourite("1");
            }else {
                bookFavouriteReturnVo.setFavourite("0");
            }

            return bookFavouriteReturnVo;
        }
    }

    @Override
    public String likeOrUnlikeBook(BookFavourite bookFavourite) {
        // find favourite book
        QueryWrapper bookFavouriteWrapper = new QueryWrapper();
        bookFavouriteWrapper.eq("isbn", bookFavourite.getIsbn());
        bookFavouriteWrapper.eq("email", bookFavourite.getEmail());

        BookFavourite findBookFavourite = baseMapper.selectOne(bookFavouriteWrapper);

        // add new rating or update rating
        if(findBookFavourite == null && bookFavourite.getFavourite().equals("0")) {
            BookFavourite newBookFavourite = new BookFavourite();

            BeanUtils.copyProperties(bookFavourite, newBookFavourite);
            newBookFavourite.setFavourite("T");
            newBookFavourite.setCreateTime(new Date());
            newBookFavourite.setUpdateTime(new Date());
            newBookFavourite.setIsDeleted(0);

            int insert = baseMapper.insert(newBookFavourite);

            return insert > 0 ? "Liked" : "Error";
        }else if(findBookFavourite == null && bookFavourite.getFavourite().equals("1")) {
            return "Bad operation";
        }else if(findBookFavourite != null) {
            if(findBookFavourite.getFavourite().equals("F") && bookFavourite.getFavourite().equals("0")) {
                findBookFavourite.setFavourite("T");
                findBookFavourite.setUpdateTime(new Date());

                int update = baseMapper.update(findBookFavourite, bookFavouriteWrapper);

                return update > 0? "Liked":"Error";
            }else if(findBookFavourite.getFavourite().equals("T") && bookFavourite.getFavourite().equals("1")) {
                findBookFavourite.setFavourite("F");
                findBookFavourite.setUpdateTime(new Date());

                int update = baseMapper.update(findBookFavourite, bookFavouriteWrapper);

                return update > 0? "Unliked":"Error";
            }else {
                return "Bad operation";
            }
        }

        return "Bad operation";
    }
}