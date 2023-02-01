package com.yushun.recommender.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yushun.recommender.model.user.BookRating;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Book Rating Mapper
 * </p>
 *
 * @author yushun zeng
 * @since 2023-2-1
 */


@Repository
@Mapper
public interface BookRatingMapper extends BaseMapper<BookRating> {
}
