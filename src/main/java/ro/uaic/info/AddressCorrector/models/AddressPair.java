package ro.uaic.info.AddressCorrector.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddressPair {
    private Address inputAddress;
    private Address expectedAddress;
}
