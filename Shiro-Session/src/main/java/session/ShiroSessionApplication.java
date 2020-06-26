package session;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "session.dao")
public class ShiroSessionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShiroSessionApplication.class, args);
	}

}
