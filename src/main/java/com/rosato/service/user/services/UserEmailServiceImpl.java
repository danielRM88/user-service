package com.rosato.service.user.services;

import java.util.Optional;

import com.rosato.service.user.controllers.UserEmailsController.UserEmailNotFoundException;
import com.rosato.service.user.models.UserEmail;
import com.rosato.service.user.repositories.UserEmailRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserEmailServiceImpl implements UserEmailService {
  @Autowired
  private UserEmailRepository userEmailRepository;

  @Override
  public UserEmail findById(Long userEmailId) {
    Optional<UserEmail> result = userEmailRepository.findById(userEmailId);
    if (!result.isPresent()) {
      throw new UserEmailNotFoundException();
    }
    return result.get();
  }

  @Override
  public UserEmail update(UserEmail userEmail) {
    return userEmailRepository.save(userEmail);
  }
}