package com.dt87.miaosha.mapper;

import com.dt87.miaosha.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    public User findUser(@Param("id") Long id);
}
