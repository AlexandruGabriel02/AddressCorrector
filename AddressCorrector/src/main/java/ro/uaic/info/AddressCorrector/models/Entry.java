package ro.uaic.info.AddressCorrector.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import ro.uaic.info.AddressCorrector.database.Node;
@Data
@AllArgsConstructor
public class Entry {
    private Node node;
    private String name;

    private boolean isOnCorrectField;
}
