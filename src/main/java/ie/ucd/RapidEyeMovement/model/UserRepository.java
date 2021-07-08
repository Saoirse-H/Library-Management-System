package ie.ucd.RapidEyeMovement.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndPassword(String email, String password);

    @Query("SELECT u FROM User u WHERE u.name LIKE %?1%")
    public List<User> memberSearch(String keyword);

    @Query("SELECT u FROM User u WHERE u.role = 'member'")
    public List<User> getMembers();
}
