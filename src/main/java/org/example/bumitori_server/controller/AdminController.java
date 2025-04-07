package org.example.bumitori_server.controller;

import lombok.RequiredArgsConstructor;
import org.example.bumitori_server.dto.AbsentResponseDto;
import org.example.bumitori_server.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

  private final AdminService adminService;

  // 미입사 신청 목록 조회
  @GetMapping("/absent")
  public ResponseEntity<List<AbsentResponseDto>> getAbsentRequests() {
    List<AbsentResponseDto> response = adminService.getAbsentRequests();
    return ResponseEntity.ok(response);
  }

  // 미입사 신청 상세 조회
  @GetMapping("/absent/{absentId}")
  public ResponseEntity<AbsentResponseDto> getAbsentDetail(@PathVariable Long absentId) {
    AbsentResponseDto response = adminService.getAbsentDetail(absentId);
    return ResponseEntity.ok(response);
  }

  // 미입사 신청 승인 처리
  @PatchMapping("/absent/{absentId}")
  public ResponseEntity<Map<String, String>> approveAbsent(@PathVariable Long absentId) {
    String message = adminService.approveAbsent(absentId);
    return ResponseEntity.ok(Map.of("message", message));
  }
}
