package ro.uaic.info.AddressCorrector.utils.scoring;


import ro.uaic.info.AddressCorrector.database.Node;
import ro.uaic.info.AddressCorrector.models.CorrectedAddress;
import ro.uaic.info.AddressCorrector.models.Entry;
import ro.uaic.info.AddressCorrector.models.NormalizedAddress;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AddressCorrector {
    private final NormalizedAddress normalizedAddress;
    private int maxScore;

    public AddressCorrector(NormalizedAddress normalizedAddress) {
        this.normalizedAddress = normalizedAddress;
        maxScore = 0;
    }

    public List<CorrectedAddress> generateBestAddresses() {
        List<CorrectedAddress> addresses = new ArrayList<>();
        for (CorrectedAddress address : generateCorrectedAddresses()) {
            if (address.getScore() == maxScore) {
                addresses.add(address);
            }
        }
        return addresses;
    }

    public List<CorrectedAddress> generateCorrectedAddresses() {
        List<CorrectedAddress> correctedAddresses = new ArrayList<>();

        for (Entry city : normalizedAddress.getCities()) {
            //TODO: check why there are blank cities
            if (city.getName().isBlank())
                break;

            CorrectedAddress correctedAddress = new CorrectedAddress();
            if (!city.isOnCorrectField())
                correctedAddress.lowerScoreByPriority();

            Node cityNode = city.getNode();
            correctedAddress.setCity(cityNode.getDefaultEntityName());

            Node stateNode = cityNode.getParentNode();
            setCorrectName(correctedAddress, stateNode);

            Node countryNode = stateNode.getParentNode();
            setCorrectName(correctedAddress, countryNode);

            maxScore = Math.max(maxScore, correctedAddress.getScore());
            correctedAddresses.add(correctedAddress);
        }

        return correctedAddresses;
    }


    private void setCorrectName(CorrectedAddress correctedAddress, Node node) {
        Optional<Entry> entryOptional = normalizedAddress.getContainingNode(node);
        correctedAddress.setEntity(node.getDefaultEntityName(), node.getType());
        if (entryOptional.isEmpty()) {
            correctedAddress.lowerScoreByNonExistentEntity();
            return;
        }
        Entry entry = entryOptional.get();
        if (!entry.isOnCorrectField()) {
            correctedAddress.lowerScoreByPriority();
        }
    }
}
