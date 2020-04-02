package com.rosato.service.user.services;

import com.rosato.service.user.models.Phone;

import org.springframework.stereotype.Service;

@Service
public interface PhoneService {
  Phone findById(Long phoneId);

  Phone update(Phone phone);
}