package com.codepick.event.handler;

import com.codepick.domain.event.UserRegisteredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 사용자 관련 도메인 이벤트 핸들러
 * 이벤트를 비동기로 처리하여 메인 로직과 분리
 */
@Slf4j
@Component
public class UserEventHandler {

    /**
     * 사용자 등록 이벤트 처리
     * 예: 환영 이메일 발송, 통계 업데이트 등
     *
     * @param event 사용자 등록 이벤트
     */
    @Async
    @EventListener
    public void handleUserRegistered(UserRegisteredEvent event) {
        log.info("사용자 등록 이벤트 처리 시작 - userId: {}, username: {}, email: {}",
                event.getUserId(), event.getUsername(), event.getEmail());

        // TODO: 환영 이메일 발송
        // TODO: 사용자 통계 업데이트
        // TODO: 초기 설정 데이터 생성

        log.info("사용자 등록 이벤트 처리 완료 - userId: {}", event.getUserId());
    }
}
