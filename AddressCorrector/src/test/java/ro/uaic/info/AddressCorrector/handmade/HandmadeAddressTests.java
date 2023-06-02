package ro.uaic.info.AddressCorrector.handmade;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ro.uaic.info.AddressCorrector.models.Address;
import ro.uaic.info.AddressCorrector.services.AddressService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class HandmadeAddressTests {
    @Autowired
    private AddressService addressService;

    void assertAddress(Address inputAddress, Address expectedAddress) {
        List<Address> correctedAddresses = addressService
                .getCorrectedAddress(inputAddress)
                .stream()
                .map(correctedAddress -> (Address) correctedAddress)
                .toList();

        assertTrue(correctedAddresses.contains(expectedAddress));
    }
    @Test
    void givenRomanianAndItalianAddress_whenRomaniaInForeignLanguage_thenShouldReturnRomanianAddress() {
        String countryField = "Романија;   France";
        String stateField = "galati ,,random_garbage1244,, lombardia";
        String cityField = "tecuci, milano";

        Address inputAddress = new Address(countryField, stateField, cityField);
        Address expectedAddress = new Address("România", "Galaţi", "Tecuci");

        assertAddress(inputAddress, expectedAddress);
    }

    @Test
    void givenSpanishCity_whenNoOtherCitiesExist_thenShouldReturnSpanishAddress() {
        String countryField = "roumania";
        String stateField = "dolj";
        String cityField = "barcelona  ;;,;";

        Address inputAddress = new Address(countryField, stateField, cityField);
        Address expectedAddress = new Address("Kingdom of Spain", "Catalunya", "Barcelona");

        assertAddress(inputAddress, expectedAddress);
    }

    @Test
    void givenDenmarkCapital_whenLanguageIsForeignAndOnCountryField_thenShouldReturnDanishAddress() {
        String countryField = "København";
        String stateField = "-";
        String cityField = "-";

        Address inputAddress = new Address(countryField, stateField, cityField);
        Address expectedAddress = new Address("Kingdom of Denmark", "Region Hovedstaden", "Copenhagen");

        assertAddress(inputAddress, expectedAddress);
    }

    @Test
    void givenRomanianAndFrenchAddress_whenRomanianFieldsOnCorrectPosition_thenShouldReturnRomanianAddress() {
        String countryField = "romania paris";
        String stateField = "iasi ile-de-france";
        String cityField = "pascani franta";

        Address inputAddress = new Address(countryField, stateField, cityField);
        Address expectedAddress = new Address("România", "Iaşi", "Paşcani");

        assertAddress(inputAddress, expectedAddress);
    }

    @Test
    void givenRomanianAndFrenchAddress_whenFrenchFieldsOnCorrectPosition_thenShouldReturnFrenchAddress() {
        String countryField = "pascani - franta";
        String stateField = "iasi ; ile-de-france";
        String cityField = "romania ,,paris";

        Address inputAddress = new Address(countryField, stateField, cityField);
        Address expectedAddress = new Address("Republic of France", "Île-de-France", "Paris");

        assertAddress(inputAddress, expectedAddress);
    }

    @Test
    void givenThreeDifferentCities_whenEachOnDifferentField_thenShouldReturnTheOneOnTheCorrectField() {
        String countryField = "oslo";
        String stateField = "atena";
        String cityField = "klaipeda";

        Address inputAddress = new Address(countryField, stateField, cityField);
        Address expectedAddress = new Address("Republic of Lithuania", "Klaipėda County", "Klaipėda");

        assertAddress(inputAddress, expectedAddress);
    }
}
