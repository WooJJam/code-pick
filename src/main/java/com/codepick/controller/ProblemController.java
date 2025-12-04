package com.codepick.controller;

import com.codepick.dto.ProblemDto;
import com.codepick.dto.solvedac.response.SearchProblemResponse;
import com.codepick.service.SolvedAcService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/problems")
@RequiredArgsConstructor
public class ProblemController {

	private final SolvedAcService solvedAcService;

	/**
	 * 조건에 맞는 문제 목록을 검색합니다.
	 *
	 * @param username 백준 닉네임
	 * @param unsolved true이면 안 푼 문제, false이면 푼 문제 (기본값: true)
	 * @param tierRange 난이도 범위 (예: "g4..s3" - g4 이하 s3 이상)
	 * @param tags 알고리즘 유형 목록 (예: ["implementation", "dfs"])
	 * @param minSolvedCount 최소 푼 사람 수 (예: 10000)
	 * @return 검색된 문제 목록과 총 개수
	 */
	@GetMapping("/search")
	public ResponseEntity<?> searchProblems(
		@RequestParam(required = false) String username,
		@RequestParam(required = false, defaultValue = "true") Boolean unsolved,
		@RequestParam(required = false) String tierRange,
		@RequestParam(required = false) List<String> tags,
		@RequestParam(required = false) Integer minSolvedCount,
		@RequestParam(required = false) String direction,
        @RequestParam(required = false, defaultValue = "1") Integer page,
		@RequestParam(required = false, defaultValue = "id") String sortBy) {

		log.info("요청 정보: username: {}, unsolved: {}, tierRange: {}, tags: {}, minSolvedCount: {}, direction: {}, page: {}, sortBy: {}",
			username != null ? "***" : null, unsolved, tierRange, tags, minSolvedCount, direction, page, sortBy);

		SearchProblemResponse response = solvedAcService.searchProblems(username, unsolved, tierRange, tags,
			minSolvedCount, direction, page, sortBy);

		return ResponseEntity.ok(Map.of("data", response));
	}
}
