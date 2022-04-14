package com.skilltracker.api.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

;

@Configuration
public class DynamoDBConfig {

    @Value("${amazon.aws.region}")
    private String region;

    @Value("${amazon.dynamodb.endpoint}")
    private String dynamodbendpoint;
    @Value("${amazon.aws.accesskey}")
    private String accesskey;
    @Value("${amazon.aws.secretkey}")

    private String secretkey;
    @Autowired
    ObjectMapper objectMapper;
    private static final String AWS_ACCESS_KEY_ID = "AWS_ACCESS_KEY_ID";
    private static final String AWS_SECRET_ACCESS_KEY = "AWS_SECRET_ACCESS_KEY";
    private static final String AWS_DYNAMODB_ENDPOINT = "AWS_DYNAMODB_ENDPOINT";

    @Value("${amazon.aws.secretsmanager}")
    private String AWS_SECRET_NAME;
    private String AWS_REGION = "us-east-1";

    @Bean
    public DynamoDBMapper mapper() {
        return new DynamoDBMapper(amazonDynamoDBConfig());
    }

    private AmazonDynamoDB amazonDynamoDBConfig() {
       // getSecretsFormSecretManger();
        return AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(dynamodbendpoint, region))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accesskey, secretkey))).build();
    }

    private String getSecret() {
        // Create a Secrets Manager client
        AWSSecretsManager client = AWSSecretsManagerClientBuilder.standard().withRegion(AWS_REGION).build();

        String secret = null;
        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest().withSecretId(AWS_SECRET_NAME);
        GetSecretValueResult getSecretValueResult = null;

        try {
            getSecretValueResult = client.getSecretValue(getSecretValueRequest);
            if (getSecretValueResult != null && getSecretValueResult.
                    getSecretString() != null) {
                secret = getSecretValueResult.getSecretString();
            }
        } catch (DecryptionFailureException | InternalServiceErrorException
                | InvalidParameterException
                | InvalidRequestException | ResourceNotFoundException e) {

            return null;
        }
        return secret;
    }

    public void getSecretsFormSecretManger() {
        String secretJson = getSecret();
        accesskey = getString(secretJson, AWS_ACCESS_KEY_ID);
        secretkey = getString(secretJson, AWS_SECRET_ACCESS_KEY);
        dynamodbendpoint = getString(secretJson, AWS_DYNAMODB_ENDPOINT);
    }

    private String getString(String json, String path) {
        try {
            JsonNode root = objectMapper.readTree(json);
            return root.path(path).asText();
        } catch (IOException e) {

            return null;
        }
    }

}
