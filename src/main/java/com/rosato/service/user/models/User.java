package com.rosato.service.user.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = { "lastName", "firstName" }) })
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(min = 1, max = 100)
  private String lastName;

  @NotBlank
  @Size(min = 1, max = 100)
  private String firstName;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
  private List<UserEmail> emails;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
  private List<Phone> phones;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void addUserEmail(UserEmail email) {
    if (this.emails == null) {
      this.emails = new ArrayList<>();
    }

    email.setUser(this);
    this.emails.add(email);
  }

  public void addPhone(Phone phone) {
    if (this.phones == null) {
      this.phones = new ArrayList<>();
    }

    phone.setUser(this);
    this.phones.add(phone);
  }

  public List<UserEmail> getEmails() {
    return this.emails;
  }

  public List<Phone> phones() {
    return this.phones;
  }

  public void setEmails(List<UserEmail> emails) {
    this.emails = emails;
  }

  public List<Phone> getPhones() {
    return phones;
  }

  public void setPhones(List<Phone> phones) {
    this.phones = phones;
  }

}
