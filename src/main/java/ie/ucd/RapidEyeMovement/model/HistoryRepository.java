package ie.ucd.RapidEyeMovement.model;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
    @Query("SELECT h FROM History h JOIN h.artifact a JOIN h.user u WHERE a.id = ?1 AND u.id = ?2 AND h.returned = 'false'")
    public List<History> findNotReturned(Long artifact, Long user);

    @Modifying
    @Transactional
    @Query("UPDATE History h SET h.returned = 'true' WHERE h.id = ?1")
    public void returnBook(Long id);
}
