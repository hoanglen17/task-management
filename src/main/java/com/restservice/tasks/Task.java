package com.restservice.tasks;

import com.restservice.users.User;
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
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long parentId;
}
