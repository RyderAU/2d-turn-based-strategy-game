package unsw.gloriaromanus.states;

import java.io.Serializable;

import unsw.gloriaromanus.classes.Province;
import unsw.gloriaromanus.classes.units.Unit;

public class UnitTrainingState implements UnitState, Serializable {
    
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    Unit unit;

    public UnitTrainingState(Unit unit) {
        
        this.unit = unit;
    } 

    public boolean actionMoveAttempt(Province destinationProvince, int movementPointsRequired) {
        //Do Nothing
        return false;
    }



}
