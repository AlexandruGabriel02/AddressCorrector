package ro.uaic.info.AddressCorrector.services;

import org.springframework.stereotype.Service;
import ro.uaic.info.AddressCorrector.models.Address;
import ro.uaic.info.AddressCorrector.models.Entry;
import ro.uaic.info.AddressCorrector.models.NormalizedAddress;
import ro.uaic.info.AddressCorrector.utils.Normalizer;

@Service
public class AddressService {
    public Address getCorrectedAddress(Address inputAddress) {
        NormalizedAddress normalizedAddress = new Normalizer(inputAddress).normalize();

        System.out.println("Countries: ");
        for (Entry entry : normalizedAddress.getCountries()) {
            System.out.printf("%s - %s\n", entry.getNode().getDefaultEntityName(), entry.getNode());
        }

        System.out.println("States: ");
        for (Entry entry : normalizedAddress.getStates()) {
            System.out.printf("%s - %s\n", entry.getNode().getDefaultEntityName(), entry.getNode());
        }

        System.out.println("Cities: ");
        for (Entry entry : normalizedAddress.getCities()) {
            System.out.printf("%s - %s\n", entry.getNode().getDefaultEntityName(), entry.getNode());
        }

        return inputAddress;
    }
}
