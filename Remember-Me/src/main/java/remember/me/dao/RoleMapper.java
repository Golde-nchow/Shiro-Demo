package remember.me.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import remember.me.model.Role;

import java.util.Set;


/**
 * @author: Kam-Chou
 * @date: 2020/6/20 16:23
 * @description: 角色Mapper类
 * @version: 1.0
 */
@Mapper
public interface RoleMapper {
    Set<Role> findRolesByUserId(@Param("uid") Integer uid);
}
