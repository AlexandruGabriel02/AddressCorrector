package ro.uaic.info.AddressCorrector.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import ro.uaic.info.AddressCorrector.database.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
public class NormalizedAddress {
    private List<Entry> countries;
    private List<Entry> states;
    private List<Entry> cities;


    public NormalizedAddress() {
        this.countries = new ArrayList<>();
        this.states = new ArrayList<>();
        this.cities = new ArrayList<>();
    }

    public void add(Entry entry, NodeType type) {
        switch (type) {
            case COUNTRY -> countries.add(entry);
            case STATE -> states.add(entry);
            case CITY -> cities.add(entry);
            default -> {
            }
        }
    }

    private List<Entry> getCorrespondingList(NodeType type) {
        return switch (type) {
            case COUNTRY -> countries;
            case STATE -> states;
            case CITY -> cities;
            default -> null;
        };
    }

    public Optional<Entry> getContainingNode(Node node) {
        Entry entry = null;
        for (Entry candEntry : getCorrespondingList(node.getType())) {
            if (candEntry.getNode().equals(node)) {
                entry = candEntry;
                break;
            }
        }
        return Optional.ofNullable(entry);
    }
}
