package com.yushun.recommender.controller.user;

import com.yushun.recommender.model.user.MovieRating;
import com.yushun.recommender.security.result.Result;
import com.yushun.recommender.service.MovieRatingService;
import com.yushun.recommender.vo.user.movie.MovieRatingReturnVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * User Interface User Rating Controller
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-27
 */

@CrossOrigin
@RestController
@RequestMapping("/movie/rating")
public class UserInterfaceMovieRatingController {
    @Autowired
    private MovieRatingService movieRatingService;

    @GetMapping("/getUserMovieRating")
    public Result getUserMovieRating(@RequestParam("movieId") Integer movieId,
                                 @RequestParam("email") String email) {
        MovieRatingReturnVo existMovieRating = movieRatingService.getUserMovieRating(movieId, email);

        if(existMovieRating != null) {
            return Result.ok(existMovieRating).message("Successfully get movie rating");
        }else {
            return Result.ok(new MovieRatingReturnVo(movieId, email, (float) 0)).message("User not rating for this movie");
        }
    }

    @PostMapping("/addOrUpdateUserMovieRating")
    public Result addOrUpdateUserMovieRating(@RequestBody MovieRating movieRating) {
        if(movieRatingService.addOrUpdateUserMovieRating(movieRating)) {
            return Result.ok(movieRating).message("Successfully rating the movie");
        }else {
            return Result.fail().message("Movie rating failed with server error");
        }
    }
}
