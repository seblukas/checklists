package checkr;

import at.lukas.sebastian.checkr.Checklist;
import at.lukas.sebastian.checkr.ChecklistRepository;
import at.lukas.sebastian.checkr.InMemoryChecklistRepository;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InMemoryChecklistTest {

    ChecklistRepository repository = new InMemoryChecklistRepository();

    String dummyName = "My first Checklist";

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void shouldSaveNewChecklist() {
        Checklist checklist = new Checklist(null, dummyName);

        repository.save(checklist);

        assertThat(repository.getAll(), hasItem(checklist));
    }

    @Test
    void  savedChecklistShouldHaveId() {
        Checklist checklist = new Checklist(null, dummyName);

        Checklist savedChecklist = repository.save(checklist);

        assertThat(savedChecklist.getId(), is("cl#1"));
    }

    @Test
    void shouldInsertTwoChecklists() {
        Checklist checklist1 = new Checklist(null, dummyName);
        Checklist checklist2 = new Checklist(null, dummyName);

        repository.save(checklist1);
        repository.save(checklist2);

        assertThat(repository.getAll().size(), is(2));
    }

    @Test
    void shouldGetCheckListById() {
        Checklist checklist = new Checklist(null, dummyName);
        Checklist savedChecklist = repository.save(checklist);

        Checklist foundChecklist = repository.getById(savedChecklist.getId());

        assertThat(foundChecklist, is(savedChecklist));
    }

    @Test
    void shouldUpdateNameOfChecklist() {
        Checklist checklist = new Checklist(null, dummyName);
        Checklist savedChecklist = repository.save(checklist);
        savedChecklist.setName("My second Checklist");

        Checklist updatedChecklist = repository.save(savedChecklist);

        Checklist persistedChecklist = repository.getById(updatedChecklist.getId());
        assertThat(persistedChecklist.getName(), is("My second Checklist"));
    }

    @Test
    void shouldDeleteSingleChecklist() {
        Checklist checklist = new Checklist(null, dummyName);
        Checklist savedChecklist = repository.save(checklist);

        repository.delete(savedChecklist.getId());

        assertThat(repository.getAll().size(), is(0));
    }

    @Test
    void shouldDeleteAllChecklists() {
        Checklist checklist1 = new Checklist(null, dummyName);
        Checklist checklist2 = new Checklist(null, dummyName);
        repository.save(checklist1);
        repository.save(checklist2);

        repository.deleteAll();

        assertThat(repository.getAll().size(), is(0));
    }

    @Test
    void shouldThrowNotFoundExceptionIfChecklistDoesNotExist() {
        assertThrows(NotFoundException.class, () -> repository.getById("cl#100"));
    }

    @Test
    void shouldThrowNotFoundExceptionIfChecklistToUpdateDoesNotExist() {
        Checklist checklist = new Checklist("cl#100", dummyName);

        assertThrows(NotFoundException.class, () -> repository.save(checklist));
    }
}
