package at.lukas.sebastian.checkr;

import java.util.List;

public interface ChecklistRepository {

    Checklist getById(String id);

    Checklist save(Checklist checklist);

    void delete(String id);

    void deleteAll();

    List<Checklist> getAll();
}
