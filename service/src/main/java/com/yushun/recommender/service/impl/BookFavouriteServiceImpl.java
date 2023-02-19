package com.yushun.recommender.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yushun.recommender.mapper.BookFavouriteMapper;
import com.yushun.recommender.model.common.User;
import com.yushun.recommender.model.common.mongoEntity.book.Book;
import com.yushun.recommender.model.user.BookFavourite;
import com.yushun.recommender.service.BookFavouriteService;
import com.yushun.recommender.service.BookService;
import com.yushun.recommender.service.UserService;
import com.yushun.recommender.vo.user.book.BookFavouriteReturnVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
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
    @Autowired
    private  UserService userService;

    @Autowired
    private BookService bookService;

    @Override
    public BookFavouriteReturnVo getUserBookFavourite(String isbn, String email) {
        // find if user is existed
        QueryWrapper userWrapper = new QueryWrapper();
        userWrapper.eq("email", email);

        User findUser = userService.getOne(userWrapper);

        if(findUser == null) return null;

        // find if the book exist
        Book bookByISBN = bookService.getBookByISBN(isbn);

        if(bookByISBN == null) return null;

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

            if (bookFavouriteReturnVo.getFavourite().equals("T")) {
                bookFavouriteReturnVo.setFavourite("3");
            } else if (bookFavouriteReturnVo.getFavourite().equals("F")) {
                bookFavouriteReturnVo.setFavourite("1");
            } else {
                bookFavouriteReturnVo.setFavourite("2");
            }

            return bookFavouriteReturnVo;
        }
    }

    @Override
    public String likeOrUnlikeBook(BookFavourite bookFavourite) {
        // find if user is existed
        QueryWrapper userWrapper = new QueryWrapper();
        userWrapper.eq("email", bookFavourite.getEmail());

        User findUser = userService.getOne(userWrapper);

        if(findUser == null) return "User not find";

        // find if the book exist
        Book bookByISBN = bookService.getBookByISBN(bookFavourite.getIsbn());

        if(bookByISBN == null) return "Book not find";

        // find favourite book
        QueryWrapper bookFavouriteWrapper = new QueryWrapper();
        bookFavouriteWrapper.eq("isbn", bookFavourite.getIsbn());
        bookFavouriteWrapper.eq("email", bookFavourite.getEmail());

        BookFavourite findBookFavourite = baseMapper.selectOne(bookFavouriteWrapper);

        // add new rating or update rating
        if(findBookFavourite == null) { // add new record
            switch (bookFavourite.getFavourite()) {
                case "1": { // do not like
                    BookFavourite newBookFavourite = addNewBookFavourite(bookFavourite, "F");

                    int insert = baseMapper.insert(newBookFavourite);

                    return insert > 0 ? "Don't like" : "Error";
                }
                case "2": { // normal
                    BookFavourite newBookFavourite = addNewBookFavourite(bookFavourite, "N");

                    int insert = baseMapper.insert(newBookFavourite);

                    return insert > 0 ? "Normal" : "Error";
                }
                case "3": { // like
                    BookFavourite newBookFavourite = addNewBookFavourite(bookFavourite, "T");

                    int insert = baseMapper.insert(newBookFavourite);

                    return insert > 0 ? "Favourite" : "Error";
                }
            }
        }else {
            switch (bookFavourite.getFavourite()) {
                case "1": { // do not like
                    findBookFavourite.setFavourite("F");

                    int update = baseMapper.update(findBookFavourite, bookFavouriteWrapper);

                    return update > 0 ? "Don't like" : "Error";
                }
                case "2": { // normal
                    findBookFavourite.setFavourite("N");

                    int update = baseMapper.update(findBookFavourite, bookFavouriteWrapper);

                    return update > 0 ? "Normal" : "Error";
                }
                case "3": { // like
                    findBookFavourite.setFavourite("T");

                    int update = baseMapper.update(findBookFavourite, bookFavouriteWrapper);

                    return update > 0 ? "Favourite" : "Error";
                }
            }
        }

        return "Error";
    }

    public BookFavourite addNewBookFavourite(BookFavourite bookFavourite, String favourite) {
        BookFavourite newBookFavourite = new BookFavourite();

        BeanUtils.copyProperties(bookFavourite, newBookFavourite);
        newBookFavourite.setFavourite(favourite);
        newBookFavourite.setCreateTime(new Date());
        newBookFavourite.setUpdateTime(new Date());
        newBookFavourite.setIsDeleted(0);

        return newBookFavourite;
    }
}
