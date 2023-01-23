package com.yushun.recommender.controller.user;

import com.yushun.recommender.model.common.mongoEntity.movie.Movie;
import com.yushun.recommender.repository.MovieRepository;
import com.yushun.recommender.security.result.Result;
import com.yushun.recommender.service.MovieService;
import com.yushun.recommender.vo.user.movie.MovieReturnVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

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
        // initial random movie list
        List<Movie> movieList = movieService.getRandomMovie();

        // result variable
        List<MovieReturnVo> movieReturnList;
        MovieReturnVo movieReturnVo = new MovieReturnVo();

        // form result
        movieReturnList = movieList.stream().map(movie -> {
            BeanUtils.copyProperties(movie, movieReturnVo);
            movieReturnVo.setRate((String)movie.getParam().get("rate"));

            return movieReturnVo;
        }).collect(Collectors.toList());

        return Result.ok(movieReturnList);
    }
}
