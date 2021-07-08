package ie.ucd.RapidEyeMovement.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
 
@Entity
public class UserBooks{
    @Id 
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String title;
    private Date checked;
    private Date due;
    private Boolean returned;

    protected UserBooks(){}

    public UserBooks(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public UserBooks(Long id, String title, Date checked, Date due) {
        this.id = id;
        this.title = title;
        this.checked = checked;
        this.due = due;
    }

    public UserBooks(Long id, String title, Boolean returned) {
        this.id = id;
        this.title = title;
        this.returned = returned;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id; 
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setChecked(Date checked) {
        this.checked = checked;
    }

    public Date getChecked() {
        return checked;
    }

    public void setDue(Date due) {
        this.due = due;
    }

    public Date getDue() {
        return due;
    }

    public void getReturned(Boolean returned) {
        this.returned = returned;
    }

    public Boolean getReturned() {
        return returned;
    }
}