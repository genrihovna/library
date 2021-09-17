package acc.roadmap1.library.repository;

import acc.roadmap1.library.model.Reader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReaderRepository extends JpaRepository<Reader, Long> {

    Optional<Reader> findReaderByName(String name);
}
