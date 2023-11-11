package at.lukas.sebastian.checkr;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.ResponseStatus;

import java.util.List;

@Path("api/checklists")
public class ChecklistResource {

    @Inject
    protected ChecklistRepository checklistRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Checklist> getAll() {
        return checklistRepository.getAll();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Checklist get(String  id) {
        return checklistRepository.getById(id);
    }

    @POST
    @ResponseStatus(201)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Checklist createNewChecklist(Checklist checklist) {
        String id = "cl#" + checklist.getName().replaceAll(" ", "");
        checklist.setId(id);
        return checklistRepository.save(checklist);
    }
}
