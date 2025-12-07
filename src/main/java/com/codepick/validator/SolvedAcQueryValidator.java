package com.codepick.validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class SolvedAcQueryValidator {

    /**
     * username 규칙
     * - 영문/숫자/'_' 만 허용
     * - '_' 는 최대 1개까지 허용
     */

    public void validateQuery(final String username, final List<String> tags, final String tierRange, final Integer solvedCount) {
        validateUsername(username);
        validateTags(tags);
        validateTierRange(tierRange);
        validateSolvedCount(solvedCount);
    }


    private String validateUsername(final String username) {
        if (username == null || username.isEmpty()) {
            return null; // 선택 값이므로 null 허용
        }

        if (!username.matches("^[A-Za-z0-9_]+$")) {
            throw new IllegalArgumentException("username에는 영문, 숫자, 밑줄(_)만 사용할 수 있습니다: " + username);
        }

        long underscoreCount = username.chars().filter(c -> c == '_').count();
        if (underscoreCount > 1) {
            throw new IllegalArgumentException("username에는 밑줄(_)을 최대 한 번만 사용할 수 있습니다: " + username);
        }

        return username;
    }

    /**
     * tag 규칙
     * - 영문/숫자/공백만 허용
     * - 공백(띄어쓰기)은 최대 1번만 허용 → 최대 두 단어까지만 가능
     */
    private List<String> validateTags(final List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> safeTags = new ArrayList<>(tags.size());

        for (String tag : tags) {
            if (tag == null || tag.isBlank()) {
                continue; // 빈 태그는 무시
            }

            long spaces = tag.chars().filter(c -> c == ' ').count();
            if (spaces > 1) {
                throw new IllegalArgumentException("tag에는 띄어쓰기를 최대 한 번만 사용할 수 있습니다: " + tag);
            }

            if (!tag.matches("^[A-Za-z0-9 ]+$")) {
                throw new IllegalArgumentException("tag에는 영문, 숫자, 공백만 사용할 수 있습니다: " + tag);
            }

            safeTags.add(tag.trim());
        }

        return safeTags;
    }

    /**
     * tierRange 규칙
     *
     * 1) 범위 형태 (.. 사용)
     *    - 예: g5..s3
     *    - ".." 는 정확히 한 번만 등장
     *    - 앞, 뒤 티어는 각각 영문/숫자만 허용
     *
     * 2) 단일 티어 형태 (.. 없음)
     *    - 예: g5
     *    - '.' 문자는 포함되면 안 됨
     *    - 전체가 영문/숫자만으로 이루어져야 함
     *
     * 3) null 또는 공백 문자열은 tier 필터 미사용으로 간주
     */
    private String validateTierRange(final String tierRange) {
        if (tierRange == null || tierRange.isBlank()) {
            return null;
        }

        // 범위 형태: <tier>..<tier>
        if (tierRange.contains("..")) {
            int first = tierRange.indexOf("..");
            int last  = tierRange.lastIndexOf("..");

            // ".." 가 정확히 한 번만 있어야 함
            if (first != last) {
                throw new IllegalArgumentException("tierRange에 '..'는 한 번만 사용할 수 있습니다: " + tierRange);
            }

            String[] parts = tierRange.split("\\.\\.");
            if (parts.length != 2 || parts[0].isEmpty() || parts[1].isEmpty()) {
                throw new IllegalArgumentException("tierRange는 '<티어>..<티어>' 형식이어야 합니다: " + tierRange);
            }

            if (!parts[0].matches("^[A-Za-z0-9]+$") || !parts[1].matches("^[A-Za-z0-9]+$")) {
                throw new IllegalArgumentException("tierRange에는 영문과 숫자만 사용할 수 있습니다: " + tierRange);
            }

            return tierRange;
        }

        // 단일 티어 형태: <tier>
        if (tierRange.contains(".")) {
            throw new IllegalArgumentException("단일 tierRange에서는 '.'을 사용할 수 없습니다: " + tierRange);
        }

        if (!tierRange.matches("^[A-Za-z0-9]+$")) {
            throw new IllegalArgumentException("tierRange에는 영문과 숫자만 사용할 수 있습니다: " + tierRange);
        }

        return tierRange;
    }

    /**
     * minSolvedCount 규칙
     * - null 허용
     * - 음수 불가
     */
    private Integer validateSolvedCount(final Integer solvedCount) {
        if (solvedCount == null) {
            return null;
        }

        if (solvedCount < 0) {
            throw new IllegalArgumentException("푼 사람 수(solvedCount)는 0 이상이어야 합니다: " + solvedCount);
        }

        return solvedCount;
    }
}
