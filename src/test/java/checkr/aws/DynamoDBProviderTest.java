package checkr.aws;

import at.lukas.sebastian.checkr.aws.DynamoDBProvider;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URISyntaxException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class DynamoDBProviderTest {

    @Inject
    DynamoDBProvider dynamoDBProvider;

    DynamoDbClient client;

    @AfterEach
    void tearDown() {
        if (client != null) {
            client.close();
        }
    }

    @Test
    void shouldCreateDynamoDbClientInRegion() throws URISyntaxException {
        client = dynamoDBProvider.buildDynamoDbClient();

        assertThat(client, is(notNullValue()));
    }
}
