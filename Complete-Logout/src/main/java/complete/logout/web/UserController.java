package complete.logout.web;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import complete.logout.dao.RoleMapper;
import complete.logout.dao.UserMapper;
import complete.logout.model.User;
import complete.logout.shiro.MyRealm;

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

    @Autowired
    private RoleMapper roleMapper;

    /**
     * 创建用户（写死）
     */
    @RequiresPermissions("userInfo:add")
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
    @RequiresPermissions("userInfo:del")
    @RequestMapping("del")
    public String del() {
        userMapper.del("cjz");
        return "删除用户-[cjz]-成功";
    }

    /**
     * 用户列表页面
     */
    @RequiresPermissions("userInfo:view")
    @RequestMapping("view")
    public String view() {
        return "用户列表页面";
    }

    /**
     * 给test用户添加 userInfo:del 权限
     */
    @RequestMapping("addPermission")
    @ResponseBody
    public String addPermission() {

        // 将 删除的权限 关联到test用户所在的角色
        roleMapper.addPermission(2, 2);

        // 添加成功之后 清除缓存
        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
        MyRealm shiroRealm = (MyRealm) securityManager.getRealms().iterator().next();
        // 清除当前用户缓存-权限相关
        shiroRealm.clearCurrentAuthenticationInfo();

        return "给admin用户添加 userInfo:del 权限成功";

    }

    /**
     * 删除admin用户 userInfo:del 权限
     */
    @RequestMapping("delPermission")
    @ResponseBody
    public String delPermission() {

        // 将 删除的权限 关联到test用户所在的角色
        roleMapper.delPermission(2, 2);
        // 添加成功之后 清除缓存
        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager)SecurityUtils.getSecurityManager();
        MyRealm shiroRealm = (MyRealm) securityManager.getRealms().iterator().next();
        //清除当前用户授权缓存
        shiroRealm.clearCurrentAuthenticationInfo();

        return "删除admin用户userInfo:del 权限成功";

    }
}
