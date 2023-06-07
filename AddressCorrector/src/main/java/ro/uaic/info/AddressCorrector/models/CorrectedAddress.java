package ro.uaic.info.AddressCorrector.models;

import lombok.Getter;
import lombok.ToString;

/**
 * This class represents a corrected address.
 * Apart from the fields inherited from Address, it also contains a score field.
 * Also, it contains methods for lowering the score.
 */
@ToString(callSuper = true)
@Getter
public class CorrectedAddress extends Address {
    private int score = 100;
    private static final int PRIORITY_PENALIZATION = 10;
    private static final int NONEXISTENT_ENTITY_PENALIZATION = 25;
    private static final int ALTERNATE_NAME_PENALIZATION = 1;

    public void lowerScoreByPriority() {
        score -= PRIORITY_PENALIZATION;
    }

    public void lowerScoreByNonExistentEntity() {
        score -= NONEXISTENT_ENTITY_PENALIZATION;
    }

    public void lowerScoreByAlternateName(){score -= ALTERNATE_NAME_PENALIZATION;}
}
