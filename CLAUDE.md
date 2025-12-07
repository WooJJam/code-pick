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

## Serena MCP Server Usage

**CRITICAL**: Always use Serena's symbolic tools for code exploration and editing. Serena provides intelligent, token-efficient code manipulation.

### When to Use Serena Tools

1. **Code Exploration**:
   - Use `mcp__serena__list_dir` to explore directory structure
   - Use `mcp__serena__get_symbols_overview` to understand file structure before reading
   - Use `mcp__serena__find_symbol` with `depth` parameter to navigate class hierarchies

2. **Code Search**:
   - Use `mcp__serena__find_symbol` with patterns (e.g., "Service", "Controller") to find classes
   - Use `mcp__serena__search_for_pattern` for regex-based content search
   - Use `mcp__serena__find_referencing_symbols` to understand dependencies

3. **Code Editing**:
   - Use `mcp__serena__replace_symbol_body` to replace entire methods/classes
   - Use `mcp__serena__insert_after_symbol` to add new methods after existing ones
   - Use `mcp__serena__insert_before_symbol` for imports or prepending code
   - Use `mcp__serena__rename_symbol` for safe refactoring across the codebase

### Example Workflow

```
1. Explore structure: mcp__serena__list_dir("src/main/java/com/codepick", recursive=true)
2. Get overview: mcp__serena__get_symbols_overview("src/main/java/com/codepick/service/SolvedAcService.java")
3. Find method: mcp__serena__find_symbol("searchProblems", depth=0, include_body=true)
4. Edit method: mcp__serena__replace_symbol_body("searchProblems", relative_path="...", body="...")
```

### Serena Best Practices

- **Always explore before editing**: Use overview tools to understand context
- **Use symbolic editing for precision**: Prefer `replace_symbol_body` over line-based Edit
- **Leverage depth parameter**: `depth=1` shows method signatures without bodies
- **Use name_path patterns**: Relative paths like "Service/searchProblems" for targeted search

## Important Notes

- **MacOS Netty Optimization**: Project includes `netty-resolver-dns-native-macos` for optimal WebClient performance
- **Logging**: Set to DEBUG for `com.codepick` and event processing
- **No Spring Security**: Authentication will use session-based approach (not JWT)
- **TODO.md**: Always update this file when completing tasks or making architectural decisions
- **Test Patterns**:
  - Use constants for repeated mock parameters (e.g., `DEFAULT_PAGE`, `DEFAULT_SORT`)
  - Use `@Nested` classes to group related test cases
  - Mockito matchers: Use `any()` for nullable parameters, `anyInt()` for primitives
  - When using ANY matcher in `verify()`, ALL parameters must be matchers
