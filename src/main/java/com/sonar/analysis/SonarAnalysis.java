package com.sonar.analysis;

import com.sonar.analysis.client.GithubClient;
import com.sonar.analysis.config.Logger;
import com.sonar.analysis.helpers.GithubHelper;
import com.sonar.analysis.helpers.SonarIssueHelper;
import com.sonar.analysis.pojo.SonarQubeIssuesResponse;

import java.util.List;

public class SonarAnalysis {


    public static void main(String[] args) {
        GithubClient githubClient = new GithubClient();
        /* Get Corresponding Pull Request Id Source --> Target */
        long pullRequestId = githubClient.getPullRequestId();
        if(pullRequestId==-1) return;
        Logger.getLogger().info("Pull Request Id : {}",  pullRequestId);

        /* Get All Those unresolved issues which are present in the SonarQube for this PR */
        SonarIssueHelper sonarIssueHelper = new SonarIssueHelper(pullRequestId);
        List<SonarQubeIssuesResponse.Issues> issues = sonarIssueHelper.issuesList();

        /* Perform Action */
        GithubHelper githubHelper = new GithubHelper(pullRequestId, issues);
        githubHelper.performAction();
    }
}
