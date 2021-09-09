package acc.roadmap1.library;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@ComponentScan(basePackages = "acc.roadmap1.library")
@SpringBootApplication
public class LibraryApplication {

//	@Autowired
//	JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}

}
