package com.rosato.service.user.services;

import java.util.List;

import com.rosato.service.user.models.User;
import com.rosato.service.user.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
  @Autowired
  private UserRepository userRepository;

  @Override
  public List<User> findAll() {
    return userRepository.findAll();
  }

  @Override
  public User findByFirstNameAndLastName(String firstName, String lastName) {
    return userRepository.findByFirstNameAndLastName(firstName, lastName);
  }

  @Override
  public User create(User user) {
    return userRepository.save(user);
  }

  @Override
  public User update(User user) {
    return userRepository.save(user);
  }

  @Override
  public void delete(User user) {
    userRepository.delete(user);
  }
}
