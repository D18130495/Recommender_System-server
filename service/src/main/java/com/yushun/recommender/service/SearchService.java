package com.yushun.recommender.service;

import java.util.HashMap;

/**
 * <p>
 * Search Service Mapper
 * </p>
 *
 * @author yushun zeng
 * @since 2023-2-20
 */

public interface SearchService {
    HashMap<String, Object> fuzzySearchMovieAndBookByTitleOrYear(String substring, String type);
}
