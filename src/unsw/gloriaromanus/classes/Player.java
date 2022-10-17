package unsw.gloriaromanus.classes;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.Collections;
import java.util.LinkedList;
import java.io.Serializable;

import unsw.gloriaromanus.classes.units.*;
import unsw.gloriaromanus.states.*;

public class Player implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private Game gameSave;
    private PlayerState playerState;
    private int goldAmount;
    private String faction;
    private Province selectedProvince;
    private Province destinationProvince;
    private ArrayList<Province> provincesUnderControl;
    private ArrayList<Unit> unitsUnderControl;
    private Engagement engagementStrategy;
    private GroupOfUnits selectedUnits;
    private ArrayList<String> playerGoals;
    private boolean mainGoal = false;
    private boolean subGoal1 = false;
    private boolean subGoal2 = false;
    private boolean victor = false;
    static public int engagementCounter;
    private int wealth;
    private boolean raidedThisTurn = false;
    private String unitsTrainingFinishedMessage;
    public static String engagementMessages;

    public Player(Game game, String faction) {
        this.gameSave = game;
        this.faction = faction;
        this.goldAmount = 1000;
        this.provincesUnderControl = new ArrayList<Province>();
        this.unitsUnderControl = new ArrayList<Unit>();
        this.playerGoals = new ArrayList<String>();
        this.selectedUnits = new GroupOfUnits();
        this.playerGoals.add("TREASURY");
        this.playerGoals.add("CONQUEST");
        this.playerGoals.add("WEALTH");
        Collections.shuffle(this.playerGoals);
        System.out.println("Should be " + this.playerGoals);
    }

    /**
     * Enables the user to set custom goals for victory
     * @param main
     * @param sub1
     * @param sub2
     */
    public void setCustomGoals(String main, String sub1, String sub2) {
        this.playerGoals.set(0, main);
        this.playerGoals.set(1, sub1);
        this.playerGoals.set(2, sub2);
    }

    /**
     * Runs all the necessary updates when a new turn has begun
     */
    public void sequential() {
        playerState = new PlayerActiveState(this);

        updateTrainingTimes();

        raidedThisTurn = false;

        for (Province province : provincesUnderControl) {
            goldAmount += province.collectTaxIncome();
            province.setConqueredThisTurn(false);
        }

        for (Unit unit : unitsUnderControl) {
            unit.setInvadedThisTurn(false);
            unit.resetMovementPoints();
            if (unit.getUnitState() instanceof UnitRoutedState) {
                unit.setUnitState(new UnitReadyState(unit));
                Province province = unit.getCurrentProvince();
                province.setUnitsInTraining(province.getUnitsInTraining() + 1);
            }
                
        }
        checkIfVictoryExists();
    }

    /**
     * Checks if victory conditions have been satisfied
     */
    public void checkIfVictoryExists() {
        
        mainGoal = checkVictoryConditionSatisfied(playerGoals.get(0));
        subGoal1 = checkVictoryConditionSatisfied(playerGoals.get(1));
        subGoal2 = checkVictoryConditionSatisfied(playerGoals.get(2));

        if ((mainGoal == true && subGoal1 == true) || (mainGoal == true && subGoal2 == true)) {
            victor = true;
        }

    }

    /**
     * Checks if a condition for victory has been achieved
     * @param goal
     * @return
     */
    public boolean checkVictoryConditionSatisfied(String goal) {
        
        if (goal.equals("CONQUEST")) {
            if (provincesUnderControl.size() == 53) return true;
        }
        
        if (goal.equals("TREASURY")) {
            System.out.println("TOTAL GOLD: " + goldAmount);
            if (goldAmount >= 100000) return true;
        }

        if (goal.equals("WEALTH")) {
            int totalWealth = 0;
            for (Province p : provincesUnderControl) {
                totalWealth += p.getWealth();
            }
            System.out.println("TOTAL WEALTH: " + totalWealth);
            if (totalWealth >=  400000) return true;
        }

        return false;
    } 


/////////////////////////////////////////////////////////////////////////////////
/////////////////             STATE METHODS                  ////////////////////
/////////////////////////////////////////////////////////////////////////////////


    public void actionSetSelectedProvince(Province province) {
        playerState.actionSetSelectedProvince(province);
    }

    public void actionAssignTaxRate(String taxType) {
        playerState.actionAssignTaxRate(taxType);
    }


    public void actionRemoveProvince(Province province) {
        playerState.actionRemoveProvince(province);
    }

    public void actionEndTurn() {
        playerState.actionEndTurn();
    }

    public boolean actionMoveUnits() {
        return playerState.actionMoveUnits();
    }

    public boolean actionInvadeAttempt() {
        return playerState.actionInvadeAttempt();
    }

    public boolean actionRecruitUnit(String unitType) {
        return playerState.actionRecruitUnit(unitType);
    }

/////////////////////////////////////////////////////////////////////////////////
/////////////////           OTHER METHODS                  ////////////////////
/////////////////////////////////////////////////////////////////////////////////


    public void setSelectedProvince(Province province) {
        this.selectedProvince = province;
    }

    public void selectUnit(Unit unit) {
        selectedUnits.addUnit(unit);
    }

    public void unselectUnit(Unit unit) {
        selectedUnits.removeUnit(unit);
    }
    
    public void unselectAllUnits() {
        selectedUnits.removeAllUnits();
    }

    /**
     * Assign a specified tax rate to the selected province
     */
    public void assignTaxRate(String taxType) {

        if (taxType.equals("Low Tax")) selectedProvince.setTaxRate(0.10);
        if (taxType.equals("Normal Tax")) selectedProvince.setTaxRate(0.15);
        if (taxType.equals("High Tax")) selectedProvince.setTaxRate(0.20);
        if (taxType.equals("Very High Tax")) selectedProvince.setTaxRate(0.25);
        
    } 

    public ArrayList<Province> getProvincesUnderControl() {
        return provincesUnderControl;
    }

    /**
     * Recruit a unit of a specific type to the player's army in the selected province
     * @param unitType
     * @return
     */
    public boolean recruitUnit(String unitType) {

        if (!Objects.equals(selectedProvince.getOwner(), this)) return false;
        if (selectedProvince.getUnitsInTraining() >= 2) return false;
        System.out.println("units in training is " + selectedProvince.getUnitsInTraining());
        if (unitType.equals("Druid")) {
            if (goldAmount < Druid.getCostOfDruid()) return false;
                addUnit(new Druid(selectedProvince, faction, this));
                goldAmount -= Druid.getCostOfDruid();
                return true;
            

        } if (unitType.equals("Legionary")) {
            if (goldAmount < Legionary.getCostOfLegionary()) return false;
                addUnit(new Legionary(selectedProvince, faction, this));
                goldAmount -= Legionary.getCostOfLegionary();

                if (faction.equals("Roman")) {
                    for (Unit unit : selectedProvince.getUnitsInProvince()) {
                        unit.setMorale(unit.getMorale() + 1);
                    }
                }
                return true;
            
            
        } if (unitType.equals("Berserker")) {
            if (goldAmount < Berserker.getCostOfBerserker()) return false;
                addUnit(new Berserker(selectedProvince, faction, this));
                goldAmount -= Berserker.getCostOfBerserker();
                return true;
            

        } if (unitType.equals("Pikeman")) {
            if (goldAmount < Pikeman.getCostOfPikemen()) return false;
                addUnit(new Pikeman(selectedProvince, faction, this));
                goldAmount -= Pikeman.getCostOfPikemen();
                return true;
            
            
        } if (unitType.equals("Hoplite")) {
            if (goldAmount < Hoplite.getCostOfHoplite()) return false;
                addUnit(new Hoplite(selectedProvince, faction, this));
                goldAmount -= Hoplite.getCostOfHoplite();
                return true;
            

        } if (unitType.equals("Javelin Skirmisher")) {
            if (goldAmount < JavelinSkirmisher.getCostOfJavelinSkirmisher()) return false;
                addUnit(new JavelinSkirmisher(selectedProvince, faction, this));
                goldAmount -= JavelinSkirmisher.getCostOfJavelinSkirmisher();
                return true;
            
            
        } if (unitType.equals("Missile Infantry")) {
            if (goldAmount < MissileInfantry.getCostOfMissileInfantry()) return false;
                addUnit(new MissileInfantry(selectedProvince, faction, this));
                goldAmount -= MissileInfantry.getCostOfMissileInfantry();
                return true;
            
            
        } if (unitType.equals("Elephant")) {
            if (goldAmount < Elephant.getCostOfElephant()) return false;
                addUnit(new Elephant(selectedProvince, faction, this));
                goldAmount -= Elephant.getCostOfElephant();
                return true;
            

        } if (unitType.equals("Horse Archer")) {
            if (goldAmount < HorseArcher.getCostOfHorseArcher()) return false;
                addUnit(new HorseArcher(selectedProvince, faction, this));
                goldAmount -= HorseArcher.getCostOfHorseArcher();
                return true;

        } if (unitType.equals("Melee Cavalry")) {
            if (goldAmount < MeleeCavalry.getCostOfMeleeCavalry()) return false;
                addUnit(new MeleeCavalry(selectedProvince, faction, this));
                goldAmount -= MeleeCavalry.getCostOfMeleeCavalry();
                return true;
    
        } if (unitType.equals("Chariot")) {
            if (goldAmount < Chariot.getCostOfChariot()) return false;
                addUnit(new Chariot(selectedProvince, faction, this));
                goldAmount -= Chariot.getCostOfChariot();
                return true;

        } if (unitType.equals("Artillery")) {
            if (goldAmount < Artillery.getArtilleryCost()) return false;
                addUnit(new Artillery(selectedProvince, faction, this));
                goldAmount -= Artillery.getArtilleryCost();     
                return true;
        }

        return false;
    }

    /**
     * End the current turn
     */
    public void endTurn() {
        for (Unit unit : unitsUnderControl) {
            unit.resetMovementPoints();
        }
        gameSave.advancePlayer();
    }


    /**
     * move the units in the current province to a destination province
     * @return returns true if the units are moves successfully and false if the units fail to be moved
     */
    public boolean moveUnits() {

        if (!Objects.equals(selectedProvince.getOwner(), this) ||
            !Objects.equals(destinationProvince.getOwner(), this)) {
                
                return false;
            }

        int movementPointsRequired = shortestPathMovPointsRequired();

        // move the units will fail if not possible
        return selectedUnits.moveAttempt(destinationProvince, movementPointsRequired);
        
        
    }

    /**
     * Attempt to invade the destination province from the selected province. Provinces must be adjacent.
     */
    public boolean invadeAttempt() {
        
        engagementMessages = "=== LET THE BATTLE BEGIN ===\n\n";
        if (selectedProvince.getConqueredThisTurn()) return false;
        
        // shuffle the unit list in each province, so we can get a random unit
        Collections.shuffle(selectedProvince.getUnitsInProvince());
        Collections.shuffle(destinationProvince.getUnitsInProvince());

        // select the first available unit of the shuffled list
        Unit invadingUnit = selectedProvince.getNextAvailableUnit();
        Unit defendingUnit = destinationProvince.getNextAvailableUnit();

        // Apply the druid special ability
        applyDruidEffect();
        // Get the number of defending legionary units for the roman legionary special ability
        int numberOfDefendingLegionary = 0;
        for (Unit unit : destinationProvince.getUnitsInProvince()) {
            if (unit instanceof Legionary && "Roman".equals(unit.getFaction())) {
                numberOfDefendingLegionary++;
            }
        }
        
        engagementCounter = 0;
        while (invadingUnit != null && defendingUnit != null && invadingUnit.getNumTroops() > 0 && defendingUnit.getNumTroops() > 0 && engagementCounter <= 200) {
            //meleeMissileMessage = "";
            // Melee cavalry special ability
            if (invadingUnit instanceof MeleeCavalry) {
                if (selectedProvince.numberOfAvailableUnit() < destinationProvince.numberOfAvailableUnit()/2.0) {
                    invadingUnit.applySpecialAbility();    
                }
            }  
            
            if (defendingUnit instanceof MeleeCavalry) {
                if (destinationProvince.numberOfAvailableUnit() < selectedProvince.numberOfAvailableUnit()/2.0) {
                    defendingUnit.applySpecialAbility();  
                }
            }

            // choose engagement strategy based off unit type
            if ((invadingUnit.getUnitType() == "Melee") && (defendingUnit.getUnitType() == "Melee")) {
                // 100% chance of melee engagement
                setStrategy(new MeleeEngagement());
                executeStrategy(invadingUnit, defendingUnit);
            } else if (invadingUnit.getUnitType() == "Missile" && defendingUnit.getUnitType() == "Missile") {
                // 100% chance of missile/ranged engagement
                setStrategy(new MissileEngagement());
                executeStrategy(invadingUnit, defendingUnit);
            } else {
                // one unit of each type
                // base level 50% chance of each engagement

                // calculate the chance
                double meleeChance = 0.5;

                if (invadingUnit.getUnitType() == "Melee" && defendingUnit.getUnitType() == "Missile") {
                    meleeChance = (meleeChance + (0.1 * ((double)invadingUnit.getSpeed() - (double)defendingUnit.getSpeed())));
                } else  {
                    // the invading unit is of type missile and the defending unit is of type melee
                    meleeChance = (meleeChance + (0.1 * ((double)defendingUnit.getSpeed() - (double)invadingUnit.getSpeed())));
                }

                double missileChance = 1.0 - meleeChance;

                // the maximum chance for an engagement to be either a ranged or melee engagement is 95% in either case.
                if (missileChance > 0.95) {
                    missileChance = 0.95;
                    meleeChance = 0.05;
                } else if (meleeChance > 0.95) {
                    meleeChance = 0.95;
                    missileChance = 0.05;
                }

                Random rand = new Random();
                double n = rand.nextDouble();

                if (n >= 0.0 && n <= missileChance) {
                    // missile engagement
                    setStrategy(new MissileEngagement());
                    executeStrategy(invadingUnit, defendingUnit);
                } else if (n > missileChance && n <= 1.0) {
                    // melee engagement
                    setStrategy(new MeleeEngagement());
                    executeStrategy(invadingUnit, defendingUnit);
                }
            }

            // get next available units
            System.out.println("Get next available units...");
            invadingUnit = selectedProvince.getNextAvailableUnit();
            defendingUnit = destinationProvince.getNextAvailableUnit();
            
        }

        System.out.println("There were " + engagementCounter + " engagements in total");

        if (engagementCounter > 200) {
            System.out.println("Too many engagements, go back to your provinces.");
        }

        if (destinationProvince.getNextAvailableUnit() == null && selectedProvince.getNextAvailableUnit() != null) {
            // we successfully invaded
            System.out.println("We successfully invaded! let's take over their province!");

            for (Unit u : selectedProvince.getUnitsInProvince()) {
                u.setInvadedThisTurn(true);
            }

            destinationProvince.setConqueredThisTurn(true);

            if (numberOfDefendingLegionary != 0) {
                destinationProvince.changeEagleLegionaryUnitsLostInProvince(numberOfDefendingLegionary);
            }

            destinationProvince.setOwner(this);
            destinationProvince.setTaxRate(0.15);

            ArrayList<Unit> unitsToTransfer = new ArrayList<Unit>();

            int i = 0;
            for (Unit u : selectedProvince.getUnitsInProvince()){
                if (u.getUnitState() instanceof UnitReadyState) {
                    if (i % 2 == 0) {
                        unitsToTransfer.add(u);
                    }
                    i++;
                }
            }

            // transfer units across
            for (Unit u : unitsToTransfer) {
                u.transferUnit(destinationProvince);
            }

            return true;
        }

        // otherwise, we failed to invade
        return false;
    }

    /**
     * Raid a selected province, without attempting to take them over
     * Can only be performed once per turn
     * 
     */
    public boolean raid() {
        if (raidedThisTurn) return false;
        int i = 0;
        for (Unit u : destinationProvince.getAvailableUnits()) {
            // casualties on the destination province's available units equivalent to half the number of units in the selected province
            u.destroyUnit();
            i++;
            if (i >= (selectedProvince.numberOfAvailableUnit()/2)) break;
        }
        raidedThisTurn = true;
        return true;
    }

    /**
     * Updates training times of units
     */
    public void updateTrainingTimes() {
        unitsTrainingFinishedMessage = "";
        for (Unit unit : unitsUnderControl) {
            int reducedTrainingTime = unit.getTrainingTime() - 1;
            unit.setTrainingTime(reducedTrainingTime);
            if (unit.getTrainingTime() >= 0)
            // System.out.println("TRAINING of" + unit + " " + unit.getTrainingTime());
            if (unit.getTrainingTime() == 0) {
                unitsTrainingFinishedMessage += unit.getName() + " in province " + unit.getCurrentProvince().getName() + " has finished training\n";
                unit.setUnitState(new UnitReadyState(unit));
            }
        }
    } 


    // returns the unit that won the engagement
    public void executeStrategy(Unit playerUnit, Unit enemyUnit) {
        engagementStrategy.unitEngagement(playerUnit, enemyUnit);
    }

    /**
     * Returns shortest path using BFS
     * @return
     */
    public int shortestPathMovPointsRequired() {

        int movementPointsRequired = 0;
        //Create visited array and store previous vertex if path found
        int[] visited = new int[1000]; //Use 53+ for testing
        LinkedList<Integer> queue = new LinkedList<Integer>();

        //Default values -1 for visited array
        for (int i = 0; i < visited.length; i++) {
            visited[i] = -1;
        }

        // Obtains startnode and creates adds to queue
        int startNode = selectedProvince.getNodeNumber();
        visited[startNode] = -10;
        queue.add(startNode);

        // Run BFS algorithm
        while (!queue.isEmpty()) {
    
            int dequeued_vertex = queue.poll();
            Province p = gameSave.getProvinceFromMap(dequeued_vertex);

            /**  Only consider adjacent/neighbour provinces which player is owner
             *   of and not conquered in the given turn
             *  
            */
            for (Province neighbourProvince : p.getAdjacentProvinces()) {
                if (visited[neighbourProvince.getNodeNumber()] == -1
                && Objects.equals(neighbourProvince.getOwner(), this)
                && !neighbourProvince.getConqueredThisTurn()) {

                    visited[neighbourProvince.getNodeNumber()] = dequeued_vertex;
                    queue.add(neighbourProvince.getNodeNumber());

                }       
            }
        }

        
        // Find total number of province moves
        
        int index = destinationProvince.getNodeNumber();

        if (visited[index] == -1) return 999999;
         
        
        while (visited[index] != -10) {
            movementPointsRequired += 4;
            index = visited[index];
        }

        return movementPointsRequired;
    }

   /**
    * Applies Druid special ability
    */
    public void applyDruidEffect() {
        int numberOfActiveDruidSelectedProvince = 0;
        int numberOfActiveDruidDestinationProvince = 0;
        for (Unit unit : selectedProvince.getUnitsInProvince()) {
            if (unit instanceof Druid && unit.getUnitState() instanceof UnitReadyState) {
                numberOfActiveDruidSelectedProvince++;
            }
        }

        for (Unit unit : destinationProvince.getUnitsInProvince()) {
            if (unit instanceof Druid && unit.getUnitState() instanceof UnitReadyState) {
                numberOfActiveDruidDestinationProvince++;
            }
        }

        if (numberOfActiveDruidSelectedProvince > 5) numberOfActiveDruidSelectedProvince = 5;
        if (numberOfActiveDruidDestinationProvince > 5) numberOfActiveDruidDestinationProvince = 5; 


        
        for (Unit unit : selectedProvince.getUnitsInProvince()) {
            if (numberOfActiveDruidSelectedProvince > 0)
            unit.setMorale((int)(unit.getMorale()*1.1*numberOfActiveDruidSelectedProvince));
            if (numberOfActiveDruidDestinationProvince > 0)
            unit.setMorale((int)(unit.getMorale()*0.95*numberOfActiveDruidDestinationProvince));
        }

        for (Unit unit : destinationProvince.getUnitsInProvince()) {
            if (numberOfActiveDruidSelectedProvince > 0)
            unit.setMorale((int)(unit.getMorale()*0.95*numberOfActiveDruidSelectedProvince));
            if (numberOfActiveDruidDestinationProvince > 0)
            unit.setMorale((int)(unit.getMorale()*1.1*numberOfActiveDruidDestinationProvince));

        }

    }

/////////////////////////////////////////////////////////////////////////////////
/////////////////     GETTERS/SETTERS/ADD/REMOVE                ////////////////////
/////////////////////////////////////////////////////////////////////////////////

    public void addProvince(Province province) {
        provincesUnderControl.add(province);
    }

    public void removeProvince(Province province) {
        provincesUnderControl.remove(province);
    }

    public void addUnit(Unit unit) {
        unitsUnderControl.add(unit);
    }

    public void addSelectedUnits(Unit unit) {
        selectedUnits.addUnit(unit);
    }

    public void removeUnit(Unit unit) {
        unitsUnderControl.remove(unit);
    }

    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }

    public String getFaction() {
        return faction;
    }

    public void setFaction(String faction) {
        this.faction = faction;
    }

    public Province getSelectedProvince() {
        return selectedProvince;
    }

    public void setDestinationProvince(Province province) {
        this.destinationProvince = province;
    }

    public void setStrategy(Engagement engagementStrategy) {
        this.engagementStrategy = engagementStrategy;
    }

    public int getSelectedUnitSize() {
        return selectedUnits.getSize();
    }

    public void setWealth(int wealth) {
        this.wealth = wealth;
    }

    public int getGoldAmount() {
        return goldAmount;
    }

    public void setGoldAmount(int goldAmount) {
        this.goldAmount = goldAmount;
    }

    public ArrayList<Unit> getUnitsUnderControl() {
        return unitsUnderControl;
    }

    public ArrayList<String> getPlayerGoals() {
        return playerGoals;
    }

    public void setPlayerGoals(ArrayList<String> goals) {
        this.playerGoals = goals;
    }

    public boolean isMainGoal() {
        return mainGoal;
    }
    
    public boolean isSubGoal1() {
        return subGoal1;
    }
    
    public boolean isSubGoal2() {
        return subGoal2;
    }

    public boolean isVictor() {
        return victor;
    }

    public int getFactionWealth(){
        int totalWealth = 0;
        
        for (Province p : provincesUnderControl) {
            totalWealth += p.getWealth();
        } 
        this.wealth = totalWealth;
        return wealth;
    }

    public String getUnitsTrainingFinishedMessage() {
        return unitsTrainingFinishedMessage;
    }
}
