package cn.common.dao;

import cn.common.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author: Kam-Chou
 * @date: 2020/6/20 16:10
 * @description: 用户Mapper类
 * @version: 1.0
 */
@Mapper
public interface UserMapper {

    User findByUserName(String userName);

    int insert(User user);

    int del(@Param("username") String username);

}
