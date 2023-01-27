package com.yushun.recommender.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yushun.recommender.model.common.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * User Mapper
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-5
 */

@Repository
@Mapper
public interface UserMapper extends BaseMapper<User> {

}