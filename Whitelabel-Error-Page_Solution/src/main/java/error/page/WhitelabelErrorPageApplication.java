package error.page;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

}
