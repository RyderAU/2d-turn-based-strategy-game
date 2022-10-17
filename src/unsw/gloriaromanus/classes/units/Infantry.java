package unsw.gloriaromanus.classes.units;

import unsw.gloriaromanus.classes.Player;
import unsw.gloriaromanus.classes.Province;

public class Infantry extends Unit {


    private static final long serialVersionUID = 1L;

    public Infantry(Province currentProvince, String faction, Player owner) {
        super(currentProvince, faction, owner);
        setName("Infantry");
    }
    
}
