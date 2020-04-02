package com.rosato.service.user.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.rosato.service.user.controllers.UserEmailsController.UserEmailNotFoundException;
import com.rosato.service.user.models.UserEmail;
import com.rosato.service.user.repositories.UserEmailRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
public class UserEmailServiceTest {
  @Autowired
  private UserEmailService userEmailService;
  private UserEmailRepository userEmailRepository;

  @BeforeEach
  private void mockUserEmailRepository() {
    userEmailRepository = mock(UserEmailRepository.class);
    ReflectionTestUtils.setField(userEmailService, "userEmailRepository", userEmailRepository);
  }

  @Test
  public void shouldFindUserEmailById() {
    Long userEmailId = 1L;
    UserEmail userEmail = new UserEmail("email@email.com");
    when(userEmailRepository.findById(userEmailId)).thenAnswer(new Answer<Optional<UserEmail>>() {
      public Optional<UserEmail> answer(InvocationOnMock invocation) throws Throwable {
        return Optional.of(userEmail);
      }
    });

    UserEmail result = userEmailService.findById(userEmailId);
    assertEquals("email@email.com", result.getEmail());
    verify(userEmailRepository).findById(userEmailId);
  }

  @Test
  public void shouldThrowUserEmailNotFoundException() {
    assertThrows(UserEmailNotFoundException.class, () -> {
      userEmailService.findById(-1L);
    });
  }

  @Test
  public void shouldUpdateUserEmail() {
    UserEmail userEmail = new UserEmail("test@test.com");
    when(userEmailRepository.save(userEmail)).thenAnswer(new Answer<UserEmail>() {
      public UserEmail answer(InvocationOnMock invocation) throws Throwable {
        return userEmail;
      }
    });

    userEmailService.update(userEmail);
    verify(userEmailRepository).save(userEmail);
  }
}