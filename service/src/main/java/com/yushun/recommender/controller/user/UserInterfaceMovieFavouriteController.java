package com.yushun.recommender.controller.user;

import com.yushun.recommender.model.user.MovieFavourite;
import com.yushun.recommender.security.result.Result;
import com.yushun.recommender.service.MovieFavouriteService;
import com.yushun.recommender.vo.user.movie.MovieFavouriteReturnVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * User Interface User Favourite Controller
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-28
 */

@CrossOrigin
@RestController
@RequestMapping("/movie/favourite")
public class UserInterfaceMovieFavouriteController {
    @Autowired
    private MovieFavouriteService movieFavouriteService;

    @GetMapping("/getUserMovieFavourite")
    public Result getUserMovieFavourite(@RequestParam("movieId") Integer movieId,
                                    @RequestParam("email") String email) {
        MovieFavouriteReturnVo existMovieFavourite = movieFavouriteService.getUserMovieFavourite(movieId, email);

        if(existMovieFavourite != null) {
            return Result.ok(existMovieFavourite).message("Successfully get movie favourite");
        }else {
            return Result.ok(new MovieFavouriteReturnVo(movieId, email, "0")).message("User haven't liked this movie");
        }
    }

    @PostMapping("/likeOrUnlikeMovie")
    public Result likeOrUnlikeMovie(@RequestBody MovieFavourite movieFavourite) {
        String likeOrUnlikeMovie = movieFavouriteService.likeOrUnlikeMovie(movieFavourite);

        switch(likeOrUnlikeMovie) {
            case "Liked":
                return Result.ok().message("Successfully liked this movie");
            case "Unliked":
                return Result.ok().message("Successfully unliked this movie");
            case "Bad operation":
                return Result.fail().message("Failed to like or unlike this movie");
            default:
                return Result.fail().message("Movie like or unlike failed with server error");
        }
    }
}
