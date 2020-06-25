package ehcache.model;

import ehcache.model.Role;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: Kam-Chou
 * @date: 2020/6/20 16:04
 * @description: 权限类
 * @version: 1.0
 */
@Data
public class Permission {


    private Integer id;

    private Integer parent_id;

    private String parent_ids;

    private String permission;

    private String resource_type;

    private String url;

    private String name;

    private String available;

    private Set<Role> roles = new HashSet<Role>();
}

