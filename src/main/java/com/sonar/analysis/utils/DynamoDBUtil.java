package com.sonar.analysis.utils;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sonar.analysis.config.ConfigValue;
import com.sonar.analysis.config.Logger;
import com.sonar.analysis.pojo.AnalysisRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DynamoDBUtil {

    private final DynamoDB dynamoDB;

    public DynamoDBUtil() {
        System.setProperty("aws.java.v1.disableDeprecationAnnouncement", "true");
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(ConfigValue.dynamoEndpoint(), ConfigValue.dynamoRegion()))
                .build();
        this.dynamoDB = new DynamoDB(client);
    }

    public void insertAnalysisRequest(String tableName, AnalysisRequest analysisRequest) {
        Table table = dynamoDB.getTable(tableName);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(analysisRequest);

            Item item = Item.fromJSON(json);
            table.putItem(item);
        } catch (IOException e) {
            Logger.getLogger().error("Failed to insert item: {}", e.getMessage());
        }
    }

    public List<AnalysisRequest> getAnalysisRequest(String tableName, long pullRequestId) {
        Table table = dynamoDB.getTable(tableName);
        List<AnalysisRequest> results = new ArrayList<>();
        try {
            QuerySpec spec = new QuerySpec()
                    .withKeyConditionExpression("pull_request_id = :v1")
                    .withValueMap(new ValueMap().withNumber(":v1", pullRequestId));

            for (Item value : table.query(spec)) {
                AnalysisRequest analysisRequest = new ObjectMapper().readValue(value.toJSON(), AnalysisRequest.class);
                results.add(analysisRequest);
            }
        } catch (IOException e) {
            Logger.getLogger().error("Failed to get item: {}", e.getMessage());
        }
        return results;
    }
}
