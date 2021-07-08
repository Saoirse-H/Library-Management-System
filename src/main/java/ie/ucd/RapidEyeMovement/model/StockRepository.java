package ie.ucd.RapidEyeMovement.model;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    @Query("SELECT s FROM Stock s JOIN s.artifact a WHERE a.id = ?1 AND s.location = 'Shelf'")
    public List<Stock> getAvailable(Long id);

    @Query("SELECT s FROM Stock s JOIN s.artifact a JOIN s.user u WHERE a.id = ?1 AND u.id = ?2")
    public List<Stock> getOnLoan(Long book, Long user);

    @Query("SELECT s FROM Stock s JOIN s.artifact a WHERE a.id = ?1 AND s.location = 'Reserved'")
    public List<Stock> getReserved(Long artifact_id);

    @Query("SELECT s FROM Stock s JOIN s.artifact a WHERE a.id = ?1")
    public List<Stock> getAllStockWithArtifactId(Long artifact_id);

    @Modifying
    @Transactional
    @Query("UPDATE Stock s SET s.location = 'Loan', s.checked = ?2, s.due = ?3, s.user = ?4 WHERE s.id = ?1")
    public void loanBook(Long stock_id, Date checkedOut, Date returnDate, User user);
}
