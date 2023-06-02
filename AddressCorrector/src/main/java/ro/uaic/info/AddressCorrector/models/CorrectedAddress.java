package ro.uaic.info.AddressCorrector.models;

import lombok.Getter;
import lombok.ToString;

@ToString(callSuper = true)
@Getter
public class CorrectedAddress extends Address {
    private int score = 100;
    private static final int PRIORITY_PENALIZATION = 10;
    private static final int NONEXISTENT_ENTITY_PENALIZATION = 25;


    public void lowerScoreByPriority() {
        score -= PRIORITY_PENALIZATION;
    }

    public void lowerScoreByNonExistentEntity() {
        score -= NONEXISTENT_ENTITY_PENALIZATION;
    }
}
