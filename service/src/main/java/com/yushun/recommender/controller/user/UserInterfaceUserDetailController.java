package com.yushun.recommender.controller.user;

import com.yushun.recommender.model.common.User;
import com.yushun.recommender.security.result.Result;
import com.yushun.recommender.service.BookService;
import com.yushun.recommender.service.MovieService;
import com.yushun.recommender.service.UserService;
import com.yushun.recommender.vo.user.book.BookLikeListReturnVo;
import com.yushun.recommender.vo.user.book.BookRatingListReturnVo;
import com.yushun.recommender.vo.user.movie.MovieLikeListReturnVo;
import com.yushun.recommender.vo.user.movie.MovieRatingListReturnVo;
import com.yushun.recommender.vo.user.user.UserDetailReturnVo;
import com.yushun.recommender.vo.user.user.UserUpdatePasswordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import java.util.List;

/**
 * <p>
 * User Interface User Detail Controller
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-8
 */

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserInterfaceUserDetailController {
    @Autowired
    private UserService userService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private BookService bookService;

    @GetMapping("/getUserDetailByToken")
    public Result getUserDetailByToken(@RequestParam("token") String token) {
        return userService.getUserDetailByToken(token);
    }

    @GetMapping("/getUserDetailByEmail")
    public Result getUserDetailByEmail(@PathParam("email") String email) {
        UserDetailReturnVo findUser = userService.getUserDetailByEmail(email);

        if(findUser == null) {
            return Result.fail().message("Can not find user");
        }else {
            return Result.ok(findUser).message("Successfully find user");
        }
    }

    // update username, avatar, policy
    @PutMapping("/updateUserDetail")
    public Result updateUserDetail(@RequestBody User user) {
        return userService.updateUserDetail(user);
    }

    @PostMapping("/updateUserAvatar")
    public Result updateUserAvatar(@RequestParam MultipartFile file, HttpServletRequest request) {
        return userService.updateUserAvatar(file, request);
    }

    @PutMapping("/updateSystemUserPassword")
    public Result updateSystemUserPassword(@RequestBody UserUpdatePasswordVo userUpdatePasswordVo) {
        return userService.updateSystemUserPassword(userUpdatePasswordVo);
    }

    @GetMapping("/getUserLikeAndRatingMovieCount")
    public Result getUserLikeAndRatingMovieCount(@PathParam("email") String email) {
        int movieLikeCount = movieService.getUserLikeAndRatingMovieCount(email);

        return Result.ok(movieLikeCount);
    }

    @GetMapping("/getUserLikeAndRatingBookCount")
    public Result getUserLikeAndRatingBookCount(@PathParam("email") String email) {
        int bookLikeCount = bookService.getUserLikeAndRatingBookCount(email);

        return Result.ok(bookLikeCount);
    }

    @GetMapping("/getUserMovieLikeList")
    public Result getUserMovieLikeList(@PathParam("email") String email) {
        List<MovieLikeListReturnVo> movieLikeList = movieService.getUserMovieLikeList(email);

        return Result.ok(movieLikeList);
    }

    @GetMapping("/getUserMovieRatingList")
    public Result getUserMovieRatingList(@PathParam("email") String email) {
        List<MovieRatingListReturnVo> movieRatingList = movieService.getUserMovieRatingList(email);

        return Result.ok(movieRatingList);
    }

    @GetMapping("/getUserBookLikeList")
    public Result getUserBookLikeList(@PathParam("email") String email) {
        List<BookLikeListReturnVo> bookLikeList = bookService.getUserBookLikeList(email);

        return Result.ok(bookLikeList);
    }

    @GetMapping("/getUserBookRatingList")
    public Result getUserBookRatingList(@PathParam("email") String email) {
        List<BookRatingListReturnVo> bookRatingList = bookService.getUserBookRatingList(email);

        return Result.ok(bookRatingList);
    }
}
