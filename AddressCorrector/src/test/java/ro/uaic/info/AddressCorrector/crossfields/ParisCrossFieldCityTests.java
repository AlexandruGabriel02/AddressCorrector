package ro.uaic.info.AddressCorrector.crossfields;

import org.springframework.boot.test.context.SpringBootTest;
import ro.uaic.info.AddressCorrector.models.Address;

@SpringBootTest
public class ParisCrossFieldCityTests extends AbstractCrossFieldTests {
    @Override
    Address getInputAddress() {
        return new Address("", "", "paris");
    }

    @Override
    Address getExpectedAddress() {
        return new Address("Republic of France", "Île-de-France", "Paris");
    }
}
