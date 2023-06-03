package ro.uaic.info.AddressCorrector.crossfields;

import org.springframework.boot.test.context.SpringBootTest;
import ro.uaic.info.AddressCorrector.models.Address;

@SpringBootTest
public class OradeaCrossFieldCountryStateCityTests extends AbstractCrossFieldTests{

    @Override
    Address getInputAddress() {
        return new Address("romania", "bihor", "oradea");
    }

    @Override
    Address getExpectedAddress() {
        return new Address("România", "Bihor", "Oradea");
    }
}
