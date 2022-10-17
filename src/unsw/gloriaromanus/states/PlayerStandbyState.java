package unsw.gloriaromanus.states;

import java.io.Serializable;

import unsw.gloriaromanus.classes.*;


public class PlayerStandbyState implements PlayerState , Serializable {
    
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Player player;

    public PlayerStandbyState(Player player) {
        this.player = player;
    }

    public void actionSetSelectedProvince(Province province) {
        //Do nothing
    }

    public void actionAssignTaxRate(String taxType) {
        // Do nothing
    }

    //public void actionAddProvince(Province province) {
        //Do nothing
    //}

    public void actionRemoveProvince(Province province) {
        //Do nothing
    }

   // public ArrayList<Province> actionGetProvincesUnderControl() {
        //Do nothing
    //    return null;
  //  }

    public boolean actionMoveUnits() {
        return false;
    }

    public boolean actionRecruitUnit(String unitType) {
        return false;
    }
    public void actionEndTurn() {
        // Do nothing
    }

    public boolean actionInvadeAttempt() {
        return false;
    }
}
