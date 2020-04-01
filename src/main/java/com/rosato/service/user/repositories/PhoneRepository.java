package com.rosato.service.user.repositories;

import com.rosato.service.user.models.Phone;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneRepository extends JpaRepository<Phone, Long> {
  Phone findByPhone(String phone);
}