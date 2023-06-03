package ro.uaic.info.AddressCorrector.utils;


import ro.uaic.info.AddressCorrector.models.Address;
import ro.uaic.info.AddressCorrector.models.NormalizedAddress;

import java.util.ArrayList;
import java.util.List;

public class Normalizer {
    private List<FieldNormalizer> normalizerInstances;
    private Address address;
    private NormalizedAddress normalizedAddress;

    public Normalizer(Address address) {
        this.address = address;
        this.normalizedAddress = new NormalizedAddress();
        this.normalizerInstances = new ArrayList<>();
        initNormalizerInstances();
    }

    private void initNormalizerInstances() {
        normalizerInstances.add(new CountryNormalizer(address.getCountry(), normalizedAddress));
        normalizerInstances.add(new StateNormalizer(address.getState(), normalizedAddress));
        normalizerInstances.add(new CityNormalizer(address.getCity(), normalizedAddress));
    }

    public NormalizedAddress normalize() {
        for (FieldNormalizer normalizer : normalizerInstances) {
            normalizer.normalizeField();
        }
        return normalizedAddress;
    }
}
