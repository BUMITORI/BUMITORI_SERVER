package org.example.bumitori_server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class CheckIn {
    @Id
    private String rfid;

    private LocalDateTime enterTime;
    private String enterStatus;
}
