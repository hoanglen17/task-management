package com.taskmanagement.history;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
@Data
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idTask;
    private String descriptionTask;
    private String info;
    private LocalDateTime dateTime;
}
