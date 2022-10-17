package unsw.gloriaromanus.classes.units;

import unsw.gloriaromanus.classes.Player;
import unsw.gloriaromanus.classes.Province;

public class Cavalry extends Unit {

 
    private static final long serialVersionUID = 1L;
    private static int chargeValue = 10;

    public Cavalry (Province currentProvince, String faction, Player owner) {
        super(currentProvince, faction, owner);
        setMovementPoints(15);
        setMeleeAttack(getMeleeAttack() + chargeValue);
        setName("Cavalry");
    }
    
    public static int getChargeValue() {
        return chargeValue;
    }
}
