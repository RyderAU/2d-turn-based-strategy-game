package unsw.gloriaromanus.classes.units;

import unsw.gloriaromanus.classes.MovementComponent;
import unsw.gloriaromanus.classes.Player;
import unsw.gloriaromanus.classes.Province;
import unsw.gloriaromanus.states.UnitReadyState;
import unsw.gloriaromanus.states.UnitRoutedState;
import unsw.gloriaromanus.states.UnitState;
import unsw.gloriaromanus.states.UnitTrainingState;

import java.io.Serializable;


public class Unit implements MovementComponent, Serializable {

    private static final long serialVersionUID = 1L;
    private UnitState unitState;
    private String name;
    private int numTroops = 100;
    private String unitType = "Melee";
    private int defense = 5;
    private int meleeAttack = 100;
    private int rangedAttack = 100;
    private Province currentProvince;
    private int movementPoints = 10;
    private int movPointsLeft = movementPoints;
    private int trainingTime = 1;
    private int morale = 10;
    private int armor = 5;
    private int speed = 1;
    private int shield = 5;
    private String faction;
    private Player owner;
    private boolean invadedThisTurn = false;

    public Unit (Province currentProvince, String faction, Player owner) {
        this.unitState = new UnitTrainingState(this);
        this.currentProvince = currentProvince;
        this.faction = faction;
        this.owner = owner;
        currentProvince.addUnit(this);
    }


    // METHODS

    /**
     * Applies a unit's special ability to them
     */
    public void applySpecialAbility() {
        // overriden in child classes
    }

    /**
     * Changes the unit state
     * The unit states are: Training, Ready, Routed
     * @param unitState that we want to change the 
     */
    public void changeUnitState(UnitState unitState) {
        this.unitState = unitState;
    }

    /**
     * Checks if the unit is in a valid unit state to perform a move
     * @param destinationProvince
     * @param movementPointsRequired
     * @return
     */
    public boolean actionMoveAttempt(Province destinationProvince, int movementPointsRequired) {
        return unitState.actionMoveAttempt(destinationProvince, movementPointsRequired);
    }

    /**
     * Attempts to move a single unit to a destination province
     * @param destinationProvince
     * @param movementPointsRequired
     * @return
     */
    public boolean moveAttempt(Province destinationProvince, int movementPointsRequired) { 
        
        if (movPointsLeft < movementPointsRequired) return false;
        
        movPointsLeft -= movementPointsRequired; 
        destinationProvince.getUnitsInProvince().add(this);
        currentProvince.removeUnit(this);
        this.currentProvince = destinationProvince;
        return true;
    }

    /**
     * Transfers a single unit to a destination province without movement point constraint
     * @param destinationProvince
     * @return
     */
    public void transferUnit(Province destinationProvince) { 
        destinationProvince.getUnitsInProvince().add(this);
        currentProvince.removeUnit(this);
        this.currentProvince = destinationProvince;
    }

    /**
     * Destroy a unit, removing them from it's owner and province it's in
     */
    public void destroyUnit() {
        owner.removeUnit(this);
        currentProvince.removeUnit(this);
    }

    /**
     * Put the unit in the routed state
     */
    public void routeUnit() {
        unitState = new UnitRoutedState(this);
    }
    
    /**
     * Resets the movement points of the unit
     */
    public void resetMovementPoints() {
        this.movPointsLeft = this.movementPoints;
    }

    // GETTERS

    public UnitState getUnitState() {
        return unitState;
    }

    public int getMeleeAttack() {
        return meleeAttack;
    }

    public int getRangedAttack() {
        return rangedAttack;
    }

    public int getMorale() {
        return morale;
    }

    public int getArmor() {
        return armor;
    }

    public int getShield() {
        return shield;
    }

    public int getSpeed() {
        return speed;
    }
    
    public int getDefense() {
        return defense;
    }

    public int getNumTroops() {
        return numTroops;
    }

    public int getMovPointsLeft() {
        return movPointsLeft;
    }

    public int getTrainingTime() {
        return trainingTime;
    }

    public String getUnitType() {
        return unitType;
    }


    public String getFaction() {
        return faction;
    }

    // SETTERS
    
    public void setUnitState(UnitState unitState) {
        if (unitState instanceof UnitReadyState) {
            int reducedTrainingSlot = currentProvince.getUnitsInTraining() - 1;
            currentProvince.setUnitsInTraining(reducedTrainingSlot);
        } 
        this.unitState = unitState;
    }

    public void setMeleeAttack(int meleeAttack) {
        this.meleeAttack = meleeAttack;
    }

    public void setRangedAttack(int rangedAttack) {
        this.rangedAttack = rangedAttack;
    }

    public void setMorale(int morale) {
        int setter = morale;
        if (morale < 1) {
            setter = 1;
        }
        this.morale = setter;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public void setShield(int shield) {
        this.shield = shield;
    }

    public void setInvadedThisTurn(boolean invadedThisTurn) {
        this.invadedThisTurn = invadedThisTurn;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public void setNumTroops(int numTroops) {
        this.numTroops = numTroops;
    }

    public boolean isInvadedThisTurn() {
        return invadedThisTurn;
    }

    public void setMovementPoints(int movementPoints) {
        this.movementPoints = movementPoints;
    }

    public void setTrainingTime(int trainingTime) {
        this.trainingTime = trainingTime;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Province getCurrentProvince () {
        return currentProvince;
    }

}