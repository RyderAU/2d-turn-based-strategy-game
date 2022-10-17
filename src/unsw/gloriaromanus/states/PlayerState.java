package unsw.gloriaromanus.states;

import unsw.gloriaromanus.classes.*;

public interface PlayerState {
    
    public void actionSetSelectedProvince(Province province);
    public void actionAssignTaxRate(String taxType);
    //public void actionAddProvince(Province province);
    public void actionRemoveProvince(Province province);
    //public ArrayList<Province> actionGetProvincesUnderControl();
    public void actionEndTurn();
    public boolean actionMoveUnits();
    public boolean actionInvadeAttempt();
    public boolean actionRecruitUnit(String unitType);

}
