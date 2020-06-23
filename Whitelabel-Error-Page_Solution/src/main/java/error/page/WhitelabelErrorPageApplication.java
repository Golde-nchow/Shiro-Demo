package error.page;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

/**
 * @author: Kam-Chou
 * @date: 2020/6/23 23:34
 * @description: 空白异常页面启动类
 * @version: 1.0
 */
@SpringBootApplication
@MapperScan("error.page.dao")
public class WhitelabelErrorPageApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhitelabelErrorPageApplication.class, args);
    }

    /**
     * 容器定制类，自定义错误页面
     */
    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
        return factory -> {
            ErrorPage unauthorized = new ErrorPage(HttpStatus.UNAUTHORIZED, "/unauthorized.html");
            ErrorPage errorPage = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
            ErrorPage serverError = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500.html");
            factory.addErrorPages(unauthorized, errorPage, serverError);
        };
    }
}
