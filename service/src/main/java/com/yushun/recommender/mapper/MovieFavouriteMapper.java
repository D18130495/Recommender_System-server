package com.yushun.recommender.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yushun.recommender.model.user.MovieFavourite;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Movie Favourite Mapper
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-28
 */

@Repository
@Mapper
public interface MovieFavouriteMapper extends BaseMapper<MovieFavourite> {
}
