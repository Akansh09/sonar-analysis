package com.sonar.analysis.utils;

import com.sonar.analysis.enums.RequestType;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestUtils {
    public Response getResponse(RequestSpecification requestSpecification, RequestType requestType, Integer statusCode){
        RequestSpecification requestSpecificationVal = RestAssured.given().spec(requestSpecification);
        return switch (requestType) {
            case GET -> requestSpecificationVal.log().all().get().then().statusCode(statusCode).extract().response();
            case POST -> requestSpecificationVal.log().all().post().then().statusCode(statusCode).extract().response();
        };
    }
}
