package com.yushun.recommender.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yushun.recommender.model.user.MovieRating;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Movie Rating Mapper
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-27
 */

@Repository
@Mapper
public interface MovieRatingMapper extends BaseMapper<MovieRating> {

}
