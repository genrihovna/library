package acc.roadmap1.library.repository;

import acc.roadmap1.library.model.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    List<Privilege> findPrivilegeByNameIn(List<String> names);
}
