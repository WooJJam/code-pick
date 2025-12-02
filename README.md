# CodePick

> 코딩 테스트 학습 자동화 플랫폼

CodePick은 Solved.ac API와 Notion API를 활용하여 코딩 테스트 학습을 자동화하는 플랫폼입니다.
**이벤트 드리븐 아키텍처(Event-Driven Architecture)**를 기반으로 구현되어 확장성과 유지보수성을 갖춘 시스템입니다.

## 📋 주요 기능

- **문제 추천 시스템**: Solved.ac API를 활용한 맞춤형 문제 추천
- **회고 관리**: 체계적인 문제 풀이 회고 작성 및 관리
- **Notion 연동**: 작성한 회고를 Notion 데이터베이스에 자동 동기화
- **통계 대시보드**: 학습 현황 시각화 및 분석

## 🏗️ 아키텍처

### 이벤트 드리븐 아키텍처 (Event-Driven Architecture)

CodePick은 도메인 이벤트를 중심으로 설계되어 있습니다:

- **비동기 이벤트 처리**: `@EnableAsync`를 통한 비동기 이벤트 핸들링
- **느슨한 결합**: 도메인 이벤트를 통한 컴포넌트 간 통신
- **확장성**: 새로운 이벤트 핸들러 추가를 통한 기능 확장

#### 이벤트 흐름 예시
```
회고 작성 → ReviewCreatedEvent 발행 → Notion 자동 동기화 (비동기)
                                  → 통계 업데이트 (비동기)
                                  → 알림 발송 (비동기)
```

### 패키지 구조

```
com.codepick
├── domain
│   ├── event          # 도메인 이벤트
│   └── model          # 도메인 모델 (Entity)
├── event
│   ├── handler        # 이벤트 핸들러
│   └── publisher      # 이벤트 퍼블리셔
├── controller         # REST API 컨트롤러
├── service            # 비즈니스 로직
├── repository         # 데이터 접근 계층
├── dto                # 데이터 전송 객체
└── config             # 설정 클래스
```

## 🛠️ 기술 스택

### Backend
- **Java 17 (LTS)**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Spring Security**
- **Spring Events** (이벤트 드리븐 아키텍처)

### Database
- **H2** (개발 환경)
- **MySQL** (프로덕션 환경, 선택사항)

### Build Tool
- **Gradle 8.5** (Groovy)

### External APIs
- **Solved.ac API** - 코딩 테스트 문제 정보
- **Notion API** - 회고 자동 동기화

## 🚀 시작하기

### 사전 요구사항

- Java 17 이상
- (선택) MySQL 8.0 이상

### 설치 및 실행

1. **저장소 클론**
```bash
git clone https://github.com/your-username/codepick.git
cd codepick
```

2. **프로젝트 빌드**
```bash
./gradlew build
```

3. **애플리케이션 실행**
```bash
./gradlew bootRun
```

4. **H2 Console 접속** (개발 환경)
```
http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:codepick
Username: sa
Password: (비어있음)
```

### 환경 설정

`src/main/resources/application.yml` 파일을 수정하여 환경을 설정할 수 있습니다.

**MySQL 사용 시**:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/codepick
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: your-username
    password: your-password
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQLDialect
```

## 📖 API 문서

애플리케이션 실행 후 다음 경로에서 API 문서를 확인할 수 있습니다:
- Swagger UI: `http://localhost:8080/swagger-ui.html` (추후 추가 예정)

## 🧪 테스트

```bash
# 전체 테스트 실행
./gradlew test

# 특정 테스트 실행
./gradlew test --tests "com.codepick.*"
```

## 📝 개발 로드맵

자세한 개발 계획은 [TODO.md](./TODO.md)를 참고하세요.

- [x] Phase 0: 프로젝트 초기 세팅
- [ ] Phase 1: MVP - 문제 추천 시스템
- [ ] Phase 2: 회고 작성 시스템
- [ ] Phase 3: Notion 연동

## 🤝 기여하기

기여는 언제나 환영합니다! 다음 절차를 따라주세요:

1. 이 저장소를 Fork 합니다
2. Feature 브랜치를 생성합니다 (`git checkout -b feature/amazing-feature`)
3. 변경사항을 커밋합니다 (`git commit -m 'feat: add amazing feature'`)
4. 브랜치에 Push 합니다 (`git push origin feature/amazing-feature`)
5. Pull Request를 생성합니다

### 브랜치 전략

- `main`: 프로덕션 배포 브랜치
- `develop`: 개발 브랜치
- `feature/*`: 기능 개발 브랜치
- `bugfix/*`: 버그 수정 브랜치
- `hotfix/*`: 긴급 수정 브랜치

## 📄 라이선스

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details

## 👤 개발자

- **Woojjam** - [GitHub](https://github.com/woojjam)

---

**Made with ❤️ for better coding test preparation**
