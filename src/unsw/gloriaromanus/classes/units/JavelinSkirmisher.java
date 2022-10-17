package unsw.gloriaromanus.classes.units;

import unsw.gloriaromanus.classes.Player;
import unsw.gloriaromanus.classes.Province;

public class JavelinSkirmisher extends Spearman {

 
    private static final long serialVersionUID = 1L;
    private static int costOfJavelinSkirmisher = 100;

    public JavelinSkirmisher (Province currentProvince, String faction, Player owner) {
        super(currentProvince, faction, owner);
        this.setUnitType("Missile");
        setArmor(getArmor()/2);
        setName("Javelin Skirmisher");
    }
    
    public static int getCostOfJavelinSkirmisher() {
        return costOfJavelinSkirmisher;
    }
}
