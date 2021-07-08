package ie.ucd.RapidEyeMovement.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReserveRepository extends JpaRepository<Reserve, Long> {
    @Query("SELECT r FROM Reserve r JOIN r.artifact a WHERE r.after = null AND a.id = ?1")
    public List<Reserve> getLastInQueue(Long artifact_id);

    @Query("SELECT r FROM Reserve r JOIN r.artifact a WHERE r.before = null AND a.id = ?1")
    public List<Reserve> getTopOfList(Long artifact_id);

    @Query("SELECT COUNT(r) FROM Reserve r JOIN r.artifact a JOIN r.user u JOIN a.stock s WHERE s.location = 'Reserved' AND r.before = null AND a.id = ?1 AND u.id = ?2")
    public int hasReservation(Long artifact_id, Long user_id);

    @Query("SELECT r FROM Reserve r JOIN r.artifact a WHERE a.id = ?1")
    public List<Reserve> alreadyReserved(Long artifact_id);
}