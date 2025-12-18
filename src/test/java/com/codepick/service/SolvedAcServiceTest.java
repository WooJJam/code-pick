package com.codepick.service;

import com.codepick.client.SolvedAcClient;
import com.codepick.dto.solvedac.response.SearchProblemResponse;
import com.codepick.dto.solvedac.response.SolvedAcProblemResponse;
import com.codepick.dto.solvedac.response.SolvedAcSearchResponse;
import com.codepick.validator.SolvedAcQueryValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * SolvedAcService 단위 테스트
 */
@ExtendWith(MockitoExtension.class)
class SolvedAcServiceTest {

    @Mock
    private SolvedAcClient solvedAcClient;

    @Mock
    private SolvedAcQueryValidator solvedAcQueryValidator;

    @InjectMocks
    private SolvedAcService solvedAcService;

    private SolvedAcSearchResponse mockApiResponse;

    // 기본 파라미터 상수
    private static final String DEFAULT_DIRECTION = null;
    private static final int DEFAULT_PAGE = 1;
    private static final String DEFAULT_SORT = "id";

    @BeforeEach
    void setUp() {
        // Mock API 응답 설정
        SolvedAcProblemResponse problem1 = new SolvedAcProblemResponse();
        problem1.setProblemId(1000L);
        problem1.setTitleKo("A+B");
        problem1.setTitles(List.of());
        problem1.setLevel(1);
        problem1.setSolvedCount(300000);
        problem1.setAverageTries(1.5);
        problem1.setTags(List.of());

        SolvedAcProblemResponse problem2 = new SolvedAcProblemResponse();
        problem2.setProblemId(1001L);
        problem2.setTitleKo("A-B");
        problem2.setTitles(List.of());
        problem2.setLevel(1);
        problem2.setSolvedCount(250000);
        problem2.setAverageTries(1.3);
        problem2.setTags(List.of());

        mockApiResponse = new SolvedAcSearchResponse();
        mockApiResponse.setCount(2);
        mockApiResponse.setItems(List.of(problem1, problem2));
    }

    @Test
    @DisplayName("모든 필터 조건으로 문제 검색 시 올바른 쿼리를 생성한다")
    void searchProblems_AllFilters() {
        // given
        String username = "woojjam2";
        Boolean unsolved = true;
        String tierRange = "g4..s3";
        List<String> tags = List.of("implementation", "dfs");
        Integer minSolvedCount = 10000;
        String direction = "asc";
        int page = 1;
        String sort = "random";

        when(solvedAcClient.searchProblems(any(), any(), anyInt(), any()))
                .thenReturn(mockApiResponse);

        // when
        SearchProblemResponse response = solvedAcService.searchProblems(
                username, unsolved, tierRange, tags, minSolvedCount, direction, page, sort
        );

        // then
        assertThat(response).isNotNull();
        assertThat(response.getCount()).isEqualTo(2);
        assertThat(response.getProblems()).hasSize(2);

        // 올바른 쿼리가 생성되었는지 확인
        verify(solvedAcClient).searchProblems(
                eq("!solved_by:woojjam2 (#implementation | #dfs) tier:g4..s3 s#10000.."),
                eq(direction),
                eq(page),
                eq(sort)
        );
    }

    @Test
    @DisplayName("unsolved가 false일 때 solved_by 쿼리를 생성한다")
    void searchProblems_SolvedByUser() {
        // given
        String username = "woojjam2";
        Boolean unsolved = false;

        when(solvedAcClient.searchProblems(any(), any(), anyInt(), any()))
                .thenReturn(mockApiResponse);

        // when
        solvedAcService.searchProblems(username, unsolved, null, null, null, DEFAULT_DIRECTION, DEFAULT_PAGE, DEFAULT_SORT);

        // then
        verify(solvedAcClient).searchProblems(eq("solved_by:woojjam2"), eq(DEFAULT_DIRECTION), eq(DEFAULT_PAGE), eq(DEFAULT_SORT));
    }

    @Test
    @DisplayName("username만 제공될 때 올바른 쿼리를 생성한다")
    void searchProblems_OnlyUsername() {
        // given
        String username = "woojjam2";

        when(solvedAcClient.searchProblems(any(), any(), anyInt(), any()))
                .thenReturn(mockApiResponse);

        // when
        solvedAcService.searchProblems(username, true, null, null, null, DEFAULT_DIRECTION, DEFAULT_PAGE, DEFAULT_SORT);

        // then
        verify(solvedAcClient).searchProblems(eq("!solved_by:woojjam2"), eq(DEFAULT_DIRECTION), eq(DEFAULT_PAGE), eq(DEFAULT_SORT));
    }

    @Test
    @DisplayName("tierRange만 제공될 때 올바른 쿼리를 생성한다")
    void searchProblems_OnlyTierRange() {
        // given
        String tierRange = "g4..s3";

        when(solvedAcClient.searchProblems(any(), any(), anyInt(), any()))
                .thenReturn(mockApiResponse);

        // when
        solvedAcService.searchProblems(null, null, tierRange, null, null, DEFAULT_DIRECTION, DEFAULT_PAGE, DEFAULT_SORT);

        // then
        verify(solvedAcClient).searchProblems(eq("tier:g4..s3"), eq(DEFAULT_DIRECTION), eq(DEFAULT_PAGE), eq(DEFAULT_SORT));
    }

    @Test
    @DisplayName("tags만 제공될 때 올바른 OR 쿼리를 생성한다")
    void searchProblems_OnlyTags() {
        // given
        List<String> tags = List.of("implementation", "dfs", "dp");

        when(solvedAcClient.searchProblems(any(), any(), anyInt(), any()))
                .thenReturn(mockApiResponse);

        // when
        solvedAcService.searchProblems(null, null, null, tags, null, DEFAULT_DIRECTION, DEFAULT_PAGE, DEFAULT_SORT);

        // then
        verify(solvedAcClient).searchProblems(eq("(#implementation | #dfs | #dp)"), eq(DEFAULT_DIRECTION), eq(DEFAULT_PAGE), eq(DEFAULT_SORT));
    }

    @Test
    @DisplayName("minSolvedCount만 제공될 때 올바른 쿼리를 생성한다")
    void searchProblems_OnlyMinSolvedCount() {
        // given
        Integer minSolvedCount = 10000;

        when(solvedAcClient.searchProblems(any(), any(), anyInt(), any()))
                .thenReturn(mockApiResponse);

        // when
        solvedAcService.searchProblems(null, null, null, null, minSolvedCount, DEFAULT_DIRECTION, DEFAULT_PAGE, DEFAULT_SORT);

        // then
        verify(solvedAcClient).searchProblems(eq("s#10000.."), eq(DEFAULT_DIRECTION), eq(DEFAULT_PAGE), eq(DEFAULT_SORT));
    }

    @Test
    @DisplayName("username과 tierRange만 제공될 때 올바른 쿼리를 생성한다")
    void searchProblems_UsernameAndTierRange() {
        // given
        String username = "woojjam2";
        String tierRange = "g4..s3";

        when(solvedAcClient.searchProblems(any(), any(), anyInt(), any()))
                .thenReturn(mockApiResponse);

        // when
        solvedAcService.searchProblems(username, true, tierRange, null, null, DEFAULT_DIRECTION, DEFAULT_PAGE, DEFAULT_SORT);

        // then
        verify(solvedAcClient).searchProblems(eq("!solved_by:woojjam2 tier:g4..s3"), eq(DEFAULT_DIRECTION), eq(DEFAULT_PAGE), eq(DEFAULT_SORT));
    }

    @Test
    @DisplayName("빈 결과를 올바르게 처리한다")
    void searchProblems_EmptyResult() {
        // given
        SolvedAcSearchResponse emptyResponse = new SolvedAcSearchResponse();
        emptyResponse.setCount(0);
        emptyResponse.setItems(List.of());

        when(solvedAcClient.searchProblems(any(), any(), anyInt(), any()))
                .thenReturn(emptyResponse);

        // when
        SearchProblemResponse response = solvedAcService.searchProblems(
                "woojjam2", true, "r1..r5", null, null, DEFAULT_DIRECTION, DEFAULT_PAGE, DEFAULT_SORT
        );

        // then
        assertThat(response).isNotNull();
        assertThat(response.getCount()).isEqualTo(0);
        assertThat(response.getProblems()).isEmpty();
    }

    @Test
    @DisplayName("null 응답을 올바르게 처리한다")
    void searchProblems_NullResponse() {
        // given
        when(solvedAcClient.searchProblems(any(), any(), anyInt(), any()))
                .thenReturn(null);

        // when
        SearchProblemResponse response = solvedAcService.searchProblems(
                "woojjam2", true, "g4..s3", null, null, DEFAULT_DIRECTION, DEFAULT_PAGE, DEFAULT_SORT
        );

        // then
        assertThat(response).isNotNull();
        assertThat(response.getCount()).isEqualTo(0);
        assertThat(response.getProblems()).isEmpty();
    }

    @Test
    @DisplayName("모든 파라미터가 null일 때 빈 쿼리를 생성한다")
    void searchProblems_AllNull() {
        // given
        when(solvedAcClient.searchProblems(any(), any(), anyInt(), any()))
                .thenReturn(mockApiResponse);

        // when
        solvedAcService.searchProblems(null, null, null, null, null, DEFAULT_DIRECTION, DEFAULT_PAGE, DEFAULT_SORT);

        // then
        verify(solvedAcClient).searchProblems(eq(""), eq(DEFAULT_DIRECTION), eq(DEFAULT_PAGE), eq(DEFAULT_SORT));
    }

    @Test
    @DisplayName("단일 태그로 검색 시 올바른 쿼리를 생성한다")
    void searchProblems_SingleTag() {
        // given
        List<String> tags = List.of("implementation");

        when(solvedAcClient.searchProblems(any(), any(), anyInt(), any()))
                .thenReturn(mockApiResponse);

        // when
        solvedAcService.searchProblems(null, null, null, tags, null, DEFAULT_DIRECTION, DEFAULT_PAGE, DEFAULT_SORT);

        // then
        verify(solvedAcClient).searchProblems(eq("(#implementation)"), eq(DEFAULT_DIRECTION), eq(DEFAULT_PAGE), eq(DEFAULT_SORT));
    }

    @Test
    @DisplayName("빈 태그 리스트는 무시된다")
    void searchProblems_EmptyTagList() {
        // given
        List<String> tags = List.of();

        when(solvedAcClient.searchProblems(any(), any(), anyInt(), any()))
                .thenReturn(mockApiResponse);

        // when
        solvedAcService.searchProblems(null, null, null, tags, null, DEFAULT_DIRECTION, DEFAULT_PAGE, DEFAULT_SORT);

        // then
        verify(solvedAcClient).searchProblems(eq(""), eq(DEFAULT_DIRECTION), eq(DEFAULT_PAGE), eq(DEFAULT_SORT));
    }
}
