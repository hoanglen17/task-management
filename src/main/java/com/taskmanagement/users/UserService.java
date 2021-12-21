package com.taskmanagement.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserService implements  IUserService{

    private final IUserRepo userRepo;

    @Autowired
    public UserService(IUserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public List<UserDto> findAll() {
        List<UserDto> userDtos = new ArrayList<>();

        for (User user : userRepo.findAll()) {
            UserDto dto = new UserDto();
            dto.setId(user.getId());
            dto.setName(user.getFirstName() + " " + user.getLastName());
            userDtos.add(dto);
        }

        return userDtos;
    }
}
