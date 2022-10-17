package unsw.gloriaromanus.classes.units;

import unsw.gloriaromanus.classes.Player;
import unsw.gloriaromanus.classes.Province;

public class Spearman extends Infantry {
    
    private static final long serialVersionUID = 1L;

    public Spearman(Province currentProvince, String faction, Player owner) {
        super(currentProvince, faction, owner);
        setDefense(getDefense()*2);
        setSpeed(getSpeed()/2);
        setName("Spearman");
    }
}
