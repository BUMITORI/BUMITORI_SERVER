package org.example.bumitori_server.controller;

import lombok.RequiredArgsConstructor;
import org.example.bumitori_server.dto.AbsentRequestDto;
import org.example.bumitori_server.dto.CheckInResponseDto;
import org.example.bumitori_server.service.MainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MainController {
    private final MainService mainService;

    @GetMapping("/")
    List<CheckInResponseDto> getCheckInStatus() {
        return mainService.getCheckInStatus();
    }

    @PostMapping("/checkin")
    Long checkIn(@RequestParam String rfid){
        return mainService.getUserIdByRfid(rfid);
    }

    @GetMapping("/absent")
    String Absent() {
        return "absent page";
    }

    @PostMapping("/absent/request")
    public ResponseEntity<Map<String, String>> requestAbsent(@RequestBody AbsentRequestDto requestDto) {
        mainService.AbsentRequest(requestDto);

        Map<String, String> response = new HashMap<>();
        response.put("message", "미입사 신청이 완료되었습니다");

        return ResponseEntity.ok(response);
    }


}
