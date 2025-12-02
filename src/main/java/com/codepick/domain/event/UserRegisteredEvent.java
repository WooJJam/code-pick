package com.codepick.domain.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * 사용자 등록 완료 도메인 이벤트
 * 회원가입이 완료되었을 때 발행됨
 */
@Getter
@RequiredArgsConstructor
public class UserRegisteredEvent implements DomainEvent {

    private final Long userId;
    private final String username;
    private final String email;
    private final LocalDateTime occurredAt;

    @Override
    public LocalDateTime occurredAt() {
        return occurredAt;
    }

    @Override
    public String eventType() {
        return "USER_REGISTERED";
    }

    /**
     * 정적 팩토리 메서드
     */
    public static UserRegisteredEvent of(Long userId, String username, String email) {
        return new UserRegisteredEvent(userId, username, email, LocalDateTime.now());
    }
}
