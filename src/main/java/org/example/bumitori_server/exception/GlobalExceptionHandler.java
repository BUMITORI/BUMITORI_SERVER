package org.example.bumitori_server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex) {
        // 오류 발생 시 기본적인 상태 코드와 메시지 반환
        if (ex instanceof NullPointerException) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("잘못된 요청입니다: " + ex.getMessage());
        } else if (ex instanceof RuntimeException) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("런타임 오류가 발생했습니다: " + ex.getMessage());
        }

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("서버 오류가 발생했습니다: " + ex.getMessage());
    }
}
