package com.sonar.analysis.helpers;

import com.sonar.analysis.client.SonarClient;
import com.sonar.analysis.pojo.AnalysisRequest;
import com.sonar.analysis.pojo.SonarQubeIssuesResponse;
import com.sonar.analysis.utils.DynamoDBUtil;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.sonar.analysis.constants.SonarConstants.DYNAMO_SONAR_TABLE;
import static com.sonar.analysis.constants.SonarConstants.GIT_REPO_NAME;

public class SonarIssueHelper {
    long pullRequestId;

    public SonarIssueHelper(long pullRequestId){
        this.pullRequestId = pullRequestId;
    }
    public List<SonarQubeIssuesResponse.Issues> issuesList() {
        persistNewIssues(pullRequestId);
        return getListOfUnResolvedIssues(pullRequestId);
    }

    public List<SonarQubeIssuesResponse.Issues> getListOfUnResolvedIssues(long pullRequestId){
        DynamoDBUtil dynamoDBUtil = new DynamoDBUtil();
        List<AnalysisRequest> allIssuesPertainingToPullRequest = dynamoDBUtil.getAnalysisRequest(DYNAMO_SONAR_TABLE, pullRequestId);

        Set<SonarQubeIssuesResponse.Issues> analysisRequestSet = allIssuesPertainingToPullRequest.stream().flatMap(analysisRequest -> {
            if(analysisRequest!=null && analysisRequest.getIssues()!=null){
                return Arrays.stream(analysisRequest.getIssues().getIssues());
            } else {
                return Stream.empty();
            }
        }).collect(Collectors.toSet());

        SonarClient sonarClient = new SonarClient();
        SonarQubeIssuesResponse sonarQubeIssuesResponse = sonarClient.getAllIssuesFromSonarQube();
        List<String> allKeys = Arrays.stream(sonarQubeIssuesResponse.getIssues()).map(SonarQubeIssuesResponse.Issues::getKey).toList();

        return analysisRequestSet.stream().filter(s->allKeys.stream().anyMatch(s1->s1.equals(s.getKey()))).toList();
    }

    public void persistNewIssues(long pullRequestId){
        DynamoDBUtil dynamoDBUtil = new DynamoDBUtil();
        AnalysisRequest analysisRequest = getIssueToBePersisted(pullRequestId);
        dynamoDBUtil.insertAnalysisRequest(DYNAMO_SONAR_TABLE, analysisRequest);
    }

    public AnalysisRequest getIssueToBePersisted(long pullRequestId){
        SonarClient sonarClient = new SonarClient();
        SonarQubeIssuesResponse sonarQubeIssuesResponse = sonarClient.getIssuesFromSonarQube();
        AnalysisRequest analysisRequest = new AnalysisRequest();
        analysisRequest.setIssues(sonarQubeIssuesResponse);
        analysisRequest.setPullRequestId(pullRequestId);
        analysisRequest.setAnalysedAt(LocalTime.now().toString());
        analysisRequest.setRepositoryName(GIT_REPO_NAME);
        return analysisRequest;
    }


}
