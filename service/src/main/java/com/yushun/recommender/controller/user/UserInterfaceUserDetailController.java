package com.yushun.recommender.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yushun.recommender.minio.utils.MinioUtils;
import com.yushun.recommender.model.common.User;
import com.yushun.recommender.security.result.Result;
import com.yushun.recommender.security.utils.JwtTokenProvider;
import com.yushun.recommender.service.BookService;
import com.yushun.recommender.service.MovieService;
import com.yushun.recommender.service.UserService;
import com.yushun.recommender.vo.user.book.BookLikeListReturnVo;
import com.yushun.recommender.vo.user.book.BookRatingListReturnVo;
import com.yushun.recommender.vo.user.movie.MovieLikeListReturnVo;
import com.yushun.recommender.vo.user.movie.MovieRatingListReturnVo;
import com.yushun.recommender.vo.user.user.UserDetailReturnVo;
import com.yushun.recommender.vo.user.user.UserReturnVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import java.util.Date;
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

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private MinioUtils minioUtils;

    @Value("${minio.endpoint}")
    private String address;

    @Value("${minio.bucketName}")
    private String bucketName;

    @GetMapping("/getUserDetailByToken")
    public Result getUserDetailByToken(@RequestParam("token") String token) {
        String userEmail = jwtTokenProvider.getUsername(token);

        // find user
        QueryWrapper userWrapper = new QueryWrapper();
        userWrapper.eq("email", userEmail);

        User findUser = userService.getOne(userWrapper);

        if(findUser == null) {
            return Result.fail().message("Can not find user");
        }

        // form return data
        UserDetailReturnVo userDetailReturnVo = new UserDetailReturnVo();
        BeanUtils.copyProperties(findUser, userDetailReturnVo);

        return Result.ok(userDetailReturnVo);
    }

    @GetMapping("/getUserDetailByEmail")
    public Result getUserDetailByEmail(@PathParam("email") String email) {
        UserDetailReturnVo findUser = userService.getUserDetailByEmail(email);

        if(findUser == null) {
            return Result.fail().message("Can not find user");
        }else {
            return Result.ok(findUser);
        }
    }

    // update username, avatar, policy
    @PutMapping("/updateUserDetail")
    public Result updateUserDetail(@RequestBody User user) {
        // find user
        QueryWrapper userWrapper = new QueryWrapper();
        userWrapper.eq("email", user.getEmail());

        User findUser = userService.getOne(userWrapper);

        if(findUser == null) {
            return Result.fail().message("Can not find user");
        }

        findUser.setUsername(user.getUsername());

        if(user.getPolicy().equals("true")) {
            findUser.setPolicy("T");
        }else {
            findUser.setPolicy("F");
        }

        findUser.setUpdateTime(new Date());

        boolean isUpdate = userService.update(findUser, userWrapper);

        if(isUpdate) {
            return Result.ok(findUser).message("Successfully updated user");
        }else {
            return Result.fail().message("Failed to update user");
        }
    }

    @PostMapping("/updateUserAvatar")
    public Result updateUserAvatar(@RequestParam MultipartFile file, HttpServletRequest request) {
        // upload file to minio
        List<String> upload = minioUtils.upload(new MultipartFile[]{file});

        // find user email
        String token = jwtTokenProvider.resolveToken(request);

        String userEmail = jwtTokenProvider.getUsername(token);

        // update user
        QueryWrapper userWrapper = new QueryWrapper();
        userWrapper.eq("email", userEmail);

        User findUser = userService.getOne(userWrapper);

        if(findUser == null) {
            return Result.fail().message("Can not find user");
        }

        findUser.setAvatar("//images.weserv.nl/?url=" + address+"/"+bucketName+"/"+upload.get(0));

        findUser.setUpdateTime(new Date());

        boolean isUpdate = userService.update(findUser, userWrapper);

        if(isUpdate) {
            UserReturnVo userReturnVo = new UserReturnVo();
            BeanUtils.copyProperties(findUser, userReturnVo);

            return Result.ok(userReturnVo).message("Successfully updated avatar");
        }else {
            return Result.fail().message("Failed to update avatar");
        }
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
