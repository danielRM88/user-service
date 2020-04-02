package com.rosato.service.user.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.rosato.service.user.models.Phone;
import com.rosato.service.user.models.User;
import com.rosato.service.user.models.UserEmail;
import com.rosato.service.user.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
public class UsersController {

  @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User already exists")
  public class UserAlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = 1L;
  }

  public static class CreateUserRequest {
    @NotBlank
    @Size(min = 1, max = 100)
    private String firstName;
    @NotBlank
    @Size(min = 1, max = 100)
    private String lastName;

    private List<String> emails;
    private List<String> phones;

    public String getFirstName() {
      return firstName;
    }

    public void setFirstName(String firstName) {
      this.firstName = firstName;
    }

    public String getLastName() {
      return lastName;
    }

    public void setLastName(String lastName) {
      this.lastName = lastName;
    }

    public List<String> getEmails() {
      return emails;
    }

    public void setEmails(List<String> emails) {
      this.emails = emails;
    }

    public List<String> getPhones() {
      return phones;
    }

    public void setPhones(List<String> phones) {
      this.phones = phones;
    }
  }

  public static class SearchRequest {
    private String firstName;
    private String lastName;

    public void setFirstName(String firstName) {
      this.firstName = firstName;
    }

    public void setLastName(String lastName) {
      this.lastName = lastName;
    }

    public String getFirstName() {
      return firstName;
    }

    public String getLastName() {
      return lastName;
    }
  }

  @Autowired
  private UserService userService;

  @GetMapping("")
  @ResponseBody
  public Iterable<User> index(@RequestBody(required = false) SearchRequest request) {
    Iterable<User> users;
    String searchFirstName = null;
    String searchLastName = null;
    if (request != null) {
      searchFirstName = request.getFirstName();
      searchLastName = request.getLastName();
    }

    if (searchFirstName != null && searchLastName != null) {
      List<User> list = new ArrayList<>();
      list.add(userService.findByFirstNameAndLastName(searchFirstName, searchLastName));
      users = list;
    } else if (searchFirstName != null) {
      users = userService.findByFirstName(searchFirstName);
    } else if (searchLastName != null) {
      users = userService.findByLastName(searchLastName);
    } else {
      users = userService.findAll();
    }

    return users;
  }

  @GetMapping("/{userId}")
  public User show(@PathVariable Long userId) {
    return userService.findById(userId);
  }

  @PostMapping("")
  public User create(@Valid @RequestBody CreateUserRequest request) {
    User user = new User();
    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    List<String> emails = request.getEmails();
    if (emails != null) {
      for (String email : emails) {
        UserEmail e = new UserEmail();
        e.setEmail(email);
        user.addUserEmail(e);
      }
    }
    List<String> phones = request.getPhones();
    if (phones != null) {
      for (String phone : phones) {
        Phone p = new Phone();
        p.setPhone(phone);
        user.addPhone(p);
      }
    }

    User u = userService.findByFirstNameAndLastName(user.getFirstName(), user.getLastName());
    if (u != null) {
      throw new UserAlreadyExistsException();
    }

    return userService.create(user);
  }
}
