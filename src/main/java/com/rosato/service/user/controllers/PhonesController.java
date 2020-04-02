package com.rosato.service.user.controllers;

import java.util.List;

import com.rosato.service.user.models.Phone;
import com.rosato.service.user.models.User;
import com.rosato.service.user.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users/{userId}/phones")
public class PhonesController {
  @Autowired
  private UserService userService;

  @PostMapping("")
  public User create(@PathVariable Long userId, @RequestBody List<String> phones) {
    User user = userService.findById(userId);
    for (String phone : phones) {
      Phone newPhone = new Phone(phone);
      if (!user.getPhones().contains(newPhone)) {
        user.addPhone(newPhone);
      }
    }
    return userService.update(user);
  }
}
