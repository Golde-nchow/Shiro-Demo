package frequent.read.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import frequent.read.model.Permission;
import frequent.read.model.Role;

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
