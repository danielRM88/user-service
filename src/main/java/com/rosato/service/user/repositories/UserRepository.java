package com.rosato.service.user.repositories;

import com.rosato.service.user.models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Iterable<User> findByFirstName(String firstName);

  Iterable<User> findByLastName(String lastName);

  User findByFirstNameAndLastName(String firstName, String lastName);
}