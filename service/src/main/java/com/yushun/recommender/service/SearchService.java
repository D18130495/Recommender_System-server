package com.yushun.recommender.service;

import com.yushun.recommender.vo.user.search.FuzzySearchReturnVo;

import java.util.HashMap;
import java.util.List;

public interface SearchService {
    HashMap<String, Object> fuzzySearchMovieAndBookByTitle(String titleSubstring);
}
