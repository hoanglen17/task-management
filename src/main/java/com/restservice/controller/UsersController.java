package com.restservice.controller;

import com.restservice.users.IUserService;
import com.restservice.users.User;
import com.restservice.users.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UsersController {

    @Autowired
    private final IUserService userService;

    @GetMapping("/get")
    public ResponseEntity<Iterable<UserDto>> getAllUser() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }
}
