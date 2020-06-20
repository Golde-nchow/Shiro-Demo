package cn.cjz.dao;

import cn.cjz.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @author: Kam-Chou
 * @date: 2020/6/20 16:10
 * @description: 用户Mapper类
 * @version: 1.0
 */
@Component
public interface UserMapper {

    User findByUserName(String userName);

    int insert(User user);

    int del(@Param("username") String username);

}
