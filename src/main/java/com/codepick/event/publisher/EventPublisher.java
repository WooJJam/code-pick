package com.codepick.event.publisher;

import com.codepick.domain.event.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 도메인 이벤트 퍼블리셔
 * Spring의 ApplicationEventPublisher를 래핑하여 도메인 이벤트 발행
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 도메인 이벤트 발행
     *
     * @param event 발행할 도메인 이벤트
     */
    public void publish(DomainEvent event) {
        log.info("Publishing event: {} at {}", event.eventType(), event.occurredAt());
        applicationEventPublisher.publishEvent(event);
    }
}
