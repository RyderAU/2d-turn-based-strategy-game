package unsw.gloriaromanus.classes.units;

import unsw.gloriaromanus.classes.Player;
import unsw.gloriaromanus.classes.Province;

public class MissileInfantry extends Infantry {
    
    private static final long serialVersionUID = 1L;
    private static int costOfMissileInfantry = 200;


    public MissileInfantry(Province currentProvince, String faction, Player owner) {
        super(currentProvince, faction, owner);
        this.setUnitType("Missile"); 
        setName("Missile Infantry");
    }

    public static int getCostOfMissileInfantry() {
        return costOfMissileInfantry;
    }
}
