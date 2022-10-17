package unsw.gloriaromanus.states;

import unsw.gloriaromanus.classes.Province;

public interface UnitState {

    public boolean actionMoveAttempt(Province destinationProvince, int movementPointsRequired);  

}


