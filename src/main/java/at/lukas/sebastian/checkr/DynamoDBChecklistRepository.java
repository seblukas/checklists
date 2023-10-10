package at.lukas.sebastian.checkr;

import at.lukas.sebastian.checkr.aws.DynamoDBProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class DynamoDBChecklistRepository {

    private final String tableName = "checklists";
    @Inject
    protected DynamoDBProvider dynamoDBProvider;

    public Checklist getById(String id) {
        try (DynamoDbClient dynamoDb = dynamoDBProvider.buildDynamoDbClient()) {
            GetItemRequest request = getRequest(id);
            GetItemResponse getItemResponse = dynamoDb.getItem(request);

            if (!getItemResponse.hasItem()) {
                throw generateNotFoundException(id);
            }

            Map<String, AttributeValue> item = getItemResponse.item();
            return transformToChecklist(item);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Checklist save(Checklist checklist) {
        try (DynamoDbClient dynamoDb = dynamoDBProvider.buildDynamoDbClient()) {
            PutItemRequest putRequest = putRequest(checklist);
            dynamoDb.putItem(putRequest);

            return checklist;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String id) {
        try (DynamoDbClient dynamoDb = dynamoDBProvider.buildDynamoDbClient()) {
            DeleteItemRequest deleteItemRequest = deleteRequest(id);
            dynamoDb.deleteItem(deleteItemRequest);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Checklist> getAll() {
        try (DynamoDbClient dynamoDb = dynamoDBProvider.buildDynamoDbClient()) {
            ScanRequest scanRequest = getScanRequest();

            ScanResponse scanResponse = dynamoDb.scan(scanRequest);
            Collection<Map<String, AttributeValue>> items = scanResponse.items();

            return items.stream()
                    .map(this::transformToChecklist)
                    .collect(Collectors.toList());

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private GetItemRequest getRequest(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("PK", AttributeValue.builder().s(id).build());
        key.put("SK", AttributeValue.builder().s(id).build());

        return GetItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .attributesToGet("PK", "name")
                .build();
    }

    private PutItemRequest putRequest(Checklist checklist) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("PK", AttributeValue.builder().s(checklist.getId()).build());
        item.put("SK", AttributeValue.builder().s(checklist.getId()).build());
        item.put("name", AttributeValue.builder().s(checklist.getName()).build());

        return PutItemRequest.builder()
                .tableName(tableName)
                .item(item)
                .build();
    }

    private DeleteItemRequest deleteRequest(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("PK", AttributeValue.builder().s(id).build());
        key.put("SK", AttributeValue.builder().s(id).build());

        return DeleteItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .build();
    }

    private ScanRequest getScanRequest() {
        return ScanRequest.builder()
                .tableName(tableName)
                .build();
    }

    private NotFoundException generateNotFoundException(String id) {
        return new NotFoundException("Checklist with id " + id + " not found");
    }

    private Checklist transformToChecklist(Map<String, AttributeValue> item) {
        String name = item.get("name").s();
        String storedId = item.get("PK").s();
        return new Checklist(storedId, name);
    }

    public void deleteAll() {
        try (DynamoDbClient dynamoDb = dynamoDBProvider.buildDynamoDbClient()) {
            ScanRequest scanRequest = getScanRequest();

            ScanResponse scanResponse = dynamoDb.scan(scanRequest);
            Collection<Map<String, AttributeValue>> items = scanResponse.items();

            items.forEach(item -> {
                String id = item.get("PK").s();
                DeleteItemRequest deleteItemRequest = deleteRequest(id);
                dynamoDb.deleteItem(deleteItemRequest);
            });

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
