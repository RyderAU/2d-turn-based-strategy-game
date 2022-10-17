package unsw.gloriaromanus.classes.units;

import unsw.gloriaromanus.classes.Player;
import unsw.gloriaromanus.classes.Province;

public class Druid extends HeavyInfantry {

 
    private static final long serialVersionUID = 1L;
    private static int costOfDruid = 100;

    public Druid (Province currentProvince, String faction, Player owner) {
        super(currentProvince, faction, owner);
        this.setUnitType("Missile");
        setName("Druid");
    }

    public static int getCostOfDruid() {
        return costOfDruid;
    }
    
}
