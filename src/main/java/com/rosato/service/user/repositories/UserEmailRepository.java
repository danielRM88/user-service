package com.rosato.service.user.repositories;

import com.rosato.service.user.models.UserEmail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEmailRepository extends JpaRepository<UserEmail, Long> {
  UserEmail findByEmail(String email);
}