package com.rosato.service.user.services;

import java.util.Optional;

import com.rosato.service.user.controllers.PhonesController.PhoneNotFoundException;
import com.rosato.service.user.models.Phone;
import com.rosato.service.user.repositories.PhoneRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhoneServiceImpl implements PhoneService {
  @Autowired
  private PhoneRepository phoneRepository;

  @Override
  public Phone findById(Long phoneId) {
    Optional<Phone> result = phoneRepository.findById(phoneId);
    if (!result.isPresent()) {
      throw new PhoneNotFoundException();
    }
    return result.get();
  }

  @Override
  public Phone update(Phone phone) {
    return phoneRepository.save(phone);
  }
}
