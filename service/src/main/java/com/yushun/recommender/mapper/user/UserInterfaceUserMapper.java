package com.yushun.recommender.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yushun.recommender.model.user.user.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * User Interface User Mapper
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-5
 */

@Repository
@Mapper
public interface UserInterfaceUserMapper extends BaseMapper<User> {

}