package com.sonar.analysis.config;

public class ConfigValue {
        private ConfigValue() {
        }

        public static String sonarEndpoint(){
                return System.getenv("SONARQUBE_URL");
        }

        public static String sonarUsername(){
                return System.getenv("SONARQUBE_USERNAME");
        }

        public static String sonarPassword(){
                return System.getenv("SONARQUBE_PASSWORD");
        }

        public static String dynamoEndpoint(){
                return System.getenv("DYNAMO_ENDPOINT");
        }

        public static String dynamoRegion(){
                return System.getenv("DYNAMO_REGION");
        }

        public static String githubEndpoint(){
                return System.getenv("GITHUB_ENDPOINT");
        }

        public static String githubToken(){
                return System.getenv("GITHUB_TOKEN");
        }
}
