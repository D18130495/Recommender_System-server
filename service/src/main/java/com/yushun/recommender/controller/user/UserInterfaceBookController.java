package com.yushun.recommender.controller.user;

import com.yushun.recommender.model.common.mongoEntity.book.Book;
import com.yushun.recommender.model.common.mongoEntity.movie.Movie;
import com.yushun.recommender.security.result.Result;
import com.yushun.recommender.service.BookService;
import com.yushun.recommender.vo.user.book.BookReturnVo;
import com.yushun.recommender.vo.user.movie.MovieReturnVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * User Interface Book Controller
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-31
 */

@CrossOrigin
@RestController
@RequestMapping("/book")
public class UserInterfaceBookController {
    @Autowired
    private BookService bookService;

    @GetMapping("/getRandomBookList")
    public Result getRandomBookList() {
        // initial random book list
        List<Book> randomBookList = bookService.getRandomBook();

        // result variable
        List<BookReturnVo> bookReturnList;

        // form book return result
        bookReturnList = randomBookList.stream().map(this::formBookResult).collect(Collectors.toList());

        return Result.ok(bookReturnList);
    }

    public BookReturnVo formBookResult(Book book) {
        BookReturnVo bookReturnVo = new BookReturnVo();

        BeanUtils.copyProperties(book, bookReturnVo);

        bookReturnVo.setRate(Float.parseFloat((String)book.getParam().get("rate")));

        return bookReturnVo;
    }
}
