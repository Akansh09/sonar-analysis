package com.sonar.analysis.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sonar.analysis.config.Logger;


public class JsonUtils {
    private JsonUtils(){
    }

    public static JsonNode convertJsonStringToJsonNode(String jsonString){
        try {
            return new ObjectMapper().readTree(jsonString);
        } catch (JsonProcessingException e) {
            Logger.getLogger().error("Failed to convert json string to json node: {}", e.getMessage());
        }
        return null;
    }

    public static String convertObjectNodeToJsonString(ObjectNode jsonNode) {
        try {
            return new ObjectMapper().writeValueAsString(jsonNode);
        } catch (JsonProcessingException e) {
            Logger.getLogger().error("Failed to convert object node to json string: {}", e.getMessage());
        }
        return null;
    }
}
