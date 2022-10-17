package unsw.gloriaromanus.classes.units;

import java.util.ArrayList;

import unsw.gloriaromanus.classes.MovementComponent;
import unsw.gloriaromanus.classes.Province;

import java.io.Serializable;

public class GroupOfUnits implements MovementComponent, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private ArrayList<Unit> units;
    //private int maxMovement;
    //private int groupStrength;

    public GroupOfUnits() {
        this.units = new ArrayList<Unit>();
    }

    // GETTERS

    // SETTERS

    // METHODS
    public boolean moveAttempt(Province destinationProvince, int movementPointsRequired) {

        // Check if all units have required movement points
        for (Unit unit : units) {
            if (unit.isInvadedThisTurn()) return false;
        }

        for (Unit unit : units) {
            if (unit.getMovPointsLeft() < movementPointsRequired) return false;
        }

        for (Unit unit : units) {
            unit.moveAttempt(destinationProvince, movementPointsRequired);
            
        }

        units.clear();
        return true;
    }

    public int getSize() {
        int counter = 0;
        for (Unit u : units) {
            counter++;
        }
        return counter;
    }

    /**
     * Adds a unit to the group of units
     * 
     * @param unit
     */
    public void addUnit(Unit unit) {
        units.add(unit);
    }

    /**
     * Removes a unit from the group of units
     * 
     * 
     */
    public void removeUnit(Unit unit) {
        units.remove(unit);
    }

    public void removeAllUnits() {
        units.clear();
    }

    /**
     * Calculates the strength of the group of units
     * 
     */
    ///public int getGroupStrength() {
       // int sum = 0;
       // for (Unit unit : units) {
       //     sum += unit.getStrength();
     //   }
    //    return sum;
   //}

}
