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
public class PhonesControllerTest {
  @Autowired
  private PhonesController phonesController;
  private UserService userService;

  @BeforeEach
  public void mockUserService() {
    userService = mock(UserService.class);
    ReflectionTestUtils.setField(phonesController, "userService", userService);
  }

  @Test
  public void shouldAddPhoneToUser() {
    String phone = "123456789";
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
        newUser.addPhone(new Phone(phone));
        return newUser;
      }
    });

    List<String> phones = new ArrayList<>();
    phones.add(phone);
    User user = phonesController.create(userId, phones);
    assertEquals(phone, user.getPhones().get(1).getPhone());
    verify(userService).findById(userId);
    verify(userService).update(oldUser);
  }

  @Test
  public void shouldAddMultiplePhones() {
    String phone1 = "123456789";
    String phone2 = "987654321";

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
        newUser.addPhone(new Phone(phone1));
        newUser.addPhone(new Phone(phone2));
        return newUser;
      }
    });

    List<String> phones = new ArrayList<>();
    phones.add(phone1);
    phones.add(phone2);
    User user = phonesController.create(userId, phones);
    assertEquals(phone1, user.getPhones().get(1).getPhone());
    assertEquals(phone2, user.getPhones().get(2).getPhone());
    assertEquals(3, user.getPhones().size());
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
