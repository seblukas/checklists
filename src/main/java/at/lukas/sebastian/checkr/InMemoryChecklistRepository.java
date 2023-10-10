package at.lukas.sebastian.checkr;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class InMemoryChecklistRepository implements ChecklistRepository {

    private static final AtomicInteger idGenerator = new AtomicInteger(0);

    private final List<Checklist> checklists;

    public InMemoryChecklistRepository() {
        checklists = new ArrayList<>();
    }

    public List<Checklist> getAll() {
        return checklists;
    }

    public Checklist getById(String id) {
        return checklists.stream()
                .filter(checklist -> Objects.equals(checklist.getId(), id))
                .findFirst()
                .orElseThrow(() -> generateNotFoundException(id));
    }

    public Checklist save(Checklist checklist) {
        if (!checklist.hasId()) {
            insertNewChecklist(checklist);
        } else {
            updateExistingChecklist(checklist);
        }
        return checklist;
    }

    public void delete(String id) {
        checklists.removeIf(checklist -> checklist.getId().equals(id));
    }

    public void deleteAll() {
        InMemoryChecklistRepository.idGenerator.set(0);
        checklists.clear();
    }

    private void insertNewChecklist(Checklist checklist) {
        checklist.setId("cl#" + idGenerator.incrementAndGet());
        checklists.add(checklist);
    }

    private void updateExistingChecklist(Checklist checklist) {
        int index = getIndexOfChecklist(checklist.getId());
        if (index == -1) {
            throw generateNotFoundException(checklist.getId());
        }
        checklists.set(index, checklist);
    }

    private int getIndexOfChecklist(String id) {
        return checklists.indexOf(getById(id));
    }

    private static NotFoundException generateNotFoundException(String id) {
        return new NotFoundException("Checklist with id " + id + " not found");
    }
}
