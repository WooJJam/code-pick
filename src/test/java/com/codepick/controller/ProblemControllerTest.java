package com.codepick.controller;

import com.codepick.dto.ProblemDto;
import com.codepick.dto.solvedac.response.SearchProblemResponse;
import com.codepick.service.SolvedAcService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * ProblemController 통합 테스트
 */
@WebMvcTest(ProblemController.class)
class ProblemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SolvedAcService solvedAcService;

    @Test
    @DisplayName("모든 파라미터로 문제 검색 시 200 OK와 올바른 응답을 반환한다")
    void searchProblems_AllParameters() throws Exception {
        // given
        ProblemDto problem1 = ProblemDto.builder()
                .problemId(1000L)
                .titleKo("A+B")
                .titleEn("A+B")
                .tier(1)
                .solvedCount(300000)
                .averageTries(1.5)
                .tags(List.of("math"))
                .build();

        ProblemDto problem2 = ProblemDto.builder()
                .problemId(1001L)
                .titleKo("A-B")
                .titleEn("A-B")
                .tier(1)
                .solvedCount(250000)
                .averageTries(1.3)
                .tags(List.of("math"))
                .build();

        SearchProblemResponse mockResponse = SearchProblemResponse.of(2, List.of(problem1, problem2));
        when(solvedAcService.searchProblems(anyString(), anyBoolean(), anyString(), anyList(), anyInt()))
                .thenReturn(mockResponse);

        // when & then
        mockMvc.perform(get("/api/v1/problems/search")
                        .param("username", "woojjam2")
                        .param("unsolved", "true")
                        .param("tierRange", "g4..s3")
                        .param("tags", "implementation", "dfs")
                        .param("minSolvedCount", "10000"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.count").value(2))
                .andExpect(jsonPath("$.data.problems").isArray())
                .andExpect(jsonPath("$.data.problems.length()").value(2))
                .andExpect(jsonPath("$.data.problems[0].problemId").value(1000))
                .andExpect(jsonPath("$.data.problems[0].titleKo").value("A+B"))
                .andExpect(jsonPath("$.data.problems[1].problemId").value(1001));

        verify(solvedAcService).searchProblems("woojjam2", true, "g4..s3", List.of("implementation", "dfs"), 10000);
    }

    @Test
    @DisplayName("필수 파라미터 없이 요청 시 200 OK와 빈 결과를 반환한다")
    void searchProblems_NoParameters() throws Exception {
        // given
        SearchProblemResponse mockResponse = SearchProblemResponse.of(0, List.of());
        when(solvedAcService.searchProblems(isNull(), anyBoolean(), isNull(), isNull(), isNull()))
                .thenReturn(mockResponse);

        // when & then
        mockMvc.perform(get("/api/v1/problems/search"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.count").value(0))
                .andExpect(jsonPath("$.data.problems").isArray())
                .andExpect(jsonPath("$.data.problems").isEmpty());
    }

    @Test
    @DisplayName("username만 제공 시 올바른 응답을 반환한다")
    void searchProblems_OnlyUsername() throws Exception {
        // given
        SearchProblemResponse mockResponse = SearchProblemResponse.of(5, List.of());
        when(solvedAcService.searchProblems(eq("woojjam2"), anyBoolean(), isNull(), isNull(), isNull()))
                .thenReturn(mockResponse);

        // when & then
        mockMvc.perform(get("/api/v1/problems/search")
                        .param("username", "woojjam2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.count").value(5));

        verify(solvedAcService).searchProblems("woojjam2", true, null, null, null);
    }

    @Test
    @DisplayName("unsolved 기본값이 true로 설정된다")
    void searchProblems_DefaultUnsolvedTrue() throws Exception {
        // given
        SearchProblemResponse mockResponse = SearchProblemResponse.of(0, List.of());
        when(solvedAcService.searchProblems(anyString(), eq(true), isNull(), isNull(), isNull()))
                .thenReturn(mockResponse);

        // when & then
        mockMvc.perform(get("/api/v1/problems/search")
                        .param("username", "woojjam2"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(solvedAcService).searchProblems("woojjam2", true, null, null, null);
    }

    @Test
    @DisplayName("unsolved를 false로 설정 시 올바르게 전달된다")
    void searchProblems_UnsolvedFalse() throws Exception {
        // given
        SearchProblemResponse mockResponse = SearchProblemResponse.of(10, List.of());
        when(solvedAcService.searchProblems(anyString(), eq(false), isNull(), isNull(), isNull()))
                .thenReturn(mockResponse);

        // when & then
        mockMvc.perform(get("/api/v1/problems/search")
                        .param("username", "woojjam2")
                        .param("unsolved", "false"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.count").value(10));

        verify(solvedAcService).searchProblems("woojjam2", false, null, null, null);
    }

    @Test
    @DisplayName("tierRange만 제공 시 올바른 응답을 반환한다")
    void searchProblems_OnlyTierRange() throws Exception {
        // given
        SearchProblemResponse mockResponse = SearchProblemResponse.of(100, List.of());
        when(solvedAcService.searchProblems(isNull(), anyBoolean(), eq("g4..s3"), isNull(), isNull()))
                .thenReturn(mockResponse);

        // when & then
        mockMvc.perform(get("/api/v1/problems/search")
                        .param("tierRange", "g4..s3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.count").value(100));

        verify(solvedAcService).searchProblems(null, true, "g4..s3", null, null);
    }

    @Test
    @DisplayName("tags를 리스트로 전달 시 올바르게 처리된다")
    void searchProblems_MultipleTags() throws Exception {
        // given
        SearchProblemResponse mockResponse = SearchProblemResponse.of(50, List.of());
        when(solvedAcService.searchProblems(isNull(), anyBoolean(), isNull(), anyList(), isNull()))
                .thenReturn(mockResponse);

        // when & then
        mockMvc.perform(get("/api/v1/problems/search")
                        .param("tags", "implementation")
                        .param("tags", "dfs")
                        .param("tags", "dp"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.count").value(50));

        verify(solvedAcService).searchProblems(null, true, null, List.of("implementation", "dfs", "dp"), null);
    }

    @Test
    @DisplayName("minSolvedCount만 제공 시 올바른 응답을 반환한다")
    void searchProblems_OnlyMinSolvedCount() throws Exception {
        // given
        SearchProblemResponse mockResponse = SearchProblemResponse.of(20, List.of());
        when(solvedAcService.searchProblems(isNull(), anyBoolean(), isNull(), isNull(), eq(10000)))
                .thenReturn(mockResponse);

        // when & then
        mockMvc.perform(get("/api/v1/problems/search")
                        .param("minSolvedCount", "10000"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.count").value(20));

        verify(solvedAcService).searchProblems(null, true, null, null, 10000);
    }

    @Test
    @DisplayName("빈 결과를 올바르게 처리한다")
    void searchProblems_EmptyResult() throws Exception {
        // given
        SearchProblemResponse mockResponse = SearchProblemResponse.of(0, List.of());
        when(solvedAcService.searchProblems(anyString(), anyBoolean(), anyString(), isNull(), isNull()))
                .thenReturn(mockResponse);

        // when & then
        mockMvc.perform(get("/api/v1/problems/search")
                        .param("username", "woojjam2")
                        .param("tierRange", "r1..r5"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.count").value(0))
                .andExpect(jsonPath("$.data.problems").isEmpty());
    }

    @Test
    @DisplayName("복합 조건으로 검색 시 올바른 응답을 반환한다")
    void searchProblems_ComplexQuery() throws Exception {
        // given
        ProblemDto problem = ProblemDto.builder()
                .problemId(1234L)
                .titleKo("테스트 문제")
                .titleEn("Test Problem")
                .tier(10)
                .solvedCount(50000)
                .averageTries(2.5)
                .tags(List.of("implementation", "dfs"))
                .build();

        SearchProblemResponse mockResponse = SearchProblemResponse.of(1, List.of(problem));
        when(solvedAcService.searchProblems(anyString(), anyBoolean(), anyString(), anyList(), anyInt()))
                .thenReturn(mockResponse);

        // when & then
        mockMvc.perform(get("/api/v1/problems/search")
                        .param("username", "woojjam2")
                        .param("unsolved", "true")
                        .param("tierRange", "g4..s3")
                        .param("tags", "implementation", "dfs")
                        .param("minSolvedCount", "10000"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.count").value(1))
                .andExpect(jsonPath("$.data.problems[0].problemId").value(1234))
                .andExpect(jsonPath("$.data.problems[0].titleKo").value("테스트 문제"))
                .andExpect(jsonPath("$.data.problems[0].tags").isArray())
                .andExpect(jsonPath("$.data.problems[0].tags[0]").value("implementation"));
    }
}
