package ro.uaic.info.AddressCorrector.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import ro.uaic.info.AddressCorrector.database.Node;

import java.util.ArrayList;
import java.util.List;

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
            case COUNTRY:
                countries.add(entry);
                break;
            case STATE:
                states.add(entry);
                break;
            case CITY:
                cities.add(entry);
                break;
            default:
                break;
        }
    }
    public Entry containsStateNode(Node state) {
        for (Entry entry : states) {
            if (entry.getNode().equals(state)) {
                return entry;
            }
        }
        return null;
    }

    public Entry containsCountryNode(Node state) {
        for (Entry entry : countries) {
            if (entry.getNode().equals(state)) {
                return entry;
            }
        }
        return null;
    }
}
