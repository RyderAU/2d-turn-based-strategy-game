package unsw.gloriaromanus.classes.units;

import unsw.gloriaromanus.classes.Player;
import unsw.gloriaromanus.classes.Province;

public class Chariot extends Cavalry {
    private static final long serialVersionUID = 1L;
    private static int costOfChariot = 200;

    public Chariot (Province currentProvince, String faction, Player owner) {
        super(currentProvince, faction, owner);
        this.setUnitType("Melee");
        setName("Chariot");
    }

    public static int getCostOfChariot() {
        return costOfChariot;
    }
}
