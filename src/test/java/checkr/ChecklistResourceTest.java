package checkr;

import at.lukas.sebastian.checkr.Checklist;
import at.lukas.sebastian.checkr.DynamoDBChecklistRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class ChecklistResourceTest {

    String basePath = "/api";
    @Inject
    DynamoDBChecklistRepository checklistRepository;
    Checklist myFirstChecklist = new Checklist("cl#MyfirstChecklist", "My first Checklist");
    Checklist mySecondChecklist = new Checklist("cl#MysecondChecklist", "My second Checklist");
    Checklist expectedChecklist1 = new Checklist("cl#MyfirstChecklist", "My first Checklist");
    Checklist expectedChecklist2 = new Checklist("cl#MysecondChecklist", "My second Checklist");
    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void init() {
        checklistRepository.deleteAll();
        checklistRepository.save(myFirstChecklist);
        checklistRepository.save(mySecondChecklist);
    }

    @Test
    public void shouldGetAllChecklists() throws JsonProcessingException {
        String json = mapper.writeValueAsString(List.of(expectedChecklist1, expectedChecklist2));

        given()
                .when()
                .get(basePath + "/checklists")
                .then()
                .statusCode(200)
                .body(is(json));
    }

    @Test
    public void shouldGetFirstChecklist() throws JsonProcessingException {
        String json = mapper.writeValueAsString(expectedChecklist1);

        given()
                .when()
                .get(basePath + "/checklists/cl#MyfirstChecklist")
                .then()
                .statusCode(200)
                .body(is(json));
    }

    @Test
    public void shouldGetSecondChecklist() throws JsonProcessingException {
        String json = mapper.writeValueAsString(expectedChecklist2);

        given()
                .when()
                .get(basePath + "/checklists/cl#MysecondChecklist")
                .then()
                .statusCode(200)
                .body(is(json));
    }

    @Test
    public void shouldThrowNotFoundException() {
        given()
                .when()
                .get(basePath + "/checklists/400")
                .then()
                .statusCode(404);
    }

    @Test
    public void shouldCreateNewChecklist() throws JsonProcessingException {
        String requestBody = mapper.writeValueAsString(new Checklist(null, "My third Checklist"));
        String responseBody = mapper.writeValueAsString(new Checklist("cl#MythirdChecklist", "My third Checklist"));

        given()
                .when()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post(basePath + "/checklists")
                .then()
                .statusCode(201)
                .body(is(responseBody));
    }
}
