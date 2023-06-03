package ro.uaic.info.AddressCorrector.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private String country;
    private String state;
    private String city;

    public void setEntity(String name, NodeType type) {
        switch(type) {
            case COUNTRY -> country = name;
            case STATE -> state = name;
            case CITY -> city = name;
            default -> {}
        }
    }
}
