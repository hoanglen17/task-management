package com.taskmanagement.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class UserService implements  IUserService{

    private final IUserRepo userRepo;

    @Autowired
    public UserService(IUserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public List<UserDto> findAll() {
        List<UserDto> listUserDto = new ArrayList<>();
        for (User user : userRepo.findAll()) {
            listUserDto.add(UserConverter.Converter(user));
        }
        return listUserDto;
    }

    public Optional<User> findUser(Long userId) {
        return userRepo.findById(userId);
    }
}
