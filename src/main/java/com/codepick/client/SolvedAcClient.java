package com.codepick.client;

import com.codepick.dto.solvedac.response.SolvedAcSearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * solved.ac API 호출 전용 클라이언트
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SolvedAcClient {

    private final WebClient solvedAcWebClient;

    /**
     * solved.ac API에 문제 검색 요청을 보냅니다.
     *
     * @param query solved.ac 쿼리 문자열
     * @return solved.ac API 응답
     */
    public SolvedAcSearchResponse searchProblems(final String query) {
        log.info("Calling solved.ac API with query: {}", query);

        try {
            SolvedAcSearchResponse response = solvedAcWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search/problem")
                            .queryParam("query", query)
                            .build())
                    .retrieve()
                    .bodyToMono(SolvedAcSearchResponse.class)
                    .block();

            if (response != null) {
                log.info("Received {} problems from solved.ac API", response.getCount());
            }

            return response;

        } catch (Exception e) {
            log.error("Error while calling solved.ac API with query: {}", query, e);
            throw new RuntimeException("Failed to call solved.ac API", e);
        }
    }
}
