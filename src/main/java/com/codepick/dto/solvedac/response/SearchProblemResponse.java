package com.codepick.dto.solvedac.response;

import java.util.List;

import com.codepick.dto.ProblemDto;

import lombok.Builder;
import lombok.Getter;

/**
 * 문제 검색 응답 DTO (도메인 모델)
 */
@Getter
@Builder
public class SearchProblemResponse {

	private int count;
	private List<ProblemDto> problems;

	/**
	 * count와 problems로 응답 생성
	 */
	public static SearchProblemResponse of(final int count, final List<ProblemDto> problems) {
		return SearchProblemResponse.builder()
			.count(count)
			.problems(problems)
			.build();
	}

	/**
	 * solved.ac API 응답을 도메인 응답으로 변환하는 정적 팩토리 메서드
	 * 변환 로직을 내부로 은닉
	 */
	public static SearchProblemResponse from(SolvedAcSearchResponse solvedAcResponse) {
		if (solvedAcResponse == null || solvedAcResponse.getItems() == null || solvedAcResponse.getItems().isEmpty()) {
			return SearchProblemResponse.of(0, List.of());
		}

		List<ProblemDto> problems = ProblemDto.from(solvedAcResponse.getItems());
		int count = solvedAcResponse.getCount() != null ? solvedAcResponse.getCount() : problems.size();

		return SearchProblemResponse.of(count, problems);
	}
}
