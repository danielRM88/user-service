package com.rosato.service.user.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.rosato.service.user.models.Phone;
import com.rosato.service.user.models.User;
import com.rosato.service.user.models.UserEmail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepositoryTest {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserEmailRepository userEmailRepository;
  @Autowired
  private PhoneRepository phoneRepository;

  @BeforeEach
  private void cleanUsers() {
    userRepository.deleteAll();
  }

  @Test
  void injectedComponentsAreNotNull() {
    assertNotNull(userRepository);
    assertNotNull(userEmailRepository);
    assertNotNull(phoneRepository);
  }

  @Test
  public void shouldPersistUser() {
    User u = new User();
    u.setFirstName("Daniel");
    u.setLastName("Rosato");
    userRepository.save(u);
    assertNotNull(userRepository.findByFirstNameAndLastName("Daniel", "Rosato"));
  }

  @Test
  public void shouldUpdateUser() {
    User u = new User();
    u.setFirstName("New");
    u.setLastName("Last");
    userRepository.save(u);
    assertNotNull(userRepository.findByFirstNameAndLastName("New", "Last"));
    u.setLastName("NewLast");
    userRepository.save(u);
    assertNotNull(userRepository.findByFirstNameAndLastName("New", "NewLast"));
  }

  @Test
  public void shouldFindAllUserByFirstName() {
    String firstName = "Daniel";
    User u = new User();
    u.setFirstName(firstName);
    u.setLastName("Rosato");
    userRepository.save(u);
    u = new User();
    u.setFirstName(firstName);
    u.setLastName("Test");
    userRepository.save(u);
    Iterable<User> users = userRepository.findByFirstName(firstName);
    int cont = 0;
    for (User user : users) {
      assertEquals(firstName, user.getFirstName());
      cont++;
    }
    assertEquals(2, cont);
  }

  @Test
  public void shouldFindAllUserByLastName() {
    String lastName = "Rosato";
    User u = new User();
    u.setFirstName("Daniel");
    u.setLastName(lastName);
    userRepository.save(u);
    u = new User();
    u.setFirstName("John");
    u.setLastName(lastName);
    userRepository.save(u);
    Iterable<User> users = userRepository.findByLastName(lastName);
    int cont = 0;
    for (User user : users) {
      assertEquals(lastName, user.getLastName());
      cont++;
    }
    assertEquals(2, cont);
  }

  @Test
  public void shouldFailWithFirstNameBlank() {
    User u = new User();
    u.setLastName("Rosato");
    assertThrows(javax.validation.ConstraintViolationException.class, () -> {
      userRepository.save(u);
    });
  }

  @Test
  public void shouldFailWithLastNameBlank() {
    User u = new User();
    u.setFirstName("Daniel");
    assertThrows(javax.validation.ConstraintViolationException.class, () -> {
      userRepository.save(u);
    });
  }

  @Test
  public void shouldFailIfUserAlreadyExists() {
    User u = new User();
    u.setFirstName("John");
    u.setLastName("Doe");
    userRepository.save(u);
    User u2 = new User();
    u2.setFirstName("John");
    u2.setLastName("Doe");
    assertThrows(org.springframework.dao.DataIntegrityViolationException.class, () -> {
      userRepository.save(u2);
    });
  }

  @Test
  public void shouldDeleteExistingUser() {
    User u = new User();
    u.setFirstName("First");
    u.setLastName("Last");
    userRepository.save(u);
    assertTrue(userRepository.findById(u.getId()).isPresent());
    userRepository.delete(u);
    assertFalse(userRepository.findById(u.getId()).isPresent());
  }

  @Test
  public void shouldPersistEmail() {
    String email = "test@test.com";
    User u = new User();
    u.setFirstName("Jane");
    u.setLastName("Doe");
    UserEmail ue = new UserEmail();
    ue.setEmail(email);
    u.addUserEmail(ue);
    userRepository.save(u);
    assertEquals(email, userEmailRepository.findByEmail(email).getEmail());
  }

  @Test
  public void shouldCascadeRemoveAnyEmails() {
    String email = "test@test.com";
    User u = new User();
    u.setFirstName("Jane");
    u.setLastName("Doe");
    UserEmail ue = new UserEmail();
    ue.setEmail(email);
    u.addUserEmail(ue);
    userRepository.save(u);
    assertEquals(email, userEmailRepository.findByEmail(email).getEmail());
    userRepository.delete(u);
    assertTrue(userEmailRepository.findByEmail(email) == null);
  }

  @Test
  public void shouldPersistPhone() {
    String phone = "123456789";
    User u = new User();
    u.setFirstName("FirstName");
    u.setLastName("LastName");
    Phone p = new Phone();
    p.setPhone(phone);
    u.addPhone(p);
    userRepository.save(u);
    assertEquals(phone, phoneRepository.findByPhone(phone).getPhone());
  }

  @Test
  public void shouldCascadeRemoveAnyPhones() {
    String phone = "0987654321";
    User u = new User();
    u.setFirstName("Test");
    u.setLastName("Test");
    Phone p = new Phone();
    p.setPhone(phone);
    u.addPhone(p);
    userRepository.save(u);
    assertEquals(phone, phoneRepository.findByPhone(phone).getPhone());
    userRepository.delete(u);
    assertTrue(phoneRepository.findByPhone(phone) == null);
  }
}
