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
    private MovieRatingService userRatingService;

    @GetMapping("/getMovieRating")
    public Result getMovieRating(@RequestParam("movieId") Integer movieId,
                                 @RequestParam("email") String email) {
        MovieRatingReturnVo existMovieRating = userRatingService.getMovieRating(movieId, email);

        if(existMovieRating != null) {
            return Result.ok(existMovieRating).message("Successfully get movie rating");
        }else {
            return Result.ok(new MovieRatingReturnVo(movieId, email, (float) 0)).message("User not rating for this movie");
        }
    }

    @PostMapping("/addOrUpdateMovieRating")
    public Result addOrUpdateMovieRating(@RequestBody MovieRating movieRate) {
        if(userRatingService.addOrUpdateMovieRating(movieRate)) {
            return Result.ok(movieRate).message("Successfully rating the movie");
        }else {
            return Result.fail(movieRate).message("Movie rating failed with server error");
        }
    }
}
