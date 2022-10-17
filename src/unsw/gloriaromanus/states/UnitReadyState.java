package unsw.gloriaromanus.states;

import java.io.Serializable;

import unsw.gloriaromanus.classes.Province;
import unsw.gloriaromanus.classes.units.Unit;

public class UnitReadyState implements UnitState, Serializable {
    
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    Unit unit;

    public UnitReadyState(Unit unit) {
        this.unit = unit;
    } 

    public boolean actionMoveAttempt(Province destinationProvince, int movementPointsRequired) {
        return unit.moveAttempt(destinationProvince, movementPointsRequired);
    }
}


