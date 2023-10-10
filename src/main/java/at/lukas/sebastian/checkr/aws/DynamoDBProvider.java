package at.lukas.sebastian.checkr.aws;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@ApplicationScoped
public class DynamoDBProvider {

    @ConfigProperty(name = "quarkus.dynamodb.aws.region")
    String region;

    @ConfigProperty(name = "quarkus.dynamodb.endpoint-override")
    String endpointUrl;

    @ConfigProperty(name = "quarkus.dynamodb.aws.credentials.static-provider.access-key-id")
    Optional<String> accessKey;

    @ConfigProperty(name = "quarkus.dynamodb.aws.credentials.static-provider.secret-access-key")
    Optional<String> secretKey;

    @SuppressWarnings("OptionalGetWithoutIsPresent") // Reason: It is checked in the isValid methods
    public DynamoDbClient buildDynamoDbClient() throws URISyntaxException {
        DynamoDbClientBuilder dynamoDbClientBuilder = DynamoDbClient.builder()
                .region(Region.of(region))
                .endpointOverride(new URI(endpointUrl));

        if (isValidAccessKey() && isValidSecretKey()) {
            AwsCredentials credentials = new SimpleAwsCredentials(accessKey.get(), secretKey.get());
            dynamoDbClientBuilder.credentialsProvider(() -> credentials);
        }

        return dynamoDbClientBuilder
                .build();
    }

    private boolean isValidAccessKey() {
        return accessKey.isPresent() && !accessKey.get().isEmpty();
    }

    private boolean isValidSecretKey() {
        return secretKey.isPresent() && !secretKey.get().isEmpty();
    }
}
