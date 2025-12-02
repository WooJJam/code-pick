package com.codepick.dto.solvedac.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * solved.ac API 검색 응답 전용 DTO
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SolvedAcSearchResponse {

    @JsonProperty("count")
    private Integer count;

    @JsonProperty("items")
    private List<SolvedAcProblemResponse> items;
}
