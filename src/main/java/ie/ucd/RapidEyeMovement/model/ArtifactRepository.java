package ie.ucd.RapidEyeMovement.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtifactRepository extends JpaRepository<Artifact, Long> {
    @Query("SELECT a FROM Artifact a WHERE a.author LIKE %?1% OR a.title LIKE %?1% OR a.synopsis LIKE %?1%")
    public List<Artifact> keywordSearch(String keyword);

    @Query("SELECT DISTINCT (a) FROM Artifact a JOIN a.stock s WHERE a.title LIKE %?1% AND a.author LIKE %?2% AND a.media = ?3 AND (s.location = ?4 OR s.location = 'Shelf')")
    public List<Artifact> advancedSearch(String title, String author, String media, String stock);

    @Query("SELECT DISTINCT (a) FROM Artifact a JOIN a.stock s JOIN a.reserved r JOIN r.user u WHERE s.location = 'Reserved' AND r.before = null AND u.id = ?1")
    public List<Artifact> getReservedCollection(Long userId);

    @Query("SELECT new ie.ucd.RapidEyeMovement.model.UserBooks(a.id, a.title, s.checked, s.due) FROM Artifact a JOIN a.stock s JOIN s.user u WHERE u.id = ?1")
    public List<UserBooks> getUserBooks(Long userId);

    @Query("SELECT new ie.ucd.RapidEyeMovement.model.UserBooks(a.id, a.title, s.checked, s.due) FROM Artifact a JOIN a.stock s JOIN s.user u WHERE u.id = ?1 AND s.due < CURRENT_TIMESTAMP")
    public List<UserBooks> getOverdue(Long userId);

    @Query("SELECT new ie.ucd.RapidEyeMovement.model.UserBooks(a.id, a.title, h.returned) FROM Artifact a JOIN a.history h JOIN h.user u WHERE u.id = ?1")
    public List<UserBooks> getHistory(Long userId);
    
    @Query("SELECT new ie.ucd.RapidEyeMovement.model.UserBooks(a.id, a.title) FROM Artifact a JOIN a.reserved r JOIN r.user u where u.id = ?1")
    public List<UserBooks> getReserved(Long userId);

    @Query("SELECT Count(s) FROM Artifact a JOIN a.stock s WHERE s.location = 'Shelf' AND a.id = ?1")
    public int inStock(Long id);
    
}
