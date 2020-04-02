package com.rosato.service.user.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.rosato.service.user.controllers.UsersController.CreateUserRequest;
import com.rosato.service.user.controllers.UsersController.SearchRequest;
import com.rosato.service.user.controllers.UsersController.UserAlreadyExistsException;
import com.rosato.service.user.models.Phone;
import com.rosato.service.user.models.User;
import com.rosato.service.user.models.UserEmail;
import com.rosato.service.user.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
public class UsersControllerTest {
  @Autowired
  private UsersController usersController;
  private UserService userService;

  @BeforeEach
  private void mockUserService() {
    userService = mock(UserService.class);
    ReflectionTestUtils.setField(usersController, "userService", userService);
  }

  @Test
  public void shouldReturnAllUsers() {
    when(userService.findAll()).thenAnswer(new Answer<Iterable<User>>() {
      public Iterable<User> answer(InvocationOnMock invocation) throws Throwable {
        List<User> list = new ArrayList<>();
        list.add(buildUser());
        return list;
      }
    });
    Iterable<User> users = usersController.index(null);
    for (User user : users) {
      assertEquals("Rosato", user.getLastName());
    }
    verify(userService).findAll();
  }

  @Test
  public void shouldReturnUserByNameAndLastName() {
    String firstName = "Daniel";
    String lastName = "Rosato";

    when(userService.findByFirstNameAndLastName(firstName, lastName)).thenAnswer(new Answer<User>() {
      public User answer(InvocationOnMock invocation) throws Throwable {
        User u = new User();
        u.setFirstName(firstName);
        u.setLastName(lastName);
        return u;
      }
    });

    SearchRequest searchRequest = new SearchRequest();
    searchRequest.setFirstName(firstName);
    searchRequest.setLastName(lastName);
    Iterable<User> users = usersController.index(searchRequest);
    int cont = 0;
    for (User user : users) {
      assertEquals(firstName, user.getFirstName());
      assertEquals(lastName, user.getLastName());
      cont++;
    }
    assertEquals(1, cont);
    verify(userService).findByFirstNameAndLastName(firstName, lastName);
  }

  @Test
  public void shouldReturnAllUserByFirstName() {
    String firstName = "Daniel";

    when(userService.findByFirstName(firstName)).thenAnswer(new Answer<Iterable<User>>() {
      public Iterable<User> answer(InvocationOnMock invocation) throws Throwable {
        List<User> list = new ArrayList<>();
        User u = new User();
        u.setFirstName(firstName);
        u.setLastName("Rosato");
        list.add(u);
        u = new User();
        u.setFirstName(firstName);
        u.setLastName("Doe");
        list.add(u);
        return list;
      }
    });

    SearchRequest searchRequest = new SearchRequest();
    searchRequest.setFirstName(firstName);
    Iterable<User> users = usersController.index(searchRequest);
    int cont = 0;
    for (User user : users) {
      assertEquals(firstName, user.getFirstName());
      cont++;
    }
    assertEquals(2, cont);
    verify(userService).findByFirstName(firstName);
  }

  @Test
  public void shouldReturnAllUserByLastName() {
    String lastName = "Rosato";

    when(userService.findByLastName(lastName)).thenAnswer(new Answer<Iterable<User>>() {
      public Iterable<User> answer(InvocationOnMock invocation) throws Throwable {
        List<User> list = new ArrayList<>();
        User u = new User();
        u.setFirstName("Daniel");
        u.setLastName(lastName);
        list.add(u);
        u = new User();
        u.setFirstName("John");
        u.setLastName(lastName);
        list.add(u);
        return list;
      }
    });

    SearchRequest searchRequest = new SearchRequest();
    searchRequest.setLastName(lastName);
    Iterable<User> users = usersController.index(searchRequest);
    int cont = 0;
    for (User user : users) {
      assertEquals(lastName, user.getLastName());
      cont++;
    }
    assertEquals(2, cont);
    verify(userService).findByLastName(lastName);
  }

  @Test
  public void shouldReturnUserById() {
    Long userId = 1L;
    String lastName = "Rosato";
    when(userService.findById(1L)).thenAnswer(new Answer<User>() {
      public User answer(InvocationOnMock invocation) throws Throwable {
        return buildUser();
      }
    });

    User user = usersController.show(userId);
    assertEquals(lastName, user.getLastName());
    verify(userService).findById(userId);
  }

  @Test
  public void shouldCreateUser() {
    CreateUserRequest req = new CreateUserRequest();

    req.setFirstName("Daniel");
    req.setLastName("Rosato");
    List<String> emails = Arrays.asList(new String[] { "test@test.com", "test2@test.com" });
    req.setEmails(emails);
    List<String> phones = Arrays.asList(new String[] { "123456789", "987654321" });
    req.setPhones(phones);

    when(userService.create(any(User.class))).thenAnswer(new Answer<User>() {
      public User answer(InvocationOnMock invocation) throws Throwable {
        User user = new User();
        user.setFirstName("Daniel");
        user.setLastName("Rosato");
        user.addUserEmail(new UserEmail("test@test.com"));
        user.addUserEmail(new UserEmail("test2@test.com"));
        user.addPhone(new Phone("123456789"));
        user.addPhone(new Phone("987654321"));
        return user;
      }
    });

    User user = usersController.create(req);
    assertEquals("Rosato", user.getLastName());
    assertEquals("test@test.com", user.getEmails().get(0).getEmail());
    assertEquals("test2@test.com", user.getEmails().get(1).getEmail());
    assertEquals("123456789", user.getPhones().get(0).getPhone());
    assertEquals("987654321", user.getPhones().get(1).getPhone());
  }

  @Test
  public void shouldThrowUserAlreadyExistsException() {
    CreateUserRequest req = new CreateUserRequest();

    String firstName = "Daniel";
    String lastName = "Rosato";
    req.setFirstName(firstName);
    req.setLastName(lastName);
    List<String> emails = Arrays.asList(new String[] { "test@test.com", "test2@test.com" });
    req.setEmails(emails);
    List<String> phones = Arrays.asList(new String[] { "123456789", "987654321" });
    req.setPhones(phones);

    when(userService.findByFirstNameAndLastName(firstName, lastName)).thenAnswer(new Answer<User>() {
      public User answer(InvocationOnMock invocation) throws Throwable {
        User u = new User();
        u.setFirstName(firstName);
        u.setLastName(lastName);
        return u;
      }
    });

    assertThrows(UserAlreadyExistsException.class, () -> {
      usersController.create(req);
    });
  }

  private User buildUser() {
    User user = new User();
    user.setFirstName("Daniel");
    user.setLastName("Rosato");
    user.addUserEmail(new UserEmail("email@test.com"));
    user.addPhone(new Phone("1234567890"));
    return user;
  }
}
