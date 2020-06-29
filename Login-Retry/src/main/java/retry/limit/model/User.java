package retry.limit.model;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: Kam-Chou
 * @date: 2020/6/20 16:03
 * @description: 用户类
 * @version: 1.0
 */
@Data
public class User {

    private Integer uid;

    private String username;

    private String password;

    private String name;

    private String id_card_num;

    private String state;

    private Set<Role> roles = new HashSet<Role>();



}
