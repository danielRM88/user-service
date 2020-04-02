package com.rosato.service.user.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.rosato.service.user.controllers.PhonesController.PhoneNotFoundException;
import com.rosato.service.user.models.Phone;
import com.rosato.service.user.repositories.PhoneRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
public class PhoneServiceTest {
  @Autowired
  private PhoneService phoneService;
  private PhoneRepository phoneRepository;

  @BeforeEach
  private void mockPhoneRepository() {
    phoneRepository = mock(PhoneRepository.class);
    ReflectionTestUtils.setField(phoneService, "phoneRepository", phoneRepository);
  }

  @Test
  public void shouldFindPhoneById() {
    Long phoneId = 1L;
    Phone phone = new Phone("123456789");
    when(phoneRepository.findById(phoneId)).thenAnswer(new Answer<Optional<Phone>>() {
      public Optional<Phone> answer(InvocationOnMock invocation) throws Throwable {
        return Optional.of(phone);
      }
    });

    Phone result = phoneService.findById(phoneId);
    assertEquals("123456789", result.getPhone());
    verify(phoneRepository).findById(phoneId);
  }

  @Test
  public void shouldThrowPhoneNotFoundException() {
    assertThrows(PhoneNotFoundException.class, () -> {
      phoneService.findById(-1L);
    });
  }

  @Test
  public void shouldUpdatePhone() {
    Phone phone = new Phone("123456789");
    when(phoneRepository.save(phone)).thenAnswer(new Answer<Phone>() {
      public Phone answer(InvocationOnMock invocation) throws Throwable {
        return phone;
      }
    });

    phoneService.update(phone);
    verify(phoneRepository).save(phone);
  }
}
