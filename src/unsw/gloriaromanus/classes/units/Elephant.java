package unsw.gloriaromanus.classes.units;

import unsw.gloriaromanus.classes.Player;
import unsw.gloriaromanus.classes.Province;

public class Elephant extends Cavalry {
    
    private static final long serialVersionUID = 1L;
    private static int costOfElephant = 500;

    public Elephant(Province currentProvince, String faction, Player owner) {
        super(currentProvince, faction, owner);
        setTrainingTime(2);
        setName("Elephant");

    }

    public static int getCostOfElephant() {
        return costOfElephant;
    }

}
