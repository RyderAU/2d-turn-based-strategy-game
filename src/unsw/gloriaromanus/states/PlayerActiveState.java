package unsw.gloriaromanus.states;

import java.io.Serializable;

import unsw.gloriaromanus.classes.*;


public class PlayerActiveState implements PlayerState, Serializable{
    
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    Player player;

    public PlayerActiveState(Player player) {
        this.player = player;
    } 

    public void actionSetSelectedProvince(Province province) {
        player.setSelectedProvince(province);
    }

    public void actionAssignTaxRate(String taxType) {
        player.assignTaxRate(taxType);
    }

    //public void actionAddProvince(Province province) {
    //    player.addProvince(province);
    //}
    public void actionRemoveProvince(Province province) {
        player.removeProvince(province);
    }
    //public ArrayList<Province> actionGetProvincesUnderControl() {
    //    return player.getProvincesUnderControl();
    //}

    public void actionEndTurn() {
        player.setPlayerState(new PlayerStandbyState(player));
        player.endTurn();
    }

    public boolean actionMoveUnits() {
        return player.moveUnits();
    }

    public boolean actionInvadeAttempt() {
        return player.invadeAttempt();
    }

    public boolean actionRecruitUnit(String unitType) {
        return player.recruitUnit(unitType);
    }

}
