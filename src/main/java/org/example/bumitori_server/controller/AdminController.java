package org.example.bumitori_server.controller;

import lombok.RequiredArgsConstructor;
import org.example.bumitori_server.dto.AbsentRequestDto;
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

  @GetMapping("/")
  @ResponseBody
  public String v(){
    return "asfd";
  }

    @GetMapping("/absent")
    public ResponseEntity<List<AbsentResponseDto>> getAbsentRequests() {
        return ResponseEntity.ok(adminService.getAbsentRequests());
    }

    @GetMapping("/absent/{absentId}")
    public ResponseEntity<AbsentResponseDto> getAbsentDetail(@PathVariable Long absentId) {
        return ResponseEntity.ok(adminService.getAbsentDetail(absentId));
    }

    @PatchMapping("/absent/{absentId}")
    public ResponseEntity<Map<String, String>> approveAbsent(
            @PathVariable Long absentId, @RequestBody AbsentRequestDto requestDto) {
        String message = adminService.approveAbsent(absentId, requestDto.getApproved(), requestDto.getName());
        return ResponseEntity.ok(Map.of("message", message));
    }
}
