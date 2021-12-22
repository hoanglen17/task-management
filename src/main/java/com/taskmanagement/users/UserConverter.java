package com.taskmanagement.users;

public class UserConverter {
    public static UserDto Converter(User user){
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getFirstName() + " " + user.getLastName());
        return dto;
    }
}
