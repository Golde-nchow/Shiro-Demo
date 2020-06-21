package cn.cjz.web;

import cn.cjz.dao.UserMapper;
import cn.cjz.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Kam-Chou
 * @date: 2020/6/21 10:07
 * @description: 用户控制器
 * @version: 1.0
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    /**
     * 创建用户（写死）
     */
    @RequestMapping("add")
    public String add() {
        User user = new User();
        user.setName("曹金洲");
        user.setId_card_num("1777777777");
        user.setUsername("cjz");

        userMapper.insert(user);

        return "用户创建成功";
    }

    /**
     * 删除用户（写死）
     */
    @RequestMapping("del")
    public String del() {
        userMapper.del("cjz");
        return "删除用户-[cjz]-成功";
    }

    /**
     * 用户列表页面
     */
    @RequestMapping("view")
    public String view() {
        return "用户列表页面";
    }

}