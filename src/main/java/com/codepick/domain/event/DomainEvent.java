package com.codepick.domain.event;

import java.time.LocalDateTime;

/**
 * 도메인 이벤트 기본 인터페이스
 * 모든 도메인 이벤트는 이 인터페이스를 구현해야 함
 */
public interface DomainEvent {

    /**
     * 이벤트 발생 시각
     */
    LocalDateTime occurredAt();

    /**
     * 이벤트 타입
     */
    String eventType();
}
