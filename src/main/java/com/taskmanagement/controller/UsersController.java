package com.taskmanagement.controller;

import com.taskmanagement.users.IUserService;
import com.taskmanagement.users.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UsersController {
    private final IUserService userService;

    @GetMapping("/get")
    public ResponseEntity<Iterable<UserDto>> getAllUser() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }
}
