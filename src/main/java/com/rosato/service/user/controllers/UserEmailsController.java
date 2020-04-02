package com.rosato.service.user.controllers;

import java.util.List;

import com.rosato.service.user.models.User;
import com.rosato.service.user.models.UserEmail;
import com.rosato.service.user.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users/{userId}/emails")
public class UserEmailsController {

  @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "user not found")
  public static class UserNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
  }

  @Autowired
  private UserService userService;

  @PostMapping("")
  public User create(@PathVariable Long userId, @RequestBody List<String> emails) {
    User user = userService.findById(userId);
    for (String email : emails) {
      UserEmail newEmail = new UserEmail(email);
      if (!user.getEmails().contains(newEmail)) {
        user.addUserEmail(newEmail);
      }
    }
    return userService.update(user);
  }
}