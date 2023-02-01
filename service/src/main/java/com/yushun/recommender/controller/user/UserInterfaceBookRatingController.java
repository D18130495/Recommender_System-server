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
        if(bookRatingService.addOrUpdateUserBookRating(bookRating)) {
            return Result.ok(bookRating).message("Successfully rating the book");
        }else {
            return Result.fail().message("Book rating failed with server error");
        }
    }
}
