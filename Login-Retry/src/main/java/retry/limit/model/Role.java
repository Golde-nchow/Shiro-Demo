package retry.limit.model;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: Kam-Chou
 * @date: 2020/6/20 16:04
 * @description: 角色类
 * @version: 1.0
 */
@Data
public class Role {

    private Integer id;

    private String role;

    private String description;

    private String available;

    private Set<User> users = new HashSet<User>();

    private Set<Permission> permissions = new HashSet<Permission>();


}
