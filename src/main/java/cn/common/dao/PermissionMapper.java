package cn.common.dao;

import cn.common.model.Permission;
import cn.common.model.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * @author: Kam-Chou
 * @date: 2020/6/20 16:23
 * @description: 角色Mapper类
 * @version: 1.0
 */
@Mapper
public interface PermissionMapper {

    Set<Permission> findPermissionsByRoleId(@Param("roles") Set<Role> roles);
}
