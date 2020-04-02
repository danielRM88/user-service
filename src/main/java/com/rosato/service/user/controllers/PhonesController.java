package com.rosato.service.user.controllers;

import java.util.List;

import com.rosato.service.user.models.Phone;
import com.rosato.service.user.models.User;
import com.rosato.service.user.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/v1/users/{userId}/phones")
@Api(value = "useremailservice", description = "Operations with the application's user's phones")
public class PhonesController {
  @Autowired
  private UserService userService;

  @ResponseStatus(code = HttpStatus.NOT_MODIFIED, reason = "phone(s) already added to the user")
  public static class PhonesAlreadyAddedException extends RuntimeException {
    private static final long serialVersionUID = 1L;
  }

  @PostMapping("")
  @ApiOperation(value = "Add one or more phones to a user", response = User.class)
  @ApiResponses(value = { @ApiResponse(code = 201, message = "Successfully created phone(s)"),
      @ApiResponse(code = 304, message = "Phone(s) already added"), @ApiResponse(code = 400, message = "Bad request"),
      @ApiResponse(code = 404, message = "User not found") })
  @ResponseStatus(HttpStatus.CREATED)
  public User create(@PathVariable Long userId, @RequestBody List<String> phones) {
    User user = userService.findById(userId);
    int added = 0;
    for (String phone : phones) {
      Phone newPhone = new Phone(phone);
      if (!user.getPhones().contains(newPhone)) {
        user.addPhone(newPhone);
        added++;
      }
    }

    if (added == 0) {
      throw new PhonesAlreadyAddedException();
    }

    return userService.update(user);
  }
}
