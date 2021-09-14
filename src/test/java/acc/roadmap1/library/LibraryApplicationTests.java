package acc.roadmap1.library;

import acc.roadmap1.library.controller.CommonController;
import acc.roadmap1.library.service.BookService;
import acc.roadmap1.library.service.ReaderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LibraryApplicationTests {

	@Autowired
	private CommonController commonController;

	@MockBean
	private DataSource securityDataSource;

	@MockBean
	private BookService bookService;

	@MockBean
	private ReaderService readerService;

	@Test
	void contextLoads() {
//		assertThat(commonController).isNotNull();
	}

}
