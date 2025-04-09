package com.text;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

public class Main implements RequestHandler<S3Event, String> {

    private final SnsClient snsClient = SnsClient.create();
    private final String topicArn = System.getenv("TOPIC_ARN");

    @Override
    public String handleRequest(S3Event s3Event, Context context) {
        try{
            String bucket = s3Event.getRecords().get(0).getS3().getBucket().getName();
            String key = s3Event.getRecords().get(0).getS3().getObject().getKey();

            String message = String.format("New file uploaded: %s/%s", bucket, key);

            PublishRequest request = PublishRequest.builder()
                    .topicArn(topicArn)
                    .message(message)
                    .build();

            snsClient.publish(request);
            return "{\"statusCode\": 200, \"body\": \"Notification sent successfully\"}" + message;
        }
        catch (Exception e) {
            context.getLogger().log("Error: " + e.getMessage());
            return "{\"statusCode\": 500, \"body\": \"" + e.getMessage() + "\"}";
        }
    }
}