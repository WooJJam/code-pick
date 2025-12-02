package com.codepick;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * CodePick Application - 코딩 테스트 학습 자동화 플랫폼
 *
 * Event-Driven Architecture 기반으로 구현
 * - 도메인 이벤트를 활용한 느슨한 결합
 * - 비동기 이벤트 처리를 통한 확장성 확보
 */
@SpringBootApplication
@EnableAsync // 이벤트 리스너 비동기 처리 활성화
public class CodePickApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodePickApplication.class, args);
	}

}
