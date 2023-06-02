package ro.uaic.info.AddressCorrector.integrationtests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class AddressControllerTest {
    String API_URL = "http://localhost:8081/api/v1/addresses";

    @Test
    void test() {
        String country = "Romania";
        String state = "Iasi";
        String city = "Iasi";
        String url = API_URL + "?country=Romania&state=Iasi&city=Iasi";
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        for(int i = 0; i < 10000; i++) {
            ResponseEntity<String> response = testRestTemplate.
                    getForEntity(url, String.class);

            Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        }

    }
}
