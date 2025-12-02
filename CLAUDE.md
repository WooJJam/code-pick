# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build and Run Commands

```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun

# Run tests
./gradlew test

# Run specific tests
./gradlew test --tests "com.codepick.*"

# Kill processes using port 8080 (if port conflict occurs)
lsof -i :8080 | grep LISTEN | awk '{print $2}' | xargs kill -9
```

## Architecture Overview

CodePick is a coding test learning automation platform built with **Event-Driven Architecture (EDA)**.

### Core Architecture Principles

- **Event-Driven Design**: Domain events drive system behavior with loose coupling between components
- **Async Event Processing**: Events are processed asynchronously using Spring's `@Async` and thread pools
- **Static Factory Methods**: DTOs use static factory methods for conversion instead of direct JSON mapping
- **Layered Architecture**: Controller → Service → Client → External API

### Package Structure

```
com.codepick
├── client/              # External API communication layer (e.g., SolvedAcClient)
├── config/              # Configuration classes (AsyncConfig, WebClientConfig)
├── controller/          # REST API endpoints
├── domain/
│   └── event/           # Domain events (DomainEvent, UserRegisteredEvent)
├── dto/
│   └── solvedac/
│       └── response/    # External API DTOs (separate from domain DTOs)
├── event/
│   ├── handler/         # Async event handlers (UserEventHandler)
│   └── publisher/       # Event publisher (EventPublisher)
└── service/             # Business logic layer (SolvedAcService)
```

### Key Design Patterns

1. **Separation of External vs Domain DTOs**:
   - `dto/solvedac/response/` contains DTOs with `@JsonProperty` for external API mapping
   - Domain DTOs (like `ProblemDto`, `SearchProblemResponse`) use static factory methods
   - Conversion logic is encapsulated: `ProblemDto.from()`, `SearchProblemResponse.from()`

2. **Client Layer Pattern**:
   - `client/` package contains all external HTTP calls (uses WebClient)
   - Service layer delegates API communication to clients
   - Example: `SolvedAcService` → `SolvedAcClient` → solved.ac API

3. **Event Flow**:
   ```
   Action → Domain Event → Event Publisher → Async Event Handlers
   ```
   Example: Review creation → `ReviewCreatedEvent` → Notion sync (async) + Stats update (async)

## solved.ac API Integration

### Query Format
The query builder in `SolvedAcService` constructs queries following solved.ac syntax:
```
!solved_by:username (#tag1 | #tag2) tier:g4..s3 s#10000..
```

- `!solved_by:username` - problems NOT solved by user
- `solved_by:username` - problems solved by user
- `(#tag1 | #tag2)` - OR condition for tags
- `tier:g4..s3` - difficulty range (gold 4 to silver 3)
- `s#10000..` - minimum solved count

### API Endpoint
- **Endpoint**: `/api/v1/problems/search`
- **Parameters**:
  - `username`: 백준 username
  - `unsolved`: true = !solved_by, false = solved_by
  - `tierRange`: difficulty (e.g., "g4..s3")
  - `tags`: algorithm types (e.g., ["implementation", "dfs"])
  - `minSolvedCount`: minimum solved count

## Configuration

### Database
- **Development**: H2 in-memory database
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:codepick`
  - Username: `sa`
  - Password: (empty)
- **Production**: MySQL (optional, configured in application.yml)

### External APIs
- **Solved.ac API**: https://solved.ac/api/v3
- **WebClient**: Configured in `WebClientConfig.java`

### Async Processing
- Thread pool configured in `AsyncConfig.java`:
  - Core pool size: 5
  - Max pool size: 10
  - Queue capacity: 100
  - Thread name prefix: "event-"

## Git Conventions

- **Commit Messages**: Korean with conventional commit format
  ```
  feat: 새로운 기능 추가
  fix: 버그 수정
  build: 빌드 설정 변경
  refactor: 코드 리팩토링
  ```
- **Commit Footer**:
  ```
  🤖 Generated with [Claude Code](https://claude.com/claude-code)

  Co-Authored-By: Claude <noreply@anthropic.com>
  ```
- **Branching**: Follow git-flow (main, develop, feature/*, bugfix/*, hotfix/*)

## Development Workflow

1. **When adding features**:
   - Update TODO.md to track completed tasks
   - Follow the separation of concerns: Controller → Service → Client
   - Use static factory methods for DTO conversions
   - Keep conversion logic inside DTOs

2. **When committing**:
   - Separate logical changes into multiple commits
   - Follow Korean git conventions
   - Update TODO.md with completed items
   - Include Claude Code footer

3. **Port management**:
   - Always kill existing processes on port 8080 before running new instances
   - Use: `lsof -i :8080 | grep LISTEN | awk '{print $2}' | xargs kill -9`

## Code Review Settings

CodeRabbit is configured with:
- Korean language reviews
- "chill" review profile
- Auto-review enabled on all branches
- Request changes workflow enabled

## Important Notes

- **MacOS Netty Optimization**: Project includes `netty-resolver-dns-native-macos` for optimal WebClient performance
- **Logging**: Set to DEBUG for `com.codepick` and event processing
- **No Spring Security**: Authentication will use session-based approach (not JWT)
- **TODO.md**: Always update this file when completing tasks or making architectural decisions
