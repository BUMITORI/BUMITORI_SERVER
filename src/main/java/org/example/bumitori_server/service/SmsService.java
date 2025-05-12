package org.example.bumitori_server.service;

import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.example.bumitori_server.enums.Reason;
import org.example.bumitori_server.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class SmsService {

  private final DefaultMessageService messageService;
  private final TeacherRepository teacherRepository;

  @Value("${sms.from}")
  private String smsFrom;
  @Value("${sms.to.men}")
  private String smsToMen;
  @Value("${sms.to.women}")
  private String smsToWomen;
  @Value("${sms.absent-url-prefix}")
  private String absentUrlPrefix;

  private static final DateTimeFormatter DATE_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");

  public SmsService(
      @Value("${sms.api.key}") String smsApiKey,
      @Value("${sms.api.secret}") String smsSecret,
      TeacherRepository teacherRepository
  ) {
    this.messageService = NurigoApp.INSTANCE.initialize(
        smsApiKey,
        smsSecret,
        "https://api.coolsms.co.kr"
    );
    this.teacherRepository = teacherRepository;
  }

  public void sendToAdmin(
      String name,
      int studentNo,
      LocalDate date,
      String roomId,
      long absentId,
      Reason reason
  ) {
    String formattedDate = date.format(DATE_FORMATTER);
    String adminNumber   = determineRecipientByRoom(roomId);
    String absentUrl     = absentUrlPrefix + absentId;
    String text = String.format(
        "%s 학생이 %s에 미입사를 신청했습니다.\n사유 : %s\n신청 확인하기\n%s",
        name, formattedDate, reason, absentUrl
    );

    sendSms(adminNumber, "미입사 신청 알림", text);

    GradeClass gc = parseGradeAndClass(studentNo);
    teacherRepository.findByGradeAndClassNum(gc.grade(), gc.classNum())
        .ifPresent(teacher -> sendSms(
            String.valueOf(teacher.getPhone()),
            "미입사 신청 알림 (담임)",
            text
        ));
  }

  private void sendSms(String to, String subject, String text) {
    Message msg = new Message();
    msg.setFrom(smsFrom);
    msg.setTo(to);
    msg.setSubject(subject);
    msg.setText(text);

    try {
      messageService.send(msg);
    } catch (NurigoMessageNotReceivedException e) {
      log.error("SMS 전송 실패 - 실패 목록: {}", e.getFailedMessageList(), e);
    } catch (Exception e) {
      log.error("SMS 전송 중 예외 발생", e);
    }
  }

  private GradeClass parseGradeAndClass(int studentNo) {
    int grade    = studentNo / 1000;
    int classNum = (studentNo % 1000) / 100;
    return new GradeClass(grade, classNum);
  }

  private record GradeClass(int grade, int classNum) {}

  private String determineRecipientByRoom(String roomId) {
    String prefix = roomId.substring(0, 2);
    return (prefix.equals("B3") || prefix.equals("A3")) ? smsToMen : smsToWomen;
  }
}
