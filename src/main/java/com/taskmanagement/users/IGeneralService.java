package com.taskmanagement.users;

import java.util.List;

public interface IGeneralService<User> {
    List<UserDto> findAll();
}
