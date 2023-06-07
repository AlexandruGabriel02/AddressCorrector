package ro.uaic.info.AddressCorrector.services;

import org.springframework.stereotype.Service;
import ro.uaic.info.AddressCorrector.models.Address;
import ro.uaic.info.AddressCorrector.models.CorrectedAddress;
import ro.uaic.info.AddressCorrector.models.NormalizedAddress;
import ro.uaic.info.AddressCorrector.utils.Normalizer;
import ro.uaic.info.AddressCorrector.utils.scoring.AddressCorrector;

import java.util.List;


@Service
public class AddressService {
    public List<CorrectedAddress> getCorrectedAddress(Address inputAddress) {
        NormalizedAddress normalizedAddress = new Normalizer(inputAddress).normalize();
        AddressCorrector addressCorrector = new AddressCorrector(normalizedAddress);
        return addressCorrector.generateBestAddresses();
    }
}
