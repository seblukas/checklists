package checkr;

import at.lukas.sebastian.checkr.Checklist;
import at.lukas.sebastian.checkr.DynamoDBChecklistRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
public class DynamoDBChecklistRepositoryTest {

    @Inject
    DynamoDBChecklistRepository repository;

    @Test
    void shouldGetChecklistById() {
        Checklist checklist = repository.getById("cl#1");
        assertThat(checklist.getId(), is("cl#1"));
    }

    @Test
    void shouldThrowIfChecklistDoesNotExist() {
        assertThrows(NotFoundException.class, () -> repository.getById("cl#100"));
    }

    @Test
    void shouldSaveChecklist() {
        String id = "cl#2";
        Checklist checklist = new Checklist(id, "Checklist 2");
        Checklist savedChecklist = repository.save(checklist);

        Checklist persistedChecklist = repository.getById(id);
        assertThat(savedChecklist.getId(), is(persistedChecklist.getId()));
    }

    @Test
    void shouldDeleteChecklist() {
        String id = "cl#3";
        Checklist checklist = new Checklist(id, "Checklist 3");
        Checklist savedChecklist = repository.save(checklist);

        repository.delete(id);
        assertThrows(NotFoundException.class, () -> repository.getById(savedChecklist.getId()));
    }

    @Test
    void shouldGetAllChecklists() {
        Checklist checklist1 = new Checklist("cl#1", "Checklist #1");
        Checklist checklist2 = new Checklist("cl#2", "Checklist #2");
        repository.save(checklist1);
        repository.save(checklist2);

        assertThat(repository.getAll().size(), is(2));
    }

    @Test
    void shouldDeleteAllChecklists() {
        Checklist checklist1 = new Checklist("cl#1", "Checklist #1");
        Checklist checklist2 = new Checklist("cl#2", "Checklist #2");
        repository.save(checklist1);
        repository.save(checklist2);

        repository.deleteAll();

        assertThat(repository.getAll(), is(empty()));

    }

}
