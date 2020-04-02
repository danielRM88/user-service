package com.rosato.service.user.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.rosato.service.user.models.Phone;
import com.rosato.service.user.models.User;
import com.rosato.service.user.models.UserEmail;
import com.rosato.service.user.repositories.UserRepository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class UserServiceTest {
  @Autowired
  private UserService userService;
  private UserRepository userRepository;

  @BeforeAll
  public void mockUserRepository() {
    userRepository = mock(UserRepository.class);
    ReflectionTestUtils.setField(userService, "userRepository", userRepository);
  }

  @Test
  public void injectedComponentsAreNotNull() {
    assertNotNull(userService);
  }

  @Test
  public void shouldFindAllUsers() {
    when(userRepository.findAll()).thenAnswer(new Answer<List<User>>() {
      public List<User> answer(InvocationOnMock invocation) throws Throwable {
        List<User> list = new ArrayList<>();
        list.add(buildUser1());
        list.add(buildUser2());
        return list;
      }
    });
    List<User> users = userService.findAll();
    User user1 = users.get(0);
    User user2 = users.get(1);
    assertEquals("Rosato", user1.getLastName());
    assertEquals("Doe", user2.getLastName());
    verify(userRepository).findAll();
  }

  @Test
  public void shouldFindUserByFirstAndLastName() {
    when(userRepository.findByFirstNameAndLastName("Daniel", "Rosato")).thenAnswer(new Answer<User>() {
      public User answer(InvocationOnMock invocation) throws Throwable {
        return buildUser1();
      }
    });
    assertEquals("Rosato", userService.findByFirstNameAndLastName("Daniel", "Rosato").getLastName());
    verify(userRepository).findByFirstNameAndLastName("Daniel", "Rosato");
  }

  @Test
  public void shouldCreateUser() {
    User user = buildUser1();
    when(userRepository.save(user)).thenAnswer(new Answer<User>() {
      public User answer(InvocationOnMock invocation) throws Throwable {
        return user;
      }
    });
    assertEquals("Rosato", userService.create(user).getLastName());
    verify(userRepository).save(user);
  }

  @Test
  public void shouldUpdateUser() {
    User user = buildUser1();
    user.setLastName("NewLastName");
    when(userRepository.save(user)).thenAnswer(new Answer<User>() {
      public User answer(InvocationOnMock invocation) throws Throwable {
        return user;
      }
    });
    assertEquals("NewLastName", userService.update(user).getLastName());
    verify(userRepository).save(user);
  }

  @Test
  public void shouldDeleteUser() {
    User user = buildUser1();
    user.setLastName("NewLastName");
    doNothing().when(userRepository).delete(user);
    userService.delete(user);
    verify(userRepository).delete(user);
  }

  private User buildUser1() {
    User user = new User();
    UserEmail email = new UserEmail();
    email.setEmail("email@test.com");
    email.setUser(user);
    Phone phone = new Phone();
    phone.setPhone("1234567890");
    phone.setUser(user);
    user.setFirstName("Daniel");
    user.setLastName("Rosato");
    user.addUserEmail(email);
    user.addPhone(phone);
    return user;
  }

  private User buildUser2() {
    User user = new User();
    UserEmail email = new UserEmail();
    email.setEmail("email2@test.com");
    email.setUser(user);
    Phone phone = new Phone();
    phone.setPhone("0987654321");
    phone.setUser(user);
    user.setFirstName("John");
    user.setLastName("Doe");
    user.addUserEmail(email);
    user.addPhone(phone);
    return user;
  }
}
