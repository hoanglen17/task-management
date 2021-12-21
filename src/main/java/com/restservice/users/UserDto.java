package com.restservice.users;


import com.restservice.tasks.Task;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private Long Id;
    private String name;
}
