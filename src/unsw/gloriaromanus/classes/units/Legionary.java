package unsw.gloriaromanus.classes.units;

import unsw.gloriaromanus.classes.Player;
import unsw.gloriaromanus.classes.Province;

public class Legionary extends HeavyInfantry {

    private static final long serialVersionUID = 1L;
    private static int costOfLegionary = 100;

    public Legionary(Province currentProvince, String faction, Player owner) {
        super(currentProvince, faction, owner);
        setName("Legionary");
    }

    public static int getCostOfLegionary() {
        return costOfLegionary;
    }
   
}