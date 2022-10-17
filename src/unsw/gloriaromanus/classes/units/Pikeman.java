package unsw.gloriaromanus.classes.units;

import unsw.gloriaromanus.classes.Player;
import unsw.gloriaromanus.classes.Province;

public class Pikeman extends Spearman {

 
    private static final long serialVersionUID = 1L;
    private static int costOfPikemen = 100;

    public Pikeman (Province currentProvince, String faction, Player owner) {
        super(currentProvince, faction, owner);
        setDefense(getDefense()*2);
        setSpeed(getSpeed()/2);
        setName("Pikeman");
    }
    
    public static int getCostOfPikemen() {
        return costOfPikemen;
    }
}
