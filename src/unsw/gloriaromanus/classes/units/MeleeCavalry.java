package unsw.gloriaromanus.classes.units;

import unsw.gloriaromanus.classes.Player;
import unsw.gloriaromanus.classes.Province;

public class MeleeCavalry extends Cavalry {
    
    private static final long serialVersionUID = 1L;
    private static int costOfMeleeCavalry = 200;

    public MeleeCavalry(Province currentProvince, String faction, Player owner) {
        super(currentProvince, faction, owner);
        setName("Melee Cavalry");
    }

    public static int getCostOfMeleeCavalry() {
        return costOfMeleeCavalry;
    }

    public void applySpecialAbility() {
        setMeleeAttack(getMeleeAttack() - getChargeValue());
        setMeleeAttack(getMeleeAttack() + getChargeValue()* 2);
        setMorale((int)(getMorale()*1.5));
    }
}
