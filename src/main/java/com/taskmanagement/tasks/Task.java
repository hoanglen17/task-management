package com.taskmanagement.tasks;

import com.taskmanagement.users.User;
import lombok.Data;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private Integer point;
    private String progress;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private LocalDate startDate;
    private LocalDate endDate;
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Task parent;
}
