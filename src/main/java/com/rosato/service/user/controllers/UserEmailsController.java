package com.rosato.service.user.controllers;

import java.util.List;

import com.rosato.service.user.models.User;
import com.rosato.service.user.models.UserEmail;
import com.rosato.service.user.services.UserEmailService;
import com.rosato.service.user.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/v1/users/{userId}/emails")
@Api(value = "useremailservice", description = "Operations with the application's user's emails")
public class UserEmailsController {

  @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "user not found")
  public static class UserNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
  }

  @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "user not found")
  public static class UserEmailNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
  }

  @ResponseStatus(code = HttpStatus.NOT_MODIFIED, reason = "email(s) already added to the user")
  public static class EmailsAlreadyAddedException extends RuntimeException {
    private static final long serialVersionUID = 1L;
  }

  @Autowired
  private UserService userService;
  @Autowired
  private UserEmailService userEmailService;

  @PostMapping("")
  @ApiOperation(value = "Add one or more emails to a user", response = User.class)
  @ApiResponses(value = { @ApiResponse(code = 201, message = "Successfully created email(s)"),
      @ApiResponse(code = 304, message = "Email(s) already added"), @ApiResponse(code = 400, message = "Bad request"),
      @ApiResponse(code = 404, message = "User not found") })
  @ResponseStatus(HttpStatus.CREATED)
  public User create(@PathVariable Long userId, @RequestBody List<String> emails) {
    User user = userService.findById(userId);
    int added = 0;
    for (String email : emails) {
      UserEmail newEmail = new UserEmail(email);
      if (!user.getEmails().contains(newEmail)) {
        user.addUserEmail(newEmail);
        added++;
      }
    }

    if (added == 0) {
      throw new EmailsAlreadyAddedException();
    }

    return userService.update(user);
  }

  @PutMapping("/{userEmailId}")
  @ApiOperation(value = "Update user's phone", response = UserEmail.class)
  @ApiResponses(value = { @ApiResponse(code = 200, message = "Email successfully updated"),
      @ApiResponse(code = 404, message = "Email not found") })
  public UserEmail update(@PathVariable Long userEmailId, @RequestParam String email) {
    UserEmail userEmail = userEmailService.findById(userEmailId);
    userEmail.setEmail(email);
    return userEmailService.update(userEmail);
  }
}