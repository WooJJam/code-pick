package com.codepick.dto;

import com.codepick.dto.solvedac.response.SolvedAcProblemResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 우리 API 응답 전용 DTO
 * 외부에 노출되는 도메인 모델
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemDto {

    private Long problemId;
    private String titleKo;
    private String titleEn;
    private Integer tier;
    private Integer solvedCount;
    private Double averageTries;
    private List<String> tags;

    /**
     * solved.ac API 응답을 우리 도메인 DTO로 변환하는 정적 팩토리 메서드
     */
    public static List<ProblemDto> from(final List<SolvedAcProblemResponse> problems) {
        if (problems == null) {
            return null;
        }

        return problems.stream().map(problem -> ProblemDto.builder()
			.problemId(problem.getProblemId())
			.titleKo(problem.getTitleKo())
			.titleEn(extractEnglishTitle(problem.getTitles()))
			.tier(problem.getLevel())
			.solvedCount(problem.getSolvedCount())
			.averageTries(problem.getAverageTries())
			.tags(extractTagNames(problem.getTags()))
			.build()).toList();
    }

    /**
     * 영어 제목 추출
     */
    private static String extractEnglishTitle(List<SolvedAcProblemResponse.Title> titles) {
        if (titles == null || titles.isEmpty()) {
            return null;
        }

        return titles.stream()
                .filter(title -> "en".equals(title.getLanguage()))
                .findFirst()
                .map(SolvedAcProblemResponse.Title::getTitle)
                .orElse(null);
    }

    /**
     * 태그 이름 추출 (한글)
     */
    private static List<String> extractTagNames(List<SolvedAcProblemResponse.Tag> tags) {
        if (tags == null || tags.isEmpty()) {
            return List.of();
        }

        return tags.stream()
                .map(tag -> tag.getDisplayNames() != null && !tag.getDisplayNames().isEmpty()
                        ? tag.getDisplayNames().stream()
                                .filter(displayName -> "ko".equals(displayName.getLanguage()))
                                .findFirst()
                                .map(SolvedAcProblemResponse.DisplayName::getName)
                                .orElse(tag.getKey())
                        : tag.getKey())
                .collect(Collectors.toList());
    }
}
