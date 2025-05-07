package org.example.bumitori_server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.bumitori_server.dto.AbsentRequestDto;
import org.example.bumitori_server.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "학생 API", description = "학생용 입사 및 미입사 신청 기능")
@RestController
@RequiredArgsConstructor
public class StudentController {
  private final StudentService studentService;

  @Operation(summary = "RFID 입실 처리", description = "입실 처리 수행")
  @PostMapping("/checkin")
  public ResponseEntity<Void> checkIn(@RequestParam String rfid) {
    studentService.updateCheckInByRfid(rfid);
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "미입사 신청", description = "미입사 신청 등록")
  @PostMapping("/absent/request")
  public ResponseEntity<Map<String, String>> requestAbsent(
      @RequestBody AbsentRequestDto requestDto) {
    studentService.requestAbsent(requestDto);
    return ResponseEntity.ok(Map.of("message", "미입사 신청이 완료되었습니다"));
  }
}
