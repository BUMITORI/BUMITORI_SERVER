package org.example.bumitori_server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Absent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer studentId;
    private String reason;
    private String specificReason;

    @Column(columnDefinition = "BOOLEAN default false")
    private Boolean approval = false;

    private String adminName;
}
