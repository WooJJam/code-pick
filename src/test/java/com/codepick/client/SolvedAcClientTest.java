package com.codepick.client;

import com.codepick.dto.solvedac.response.SolvedAcSearchResponse;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * SolvedAcClient 단위 테스트
 */
class SolvedAcClientTest {

	private MockWebServer mockWebServer;
	private SolvedAcClient solvedAcClient;

	// 기본 파라미터 상수
	private static final String DEFAULT_DIRECTION = "asc";
	private static final int DEFAULT_PAGE = 1;
	private static final String DEFAULT_SORT = "random";

	// 공통 Mock 응답
	private static final String SUCCESS_RESPONSE_WITH_ITEMS = """
		{
		    "count": 2,
		    "items": [
		        {
		            "problemId": 1000,
		            "titleKo": "A+B",
		            "titles": [
		                {"language": "en", "languageDisplayName": "en", "title": "A+B", "isOriginal": true}
		            ],
		            "level": 1,
		            "solvedCount": 300000,
		            "averageTries": 1.5,
		            "tags": [
		                {"key": "math", "displayNames": [{"language": "ko", "name": "수학"}]}
		            ]
		        },
		        {
		            "problemId": 1001,
		            "titleKo": "A-B",
		            "titles": [
		                {"language": "en", "languageDisplayName": "en", "title": "A-B", "isOriginal": true}
		            ],
		            "level": 1,
		            "solvedCount": 250000,
		            "averageTries": 1.3,
		            "tags": [
		                {"key": "math", "displayNames": [{"language": "ko", "name": "수학"}]}
		            ]
		        }
		    ]
		}
		""";

	private static final String EMPTY_RESPONSE = """
		{
		    "count": 0,
		    "items": []
		}
		""";

	@BeforeEach
	void setUp() throws IOException {
		mockWebServer = new MockWebServer();
		mockWebServer.start();

		WebClient webClient = WebClient.builder()
			.baseUrl(mockWebServer.url("/").toString())
			.build();

		solvedAcClient = new SolvedAcClient(webClient);
	}

	@AfterEach
	void tearDown() throws IOException {
		mockWebServer.shutdown();
	}

	@Nested
	@DisplayName("성공 케이스")
	class SuccessCases {

		@Test
		@DisplayName("정상적인 쿼리로 문제 검색 시 성공적으로 응답을 받는다")
		void searchProblems_Success() throws InterruptedException {
			// given
			String query = "!solved_by:woojjam2 tier:g4..s3";

			mockWebServer.enqueue(new MockResponse()
				.setBody(SUCCESS_RESPONSE_WITH_ITEMS)
				.setHeader("Content-Type", "application/json"));

			// when
			SolvedAcSearchResponse response = solvedAcClient.searchProblems(query, DEFAULT_DIRECTION, DEFAULT_PAGE, DEFAULT_SORT);

			// then
			assertThat(response).isNotNull();
			assertThat(response.getCount()).isEqualTo(2);
			assertThat(response.getItems()).hasSize(2);
			assertThat(response.getItems().get(0).getProblemId()).isEqualTo(1000);
			assertThat(response.getItems().get(0).getTitleKo()).isEqualTo("A+B");

			// 요청 검증
			RecordedRequest recordedRequest = mockWebServer.takeRequest();
			assertThat(recordedRequest.getPath()).contains("/search/problem");
			assertThat(recordedRequest.getPath()).contains("query=" + query.replace(" ", "%20"));
		}

		@Test
		@DisplayName("빈 결과가 반환되는 경우 count가 0이고 items가 비어있다")
		void searchProblems_EmptyResult() throws InterruptedException {
			// given
			String query = "tier:r1..r5";

			mockWebServer.enqueue(new MockResponse()
				.setBody(EMPTY_RESPONSE)
				.setHeader("Content-Type", "application/json"));

			// when
			SolvedAcSearchResponse response = solvedAcClient.searchProblems(query, DEFAULT_DIRECTION, DEFAULT_PAGE, DEFAULT_SORT);

			// then
			assertThat(response).isNotNull();
			assertThat(response.getCount()).isEqualTo(0);
			assertThat(response.getItems()).isEmpty();

			RecordedRequest recordedRequest = mockWebServer.takeRequest();
			assertThat(recordedRequest.getPath()).contains("/search/problem");
		}

		@Test
		@DisplayName("복잡한 쿼리로 검색 시 올바른 요청을 보낸다")
		void searchProblems_ComplexQuery() throws InterruptedException {
			// given
			String query = "!solved_by:woojjam2 (#implementation | #dfs) tier:g4..s3 s#10000..";
			String mockResponse = """
				{
				    "count": 5,
				    "items": []
				}
				""";

			mockWebServer.enqueue(new MockResponse()
				.setBody(mockResponse)
				.setHeader("Content-Type", "application/json"));

			// when
			SolvedAcSearchResponse response = solvedAcClient.searchProblems(query, DEFAULT_DIRECTION, DEFAULT_PAGE, DEFAULT_SORT);

			// then
			assertThat(response).isNotNull();
			assertThat(response.getCount()).isEqualTo(5);

			RecordedRequest recordedRequest = mockWebServer.takeRequest();
			String requestPath = recordedRequest.getPath();
			assertThat(requestPath).contains("/search/problem");
			assertThat(requestPath).contains("query=");
		}
	}

	@Nested
	@DisplayName("실패 케이스")
	class FailureCases {

		@Test
		@DisplayName("API 호출 실패 시 RuntimeException을 던진다")
		void searchProblems_ApiFailure() {
			// given
			String query = "invalid query";

			mockWebServer.enqueue(new MockResponse()
				.setResponseCode(500)
				.setBody("Internal Server Error"));

			// when & then
			assertThatThrownBy(() -> solvedAcClient.searchProblems(query, DEFAULT_DIRECTION, DEFAULT_PAGE, DEFAULT_SORT))
				.isInstanceOf(RuntimeException.class)
				.hasMessageContaining("Failed to call solved.ac API");
		}

		@Test
		@DisplayName("네트워크 에러 발생 시 RuntimeException을 던진다")
		void searchProblems_NetworkError() throws IOException {
			// given
			String query = "tier:g4..s3";

			mockWebServer.shutdown(); // 서버를 종료하여 네트워크 에러 발생

			// when & then
			assertThatThrownBy(() -> solvedAcClient.searchProblems(query, DEFAULT_DIRECTION, DEFAULT_PAGE, DEFAULT_SORT))
				.isInstanceOf(RuntimeException.class)
				.hasMessageContaining("Failed to call solved.ac API");
		}
	}
}
