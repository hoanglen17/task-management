package com.taskmanagement.tasks;

import com.taskmanagement.history.History;
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
    private String status;
    private Integer pointStatus;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private LocalDate startDate;
    private LocalDate endDate;
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Task parent;
    @ManyToOne
    @JoinColumn(name = "history_id")
    private History history;

    public void setPointStatus(String status){
        if(status.equals("TODO")){
            this.pointStatus = 1;
        }
        if(status.equals("IN_PROGRESS")){
            this.pointStatus = 2;
        }
        if(status.equals("DONE")){
            this.pointStatus = 3;
        }
    }
}
