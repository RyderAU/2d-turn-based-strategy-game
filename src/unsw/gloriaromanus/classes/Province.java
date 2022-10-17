package unsw.gloriaromanus.classes;

import java.util.ArrayList;
import java.io.Serializable;
import unsw.gloriaromanus.classes.units.Unit;
import unsw.gloriaromanus.states.UnitReadyState;

public class Province implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private Player owner;
    private int nodeNumber = -1;
    private ArrayList<Province> adjacentProvinces;
    private ArrayList<Unit> unitsInProvince;
    private int wealth;
    private int wealthGrowthRate;
    private double taxRate;
    private boolean conqueredThisTurn;
    private int unitsInTraining = 0; // Max 2
    private int eagleLegionaryUnitsLostInProvince = 0;


    public Province(String name, Player player) {
        this.name = name;
        this.owner = player;
        this.owner.addProvince(this);
        this.adjacentProvinces = new ArrayList<Province>();
        this.unitsInProvince = new ArrayList<Unit>();
        // this.wealth = 5000000;
        this.wealth = 50;
        this.wealthGrowthRate = 10;
        this.taxRate = 0.15;
        this.conqueredThisTurn = false;
    }

    public ArrayList<Province> getAdjacentProvinces() {
        return adjacentProvinces;
    }   
    
    /**
     * Collects tax from the province and sets new wealth growth rate
     * @return
     */
    public int collectTaxIncome() {
        wealth += wealthGrowthRate;
        int income = (int) (wealth * taxRate);
        wealth -= income;
        // minimum wealth of a province is 0
        if (wealth < 0) wealth = 0;

        if (taxRate == 0.10) wealthGrowthRate += 10;
        if (taxRate == 0.20) wealthGrowthRate -= 10;
        if (taxRate == 0.25) {
            wealthGrowthRate -= 30;
            for (Unit unit : unitsInProvince) {
                unit.setMorale(unit.getMorale() - 1);
            }
        }

        return income;
    }

    /**
     * Adds a unit to the unitsInProvince list
     * @param unit
     */
    public void addUnit(Unit unit) {
        unitsInProvince.add(unit);
        unitsInTraining++;
    }

    /**
     * Removes a unit from the unitsInProvince list
     * @param unit
     */
    public void removeUnit(Unit unit) {
        unitsInProvince.remove(unit);
    }

    /**
     * Adds adjacent provinces to the current one
     * @param province
     */
    public void addAdjacentProvince(Province province) {
        if (!adjacentProvinces.contains(province)) {
            adjacentProvinces.add(province);
            province.addAdjacentProvince(this);
        }     
    }

    /**
     * Checks if the provided province is adjacent to the current one
     * @param province
     * @return
     */
    public boolean isAdjacent(Province province) {
        if (adjacentProvinces.contains(province)) return true;
        else return false; 
    }

    /**
     * Gets the first unit in the UnitsInProvince list which is in UnitReadyState
     * @return
     */
    public Unit getNextAvailableUnit() {
        for (Unit unit : unitsInProvince) {
            if (unit.getUnitState() instanceof UnitReadyState) {
                return unit;
            }
        }
        return null;
    }

    /**
     * Returns an array list of ready units in the province
     * @return
     */
    public ArrayList<Unit> getAvailableUnits() {
        ArrayList<Unit> available = new ArrayList<Unit>();
        for (Unit unit : unitsInProvince) {
            if (unit.getUnitState() instanceof UnitReadyState) {
                available.add(unit);
            }
        }
        return available;
    }

    /**
     * Returns the number of units in unitsInProvince in UnitReadyState
     * @return
     */
    public int numberOfAvailableUnit() {
        int i = 0;
        for (Unit unit : unitsInProvince) {
            if (unit.getUnitState() instanceof UnitReadyState) {
                i++;
            }
        }
        return i;
    }

    /**
     * Sets new owner of province when invaded.
     * If re-invaded by Roman, regain points lost with Eagle Legionary Casualties 
     */
    public void setOwner(Player owner) {
        unitsInProvince.clear();
        this.owner.removeProvince(this);
        this.owner = owner;
        this.owner.addProvince(this);
        System.out.println("EAGLES GAIN" + eagleLegionaryUnitsLostInProvince);

        if (eagleLegionaryUnitsLostInProvince > 0 && "Roman".equals(owner.getFaction())) {
            for (Unit unit : owner.getUnitsUnderControl()) {
                unit.setMorale((int)(unit.getMorale() + 0.2 * eagleLegionaryUnitsLostInProvince));
            }
            eagleLegionaryUnitsLostInProvince = 0;
        }
    }

    /**
     * Gets the number of Eagle Legionary Units lost and reduces morale across faction
     * Saves value for when settlement is recaptured
     * @param eagleLegionaryUnitsLostInProvince
     */
    public void changeEagleLegionaryUnitsLostInProvince(int eagleLegionaryUnitsLostInProvince) {
        
        for (Unit unit : owner.getUnitsUnderControl()) {
            unit.setMorale((int)(unit.getMorale() - 0.2 * eagleLegionaryUnitsLostInProvince));
        }

        this.eagleLegionaryUnitsLostInProvince = eagleLegionaryUnitsLostInProvince;
    }


/////////////////////////////////////////////////////////////////////////////////
/////////////////           GETTERS/SETTERS                  ////////////////////
/////////////////////////////////////////////////////////////////////////////////

    public void setNodeNumber(int nodeNumber) {
        this.nodeNumber = nodeNumber;
    }

    public int getNodeNumber() {
        return nodeNumber;
    }

    public int getUnitsInTraining() {
        return unitsInTraining;
    }

    public void setUnitsInTraining(int unitsInTraining) {
        this.unitsInTraining = unitsInTraining;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public void setWealthGrowthRate(int wealthGrowthRate) {
        this.wealthGrowthRate = wealthGrowthRate;
    }


    public String getName() {
        return name;
    }

    public ArrayList<Unit> getUnitsInProvince() {
        return unitsInProvince;
    }

    public Player getOwner() {
        return owner;
    }

    public int getNumberOfUnits() {
        return unitsInProvince.size();
    }

    public double getTaxRate() {
        return taxRate;
    }

    public int getWealthGrowthRate() {
        return wealthGrowthRate;
    }


    public void setConqueredThisTurn(boolean conqueredThisTurn) {
        this.conqueredThisTurn = conqueredThisTurn;
    }

    public boolean getConqueredThisTurn() {
        return conqueredThisTurn;
    }

    public int getWealth() {
        return wealth;
    }

    public void setWealth(int wealth) {
        this.wealth = wealth;
    }
    
}
