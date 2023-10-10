package at.lukas.sebastian.checkr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Checklist {

    private String id;
    private String name;

    private ArrayList<ChecklistItem> items;

    public Checklist(String name) {
        this.name = name;
        this.items = new ArrayList<>();
    }

    public Checklist(String id, String name) {
        this.id = id;
        this.name = name;
        this.items = new ArrayList<>();
    }

    public boolean hasId() {
        return id != null;
    }
}
