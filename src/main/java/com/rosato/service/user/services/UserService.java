package com.rosato.service.user.services;

import java.util.List;

import com.rosato.service.user.models.User;

import org.springframework.stereotype.Service;

@Service
public interface UserService {
  List<User> findAll();

  User findById(Long userId);

  Iterable<User> findByFirstName(String firstName);

  Iterable<User> findByLastName(String lastName);

  User findByFirstNameAndLastName(String firstName, String lastName);

  User create(User user);

  User update(User user);

  void delete(User user);
}
