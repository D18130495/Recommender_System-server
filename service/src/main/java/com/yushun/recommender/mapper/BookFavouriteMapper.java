package com.yushun.recommender.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yushun.recommender.model.user.BookFavourite;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Book Favourite Mapper
 * </p>
 *
 * @author yushun zeng
 * @since 2023-2-1
 */

@Repository
@Mapper
public interface BookFavouriteMapper extends BaseMapper<BookFavourite> {
}
