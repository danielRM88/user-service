package com.rosato.service.user.services;

import com.rosato.service.user.models.UserEmail;

import org.springframework.stereotype.Service;

@Service
public interface UserEmailService {
  UserEmail findById(Long userEmailId);

  UserEmail update(UserEmail userEmail);
}