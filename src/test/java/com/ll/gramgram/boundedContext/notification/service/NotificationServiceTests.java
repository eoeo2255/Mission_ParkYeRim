package com.ll.gramgram.boundedContext.notification.service;


import com.ll.gramgram.boundedContext.likeablePerson.service.LikeablePersonService;
import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.service.MemberService;
import com.ll.gramgram.boundedContext.notification.entity.Notification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
public class NotificationServiceTests {
    @Autowired
    private MemberService memberService;
    @Autowired
    private LikeablePersonService likeablePersonService;
    @Autowired
    private NotificationService notificationService;

    @Test
    @DisplayName("호감 표시를 하면 알림이 생성된다.")
    void t001() throws Exception {
        Member memberUser3 = memberService.findByUsername("user3").orElseThrow();
        Member memberUser5 = memberService.findByUsername("user5").orElseThrow();

        likeablePersonService.like(memberUser3, "insta_user5", 1);

        // user5 에게 전송된 알림들 모두 가져오기
        List<Notification> notifications = notificationService.findByToInstaMember(memberUser5.getInstaMember());

        // 그중 최신 알림 가져오기
        Notification lastNotification = notifications.get(notifications.size() - 1);

        assertThat(lastNotification.getFromInstaMember().getUsername()).isEqualTo("insta_user3");
        assertThat(lastNotification.getTypeCode()).isEqualTo("LIKE");
        assertThat(lastNotification.getNewAttractiveTypeCode()).isEqualTo(1);
    }

    @Test
    @DisplayName("호감사유를 수정하면 알림이 생성된다.")
    void t002() throws Exception {
        Member memberUser3 = memberService.findByUsername("user3").orElseThrow();
        Member memberUser4 = memberService.findByUsername("user4").orElseThrow();

        // 기존에 호감 표시 : 1
        // 바뀐 호감 표시 :  2
        likeablePersonService.modifyAttractive(memberUser3, "insta_user4", 2);

        // user4 에게 전송된 알림들 모두 가져오기
        List<Notification> notifications = notificationService.findByToInstaMember(memberUser4.getInstaMember());

        // 그중에 최신 알림 가져오기
        Notification lastNotification = notifications.get(notifications.size() - 1);

        // 보낸이의 인스타 아이디가 insta_user3 인지 체크
        assertThat(lastNotification.getFromInstaMember().getUsername()).isEqualTo("insta_user3");
        // 알림의 사유가 LIKE 인지 체크
        assertThat(lastNotification.getTypeCode()).isEqualTo("MODIFY_ATTRACTIVE_TYPE");
        // 알림내용 중에서 기존 호감사유코드가 1 인지 체크
        assertThat(lastNotification.getOldAttractiveTypeCode()).isEqualTo(1);
        // 알림내용 중에서 새 호감사유코드가 2 인지 체크
        assertThat(lastNotification.getNewAttractiveTypeCode()).isEqualTo(2);
    }
}