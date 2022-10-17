package unsw.gloriaromanus.classes.units;

import unsw.gloriaromanus.classes.Player;
import unsw.gloriaromanus.classes.Province;

public class HeavyInfantry extends Infantry {
    
    private static final long serialVersionUID = 1L;

    public HeavyInfantry(Province currentProvince, String faction, Player owner) {
        super(currentProvince, faction, owner);
        setName("Heavy Infantry");
    }
}
