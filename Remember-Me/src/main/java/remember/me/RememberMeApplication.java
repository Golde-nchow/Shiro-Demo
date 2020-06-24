package remember.me;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "remember.me.dao")
public class RememberMeApplication {

	public static void main(String[] args) {
		SpringApplication.run(RememberMeApplication.class, args);
	}

}
