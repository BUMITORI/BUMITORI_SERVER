package org.example.bumitori_server.controller;

import lombok.RequiredArgsConstructor;
import org.example.bumitori_server.dto.AbsentRequestDto;
import org.example.bumitori_server.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @PostMapping("/checkin")
    Long checkIn(@RequestParam String rfid) {
        return studentService.updateCheckInByRfid(rfid);
    }

    @PostMapping("/absent/request")
    public ResponseEntity<Map<String, String>> requestAbsent(@RequestBody AbsentRequestDto requestDto) {
        studentService.AbsentRequest(requestDto);
        return ResponseEntity.ok(Map.of("message", "미입사 신청이 완료되었습니다"));
    }
}
