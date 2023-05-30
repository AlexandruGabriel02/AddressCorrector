package ro.uaic.info.AddressCorrector.utils.scoring;


import ro.uaic.info.AddressCorrector.database.Node;
import ro.uaic.info.AddressCorrector.models.CorrectedAddress;
import ro.uaic.info.AddressCorrector.models.Entry;
import ro.uaic.info.AddressCorrector.models.NormalizedAddress;

import java.util.ArrayList;
import java.util.List;

public class ScoreGenerator {
    private final NormalizedAddress normalizedAddress;
    private List<CorrectedAddress> correctedAddresses;

    public ScoreGenerator(NormalizedAddress normalizedAddress) {
        this.normalizedAddress = normalizedAddress;
        correctedAddresses = new ArrayList<>();
    }

    public List<CorrectedAddress> generateScores() {
        for (Entry city : normalizedAddress.getCities()) {
            //TODO: check why there are blank cities
            if(city.getName().isBlank())
                break;
            CorrectedAddress correctedAddress = new CorrectedAddress();
            if (!city.isOnCorrectField())
                correctedAddress.lowerPriority();
            correctedAddress.setCity(city.getName());
            setCorrectParentNameForEntry(correctedAddress, city);
            setCorrectParentNameForNode(correctedAddress, city.getNode().getParentNode());

            correctedAddresses.add(correctedAddress);
        }
        return correctedAddresses;
    }

    public void setCorrectParentNameForEntry(CorrectedAddress correctedAddress, Entry entry) {

        Entry stateEntry = normalizedAddress.containsStateNode(entry.getNode().getParentNode());
        if (stateEntry != null) {
            if (!stateEntry.isOnCorrectField())
                correctedAddress.lowerPriority();
            correctedAddress.setState(stateEntry.getName());
        } else {
            correctedAddress.lowerPriority(0.2f);
            correctedAddress.setState(entry.getNode().getParentNode().getDefaultEntityName());
        }
    }

    public void setCorrectParentNameForNode(CorrectedAddress correctedAddress, Node node) {
        Entry entry = normalizedAddress.containsCountryNode(node);

        if (entry != null) {
            if (!entry.isOnCorrectField())
                correctedAddress.lowerPriority();
            correctedAddress.setCountry(entry.getName());
        } else {
            correctedAddress.lowerPriority(0.2f);
            correctedAddress.setCountry(node.getParentNode().getDefaultEntityName());
        }
    }
}
