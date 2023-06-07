package ro.uaic.info.AddressCorrector.utils;

import lombok.AllArgsConstructor;
import ro.uaic.info.AddressCorrector.database.MultimapDatabase;
import ro.uaic.info.AddressCorrector.database.Node;
import ro.uaic.info.AddressCorrector.models.Entry;
import ro.uaic.info.AddressCorrector.models.NormalizedAddress;

import java.util.List;

/**
 * This class is used to normalize a field from an input address. It performs multiple operations on the field, such as
 * removing punctuation marks, splitting the field into tokens and lowering each token.
 */
@AllArgsConstructor
public abstract class FieldNormalizer {
    private static final String[] PUNCTUATION_MARKS = {",", ".", "!", "?", ";", ":", "(", ")", "[", "]", "{", "}", "<", ">", "\\", "|", "#", "$", "%", "^", "&", "*", "+", "="};
    private static final int TOKENS_LIMIT = 4;
    private String addressField;
    private NormalizedAddress normalizedAddress;

    private void removePunctuationMarks() {
        for (String punctuationMark : PUNCTUATION_MARKS) {
            addressField = addressField.replace(punctuationMark, "");
        }
    }

    private String[] splitAddressField() {
        String[] tokens = addressField.split(" ");
        return tokens;
    }

    private String[] allTokensToLower(String[] tokens) {
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = tokens[i].toLowerCase();
        }
        return tokens;
    }

    protected abstract boolean isOnCorrectField(Node node);

    /**
     * This method gets all the values from the {@link MultimapDatabase} that have with key the - entityName.
     * For each value, it creates an {@link Entry} object and adds it in the {@link NormalizedAddress} in the right list.
     * @param entityName the name to be searched in the {@link MultimapDatabase}
     */
    public void processEntity(String entityName) {
        List<Node> nodes = MultimapDatabase.INSTANCE.get(entityName);
        if (nodes.isEmpty()) {
            return;
        }

        for (Node node : nodes) {
            Entry entry = new Entry(node, entityName, isOnCorrectField(node));
            normalizedAddress.add(entry, node.getType());
        }
    }

    /**
     * This method processes the tokens and generates the possible addresses.
     * For each token, it creates a concatenation of the following {@link FieldNormalizer#TOKENS_LIMIT} tokens.
     * Each concatenation of tokens is passed to the {@link FieldNormalizer#processEntity(String)} method that checks if the concatenation is a valid entity name.
     */
    public void normalizeField() {
        removePunctuationMarks();
        String[] tokens = splitAddressField();
        String[] normalizedTokens = allTokensToLower(tokens);

        for (int i = 0; i < normalizedTokens.length; i++) {
            StringBuilder entityName = new StringBuilder();
            for(int j = 0; j < TOKENS_LIMIT; j++){
                if (i + j >= normalizedTokens.length) {
                    break;
                }

                entityName.append(normalizedTokens[i + j]);
                processEntity(entityName.toString());
                if (j < TOKENS_LIMIT - 1) {
                    entityName.append(" ");
                }
            }
        }
    }
}
