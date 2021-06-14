package acc.roadmap1.library.dao;

import acc.roadmap1.library.model.Reader;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReaderRepo extends JpaRepository <Reader, Integer> {
}
