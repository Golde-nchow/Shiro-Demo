package frequent.read.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import frequent.read.model.Role;

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

    @Delete("DELETE FROM sys_role_permission WHERE role_id = #{rid} AND permission_id = #{pid}")
    void delPermission(@Param("rid") Integer roleId, @Param("pid") Integer permissionId);

    @Insert("INSERT INTO sys_role_permission(role_id, permission_id) VALUES(#{rid}, #{pid})")
    void addPermission(@Param("rid") Integer roleId, @Param("pid") Integer permissionId);
}
