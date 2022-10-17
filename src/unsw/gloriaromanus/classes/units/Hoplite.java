package unsw.gloriaromanus.classes.units;

import unsw.gloriaromanus.classes.Player;
import unsw.gloriaromanus.classes.Province;

public class Hoplite extends Spearman {

 
    private static final long serialVersionUID = 1L;
    private static int costOfHoplite = 100;

    public Hoplite (Province currentProvince, String faction, Player owner) {
        super(currentProvince, faction, owner);
        setName("Hoplite");
    }
    
    public static int getCostOfHoplite() {
        return costOfHoplite;
    }
}
