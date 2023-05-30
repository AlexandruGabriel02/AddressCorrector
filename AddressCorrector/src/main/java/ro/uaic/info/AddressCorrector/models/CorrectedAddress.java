package ro.uaic.info.AddressCorrector.models;

import lombok.Getter;
import lombok.ToString;

@ToString(callSuper = true)
@Getter
public class CorrectedAddress extends Address {
    private Float priority = 1f;

    public void lowerPriority() {
        priority -= 0.1f;
    }

    public void lowerPriority(float value) {
        priority -= value;
    }
}
