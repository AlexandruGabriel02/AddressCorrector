package ro.uaic.info.AddressCorrector.services;

import org.springframework.stereotype.Service;
import ro.uaic.info.AddressCorrector.models.Address;
import ro.uaic.info.AddressCorrector.models.CorrectedAddress;
import ro.uaic.info.AddressCorrector.models.Entry;
import ro.uaic.info.AddressCorrector.models.NormalizedAddress;
import ro.uaic.info.AddressCorrector.utils.Normalizer;
import ro.uaic.info.AddressCorrector.utils.scoring.ScoreGenerator;

import java.util.Comparator;
import java.util.List;

import static java.lang.Math.min;

@Service
public class AddressService {
    public Address getCorrectedAddress(Address inputAddress) {
        NormalizedAddress normalizedAddress = new Normalizer(inputAddress).normalize();
        ScoreGenerator scoreGenerator = new ScoreGenerator(normalizedAddress);
        List<CorrectedAddress> correctedAddresses = scoreGenerator.generateScores();
        correctedAddresses = correctedAddresses.stream().sorted((a, b) -> b.getPriority().compareTo(a.getPriority())).toList();
        for (int i = 0; i < Math.min(10, correctedAddresses.size()); i++)
            System.out.println(correctedAddresses.get(i));
        System.out.println();
        return inputAddress;
    }
}
