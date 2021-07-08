package ie.ucd.RapidEyeMovement.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@SequenceGenerator(name="user_id", initialValue = 5, allocationSize = 100)
public class User {
  @Id 
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="user_id")
  private Long id;

  @Column private String name;
  @Column private String address;
  @Column private String email;
  @Column private String phone;
  @Column private String role;

  @Column private String password;

  @CreationTimestamp
  @Column private Date joined;
  
  @Column private Date birthday;    

  @Column private Float fee;

  @OneToMany(mappedBy = "user")
  private List<Stock> stock;

  @OneToMany(mappedBy = "user")
  private List<History> history;

  @OneToMany(mappedBy = "user")
  private List<Reserve> reserved;

  public void setId(Long id) {
      this.id = id;
  }

  public Long getId() {
      return id;
  }

  public void setName(String name) {
      this.name = name;
  }

  public String getName() {
      return name;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public String getEmail()
  {
    return email;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getAddress() {
    return address;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getPhone() {
    return phone;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getRole() {
    return role;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPassword() {
    return password;
  }

  public void setJoinDate(Date joined) {
    this.joined = joined;
  }

  public Date getJoinDate() {
    return joined;
  }

  public void setBirthday(Date birthday) {
    this.birthday = birthday;
  }

  public Date getBirthday() {
    return birthday;
  }

  public Float getLateFee() {
    return fee;
  }

  public void setLateFee(Float fee) {
    this.fee = fee;
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

  public void setUserHistoy(List<History> history) {
    this.history = history;
  }

  public List<Reserve> getReserved() {
    return reserved;
  }

  public void setReserved(List<Reserve> reserved) {
    this.reserved = reserved;
  }
}