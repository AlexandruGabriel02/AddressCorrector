package ro.uaic.info.AddressCorrector.models;

import lombok.AllArgsConstructor;
import lombok.Data;

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
}
