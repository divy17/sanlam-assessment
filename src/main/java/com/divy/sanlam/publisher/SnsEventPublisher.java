package com.divy.sanlam.publisher;

import com.divy.sanlam.event.WithdrawalEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Component
@Profile("!mock")
public class SnsEventPublisher implements EventPublisher {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Executor executor = Executors.newFixedThreadPool(2);
    private final SnsClient snsClient;
    private final String topicArn;

    public SnsEventPublisher(@Value("${aws.region}") String region,
                             @Value("${aws.sns.topic.arn}") String topicArn) {
        this.snsClient = SnsClient.builder()
                .region(Region.of(region))
                .build();
        this.topicArn = topicArn;
    }

    @Override
    public void publish(WithdrawalEvent event) {
        CompletableFuture.runAsync(() -> {
            int maxAttempts = 3;
            long backoff = 1000;

            for (int attempt = 1; attempt <= maxAttempts; attempt++) {
                try {
                    String message = objectMapper.writeValueAsString(event);
                    PublishRequest request = PublishRequest.builder()
                            .topicArn(topicArn)
                            .message(message)
                            .build();

                    PublishResponse response = snsClient.publish(request);
                    System.out.println("✅ SNS Publish Success - MessageId: " + response.messageId());
                    break;

                } catch (Exception e) {
                    System.err.println("⚠️ SNS Publish Attempt " + attempt + " failed: " + e.getMessage());

                    if (attempt == maxAttempts) {
                        System.err.println("❌ All SNS publish attempts failed.");
                        break;
                    }

                    try {
                        Thread.sleep(backoff);
                    } catch (InterruptedException ignored) {
                        Thread.currentThread().interrupt();
                        break;
                    }

                    backoff *= 2;
                }
            }
        }, executor);
    }
}
