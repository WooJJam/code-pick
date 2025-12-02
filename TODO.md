# CodePick 개발 TODO

> 코딩 테스트 학습 자동화 플랫폼 개발 태스크 리스트

---

## 📋 프로젝트 개요

- **목표**: Solved.ac API와 Notion API를 활용한 코딩 테스트 학습 자동화 플랫폼
- **핵심 가치**: 문제 선정 자동화, 체계적인 회고 관리, Notion 연동
- **아키텍처**: 이벤트 드리븐 아키텍처 (Event-Driven Architecture)
  - 비동기 이벤트 기반 처리를 통한 시스템 확장성 및 유연성 확보
  - 도메인 이벤트를 활용한 느슨한 결합(Loose Coupling) 구현

---

## 🚀 Phase 1: MVP - 문제 추천 시스템 (1-2개월)

### 1. 프로젝트 초기 세팅

- [x] GitHub 저장소 생성 및 초기화
- [x] Spring Boot 3.x 프로젝트 생성 (Java 17+)
- [x] 프로젝트 기본 구조 설계
  - [x] 패키지 구조 설계 (controller, service, repository, domain, dto, event)
  - [x] 이벤트 드리븐 아키텍처 기본 구조 설계
    - [x] 도메인 이벤트 패키지 구성 (domain/event)
    - [x] 이벤트 핸들러 패키지 구성 (event/handler)
    - [x] 이벤트 퍼블리셔 설정
  - [x] application.yml 설정 파일 구성
- [ ] MySQL 데이터베이스 설정 (선택사항)
  - [ ] 로컬 DB 설치 및 연결
  - [ ] JPA 설정 및 연결 테스트
- [x] Git 브랜치 전략 수립 (main, develop, feature/*)
- [x] README.md 작성 (프로젝트 설명, 설치 방법)

### 2. 개발 환경 및 도구 설정

- [x] 필수 의존성 추가
  - [x] Spring Boot Starter Web
  - [x] Spring Boot Starter Data JPA
  - [x] ~~Spring Boot Starter Security~~ (제거됨 - 사용하지 않음)
  - [x] MySQL Driver (선택사항)
  - [x] Lombok
  - [x] Spring Boot Starter Validation
  - [x] Spring Events (이벤트 드리븐 아키텍처)
  - [ ] Spring Boot Starter AMQP (메시지 큐 - RabbitMQ, 선택사항)
  - [ ] Spring Kafka (이벤트 스트리밍, 선택사항)
- [x] 코드 품질 도구 설정
  - [x] Checkstyle 설정 (Google Java Style Guide 기반)
  - [x] CodeRabbit 설정 (.coderabbit.yaml)
  - [ ] 테스트 환경 설정 (JUnit 5, Mockito)
- [x] 로깅 설정 (Logback)
- [x] 이벤트 드리븐 아키텍처 기반 설정
  - [x] Spring Events 설정
  - [x] 이벤트 리스너 비동기 처리 설정 (@EnableAsync)
  - [x] 이벤트 퍼블리셔 빈 구성
  - [x] 도메인 이벤트 기본 구조 작성

### 3. 외부 API 연동

- [ ] Solved.ac API 연동
  - [ ] Solved.ac API 문서 리뷰 및 분석
  - [ ] RestTemplate 또는 WebClient 설정
  - [ ] API 호출 테스트 코드 작성
  - [ ] 문제 검색 API 연동 구현
  - [ ] Rate Limit 처리 로직 구현
  - [ ] API 응답 DTO 작성

### 4. 사용자 인증 시스템 (Spring Security 미사용)

- [ ] 사용자 Entity 설계 및 생성
  - [ ] User 엔티티 (id, username, email, solvedacHandle)
  - [ ] 테이블 스키마 설계 및 생성
- [ ] ~~Spring Security 설정~~ (사용하지 않음)
  - [ ] ~~SecurityConfig 클래스 작성~~
  - [ ] ~~JWT 인증 구현~~
  - [ ] ~~Password Encoder 설정 (BCrypt)~~
- [ ] 회원가입 기능 (간소화)
  - [ ] 회원가입 API 구현 (/api/auth/signup)
  - [ ] 입력 검증 (Validation)
  - [ ] 중복 이메일/사용자명 체크
- [ ] 로그인 기능 (간소화)
  - [ ] 로그인 API 구현 (/api/auth/login)
  - [ ] 세션 기반 인증 구현
- [ ] Solved.ac 계정 연동
  - [ ] Solved.ac 핸들 저장 기능
  - [ ] Solved.ac API로 사용자 정보 조회 기능

### 5. 문제 검색 및 필터링 시스템

- [ ] Problem Entity 설계 및 생성
  - [ ] Problem 엔티티 (id, problemNumber, title, difficulty, tags, acceptedUserCount)
  - [ ] 테이블 스키마 설계 및 생성
  - [ ] 인덱스 설정 (problemNumber, difficulty, tags)
- [ ] 문제 검색 API 구현
  - [ ] 다중 필터 검색 API (/api/problems/search)
    - [ ] 난이도 필터 (Bronze ~ Ruby)
    - [ ] 푼 사람 수 범위 필터
    - [ ] 알고리즘 태그 다중 선택 필터
    - [ ] 풀이 여부 필터 (내가 푼/안 푼 문제)
  - [ ] QueryDSL 또는 JPA Specification 설정
  - [ ] 페이지네이션 구현
- [ ] 문제 캐싱 시스템
  - [ ] Redis 설정 및 연동
  - [ ] Solved.ac에서 조회한 문제 정보 캐싱 (TTL: 24시간)
  - [ ] 캐시 히트/미스 로직 구현

### 6. 랜덤 문제 추출 기능

- [ ] 랜덤 문제 추출 API 구현 (/api/problems/random)
  - [ ] 필터 조건에 맞는 문제 목록 조회
  - [ ] 원하는 개수만큼 랜덤 추출 로직
  - [ ] 중복 제거 처리
- [ ] UserProblem Entity 설계
  - [ ] UserProblem 엔티티 (id, userId, problemId, status, pickedAt)
  - [ ] 상태 관리 (PICKED, IN_PROGRESS, SOLVED, RETRY)
- [ ] 추출된 문제 저장 기능
  - [ ] 사용자별 추출 이력 저장

### 7. 문제 목록 표시 기능

- [ ] 문제 목록 조회 API 구현
  - [ ] 내가 선택한 문제 목록 조회 (/api/user-problems)
  - [ ] 문제 상세 정보 API (/api/problems/{id})
  - [ ] 백준 링크 제공
- [ ] 문제 정보 표시 항목
  - [ ] 문제 번호, 제목, 난이도, 태그
  - [ ] 푼 사람 수, 평균 시도 횟수

### 8. 프론트엔드 개발 (MVP)

- [ ] 기술 스택 선택 (React 또는 Thymeleaf)
- [ ] 기본 레이아웃 구성
  - [ ] 헤더, 네비게이션, 푸터
- [ ] 페이지 구현
  - [ ] 로그인/회원가입 페이지
  - [ ] 문제 검색 페이지
    - [ ] 필터 UI (난이도, 태그, 푼 사람 수, 개수 선택)
    - [ ] 검색 버튼 및 랜덤 추출 버튼
  - [ ] 문제 목록 페이지
    - [ ] 테이블 형태로 문제 정보 표시
    - [ ] 백준 링크 연결
- [ ] API 연동
  - [ ] Axios 또는 Fetch API 설정
  - [ ] 인증 토큰 관리 (LocalStorage/SessionStorage)

### 9. 테스트 및 배포 준비

- [ ] 단위 테스트 작성
  - [ ] Service 레이어 테스트
  - [ ] Repository 테스트
- [ ] 통합 테스트 작성
  - [ ] API 엔드포인트 테스트
- [ ] 보안 점검
  - [ ] SQL Injection 방지 확인
  - [ ] XSS 방지 확인
  - [ ] HTTPS 적용 계획
- [ ] 성능 테스트
  - [ ] API 응답 시간 측정
  - [ ] 데이터베이스 쿼리 최적화
- [ ] 배포 환경 준비
  - [ ] 서버 선택 (AWS, GCP, Heroku 등)
  - [ ] CI/CD 파이프라인 구축 (GitHub Actions)
  - [ ] 환경 변수 관리 (.env)

---

## 📝 Phase 2: 회고 작성 시스템 (2-3개월)

### 1. 데이터 모델 확장

- [ ] Review Entity 설계 및 생성
  - [ ] Review 엔티티 (id, userProblemId, content, approach, timeComplexity, spaceComplexity, difficulties, learnings, improvements, codeSnippet, solvingTime)
  - [ ] 테이블 스키마 설계 및 생성
  - [ ] UserProblem과의 연관관계 설정 (1:N)

### 2. 회고 작성 기능

- [ ] 회고 작성 API 구현
  - [ ] 회고 생성 API (/api/reviews)
  - [ ] 회고 수정 API (/api/reviews/{id})
  - [ ] 회고 삭제 API (/api/reviews/{id})
  - [ ] 회고 관련 도메인 이벤트 발행
    - [ ] ReviewCreatedEvent (회고 작성 완료 이벤트)
    - [ ] ReviewUpdatedEvent (회고 수정 완료 이벤트)
    - [ ] ReviewDeletedEvent (회고 삭제 완료 이벤트)
- [ ] Markdown 에디터 통합
  - [ ] 프론트엔드 Markdown 에디터 라이브러리 선택 (react-markdown-editor, Toast UI Editor 등)
  - [ ] Markdown 렌더링 기능
- [ ] 회고 템플릿 제공
  - [ ] 기본 템플릿 작성 (접근 방법, 복잡도 분석, 어려웠던 점, 배운 점, 개선 방안)
  - [ ] 문제 정보 자동 표시
- [ ] 코드 스니펫 저장
  - [ ] 코드 하이라이팅 기능
  - [ ] 언어 선택 기능 (Java, Python, C++, JavaScript 등)

### 3. 문제 상태 관리

- [ ] 문제 상태 업데이트 API
  - [ ] 상태 변경 API (/api/user-problems/{id}/status)
  - [ ] 상태 타입: PICKED, IN_PROGRESS, SOLVED, RETRY
- [ ] 상태별 필터링 기능
  - [ ] 진행 중인 문제 조회
  - [ ] 완료한 문제 조회
  - [ ] 재시도 필요한 문제 조회

### 4. 회고 검색 및 필터링

- [ ] 회고 검색 API 구현
  - [ ] 날짜별 검색
  - [ ] 난이도별 검색
  - [ ] 태그별 검색
  - [ ] 키워드 검색 (제목, 내용)
- [ ] 정렬 기능
  - [ ] 최신순, 오래된순
  - [ ] 난이도순

### 5. 통계 대시보드

- [ ] 통계 조회 API 구현
  - [ ] 해결한 문제 수 조회
  - [ ] 난이도별 분포 조회
  - [ ] 태그별 분포 조회
  - [ ] 학습 스트릭 계산 (연속 학습 일수)
  - [ ] 평균 소요 시간 분석
- [ ] 대시보드 UI 구현
  - [ ] 차트 라이브러리 통합 (Chart.js, Recharts 등)
  - [ ] 통계 시각화 (파이 차트, 바 차트, 라인 차트)
  - [ ] 학습 캘린더 뷰

### 6. 프론트엔드 확장

- [ ] 회고 작성 페이지
  - [ ] Markdown 에디터 UI
  - [ ] 자동 저장 기능
  - [ ] 미리보기 기능
- [ ] 회고 목록 페이지
  - [ ] 필터 및 검색 UI
  - [ ] 페이지네이션
- [ ] 회고 상세 페이지
  - [ ] Markdown 렌더링
  - [ ] 수정/삭제 버튼
- [ ] 통계 대시보드 페이지

### 7. 테스트 및 개선

- [ ] 회고 관련 테스트 작성
- [ ] 성능 최적화
  - [ ] N+1 쿼리 문제 해결
  - [ ] Lazy Loading 적용
- [ ] UX 개선
  - [ ] 로딩 상태 표시
  - [ ] 에러 처리 및 사용자 피드백

---

## 🔗 Phase 3: Notion 연동 (1-2개월)

### 1. Notion API 연동 준비

- [ ] Notion API 문서 리뷰
- [ ] Notion Integration 생성
- [ ] OAuth 2.0 인증 플로우 구현
  - [ ] Notion OAuth URL 생성
  - [ ] Callback URL 처리
  - [ ] Access Token 발급 및 저장

### 2. Notion 연동 설정

- [ ] User Entity 확장
  - [ ] notionToken (AES-256 암호화), notionDatabaseId 필드 추가
- [ ] Notion 연동 API 구현
  - [ ] Notion 인증 시작 API (/api/notion/auth)
  - [ ] Notion OAuth Callback API (/api/notion/callback)
  - [ ] Workspace 및 Database 목록 조회 API
  - [ ] 목표 Database 선택 및 저장 API
- [ ] 보안 강화
  - [ ] Notion Token 암호화 저장
  - [ ] Token 갱신 로직 구현

### 3. 자동 동기화 기능

- [ ] Review Entity 확장
  - [ ] notionPageId, syncedAt 필드 추가
- [ ] Notion 동기화 API 구현
  - [ ] CodePick → Notion 동기화 API (/api/notion/sync)
  - [ ] Notion Page 생성 로직
  - [ ] Notion Page 업데이트 로직
  - [ ] 동기화 상태 관리
- [ ] 이벤트 기반 자동 동기화 구현
  - [ ] ReviewCreatedEvent 핸들러 → Notion 페이지 자동 생성
  - [ ] ReviewUpdatedEvent 핸들러 → Notion 페이지 자동 업데이트
  - [ ] 이벤트 리스너 비동기 처리 (@Async)
  - [ ] 동기화 실패 시 재시도 로직 (이벤트 재발행)
- [ ] 자동 동기화 트리거
  - [ ] 회고 작성/수정 시 자동 동기화 옵션
  - [ ] 배치 작업으로 정기 동기화 (선택적)

### 4. 템플릿 설정 기능

- [ ] Notion 페이지 템플릿 관리
  - [ ] 기본 템플릿 제공
  - [ ] 사용자 정의 템플릿 설정 UI
  - [ ] 동기화 항목 선택 (전체/일부)
- [ ] 템플릿 미리보기 기능

### 5. 동기화 옵션 및 이력

- [ ] 동기화 설정 API
  - [ ] 자동/수동 동기화 선택
  - [ ] 동기화 빈도 설정 (즉시, 일일, 주간)
- [ ] 동기화 이력 조회
  - [ ] SyncHistory Entity 생성
  - [ ] 동기화 성공/실패 로그 저장
  - [ ] 동기화 이력 조회 API
- [ ] 에러 처리
  - [ ] Notion API 실패 시 재시도 로직
  - [ ] 사용자 알림 기능

### 6. 프론트엔드 개발

- [ ] Notion 연동 설정 페이지
  - [ ] Notion 인증 버튼
  - [ ] Database 선택 UI
  - [ ] 템플릿 설정 UI
- [ ] 동기화 상태 표시
  - [ ] 실시간 동기화 상태 표시
  - [ ] 동기화 이력 목록
- [ ] 설정 페이지
  - [ ] 자동/수동 동기화 토글
  - [ ] 동기화 빈도 선택

### 7. 테스트 및 모니터링

- [ ] Notion 연동 테스트
  - [ ] OAuth 플로우 테스트
  - [ ] 동기화 기능 테스트
- [ ] 성공률 모니터링
  - [ ] 동기화 성공률 95% 이상 목표
  - [ ] 실패 케이스 분석 및 개선
- [ ] 에러 알림 시스템 구축

---

## 🔧 기술적 개선 사항 (지속적)

### 보안

- [ ] HTTPS 적용 (Let's Encrypt)
- [ ] SQL Injection 방지 재점검
- [ ] XSS 방지 재점검
- [ ] CSRF 토큰 설정
- [ ] 정기 보안 감사

### 성능 최적화

- [ ] 데이터베이스 쿼리 최적화
- [ ] 인덱스 추가 및 튜닝
- [ ] Redis 캐싱 확대 적용
- [ ] API 응답 시간 모니터링 및 개선

### 모니터링 및 로깅

- [ ] 로그 레벨 최적화
- [ ] API 호출 성공/실패율 모니터링
- [ ] 사용자 행동 분석 시스템 구축
- [ ] 에러 추적 시스템 (Sentry 등)
- [ ] 이벤트 드리븐 아키텍처 모니터링
  - [ ] 이벤트 발행/구독 추적
  - [ ] 이벤트 처리 시간 모니터링
  - [ ] 실패한 이벤트 추적 및 재처리

### 문서화

- [ ] API 문서 작성 (Swagger/OpenAPI)
- [ ] 사용자 가이드 작성
- [ ] 개발자 문서 작성
- [ ] 기여 가이드 작성 (오픈소스 공개 시)

---

## 🚀 향후 확장 기능 (백로그)

### 단기 확장 (6개월 내)

- [ ] 스터디 그룹 기능
  - [ ] 그룹 생성 및 관리
  - [ ] 팀원 초대
  - [ ] 문제 공유
  - [ ] 공동 회고
- [ ] 타이머 기능
  - [ ] 문제 풀이 시간 측정
  - [ ] 시간 기록 저장
  - [ ] 평균 시간 분석
- [ ] 문제 북마크 기능
  - [ ] 나중에 풀 문제 저장
  - [ ] 북마크 관리

### 중기 확장 (1년 내)

- [ ] AI 추천 시스템
  - [ ] 약점 분석
  - [ ] 맞춤형 문제 추천
  - [ ] 학습 패턴 분석
- [ ] 다른 플랫폼 연동
  - [ ] Velog 연동
  - [ ] GitHub Gist 연동
  - [ ] Obsidian 연동
- [ ] 학습 로드맵 제공
  - [ ] 난이도별 커리큘럼
  - [ ] 주제별 가이드

### 장기 확장 (1년 이상)

- [ ] 모바일 앱 개발
  - [ ] iOS 앱
  - [ ] Android 앱
- [ ] 기업용 버전
  - [ ] 채용 담당자용 기능
  - [ ] 문제 출제/관리 시스템
- [ ] 다국어 지원
  - [ ] 영어
  - [ ] 중국어

---

## 📊 성공 지표 (KPI) 체크리스트

- [ ] 문제 선정 시간 90% 이상 단축 (사용자 설문)
- [ ] 회고 작성률 70% 이상
- [ ] DAU (일 활성 사용자) Phase 1: 100명
- [ ] 사용자 유지율 월 60% 이상
- [ ] Notion 동기화 성공률 95% 이상

---

## 📝 현재 상태

- **현재 Phase**: 준비 단계
- **다음 작업**: Phase 1 - 프로젝트 초기 세팅
- **우선순위**: 프로젝트 구조 설계 및 기본 환경 구축

---

**마지막 업데이트**: 2025년 12월 2일
**주요 변경사항**:
- 아키텍처를 이벤트 드리븐 아키텍처(Event-Driven Architecture)로 변경
- 데이터베이스를 PostgreSQL에서 MySQL로 변경 (선택사항)
- 이벤트 기반 자동 동기화 로직 추가
- Spring Security와 JWT 제거 (세션 기반 인증으로 변경)
- Checkstyle 설정 완료 (Google Java Style Guide 기반)
- CodeRabbit 설정 완료 (.coderabbit.yaml)
- 코드 컨벤션 문서 작성 완료 (CODING_CONVENTION.md)
