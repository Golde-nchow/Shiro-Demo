package session.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import session.model.Role;

import java.util.Set;


/**
 * @author: Kam-Chou
 * @date: 2020/6/20 16:23
 * @description: 角色Mapper类
 * @version: 1.0
 */
@Component
public interface RoleMapper {
    Set<Role> findRolesByUserId(@Param("uid") Integer uid);
}
