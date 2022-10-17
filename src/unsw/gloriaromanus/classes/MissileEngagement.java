package unsw.gloriaromanus.classes;

import java.io.Serializable;
import java.util.Random;

import unsw.gloriaromanus.classes.units.HorseArcher;
import unsw.gloriaromanus.classes.units.Unit;
import unsw.gloriaromanus.states.UnitRoutedState;

public class MissileEngagement implements Engagement, Serializable {
    
    private static final long serialVersionUID = 1L;

    public void unitEngagement(Unit playerUnit, Unit enemyUnit) {
        int playerDamage = 0;
        int enemyDamage = 0;
        Random rand = new Random();
        boolean playerUnitBreakStatus = false;
        boolean enemyUnitBreakStatus = false;
        double fleeChance = 0.5;
        double n; // used for chance
        // default is that they're melee and can't do damage

        if (playerUnit instanceof HorseArcher && "Missile".equals(enemyUnit.getUnitType())) {
            enemyUnit.setRangedAttack(enemyUnit.getRangedAttack()/2);
        } 

        if (enemyUnit instanceof HorseArcher && "Missile".equals(playerUnit.getUnitType())) {
            playerUnit.setRangedAttack(playerUnit.getRangedAttack()/2);
        } 

        System.out.println("~~~~~~~~~~~~~~MISSILE ENGAGEMENT~~~~~~~~~~~~~~");
        Player.engagementMessages += "~~~~~MISSILE ENGAGEMENT~~~~~\n";

        while (Player.engagementCounter <= 200) {
            // get units at start of engagement
            int playerUnitsAtStart = playerUnit.getNumTroops();
            int enemyUnitsAtStart = enemyUnit.getNumTroops();

            if (playerUnit.getUnitType() == "Missile") {
                playerDamage = (int)((0.1 * enemyUnit.getNumTroops()) * ((double)playerUnit.getRangedAttack() / (enemyUnit.getArmor() + enemyUnit.getShield())) * (rand.nextGaussian() + 1));
            }
            if (playerDamage < 0) playerDamage = 0;
            if (playerDamage > enemyUnit.getNumTroops()) playerDamage = enemyUnit.getNumTroops();

            // calculate enemy damage
            if (enemyUnit.getUnitType() == "Missile") {
                enemyDamage = (int)((0.1 * playerUnit.getNumTroops()) * ((double)enemyUnit.getRangedAttack() / (playerUnit.getArmor() + playerUnit.getShield())) * (rand.nextGaussian() + 1));
            }
            if (enemyDamage < 0) enemyDamage = 0;
            if (enemyDamage > playerUnit.getNumTroops()) enemyDamage = playerUnit.getNumTroops();

            // attack the enemy
            System.out.println("We strike the enemy! Taking " + playerDamage + " casualties from their " + enemyUnit.getNumTroops() + " troops.");
            Player.engagementMessages += "Our " + playerUnit.getName() + " strike the enemy " + enemyUnit.getName() + "! Taking " + playerDamage + " casualties from their " + enemyUnit.getNumTroops() + " troops.\n";

            enemyUnit.setNumTroops(enemyUnit.getNumTroops() - playerDamage);
            Player.engagementCounter++;


            if (enemyUnit.getNumTroops() <= 0) {
                // the playerUnit has won the skirmish
                System.out.println("Yes! We won the skirmish and the enemy " + enemyUnit.getName() + " is destroyed!");
                Player.engagementMessages += "Yes! Our " + playerUnit.getName() + " won the skirmish and the enemy " + enemyUnit.getName() + " is destroyed!\n";

                enemyUnit.destroyUnit();
                break;
            }

            // enemy strikes back
            System.out.println("The enemy strikes back! Taking " + enemyDamage + " casualties from our " + playerUnit.getNumTroops() + " troops.");
            Player.engagementMessages += "The enemy " + enemyUnit.getName() + " strikes back! Taking " + enemyDamage + " casualties from our " + playerUnit.getNumTroops() + " troops.\n";

            playerUnit.setNumTroops(playerUnit.getNumTroops() - enemyDamage);
            Player.engagementCounter++;
            
            if (playerUnit.getNumTroops() <= 0) {
                // the enemyUnit has won the skirmish
                System.out.println("No! We lost the skirmish and our unit is destroyed!");
                Player.engagementMessages += "No! Our " + playerUnit.getName() + " lost the skirmish and our unit is destroyed!\n";

                playerUnit.destroyUnit();
                break;
            }

            // calculate the enemy's chance to break (if they were damaged)
            if (playerDamage != 0) {
                double enemyBaseBreakProbability = (1 - (0.1 * enemyUnit.getMorale()));

                int playerCasualties = playerUnitsAtStart-playerUnit.getNumTroops();
                if (playerCasualties <= 0) playerCasualties = 10;

                enemyBaseBreakProbability = enemyBaseBreakProbability + ((((double)(enemyUnitsAtStart-enemyUnit.getNumTroops()))/(double)enemyUnitsAtStart)/((double)playerCasualties/(double)playerUnitsAtStart)) * 0.1;

                // for any engagement, the minimum chance of breaking is 5%, and the maximum chance of breaking is 100%.
                if (enemyBaseBreakProbability < 0.05) {
                    enemyBaseBreakProbability = 0.05;
                //} else if (enemyBaseBreakProbability > 1) {
                //  enemyBaseBreakProbability = 1;
                }

                System.out.println("The enemy has a " + enemyBaseBreakProbability + " chance to break.");
                Player.engagementMessages += "The enemy " + enemyUnit.getName() + " has a " + enemyBaseBreakProbability + " chance to break.\n";

                n = rand.nextDouble();

                if (n <= enemyBaseBreakProbability) {
                    // unit breaks
                    enemyUnitBreakStatus = true;
                    System.out.println("Enemy unit breaks! It will now attempt to flee the battle.");
                    Player.engagementMessages += "Enemy " + enemyUnit.getName() + " breaks! It will now attempt to flee the battle.\n";

                } else {
                    System.out.println("Enemy unit does not break :(");
                    Player.engagementMessages += "Enemy " + enemyUnit.getName() + " does not break :(\n";

                }
            }
             
            // calculate the player's chance to break
            if (enemyDamage != 0) {
                double playerBaseBreakProbability = (1 - (0.1 * playerUnit.getMorale()));

                int enemyCasualties = enemyUnitsAtStart-enemyUnit.getNumTroops();
                if (enemyCasualties <= 0) enemyCasualties = 10;

                playerBaseBreakProbability = playerBaseBreakProbability + (((((double)(playerUnitsAtStart-playerUnit.getNumTroops()))/(double)playerUnitsAtStart)/((double)enemyCasualties/(double)enemyUnitsAtStart))) * 0.1;
                
                if (playerBaseBreakProbability < 0.05) {
                    playerBaseBreakProbability = 0.5;
                //} else if (playerBaseBreakProbability > 1) {
                //   playerBaseBreakProbability = 1;
                }
                
                System.out.println("The player has a " + playerBaseBreakProbability + " chance to break.");
                Player.engagementMessages += "The Player " + playerUnit.getName() + " has a " + playerBaseBreakProbability + " chance to break.\n";

                // check if we change units to break status
                // rand int stuff goes here
                n = rand.nextDouble();

                if (n <= playerBaseBreakProbability) {
                    // unit breaks
                    playerUnitBreakStatus = true;
                    System.out.println("Player unit breaks! It will now attempt to flee the battle.");
                    Player.engagementMessages += "Player " + playerUnit.getName() + " breaks! It will now attempt to flee the battle.\n";

                } else {
                    System.out.println("Player unit does not break :)");
                    Player.engagementMessages += "Player " + playerUnit.getName() + " does not break :)\n";

                }
            }
            
            // if the enemy unit is broken, it will now attempt to flee the battle
            if (enemyUnitBreakStatus && !playerUnitBreakStatus) {
                // enemy attempts to route
                fleeChance = fleeChance + 0.1 * ((double)enemyUnit.getSpeed() - (double)playerUnit.getSpeed());
                while (enemyUnit.getNumTroops() > 0) {
                    // calculate chance of fleeing
                    n = rand.nextDouble();
                    if (n <= fleeChance) {
                        // player flees
                        enemyUnit.changeUnitState(new UnitRoutedState(enemyUnit));
                        System.out.println("enemy routes");
                        Player.engagementMessages += "Enemy " + enemyUnit.getName() + " routes\n";

                        break;
                    }
                    // enemy gets damaged

                    playerDamage = (int)((0.1 * enemyUnit.getNumTroops()) * ((double)playerUnit.getMeleeAttack() / (enemyUnit.getArmor() + enemyUnit.getShield() + enemyUnit.getDefense())) * (rand.nextGaussian() + 1));
                    //if (playerDamage < 0) playerDamage = 0;
                    if (playerDamage > enemyUnit.getNumTroops()) playerDamage = enemyUnit.getNumTroops();

                    Player.engagementCounter++;

                    System.out.println("The enemy attempted to flee, but instead lost " + playerDamage + " units of " + enemyUnit.getNumTroops());
                    Player.engagementMessages += "The enemy " + enemyUnit.getName() + " attempted to flee, but instead lost " + playerDamage + " units of " + enemyUnit.getNumTroops() + "\n";

                    enemyUnit.setNumTroops(enemyUnit.getNumTroops() - playerDamage);
                }
                if (enemyUnit.getNumTroops() <= 0) {
                    System.out.println("Enemy unit destroyed!");
                    Player.engagementMessages += "Enemy " + enemyUnit.getName() + " destroyed!\n";

                    enemyUnit.destroyUnit();
                    break;
                }
                /* if fleeAttemptSuccessful */
                //if (enemyUnit.getUnitState() instanceof UnitRoutedState) {
                break;
                //}
            }

            if (playerUnitBreakStatus && !enemyUnitBreakStatus) {
                // player attempts to route
                // maybe a loop here
                fleeChance = fleeChance + 0.1 * ((double)playerUnit.getSpeed() - (double)enemyUnit.getSpeed());
                while (playerUnit.getNumTroops() > 0) {
                    // calculate chance of fleeing
                    n = rand.nextDouble();
                    if (n <= fleeChance) {
                        // player flees
                        playerUnit.changeUnitState(new UnitRoutedState(playerUnit));
                        System.out.println("player routes");
                        Player.engagementMessages += "Player " + playerUnit.getName() + " routes\n";

                        break;
                    }
                    // player gets damaged

                    Player.engagementCounter++;

                    enemyDamage = (int)((0.1 * playerUnit.getNumTroops()) * ((double)enemyUnit.getMeleeAttack() / (playerUnit.getArmor() + playerUnit.getShield() + playerUnit.getDefense())) * (rand.nextGaussian() + 1));
                    if (enemyDamage < 0) enemyDamage = 0;
                    if (enemyDamage > playerUnit.getNumTroops()) enemyDamage = playerUnit.getNumTroops();

                    System.out.println("We attempted to flee, but instead lost " + enemyDamage + " units of " + playerUnit.getNumTroops());
                    Player.engagementMessages += "Player " + playerUnit.getName() + " attempted to flee, but instead lost " + enemyDamage + " units of " + playerUnit.getNumTroops() + "\n";

                    playerUnit.setNumTroops(playerUnit.getNumTroops() - enemyDamage);
                }

                if (playerUnit.getNumTroops() <= 0) {
                    System.out.println("Player " + playerUnit.getName() + " destroyed :(");
                    Player.engagementMessages += "Player " + playerUnit.getName() + " destroyed :(\n";

                    playerUnit.destroyUnit();
                    break;
                }

                /* if fleeAttemptSuccessful */
                if (playerUnit.getUnitState() instanceof UnitRoutedState) {
                break;
                }
            }

            if (playerUnitBreakStatus && enemyUnitBreakStatus) {
                // put both units in routed state
                playerUnit.changeUnitState(new UnitRoutedState(playerUnit));
                enemyUnit.changeUnitState(new UnitRoutedState(enemyUnit));
                System.out.println("Both units have broken, they both flee the battle without inflicting further casualties upon each other");
                Player.engagementMessages += "Both units have broken, they both flee the battle without inflicting further casualties upon each other\n";

                break;
            }
        }
    }
}
