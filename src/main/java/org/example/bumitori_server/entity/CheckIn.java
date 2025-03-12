package org.example.bumitori_server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CheckIn {
    @Id
    private String rfid;

    private String enterTime;
    private Integer enterStatus;
}