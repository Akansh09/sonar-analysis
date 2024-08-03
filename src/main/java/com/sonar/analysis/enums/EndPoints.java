package com.sonar.analysis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EndPoints {
    SONARQUBE_ISSUES("/api/issues/search"),
    GIT_COMMENT_PULL_REQUEST("repos/%s/%s/pulls/%s/reviews"),
    GET_PULL_REQUEST("repos/%s/%s/pulls");

    String uri;
}
