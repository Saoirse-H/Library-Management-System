package ie.ucd.RapidEyeMovement.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name="history_id", initialValue = 13, allocationSize = 100)
public class History {
  @Id 
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="history_id")
  private Long id;

  @Column Boolean returned;

  @ManyToOne
  private Artifact artifact;
  @ManyToOne
  private User user;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Boolean getReturned() {
      return returned;
  }

  public void setReturned(Boolean returned) {
    this.returned = returned;
  }

  public Artifact getArtifact() {
    return artifact;
  }

  public void setArtifact(Artifact artifact) {
    this.artifact = artifact;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}