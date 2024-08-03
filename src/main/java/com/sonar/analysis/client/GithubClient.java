package com.sonar.analysis.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sonar.analysis.config.ConfigValue;
import com.sonar.analysis.enums.EndPoints;
import com.sonar.analysis.enums.RequestType;
import com.sonar.analysis.utils.JsonUtils;
import com.sonar.analysis.utils.RestUtils;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

import static com.sonar.analysis.constants.SonarConstants.*;

public class GithubClient {
    String gitRepoOwner;
    String repoName;
    String targetBranch;
    String branchAnalysed;
    RestUtils restUtils;

    public GithubClient(){
        this.gitRepoOwner = GIT_REPO_OWNER;
        this.repoName = GIT_REPO_NAME;
        this.targetBranch = GIT_TARGET_BRANCH;
        this.branchAnalysed = GIT_SOURCE_BRANCH;
        this.restUtils = new RestUtils();
    }

    public JsonNode getPullRequestDetails(){
        Map<String,String> queryParam = new HashMap<>();
        queryParam.put("head", this.gitRepoOwner +":"+this.branchAnalysed);
        queryParam.put("base", this.targetBranch);
        queryParam.put("state","open");

        RequestSpecification requestSpecification = RestAssured.given().header("Authorization", "Bearer "+ConfigValue.githubToken())
                .baseUri(ConfigValue.githubEndpoint())
                .basePath(String.format(EndPoints.GET_PULL_REQUEST.getUri(), this.gitRepoOwner, this.repoName))
                .queryParams(queryParam);

        Response response = restUtils.getResponse(requestSpecification, RequestType.GET, 200);
        return JsonUtils.convertJsonStringToJsonNode(response.body().asString());
    }

    public void performAction(long pullRequestId, String commentToBePosted, String event) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("body", commentToBePosted);
        objectNode.put("event", event);

        RequestSpecification requestSpecification = RestAssured.given().header("Authorization", "Bearer "+ConfigValue.githubToken())
                .header("Content-Type","application/json")
                .baseUri(ConfigValue.githubEndpoint())
                .basePath(String.format(EndPoints.GIT_COMMENT_PULL_REQUEST.getUri(), this.gitRepoOwner, this.repoName, pullRequestId))
                .body(JsonUtils.convertObjectNodeToJsonString(objectNode));

        restUtils.getResponse(requestSpecification, RequestType.POST, 200);
    }

    public long getPullRequestId() {
        JsonNode jsonNode = getPullRequestDetails();
        if(jsonNode!=null && !jsonNode.isEmpty()) {
            return jsonNode.get(0).get("number").asLong();
        } else {
            return -1;
        }
    }
}
