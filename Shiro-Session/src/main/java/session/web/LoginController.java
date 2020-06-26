package session.web;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import session.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author: Kam-Chou
 * @date: 2020/6/21 9:16
 * @description: 登录控制器
 * @version: 1.0
 */
@Controller
public class LoginController {

    @RequestMapping("/")
    public String root() {
        // 获取当前正在操作的用户, 所有的Subject都绑定到安全管理器当中, 每个操作通过安全管理器中的Realm来鉴权.
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();

        if (user == null) {
            return "redirect:/login";
        } else {
            return "redirect:/index";
        }
    }

    /**
     * 登录页面
     */
    @RequestMapping("login")
    public String login() {
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();

        // 查看是否已经登录
        if (user == null) {
            return "login";
        } else {
            return "redirect:/index";
        }
    }

    /**
     * 登录用户
     */
    @PostMapping("login")
    public String loginUser(HttpServletRequest request, String username, String password, Model model, HttpSession session) {
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();

        try {
            subject.login(usernamePasswordToken);
            // 通过鉴权的逻辑，获取设置的 Principal
            User user = (User) subject.getPrincipal();
            session.setAttribute("user", user);
            model.addAttribute("user", user);
            return "index";
        } catch (Exception e) {
            // 从request获取 shiro 异常
            String exception = (String) request.getAttribute("shiroLoginFailure");
            model.addAttribute("msg", e.getMessage());

            // 返回登录页面
            return "login";
        }
    }

    /**
     * 首页
     */
    @RequestMapping("index")
    public String index() {
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (user == null) {
            return "login";
        } else {
            return "index";
        }
    }

    /**
     * 登出
     */
    @RequestMapping("logout")
    public String logout(Model model) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();

        model.addAttribute("msg", "安全退出");
        return "login";
    }

    /**
     * 无权限页面
     */
    @RequestMapping("unauthorized")
    public String unauthorized() {
        return "unauthorized";
    }
}
