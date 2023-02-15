package com.yushun.recommender.controller.user;

import com.yushun.recommender.model.common.mongoEntity.book.Book;
import com.yushun.recommender.security.result.Result;
import com.yushun.recommender.service.BookService;
import com.yushun.recommender.vo.user.book.BookReturnVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        List<Book> randomBookList = bookService.getRandomBookList();

        // result variable
        List<BookReturnVo> bookReturnList;

        // form book return result
        bookReturnList = randomBookList.stream().map(this::formBookResult).collect(Collectors.toList());

        return Result.ok(bookReturnList);
    }

    @GetMapping("/getBookByISBN/{isbn}")
    public Result getBookByISBN(@PathVariable String isbn) {
        // find book
        Book book = bookService.getBookByISBN(isbn);

        // form book return result
        if(book != null) {
            BookReturnVo bookReturnVo = formBookResult(book);

            return Result.ok(bookReturnVo).message("Successfully find book");
        }else {
            return Result.fail().message("Can not find this book");
        }
    }

    public BookReturnVo formBookResult(Book book) {
        BookReturnVo bookReturnVo = new BookReturnVo();

        BeanUtils.copyProperties(book, bookReturnVo);

        bookReturnVo.setRate(Float.parseFloat((String)book.getParam().get("rate")));

        return bookReturnVo;
    }
}
