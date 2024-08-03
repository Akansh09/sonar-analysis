package com.sonar.analysis.helpers;

import com.sonar.analysis.client.GithubClient;
import com.sonar.analysis.config.Logger;
import com.sonar.analysis.pojo.SonarQubeIssuesResponse;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.sonar.analysis.constants.SonarConstants.TABLE_ROW;

public class GithubHelper {
    long pullRequestId;
    List<SonarQubeIssuesResponse.Issues> issues;

    public GithubHelper(long pullRequestId,  List<SonarQubeIssuesResponse.Issues> issues){
        this.issues = issues;
        this.pullRequestId = pullRequestId;
    }


    public void performAction(){
        GithubClient githubClient = new GithubClient();
        Logger.getLogger().info("Issues Found : {}", issues.size());
        if(issues.isEmpty()){
            Logger.getLogger().info("Approving the PR");
            //Approve the PR
            githubClient.performAction(pullRequestId, "No Issue Found", "APPROVE");
        } else {
            Logger.getLogger().info("Requesting Changes");
            /* Format Those issues in table format */
            String comments = formatIssuesInTabularHTMLFormat();
            Logger.getLogger().info("Comments : {}", comments);
            /* Post those issues on the PR */
            githubClient.performAction(pullRequestId, comments, "REQUEST_CHANGES");
        }
    }

    public String formatIssuesInTabularHTMLFormat(){
        StringBuilder issuesStr = new StringBuilder();
        issuesStr.append("<table>");
        String heading = "<tr><th>Issue</th><th>File</th><th>Line Number</th><th>Severity</th></tr>";
        issuesStr.append(heading);
        for (SonarQubeIssuesResponse.Issues issue : issues) {
            String file = issue.getComponent().replace(issue.getProject() + ":", "").trim();
            String message = issue.getMessage();
            String author = issue.getSeverity();
            String lineNumber = lineNumber(issue.getTextRange());
            String sanitizedMessage = message.replace("\"", "\\\"").replace("|", "\\|").replace("<","&lt;").replace(">","&gt;");
            issuesStr.append("<tr><td>").append(sanitizedMessage).append(TABLE_ROW)
                    .append(file).append(TABLE_ROW).append(lineNumber).append(TABLE_ROW).append(author).append("</td></tr>");
        }
        issuesStr.append("</table>");
        return issuesStr.toString();
    }

    public String lineNumber(SonarQubeIssuesResponse.TextRange textRange){
        if (Objects.nonNull(textRange)) {
            Integer startLine = Optional.ofNullable(textRange.getStartLine()).orElse(0);
            Integer endLine = textRange.getEndLine();

            if (Objects.equals(startLine, endLine)) {
                 return startLine.toString();
            } else {
                return "Start : " + startLine + " - End : " + endLine;
            }

        }
        return "";
    }
}
