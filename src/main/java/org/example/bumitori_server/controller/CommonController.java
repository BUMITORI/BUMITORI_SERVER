package org.example.bumitori_server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.bumitori_server.dto.CheckInResponseDto;
import org.example.bumitori_server.service.CommonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "공용 API", description = "공통 정보 제공")
@RestController
@RequiredArgsConstructor
public class CommonController {
  private final CommonService commonService;

  @Operation(summary = "전체 입실 현황 조회", description = "모든 사용자의 입실 여부 조회")
  @GetMapping("/")
  public List<CheckInResponseDto> getCheckInStatus() {
    return commonService.getCheckInStatus();
  }
}
