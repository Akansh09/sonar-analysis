package com.sonar.analysis.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SonarQubeIssuesResponse {
    Integer total;
    Integer p;
    Integer ps;
    Object paging;
    Integer effortTotal;
    Issues[] issues;
    Object[] components;
    Object[] facets;
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Issues {
        String key;
        String rule;
        String severity;
        String component;
        String project;
        String hash;
        Integer line;
        Object[] flows;
        String status;
        String message;
        String effort;
        String debt;
        String author;
        String[] tags;
        String creationDate;
        String updateDate;
        String type;
        String scope;
        TextRange textRange;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Issues issues = (Issues) o;
            return Objects.equals(key, issues.key) &&
                    Objects.equals(message, issues.message);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, message);
        }
    }
    @Getter
    @Setter
    public static class TextRange{
        Integer startLine;
        Integer endLine;
        Integer startOffset;
        Integer endOffset;
    }
}