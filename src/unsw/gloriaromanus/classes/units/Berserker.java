package unsw.gloriaromanus.classes.units;

import unsw.gloriaromanus.classes.Player;
import unsw.gloriaromanus.classes.Province;

public class Berserker extends HeavyInfantry {

    
    private static final long serialVersionUID = 1L;
    private static int costOfBerserker = 200;

    /*For all Gallic/Celtic Briton/Germanic berserker units: "Berserker rage" - unit receives infinite morale and double melee attack damage, but has no armor or shield protection, in all battles*/

    public Berserker (Province currentProvince, String faction, Player owner) {
        super(currentProvince, faction, owner);
        // Berserker Rage
        if (faction == "Gallic" || faction  == "Celtic Briton" || faction  == "Germanic") {
            setMorale(9999999); // infinite
            setMeleeAttack(getMeleeAttack()*2);
            setArmor(0);
            setShield(0);
        }
        setName("Berserker");
    }
    
    public static int getCostOfBerserker() {
        return costOfBerserker;
    }
    
}
