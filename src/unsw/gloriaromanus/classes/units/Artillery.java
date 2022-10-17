package unsw.gloriaromanus.classes.units;

import unsw.gloriaromanus.classes.Player;
import unsw.gloriaromanus.classes.Province;

public class Artillery extends Unit {

 
    private static final long serialVersionUID = 1L;
    private static int artilleryCost = 300;

    public Artillery (Province currentProvince, String faction, Player owner) {
        super(currentProvince, faction, owner);
        setMovementPoints(4);
        this.setUnitType("Missile");
        setName("Artillery");
    }
    
    public static int getArtilleryCost() {
        return artilleryCost;
    }
    
}
