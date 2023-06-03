package ro.uaic.info.AddressCorrector.crossfields;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import ro.uaic.info.AddressCorrector.models.Address;
import ro.uaic.info.AddressCorrector.models.AddressPair;
import ro.uaic.info.AddressCorrector.services.AddressService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;


@Log4j2
public abstract class AbstractCrossFieldTests {

    private static final int ADDRESS_FIELDS = 3;

    @Autowired
    private AddressService addressService;

    abstract Address getExpectedAddress();
    abstract Address getInputAddress();

    Set<AddressPair> getCrossFieldAddresses() {
        Address correctAddress = getInputAddress();
        Set<AddressPair> crossFieldAddresses = new HashSet<>();

        for (int countryIndex = 0; countryIndex < ADDRESS_FIELDS; countryIndex++) {
            for (int stateIndex = 0; stateIndex < ADDRESS_FIELDS; stateIndex++) {
                for (int cityIndex = 0; cityIndex < ADDRESS_FIELDS; cityIndex++) {
                    StringBuilder[] addressFields = new StringBuilder[ADDRESS_FIELDS];
                    for (int i = 0; i < ADDRESS_FIELDS; i++) {
                        addressFields[i] = new StringBuilder(" ");
                    }

                    addressFields[countryIndex].append(correctAddress.getCountry()).append(" ");
                    addressFields[stateIndex].append(correctAddress.getState()).append(" ");
                    addressFields[cityIndex].append(correctAddress.getCity()).append(" ");

                    crossFieldAddresses.add(new AddressPair(new Address(addressFields[0].toString().trim(), addressFields[1].toString().trim(), addressFields[2].toString().trim()), getExpectedAddress()));
                }
            }
        }

        return crossFieldAddresses;
    }

    @TestFactory
    Stream<DynamicTest> dynamicTestAddress() {
        return getCrossFieldAddresses().stream().map(
                arguments -> DynamicTest.dynamicTest(
                        "Test address by cross-fielding. Input: " + arguments.getInputAddress().toString() + ". Expected: " + arguments.getExpectedAddress().toString(),
                        () -> givenModifiedAddressThroughCrossField_thenReturnCorrectAddress(
                                arguments.getInputAddress(),
                                arguments.getExpectedAddress()
                        )
                )
        );
    }

    void givenModifiedAddressThroughCrossField_thenReturnCorrectAddress(Address inputAddress, Address expectedAddress) {
        List<Address> correctedAddresses = addressService
                .getCorrectedAddress(inputAddress)
                .stream()
                .map(correctedAddress -> (Address) correctedAddress)
                .toList();

        log.info("Input address: " + inputAddress.toString());
        log.info("Expected address: " + expectedAddress.toString());
        log.info("One of the corrected addresses: " + correctedAddresses.get(0).toString());
        assertTrue(correctedAddresses.contains(expectedAddress));
    }
}
