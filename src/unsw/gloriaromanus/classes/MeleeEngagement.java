package unsw.gloriaromanus.classes;

import java.io.Serializable;
import java.util.Random;

import unsw.gloriaromanus.classes.units.Elephant;
import unsw.gloriaromanus.classes.units.Infantry;
import unsw.gloriaromanus.classes.units.Unit;
import unsw.gloriaromanus.states.UnitRoutedState;

public class MeleeEngagement implements Engagement, Serializable {
    
    private static final long serialVersionUID = 1L;

    public void unitEngagement(Unit playerUnit, Unit enemyUnit) {
        // melee engagement
        int playerDamage = 0;
        int enemyDamage = 0;
        Random rand = new Random();
        boolean playerUnitBreakStatus = false;
        boolean enemyUnitBreakStatus = false;
        double fleeChance = 0.5;
        double n;
        // apply special abilities method
        System.out.println("~~~~~~~~~~~~~~MELEE ENGAGEMENT~~~~~~~~~~~~~~");
        Player.engagementMessages += "~~~~~MELEE ENGAGEMENT~~~~~\n";

        while (Player.engagementCounter <= 200) {
            // get units at start of engagement
            //System.out.println("runn");
            int playerUnitsAtStart = playerUnit.getNumTroops();
            int enemyUnitsAtStart = enemyUnit.getNumTroops();

            // calculate player damage
            playerDamage = (int)((0.1 * enemyUnit.getNumTroops()) * ((double)playerUnit.getMeleeAttack() / (enemyUnit.getArmor() + enemyUnit.getShield() + enemyUnit.getDefense())) * (rand.nextGaussian() + 1));
            // up to a maximum of the entire enemy unit, with a minimum of none of the enemy unit
            if (playerDamage < 0) playerDamage = 0;
            if (playerDamage > enemyUnit.getNumTroops()) playerDamage = enemyUnit.getNumTroops();

            if (playerUnit instanceof Elephant) {
                n = rand.nextDouble();
                if (n <= 0.1) {
                    playerUnit.setNumTroops(playerUnit.getNumTroops() - playerDamage);
                    continue; 
                }
            }

            if (Player.engagementCounter % 4 == 0) {
                if (enemyUnit instanceof Infantry && "Melee".equals(enemyUnit.getUnitType())) {
                    enemyUnit.setMeleeAttack(playerUnit.getMeleeAttack() + enemyUnit.getShield());
                }
                if (enemyUnit instanceof Infantry && "Melee".equals(enemyUnit.getUnitType())) {
                    enemyUnit.setMeleeAttack(enemyUnit.getMeleeAttack() + enemyUnit.getShield());
                }
             }


            // calculate enemy damage
            enemyDamage = (int)((0.1 * playerUnit.getNumTroops()) * ((double)enemyUnit.getMeleeAttack() / (playerUnit.getArmor() + playerUnit.getShield() + playerUnit.getDefense())) * (rand.nextGaussian() + 1));
            if (enemyDamage < 0) enemyDamage = 0;
            if (enemyDamage > playerUnit.getNumTroops()) enemyDamage = playerUnit.getNumTroops();

            // attack the enemy
            System.out.println("We strike the enemy! Taking " + playerDamage + " casualties from their " + enemyUnit.getNumTroops() + " troops.");
            Player.engagementMessages += playerUnit.getName() + " strikes the enemy " + enemyUnit.getName() + " ! Taking " + playerDamage + " casualties from their " + enemyUnit.getNumTroops() + " troops.\n";
            enemyUnit.setNumTroops(enemyUnit.getNumTroops() - playerDamage);
            Player.engagementCounter++;


            if (enemyUnit.getNumTroops() <= 0) {
                // the playerUnit has won the skirmish
                System.out.println("Yes! We won the skirmish and the enemy unit is destroyed!");
                Player.engagementMessages += "Yes! Player " + playerUnit.getName() + " won the skirmish and the enemy " + enemyUnit.getName() + " is destroyed!\n";

                enemyUnit.destroyUnit();
                break;
            }

            if (enemyUnit instanceof Elephant) {
                n = rand.nextDouble();
                if (n <= 0.1) {
                    enemyUnit.setNumTroops(enemyUnit.getNumTroops() - enemyDamage);
                    continue;
                }
            }

            // enemy strikes back
            System.out.println("The enemy " + enemyUnit.getName() + " strikes back! Taking " + enemyDamage + " casualties from our " + playerUnit.getNumTroops() + " troops.");
            Player.engagementMessages += "The enemy " + enemyUnit.getName() + " strikes back! Taking " + enemyDamage + " casualties from our " + playerUnit.getNumTroops() + " troops.\n";
            playerUnit.setNumTroops(playerUnit.getNumTroops() - enemyDamage);
            Player.engagementCounter++;
            
            if (playerUnit.getNumTroops() <= 0) {
                // the enemyUnit has won the skirmish
                System.out.println("No! We lost the skirmish and our unit is destroyed!");
                Player.engagementMessages += "No! Player " + playerUnit.getName() + " lost the skirmish and our unit is destroyed!\n";
                playerUnit.destroyUnit();
                break;
            }

            // calculate the enemy's chance to break
            if (playerDamage != 0) {
                double enemyBaseBreakProbability = (1 - (0.1 * enemyUnit.getMorale()));

                int playerCasualties = playerUnitsAtStart-playerUnit.getNumTroops();
                if (playerCasualties == 0) playerCasualties = 10;

                enemyBaseBreakProbability = enemyBaseBreakProbability + ((((double)(enemyUnitsAtStart-enemyUnit.getNumTroops()))/(double)enemyUnitsAtStart)/((double)playerCasualties/(double)playerUnitsAtStart)) * 0.1;

                // for any engagement, the minimum chance of breaking is 5%, and the maximum chance of breaking is 100%.
                if (enemyBaseBreakProbability < 0.05) {
                    enemyBaseBreakProbability = 0.05;
                } //else if (enemyBaseBreakProbability > 1) {
                //  enemyBaseBreakProbability = 1;
                // }

                System.out.println("The enemy " + enemyUnit.getName() + " has a " + enemyBaseBreakProbability + " chance to break.");
                Player.engagementMessages += "The enemy " + enemyUnit.getName() + " has a " + enemyBaseBreakProbability + " chance to break.\n";

                n = rand.nextDouble();

                if (n <= enemyBaseBreakProbability) {
                    // unit breaks
                    enemyUnitBreakStatus = true;
                    System.out.println("Enemy" + enemyUnit.getName() + "  breaks! It will now attempt to flee the battle.");
                    Player.engagementMessages += "Enemy " + enemyUnit.getName() + " breaks! It will now attempt to flee the battle.\n";

                } else {
                    System.out.println("Enemy " + enemyUnit.getName() + " does not break :(");
                    Player.engagementMessages += "Enemy " + enemyUnit.getName() + " does not break :(\n";
                } 
            }

            if (enemyDamage != 0) {
                // calculate the player's chance to break
                double playerBaseBreakProbability = (1 - (0.1 * playerUnit.getMorale()));

                int enemyCasualties = enemyUnitsAtStart-enemyUnit.getNumTroops();
                if (enemyCasualties <= 0) enemyCasualties = 10;

                playerBaseBreakProbability = playerBaseBreakProbability + (((((double)(playerUnitsAtStart-playerUnit.getNumTroops()))/(double)playerUnitsAtStart)/((double)enemyCasualties/(double)enemyUnitsAtStart))) * 0.1;
                
                if (playerBaseBreakProbability < 0.05) {
                    playerBaseBreakProbability = 0.5;
                }
                // } else if (playerBaseBreakProbability > 1) {
                //    playerBaseBreakProbability = 1;
                // }
                
                System.out.println("The player has a " + playerBaseBreakProbability + " chance to break.");
                Player.engagementMessages += "The player " + playerUnit.getName() + " has a " + playerBaseBreakProbability + " chance to break.\n";
                
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

                    System.out.println("The enemy " + enemyUnit.getName() + " attempted to flee, but instead lost " + playerDamage + " units of " + enemyUnit.getNumTroops());
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
                
            }


            else if (playerUnitBreakStatus && !enemyUnitBreakStatus) {
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
                    //if (enemyDamage < 0) enemyDamage = 0;
                    if (enemyDamage > playerUnit.getNumTroops()) enemyDamage = playerUnit.getNumTroops();

                    System.out.println("We attempted to flee, but instead lost " + enemyDamage + " units of " + playerUnit.getNumTroops());
                    Player.engagementMessages += "Our " + playerUnit.getName() + " attempted to flee, but instead lost " + enemyDamage + " units of " + playerUnit.getNumTroops() + "\n";

                    playerUnit.setNumTroops(playerUnit.getNumTroops() - enemyDamage);
                }

                if (playerUnit.getNumTroops() <= 0) {
                    System.out.println("Player unit destroyed :(");
                    Player.engagementMessages += "Player " + playerUnit.getName() + " destroyed :(\n";

                    playerUnit.destroyUnit();
                    break;
                }

                /* if fleeAttemptSuccessful */
                //if (playerUnit.getUnitState() instanceof UnitRoutedState) {
                
                break;
                //}
            }

            else if (playerUnitBreakStatus && enemyUnitBreakStatus) {
                // put both units in routed state
                playerUnit.changeUnitState(new UnitRoutedState(playerUnit));
                enemyUnit.changeUnitState(new UnitRoutedState(enemyUnit));
                System.out.println("Both player " + playerUnit.getName() + " and enemy " + enemyUnit.getName() + " have broken, they both flee the battle without inflicting further casualties upon each other");
                Player.engagementMessages += "Both " + playerUnit.getName() + " and enemy " + enemyUnit.getName() + " have broken, they both flee the battle without inflicting further casualties upon each other\n";

                break;
            } else {
                System.out.println("NEITHER " + playerUnit.getName() + " and enemy BREAK");
                Player.engagementMessages += "Neither " + playerUnit.getName() + " and enemy break\n";
                continue;
            }

            
        }
    }
}
