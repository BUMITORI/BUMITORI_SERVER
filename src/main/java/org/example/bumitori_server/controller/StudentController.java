package org.example.bumitori_server.controller;

import lombok.RequiredArgsConstructor;
import org.example.bumitori_server.dto.AbsentRequestDto;
import org.example.bumitori_server.dto.CheckInResponseDto;
import org.example.bumitori_server.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @PostMapping("/checkin")
    Long checkIn(@RequestParam String rfid) {
        return studentService.getEmailByRfid(rfid);
    }

    @GetMapping("/absent")
    String Absent() {
        return "absent page";
    }

    @PostMapping("/absent/request")
    public ResponseEntity<Map<String, String>> requestAbsent(@RequestBody AbsentRequestDto requestDto) {
        studentService.AbsentRequest(requestDto);
        return ResponseEntity.ok(Map.of("message", "미입사 신청이 완료되었습니다"));
    }
}
