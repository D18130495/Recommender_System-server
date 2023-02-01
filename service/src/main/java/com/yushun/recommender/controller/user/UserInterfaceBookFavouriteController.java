package com.yushun.recommender.controller.user;

import com.yushun.recommender.model.user.BookFavourite;
import com.yushun.recommender.security.result.Result;
import com.yushun.recommender.service.BookFavouriteService;
import com.yushun.recommender.vo.user.book.BookFavouriteReturnVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * User Interface Book Favourite Controller
 * </p>
 *
 * @author yushun zeng
 * @since 2023-2-1
 */

@CrossOrigin
@RestController
@RequestMapping("/book/favourite")
public class UserInterfaceBookFavouriteController {
    @Autowired
    private BookFavouriteService bookFavouriteService;

    @GetMapping("/getUserBookFavourite")
    public Result getUserBookFavourite(@RequestParam("isbn") String isbn,
                                        @RequestParam("email") String email) {
        BookFavouriteReturnVo existBookFavourite = bookFavouriteService.getUserBookFavourite(isbn, email);

        if(existBookFavourite != null) {
            return Result.ok(existBookFavourite).message("Successfully get book favourite");
        }else {
            return Result.ok(new BookFavouriteReturnVo(isbn, email, "0")).message("User haven't liked this book");
        }
    }

    @PostMapping("/likeOrUnlikeBook")
    public Result likeOrUnlikeBook(@RequestBody BookFavourite bookFavourite) {
        String likeOrUnlikeBook = bookFavouriteService.likeOrUnlikeBook(bookFavourite);

        switch(likeOrUnlikeBook) {
            case "Liked":
                return Result.ok().message("Successfully liked this book");
            case "Unliked":
                return Result.ok().message("Successfully unliked this book");
            case "Bad operation":
                return Result.fail().message("Failed to like or unlike this book");
            default:
                return Result.fail().message("Book like or unlike failed with server error");
        }
    }
}
