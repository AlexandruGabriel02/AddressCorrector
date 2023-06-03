package ro.uaic.info.AddressCorrector.crossfields;

import org.springframework.boot.test.context.SpringBootTest;
import ro.uaic.info.AddressCorrector.models.Address;

@SpringBootTest
public class ParisCrossFieldStateCityTests extends AbstractCrossFieldTests{
    @Override
    Address getInputAddress() {
        return new Address("", "ile-de-France", "paris");
    }

    @Override
    Address getExpectedAddress() {
        return new Address("Republic of France", "Île-de-France", "Paris");
    }
}
