package ro.uaic.info.AddressCorrector.utils;

import lombok.AllArgsConstructor;
import ro.uaic.info.AddressCorrector.database.MultimapDatabase;
import ro.uaic.info.AddressCorrector.database.Node;
import ro.uaic.info.AddressCorrector.models.Entry;
import ro.uaic.info.AddressCorrector.models.NormalizedAddress;

import java.util.List;

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
