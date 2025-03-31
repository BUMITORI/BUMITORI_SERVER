package org.example.bumitori_server.service;

import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.example.bumitori_server.dto.AbsentResponseDto;
import org.example.bumitori_server.entity.Absent;
import org.example.bumitori_server.entity.UserEntity;
import org.example.bumitori_server.repository.AbsentRepository;
import org.example.bumitori_server.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AbsentRepository absentRepository;
    private final UserRepository userRepository;

    public List<AbsentResponseDto> getAbsentRequests() {
        return absentRepository.findAll().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    public AbsentResponseDto getAbsentDetail(Long absentId) {
        Absent absent = absentRepository.findById(absentId)
            .orElseThrow(() -> new RuntimeException("미입사 신청을 찾을 수 없습니다."));
        return convertToDto(absent);
    }

    public String approveAbsent(Long absentId, Boolean approved) {
        Absent absent = absentRepository.findById(absentId)
            .orElseThrow(() -> new RuntimeException("미입사 신청을 찾을 수 없습니다."));

        absent.setApproval(approved);
        absentRepository.save(absent);

        UserEntity user = userRepository.findByEmail((absent.getEmail()))
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        return user.getName() + "님 미입사 신청이 " + "승인되었습니다.";
    }

    private AbsentResponseDto convertToDto(Absent absent) {
        UserEntity user = userRepository.findByEmail(absent.getEmail())
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        return new AbsentResponseDto(
            absent.getAbsentId(),
            user.getName(),
            user.getRoomId(),
            absent.getReason(),
            absent.getSpecificReason(),
            absent.getApproval(),
            absent.getAbsentDate()
        );
    }
}
