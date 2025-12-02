package com.codepick.service;

import com.codepick.client.SolvedAcClient;
import com.codepick.dto.solvedac.response.SearchProblemResponse;
import com.codepick.dto.solvedac.response.SolvedAcSearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SolvedAcService {

    private final SolvedAcClient solvedAcClient;

    /**
     * solved.ac API를 호출하여 조건에 맞는 문제 목록을 검색합니다.
     *
     * @param username 백준 닉네임
     * @param unsolved true이면 !solved_by (안 푼 문제), false이면 solved_by (푼 문제)
     * @param tierRange 난이도 범위 (예: "g4..s3" - g4 이하 s3 이상)
     * @param tags 알고리즘 유형 목록 (예: ["implementation", "dfs"])
     * @param minSolvedCount 최소 푼 사람 수
     * @return 검색된 문제 목록과 총 개수
     */
    public SearchProblemResponse searchProblems(
        final String username,
        final Boolean unsolved,
        final String tierRange,
        final List<String> tags,
        final Integer minSolvedCount
    ) {
        String query = buildQuery(username, unsolved, tierRange, tags, minSolvedCount);

        SolvedAcSearchResponse solvedAcResponse = solvedAcClient.searchProblems(query);

        return SearchProblemResponse.from(solvedAcResponse);
    }

    /**
     * solved.ac API 쿼리 문자열을 생성합니다.
     */
    private String buildQuery(final String username, final Boolean unsolved, final String tierRange, final List<String> tags, final Integer minSolvedCount) {
        StringBuilder queryBuilder = new StringBuilder();

        // solved_by 파라미터
        if (username != null && !username.isEmpty()) {
            if (unsolved != null && unsolved) {
                queryBuilder.append("!solved_by:").append(username).append(" ");
            } else {
                queryBuilder.append("solved_by:").append(username).append(" ");
            }
        }

        // tag 파라미터 - (#tag1 | #tag2) 형식
        if (tags != null && !tags.isEmpty()) {
            queryBuilder.append("(");
            for (int i = 0; i < tags.size(); i++) {
                queryBuilder.append("#").append(tags.get(i));
                if (i < tags.size() - 1) {
                    queryBuilder.append(" | ");
                }
            }
            queryBuilder.append(") ");
        }

        // tier 파라미터
        if (tierRange != null && !tierRange.isEmpty()) {
            queryBuilder.append("tier:").append(tierRange).append(" ");
        }

        // 푼 사람 수 파라미터
        if (minSolvedCount != null) {
            queryBuilder.append("s#").append(minSolvedCount).append("..");
        }

        return queryBuilder.toString().trim();
    }

}
