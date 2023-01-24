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

import java.util.ArrayList;
import java.util.Arrays;
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

        // form result
        movieReturnList = movieList.stream().map(this::formMovieResult).collect(Collectors.toList());

        return Result.ok(movieReturnList);
    }

    public MovieReturnVo formMovieResult(Movie movie) {
        MovieReturnVo movieReturnVo = new MovieReturnVo();

        BeanUtils.copyProperties(movie, movieReturnVo);

        // parse genres
        if(!movie.getGenres().isEmpty()) {
            String[] genreList = movie.getGenres().split("[ | ]");
            movieReturnVo.setGenres(new ArrayList<>(Arrays.asList(genreList)));
        }

        movieReturnVo.setRate(Float.parseFloat((String)movie.getParam().get("rate")));

        return movieReturnVo;
    }
}
