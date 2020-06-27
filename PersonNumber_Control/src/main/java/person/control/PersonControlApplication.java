package person.control;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "person.control.dao")
public class PersonControlApplication {

	public static void main(String[] args) {
		SpringApplication.run(PersonControlApplication.class, args);
	}

}
