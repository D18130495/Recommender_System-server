package com.yushun.recommender.controller.user;

import com.yushun.recommender.model.common.MongoEntity.Movie.Movie;
import com.yushun.recommender.repository.MovieRepository;
import com.yushun.recommender.security.result.Result;
import com.yushun.recommender.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * User Interface Movie Controller
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-22
 */

@CrossOrigin
@RestController
@RequestMapping("/movie")
public class UserInterfaceMovieController {
    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping("/getRandomMovieList")
    public Result getRandomMovieList() {
        Movie movie = movieRepository.findByMovieId(1);

        return Result.ok(movie);
    }
}
