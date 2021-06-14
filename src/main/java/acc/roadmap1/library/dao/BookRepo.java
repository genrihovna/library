package acc.roadmap1.library.dao;

import acc.roadmap1.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepo extends JpaRepository<Book, Integer> {


}
