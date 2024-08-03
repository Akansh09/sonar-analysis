package com.sonar.analysis.client;

import com.sonar.analysis.config.ConfigValue;
import com.sonar.analysis.enums.EndPoints;
import com.sonar.analysis.enums.RequestType;
import com.sonar.analysis.pojo.SonarQubeIssuesResponse;
import com.sonar.analysis.utils.RestUtils;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

import static com.sonar.analysis.constants.SonarConstants.SONAR_PROJECT_KEY;

public class SonarClient {
    String sonarProject;
    RestUtils restUtils;
    String maxCount = "500";

    public SonarClient(){
        this.sonarProject = SONAR_PROJECT_KEY;
        this.restUtils = new RestUtils();
    }

    public SonarQubeIssuesResponse getIssuesFromSonarQube() {
        Map<String,String> queryParams = new HashMap<>();
        queryParams.put("componentKeys", this.sonarProject);
        queryParams.put("ps", this.maxCount);
        queryParams.put("onlyMine", "false");
        queryParams.put("sinceLeakPeriod", "true");

        RequestSpecification requestSpecification = RestAssured.given()
                .baseUri(ConfigValue.sonarEndpoint())
                .basePath(EndPoints.SONARQUBE_ISSUES.getUri())
                .auth().preemptive().basic(ConfigValue.sonarUsername(), ConfigValue.sonarPassword())
                .queryParams(queryParams);

        Response response = restUtils.getResponse(requestSpecification, RequestType.GET, 200);
        return response.as(SonarQubeIssuesResponse.class);
    }

    public SonarQubeIssuesResponse getAllIssuesFromSonarQube() {
        Map<String,String> queryParams = new HashMap<>();
        queryParams.put("componentKeys", this.sonarProject);
        queryParams.put("ps", this.maxCount);
        queryParams.put("onlyMine", "false");
        queryParams.put("statuses", "OPEN,CONFIRMED,REOPENED");

        RequestSpecification requestSpecification = RestAssured.given().baseUri(ConfigValue.sonarEndpoint())
                .basePath(EndPoints.SONARQUBE_ISSUES.getUri())
                .auth().preemptive().basic(ConfigValue.sonarUsername(), ConfigValue.sonarPassword())
                .queryParams(queryParams);

        Response response = restUtils.getResponse(requestSpecification, RequestType.GET, 200);
        return response.as(SonarQubeIssuesResponse.class);
    }
}
