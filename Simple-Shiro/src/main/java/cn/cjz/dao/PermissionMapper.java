package cn.cjz.dao;

import cn.cjz.model.Permission;
import cn.cjz.model.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author: Kam-Chou
 * @date: 2020/6/20 16:23
 * @description: 角色Mapper类
 * @version: 1.0
 */
@Component
public interface PermissionMapper {

    Set<Permission> findPermissionsByRoleId(@Param("roles") Set<Role> roles);
}
