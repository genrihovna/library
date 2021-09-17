package acc.roadmap1.library.repository;

import acc.roadmap1.library.model.Account;
import acc.roadmap1.library.model.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findAccountByUsername(String username);
}
