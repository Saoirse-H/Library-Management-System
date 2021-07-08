package ie.ucd.RapidEyeMovement.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
 
@Entity
@SequenceGenerator(name="artifact_id", initialValue = 10, allocationSize = 100)
public class Artifact{
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="artifact_id")
    private Long id;

    private String title;
    private String synopsis;
    private String author;
    private String media;

    @OneToMany(mappedBy = "artifact", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Stock> stock;

    @OneToMany(mappedBy = "artifact", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<History> history;

    @OneToMany(mappedBy = "artifact", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reserve> reserved;

    public Artifact() {}

    public Artifact(Long id, String title, String author, String media) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.media = media;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSynopsis(){
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public List<Stock> getStock() {
        return stock;
    }

    public void setStock(List<Stock> stock) {
        this.stock = stock;
    }

    public List<History> getHistory() {
        return history;
    }

    public void setHistoy(List<History> history) {
        this.history = history;
    }

    public List<Reserve> getReserved() {
        return reserved;
    }

    public void setReserved(List<Reserve> reserved) {
        this.reserved = reserved;
    }
}