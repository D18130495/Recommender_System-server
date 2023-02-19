package com.yushun.recommender.controller.user;

import com.yushun.recommender.model.user.BookRating;
import com.yushun.recommender.security.result.Result;
import com.yushun.recommender.service.BookRatingService;
import com.yushun.recommender.vo.user.book.BookRatingReturnVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * User Interface Book Rating Controller
 * </p>
 *
 * @author yushun zeng
 * @since 2023-2-1
 */

@CrossOrigin
@RestController
@RequestMapping("/book/rating")
public class UserInterfaceBookRatingController {
    @Autowired
    private BookRatingService bookRatingService;

    @GetMapping("/getUserBookRating")
    public Result getUserBookRating(@RequestParam("isbn") String isbn,
                                 @RequestParam("email") String email) {
        BookRatingReturnVo existBookRating = bookRatingService.getUserBookRating(isbn, email);

        if(existBookRating != null) {
            return Result.ok(existBookRating).message("Successfully get book rating");
        }else {
            return Result.ok(new BookRatingReturnVo(isbn, email, (float) 0)).message("User not rating for this book");
        }
    }

    @PostMapping("/addOrUpdateUserBookRating")
    public Result addOrUpdateUserBookRating(@RequestBody BookRating bookRating) {
        String addOrUpdateState = bookRatingService.addOrUpdateUserBookRating(bookRating);

        switch (addOrUpdateState) {
            case "User not find":
                return Result.fail().message("Can not find this user");
            case "Book not find":
                return Result.fail().message("This book is not exist in this system");
            case "Successfully rating this book":
                return Result.ok(bookRating).message("Successfully rating the book");
            case "Successfully updated rating":
                return Result.ok(bookRating).message("Successfully updated rating");
            default:
                return Result.fail().message("Book rating failed with server error");
        }
    }
}
