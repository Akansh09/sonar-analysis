package com.sonar.analysis.constants;

public class SonarConstants {
    private SonarConstants() {
    }
    public static final String DYNAMO_SONAR_TABLE = "SonarIssues";
    public static final String GIT_REPO_NAME = System.getProperty("GIT_REPO_NAME");
    public static final String SONAR_PROJECT_KEY = System.getProperty("SONAR_KEY");
    public static final String GIT_REPO_OWNER = System.getProperty("GIT_REPO_OWNER");
    public static final String GIT_TARGET_BRANCH = System.getProperty("TARGET_BRANCH");
    public static final String GIT_SOURCE_BRANCH = System.getenv("JOB_BASE_NAME");
    public static final String TABLE_ROW = "</td><td>";
}
