package ro.uaic.info.AddressCorrector.utils.scoring;


import ro.uaic.info.AddressCorrector.database.Node;
import ro.uaic.info.AddressCorrector.models.CorrectedAddress;
import ro.uaic.info.AddressCorrector.models.Entry;
import ro.uaic.info.AddressCorrector.models.NormalizedAddress;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class is used to generate the best addresses for a given normalized address.
 * It uses a scoring system to determine the best addresses. In order to get the best addresses, {@link #generateBestAddresses()} should be called.
 *
 */
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

    /**
     * This method generates all the possible addresses for a given normalized address, starting from the cities.
     * For each city, it generates all the possible addresses, by going up the tree, until it reaches the country.
     * Meanwhile, it lowers the score of the addresses with the method {@link AddressCorrector#checkPenalization}.<br>
     * Also, the name of each is set to the default name from the {@link Node}.
     * @return a list of all the possible addresses for a given normalized address.
     */
    public List<CorrectedAddress> generateCorrectedAddresses() {
        List<CorrectedAddress> correctedAddresses = new ArrayList<>();

        for (Entry city : normalizedAddress.getCities()) {
            if (city.getName().isBlank())
                break;

            CorrectedAddress correctedAddress = new CorrectedAddress();

            Node cityNode = city.getNode();
            correctedAddress.setCity(cityNode.getDefaultEntityName());
            checkPenalization(city, city.getNode(), correctedAddress);

            Node stateNode = cityNode.getParentNode();
            setCorrectName(correctedAddress, stateNode);

            Node countryNode = stateNode.getParentNode();
            setCorrectName(correctedAddress, countryNode);

            maxScore = Math.max(maxScore, correctedAddress.getScore());
            correctedAddresses.add(correctedAddress);
        }

        return correctedAddresses;
    }

    /**
     * This method penalizes the addresses, by lowering their score, if the data from the input address is not on the correct field.
     * Also, a penalty is applied if the name of the node is an alternate name.
     * @param entry an Entry for which the penalization is checked
     * @param node the node corresponding to the entry
     * @param correctedAddress the address for which the penalization is checked
     */
    private void checkPenalization(Entry entry, Node node, CorrectedAddress correctedAddress) {
        if (!entry.isOnCorrectField()) {
            correctedAddress.lowerScoreByPriority();
        }
        if (!entry.getName().equalsIgnoreCase(node.getDefaultEntityName())) {
            correctedAddress.lowerScoreByAlternateName();
        }
    }

    /**
     * This method sets the correct name in the corrected address, for the corresponding Node.
     * If the node is not found in the normalized address, the score is lowered, because it means it was missing from the input address.
     * @param correctedAddress
     * @param node
     */
    private void setCorrectName(CorrectedAddress correctedAddress, Node node) {
        Optional<Entry> entryOptional = normalizedAddress.getContainingNode(node);
        correctedAddress.setEntity(node.getDefaultEntityName(), node.getType());
        if (entryOptional.isEmpty()) {
            correctedAddress.lowerScoreByNonExistentEntity();
            return;
        }
        Entry entry = entryOptional.get();
        checkPenalization(entry, node, correctedAddress);
    }
}
