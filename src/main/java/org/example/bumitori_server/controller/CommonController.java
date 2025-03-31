package org.example.bumitori_server.controller;

import lombok.RequiredArgsConstructor;
import org.example.bumitori_server.dto.CheckInResponseDto;
import org.example.bumitori_server.service.CommonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.bumitori_server.service.CommonService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommonController {
    private final CommonService commonService;

    @GetMapping("/")
    List<CheckInResponseDto> getCheckInStatus() {
        return commonService.getCheckInStatus();
    }
}
