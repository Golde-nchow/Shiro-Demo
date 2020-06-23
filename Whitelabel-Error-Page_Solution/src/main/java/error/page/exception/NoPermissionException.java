package error.page.exception;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author: Kam-Chou
 * @date: 2020/6/24 0:25
 * @description: 无权限全局异常处理
 * @version: 1.0
 */
@ControllerAdvice
public class NoPermissionException {

    /**
     * 无权限异常
     */
    @ExceptionHandler(UnauthorizedException.class)
    public String unauthorizedException() {
        return "unauthorized.html";
    }

    /**
     * 权限认证失败
     */
    @ExceptionHandler(AuthorizationException.class)
    public String authorizationException() {
        return "unauthorized.html";
    }


}
