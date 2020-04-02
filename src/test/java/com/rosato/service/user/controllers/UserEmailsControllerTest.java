package com.rosato.service.user.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

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
public class UserEmailsControllerTest {
  @Autowired
  private UserEmailsController userEmailsController;
  private UserService userService;

  @BeforeEach
  public void mockUserService() {
    userService = mock(UserService.class);
    ReflectionTestUtils.setField(userEmailsController, "userService", userService);
  }

  @Test
  public void shouldAddEmailToUser() {
    String email = "test@test.com";
    Long userId = 1L;

    User oldUser = buildUser();
    when(userService.findById(userId)).thenAnswer(new Answer<User>() {
      public User answer(InvocationOnMock invocation) throws Throwable {
        return oldUser;
      }
    });

    when(userService.update(oldUser)).thenAnswer(new Answer<User>() {
      public User answer(InvocationOnMock invocation) throws Throwable {
        User newUser = buildUser();
        newUser.addUserEmail(new UserEmail("test@test.com"));
        return newUser;
      }
    });

    List<String> emails = new ArrayList<>();
    emails.add(email);
    User user = userEmailsController.create(userId, emails);
    assertEquals(email, user.getEmails().get(1).getEmail());
    verify(userService).findById(userId);
    verify(userService).update(oldUser);
  }

  @Test
  public void shouldAddAllEmails() {
    String email = "test@test.com";
    String email2 = "test2@test.com";
    Long userId = 1L;

    User oldUser = buildUser();
    when(userService.findById(userId)).thenAnswer(new Answer<User>() {
      public User answer(InvocationOnMock invocation) throws Throwable {
        return oldUser;
      }
    });

    when(userService.update(oldUser)).thenAnswer(new Answer<User>() {
      public User answer(InvocationOnMock invocation) throws Throwable {
        User newUser = buildUser();
        newUser.addUserEmail(new UserEmail(email));
        newUser.addUserEmail(new UserEmail(email2));
        return newUser;
      }
    });

    List<String> emails = new ArrayList<>();
    emails.add(email);
    emails.add(email2);
    User user = userEmailsController.create(userId, emails);
    assertEquals(email, user.getEmails().get(1).getEmail());
    assertEquals(email2, user.getEmails().get(2).getEmail());
    assertEquals(3, user.getEmails().size());
    verify(userService).findById(userId);
    verify(userService).update(oldUser);
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
