package ro.uaic.info.AddressCorrector.latencytests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import ro.uaic.info.AddressCorrector.models.Address;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HandmadeLatencyTests {
    @Autowired
    private TestRestTemplate testRestTemplate;
    String API_URL = "http://localhost:8081/api/v1/addresses";

    private List<Address> getCorrectedAddress(String countryField, String stateField, String cityField) {
        HttpEntity requestEntity = new HttpEntity<>(null, new HttpHeaders());
        ResponseEntity<List<Address>> response = testRestTemplate.exchange(API_URL + "?country=" + countryField + "&state=" + stateField + "&city=" + cityField,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                });

        return response.getBody();
    }

    @Test
    void givenRomanianAndItalianAddress_whenRomaniaInForeignLanguage_thenShouldReturnRomanianAddress() {
        String countryField = "Романија;   France";
        String stateField = "galati ,,random_garbage1244,, lombardia";
        String cityField = "tecuci, milano";

        Address expectedAddress = new Address("România", "Galaţi", "Tecuci");

        List<Address> correctedAddresses = getCorrectedAddress(countryField, stateField, cityField);

        Assertions.assertTrue(correctedAddresses.contains(expectedAddress));
    }

    @Test
    void givenSpanishCity_whenNoOtherCitiesExist_thenShouldReturnSpanishAddress() {
        String countryField = "roumania";
        String stateField = "dolj";
        String cityField = "barcelona  ;;,;";

        Address expectedAddress = new Address("Kingdom of Spain", "Catalunya", "Barcelona");

        List<Address> correctedAddresses = getCorrectedAddress(countryField, stateField, cityField);

        assertTrue(correctedAddresses.contains(expectedAddress));
    }

    @Test
    void givenDenmarkCapital_whenLanguageIsForeignAndOnCountryField_thenShouldReturnDanishAddress() {
        String countryField = "København";
        String stateField = "-";
        String cityField = "-";

        Address expectedAddress = new Address("Kingdom of Denmark", "Region Hovedstaden", "Copenhagen");

        List<Address> correctedAddresses = getCorrectedAddress(countryField, stateField, cityField);
        assertTrue(correctedAddresses.contains(expectedAddress));
    }

    @Test
    void givenRomanianAndFrenchAddress_whenRomanianFieldsOnCorrectPosition_thenShouldReturnRomanianAddress() {
        String countryField = "romania paris";
        String stateField = "iasi ile-de-france";
        String cityField = "pascani franta";

        Address inputAddress = new Address(countryField, stateField, cityField);
        Address expectedAddress = new Address("România", "Iaşi", "Paşcani");

        List<Address> correctedAddresses = getCorrectedAddress(countryField, stateField, cityField);


        assertTrue(correctedAddresses.contains(expectedAddress));
    }
}
