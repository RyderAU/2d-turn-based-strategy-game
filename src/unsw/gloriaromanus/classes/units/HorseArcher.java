package unsw.gloriaromanus.classes.units;

import unsw.gloriaromanus.classes.Player;
import unsw.gloriaromanus.classes.Province;

public class HorseArcher extends Cavalry {
    
    private static final long serialVersionUID = 1L;
    private static int costOfHorseArcher = 200;

    public HorseArcher (Province currentProvince, String faction, Player owner) {
        super(currentProvince, faction, owner);
        this.setUnitType("Missile");
        setName("Horse Archer");
    }

    public static int getCostOfHorseArcher() {
        return costOfHorseArcher;
    }
}
