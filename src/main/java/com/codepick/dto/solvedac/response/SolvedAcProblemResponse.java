package com.codepick.dto.solvedac.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * solved.ac API 응답 전용 DTO
 * 외부 API 구조 변경에 대응하기 위한 내부 전용 클래스
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SolvedAcProblemResponse {

    @JsonProperty("problemId")
    private Long problemId;

    @JsonProperty("titleKo")
    private String titleKo;

    @JsonProperty("titles")
    private List<Title> titles;

    @JsonProperty("level")
    private Integer level;

    @JsonProperty("solvedCount")
    private Integer solvedCount;

    @JsonProperty("averageTries")
    private Double averageTries;

    @JsonProperty("tags")
    private List<Tag> tags;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Title {
        @JsonProperty("language")
        private String language;

        @JsonProperty("languageDisplayName")
        private String languageDisplayName;

        @JsonProperty("title")
        private String title;

        @JsonProperty("isOriginal")
        private Boolean isOriginal;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Tag {
        @JsonProperty("key")
        private String key;

        @JsonProperty("isMeta")
        private Boolean isMeta;

        @JsonProperty("bojTagId")
        private Integer bojTagId;

        @JsonProperty("problemCount")
        private Integer problemCount;

        @JsonProperty("displayNames")
        private List<DisplayName> displayNames;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DisplayName {
        @JsonProperty("language")
        private String language;

        @JsonProperty("name")
        private String name;

        @JsonProperty("short")
        private String shortName;
    }
}
