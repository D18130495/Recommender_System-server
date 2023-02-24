package com.yushun.recommender.controller.user;

import com.yushun.recommender.security.result.Result;
import com.yushun.recommender.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * <p>
 * Search Controller
 * </p>
 *
 * @author yushun zeng
 * @since 2023-2-20
 */

@CrossOrigin
@RestController
@RequestMapping("/search")
public class searchController {
    @Autowired
    private SearchService searchService;

    @GetMapping("/fuzzySearchMovieAndBookByTitleOrYear")
    public Result fuzzySearchMovieAndBookByTitleOrYear(@RequestParam("substring") String substring,
                                                 @RequestParam("type") String type) {
        HashMap<String, Object> resultMap = searchService.fuzzySearchMovieAndBookByTitleOrYear(substring, type);

        if(resultMap.get("count").equals(0)) {
            return Result.ok(resultMap).message("No result find");
        }else {
            return Result.ok(resultMap).message("Successfully get result map");
        }
    }
}
