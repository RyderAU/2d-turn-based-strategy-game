package test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//Added Imports

import unsw.gloriaromanus.classes.*;
import unsw.gloriaromanus.classes.units.Druid;
import unsw.gloriaromanus.classes.units.Elephant;
import unsw.gloriaromanus.classes.units.GroupOfUnits;
import unsw.gloriaromanus.classes.units.HorseArcher;
import unsw.gloriaromanus.classes.units.Legionary;
import unsw.gloriaromanus.classes.units.MeleeCavalry;
import unsw.gloriaromanus.classes.units.Pikeman;
import unsw.gloriaromanus.classes.units.Unit;
import unsw.gloriaromanus.states.PlayerStandbyState;
import unsw.gloriaromanus.states.UnitReadyState;
import unsw.gloriaromanus.states.UnitRoutedState;

// gradle test -b test.gradle

public class UnitTest {

    Game game = new Game("testGame");
    HumanPlayer humanPlayer1 = game.getHumanPlayer1();
    HumanPlayer humanPlayer2 = game.getHumanPlayer2();
    Province currentProvince = new Province("carthage", humanPlayer1);
    Unit unit = new Unit(currentProvince, humanPlayer1.getFaction(), humanPlayer1);

    @BeforeEach
    public void setup() {
        humanPlayer1.setSelectedProvince(currentProvince);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    ///////////////              GAME AND MAINMENU TESTS          //////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////


    @Test
    public void createGame() {
        MainMenu menu = new MainMenu();
        Game newGame = menu.startNewConquest("gameName");
        System.out.println(game.getListOfGameProvinces().size());
        assert (game.getListOfGameProvinces().size() == 53);

    }

    @Test
    public void createSpecifiedGame() {
        MainMenu menu = new MainMenu();
        Game newGame = menu.startNewConquest("GameName", "Roman", "Gallic", "SpecialBattleResolver");
        assert (newGame.getHumanPlayer1().getFaction().equals("Roman"));
        assert (newGame.getHumanPlayer2().getFaction().equals("Gallic"));
        assert (newGame.getEngagementStyle().equals("SpecialBattleResolver"));

    }

    @Test
    public void getNonExistantGameProvince() {
        assert (game.getGameProvince("YAHOOOO") == null);
    }

    @Test
    public void provinceInitialSetupWithWrongFile() throws IOException {
        Game newGame = new Game("GameName");
        newGame.setProvinceFile("DSDSD");
        newGame.setUpProvinces();
    }


    @Test
    public void testloadGameFromMainMenu() {

        MainMenu mn = new MainMenu();
        // shoan
        try {
            Game game = mn.loadGame("test_gameSave.txt");
        } catch (Exception e) {

        }
        // ryder
        //Game game = mn.loadGame("/tmp_amd/glass/export/glass/1/z5230735/courses/cs2511/assignments/h11a-team_hd/test_gameSave.txt");
        
        assert(game.getListOfGameProvinces().size() == 53);
    }

    @Test
    public void testloadInvalidGameFromMainMenu() throws FileNotFoundException {
        MainMenu mn = new MainMenu();
        try {
            Game game = mn.loadGame("invalid");
        } catch (Exception e) {
            
        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    ///////////////                  MOVEMENT TESTS                    //////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void testShortestPath() {
        Province province_1 = new Province("Province1", humanPlayer1);
        province_1.setNodeNumber(62);
        game.getNodeNumberToProvince().put(62, province_1);

        Province province_2 = new Province("Province2", humanPlayer1);
        game.getNodeNumberToProvince().put(63, province_2);
        province_2.setNodeNumber(63);

        Province province_3 = new Province("Province3", humanPlayer1);
        province_3.setNodeNumber(64);
        game.getNodeNumberToProvince().put(64, province_3);

        Province province_4 = new Province("Province4", humanPlayer1);
        province_4.setNodeNumber(65);
        game.getNodeNumberToProvince().put(65, province_4);

        Province province_5 = new Province("Province5", humanPlayer1);
        province_5.setNodeNumber(66);
        game.getNodeNumberToProvince().put(66, province_5);

        Province province_6 = new Province("Province6", humanPlayer1);
        province_6.setNodeNumber(67);
        game.getNodeNumberToProvince().put(67, province_6);

        province_1.addAdjacentProvince(province_2);
        province_2.addAdjacentProvince(province_3);
        province_1.addAdjacentProvince(province_4);
        province_4.addAdjacentProvince(province_5);
        province_5.addAdjacentProvince(province_3);
        province_3.addAdjacentProvince(province_6);

        humanPlayer1.actionSetSelectedProvince(province_1);
        humanPlayer1.setDestinationProvince(province_2);
        assert (humanPlayer1.shortestPathMovPointsRequired() == 4);

        humanPlayer1.setSelectedProvince(province_1);
        humanPlayer1.setDestinationProvince(province_4);
        assert (humanPlayer1.shortestPathMovPointsRequired() == 4);

        humanPlayer1.setSelectedProvince(province_1);
        humanPlayer1.setDestinationProvince(province_3);
        assert (humanPlayer1.shortestPathMovPointsRequired() == 8);

        humanPlayer1.setSelectedProvince(province_1);
        humanPlayer1.setDestinationProvince(province_6);
        assert (humanPlayer1.shortestPathMovPointsRequired() == 12);

        humanPlayer1.setSelectedProvince(province_4);
        humanPlayer1.setDestinationProvince(province_6);
        assert (humanPlayer1.shortestPathMovPointsRequired() == 12);

    }

    @Test
    public void testShortest2Path() {
        Province province_15 = new Province("Province15", humanPlayer1);
        province_15.setNodeNumber(72);
        game.getNodeNumberToProvince().put(72, province_15);

        Province province_20 = new Province("Province20", humanPlayer1);
        game.getNodeNumberToProvince().put(73, province_20);
        province_20.setNodeNumber(73);

        humanPlayer1.setSelectedProvince(province_15);
        humanPlayer1.setDestinationProvince(province_20);
        assert (humanPlayer1.shortestPathMovPointsRequired() == 999999);

    }

    @Test
    public void testShortest3PathCannotMoveThroughEnemyTerritory() {
        Province province_15 = new Province("Province15", humanPlayer1);
        province_15.setNodeNumber(72);
        game.getNodeNumberToProvince().put(72, province_15);

        Province province_20 = new Province("Province20", humanPlayer2);
        game.getNodeNumberToProvince().put(73, province_20);
        province_20.setNodeNumber(73);

        Province province_16 = new Province("Province16", humanPlayer1);
        game.getNodeNumberToProvince().put(74, province_16);
        province_16.setNodeNumber(74);

        Province province_17 = new Province("Province17", humanPlayer1);
        game.getNodeNumberToProvince().put(75, province_17);
        province_17.setNodeNumber(75);

        Province province_21 = new Province("Province21", humanPlayer1);
        game.getNodeNumberToProvince().put(76, province_21);
        province_21.setNodeNumber(76);

        province_15.addAdjacentProvince(province_20);
        province_20.addAdjacentProvince(province_21);
        province_15.addAdjacentProvince(province_16);
        province_16.addAdjacentProvince(province_17);
        province_17.addAdjacentProvince(province_21);

        humanPlayer1.setSelectedProvince(province_15);
        humanPlayer1.setDestinationProvince(province_21);
        assert (humanPlayer1.shortestPathMovPointsRequired() == 12);
    }

    @Test
    public void testShortest4PathCannotMoveToInvadedProvinceSameTurn() {
        Province province_15 = new Province("Province15", humanPlayer1);
        province_15.setNodeNumber(72);
        game.getNodeNumberToProvince().put(72, province_15);

        Province province_20 = new Province("Province20", humanPlayer1);
        game.getNodeNumberToProvince().put(73, province_20);
        province_20.setNodeNumber(73);
        province_20.setConqueredThisTurn(true);
        province_15.addAdjacentProvince(province_20);

        humanPlayer1.setSelectedProvince(province_15);
        humanPlayer1.setDestinationProvince(province_20);
        assert (humanPlayer1.shortestPathMovPointsRequired() == 999999);

    }

    @Test
    public void testMovingUnits() {

        Province province_1 = new Province("Province1", humanPlayer1);
        Province province_2 = new Province("Province2", humanPlayer1);
        Unit unit1 = new Unit(province_2, humanPlayer1.getFaction(), humanPlayer1);
        Unit unit2 = new Unit(province_2, humanPlayer1.getFaction(), humanPlayer1);
        Unit unit3 = new Unit(province_2, humanPlayer1.getFaction(), humanPlayer1);
        assert (!unit1.actionMoveAttempt(province_2, 5));
        unit1.setUnitState(new UnitRoutedState(unit1));
        assert (!unit1.actionMoveAttempt(province_2, 5));
        unit1.setUnitState(new UnitReadyState(unit1));
        assert (unit1.actionMoveAttempt(province_2, 5));
        assert (unit2.moveAttempt(province_2, 10));
        assert (!unit3.moveAttempt(province_2, 11));

    }

    @Test
    public void testMovingGroupOfUnitsSuccess() {

        Province province_1 = new Province("Province1", humanPlayer1);
        Province province_2 = new Province("Province2", humanPlayer1);
        Unit unit1 = new Unit(province_1, humanPlayer1.getFaction(), humanPlayer1);
        Unit unit2 = new Unit(province_1, humanPlayer1.getFaction(), humanPlayer1);
        Unit unit3 = new Unit(province_1, humanPlayer1.getFaction(), humanPlayer1);
        GroupOfUnits groupOfUnits = new GroupOfUnits();
        groupOfUnits.addUnit(unit1);
        groupOfUnits.addUnit(unit2);
        groupOfUnits.addUnit(unit3);
        assert (groupOfUnits.moveAttempt(province_2, 5));

    }

    @Test
    public void testMovingSelectedUnitsEnemyProvinces() {

        Province snow = new Province("snow", humanPlayer1);
        humanPlayer1.addSelectedUnits(new Unit(snow, humanPlayer1.getFaction(), humanPlayer1));
        Province sand = new Province("sand", humanPlayer2);
        humanPlayer1.setSelectedProvince(snow);
        humanPlayer1.setDestinationProvince(sand);
        assert (!humanPlayer1.actionMoveUnits());
    }

    @Test
    public void testMovingSelectedUnitsEnemyProvinces2() {

        Province snow = new Province("snow", humanPlayer2);
        humanPlayer1.addSelectedUnits(new Unit(snow, humanPlayer1.getFaction(), humanPlayer1));
        Province sand = new Province("sand", humanPlayer2);
        humanPlayer1.setSelectedProvince(snow);
        humanPlayer1.setDestinationProvince(sand);
        assert (!humanPlayer1.moveUnits());
    }

    @Test
    public void testMovingSelectedUnitsSuccesful() {

        Province province_1 = new Province("Province1", humanPlayer1);
        province_1.setNodeNumber(62);
        game.getNodeNumberToProvince().put(62, province_1);

        Province province_2 = new Province("Province2", humanPlayer1);
        game.getNodeNumberToProvince().put(63, province_2);
        province_2.setNodeNumber(63);

        province_1.addAdjacentProvince(province_2);

        humanPlayer1.addSelectedUnits(new Unit(province_1, humanPlayer1.getFaction(), humanPlayer1));

        province_1.addAdjacentProvince(province_2);
        humanPlayer1.setSelectedProvince(province_1);
        humanPlayer1.setDestinationProvince(province_2);
        assert (humanPlayer1.moveUnits());
    }

    @Test
    public void testMovingGroupOfUnitsFail() {

        Province province_1 = new Province("Province1", humanPlayer1);
        Province province_2 = new Province("Province2", humanPlayer1);
        Unit unit1 = new Unit(province_1, humanPlayer1.getFaction(), humanPlayer1);
        Unit unit2 = new Unit(province_1, humanPlayer1.getFaction(), humanPlayer1);
        Unit unit3 = new Unit(province_1, humanPlayer1.getFaction(), humanPlayer1);
        GroupOfUnits groupOfUnits = new GroupOfUnits();
        groupOfUnits.addUnit(unit1);
        groupOfUnits.addUnit(unit2);
        groupOfUnits.addUnit(unit3);
        assert (!groupOfUnits.moveAttempt(province_2, 15));

    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    /////////////// TRAINING AND UNIT TESTS ////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void trainUnitRequest() {

        Province province_1 = new Province("Province1", humanPlayer1);
        humanPlayer1.setSelectedProvince(province_1);
        humanPlayer1.setGoldAmount(1500);
        assert (humanPlayer1.getGoldAmount() == 1500);
        assert (province_1.getUnitsInProvince().size() == 0);
        assert (humanPlayer1.getUnitsUnderControl().size() == 0);
        assert (humanPlayer1.actionRecruitUnit("Elephant"));
        assert (humanPlayer1.recruitUnit("Elephant"));
        assert (province_1.getUnitsInProvince().size() == 2);
        assert (humanPlayer1.getUnitsUnderControl().size() == 2);

        // No more training space
        assert (!humanPlayer1.recruitUnit("Elephant"));
        humanPlayer1.actionEndTurn();
        humanPlayer2.actionEndTurn();
        assert (!humanPlayer1.recruitUnit("Elephant"));
        humanPlayer1.actionEndTurn();
        humanPlayer2.actionEndTurn();

        // Now should be able to train elephant
        assert (humanPlayer1.recruitUnit("Elephant"));

    }

    @Test
    public void trainUnitRequestInsufficientFunds() {

        humanPlayer1.setGoldAmount(50);
        assert (!humanPlayer1.recruitUnit("Druid"));
        assert (!humanPlayer1.recruitUnit("Legionary"));
        assert (!humanPlayer1.recruitUnit("Berserker"));
        assert (!humanPlayer1.recruitUnit("Pikemen"));
        assert (!humanPlayer1.recruitUnit("Hoplite"));
        assert (!humanPlayer1.recruitUnit("JavelinSkirmisher"));
        assert (!humanPlayer1.recruitUnit("MissileInfantry"));
        assert (!humanPlayer1.recruitUnit("Elephant"));
        assert (!humanPlayer1.recruitUnit("HorseArcher"));
        assert (!humanPlayer1.recruitUnit("Chariot"));
        assert (!humanPlayer1.recruitUnit("Artillery"));
        assert (!humanPlayer1.recruitUnit("MeleeCavalry"));

    }

    @Test
    public void recruitUnitsFromEnemyProvince() {
        Province p = new Province("P", humanPlayer2);
        humanPlayer1.setSelectedProvince(p);
        assert (!humanPlayer1.recruitUnit("Druid"));
    }

    @Test
    public void trainUnitRequestSufficientFunds() {

        humanPlayer1.setGoldAmount(5000);
        Province province_1 = new Province("Province1", humanPlayer1);
        humanPlayer1.setSelectedProvince(province_1);
        assert (humanPlayer1.recruitUnit("Druid"));
        assert (humanPlayer1.recruitUnit("Legionary"));
        System.out.println("OKIE1");
        humanPlayer1.actionEndTurn();
        humanPlayer2.actionEndTurn();
        assert (humanPlayer1.recruitUnit("Berserker"));
        assert (humanPlayer1.recruitUnit("Pikemen"));
        System.out.println("OKIE2");
        humanPlayer1.actionEndTurn();
        humanPlayer2.actionEndTurn();
        assert (humanPlayer1.recruitUnit("Hoplite"));
        assert (humanPlayer1.recruitUnit("JavelinSkirmisher"));
        System.out.println("OKIE3");
        humanPlayer1.actionEndTurn();
        humanPlayer2.actionEndTurn();
        assert (humanPlayer1.recruitUnit("MissileInfantry"));
        assert (humanPlayer1.recruitUnit("Elephant"));
        System.out.println(humanPlayer1.getSelectedProvince().getUnitsInTraining());
        System.out.println("OKIE4");
        humanPlayer1.actionEndTurn();
        humanPlayer2.actionEndTurn();
        assert (humanPlayer1.recruitUnit("HorseArcher"));
        humanPlayer1.actionEndTurn();
        humanPlayer2.actionEndTurn();
        assert (humanPlayer1.recruitUnit("Chariot"));
        assert (humanPlayer1.recruitUnit("Artillery"));
        humanPlayer1.actionEndTurn();
        humanPlayer2.actionEndTurn();
        assert (humanPlayer1.recruitUnit("MeleeCavalry"));
        humanPlayer1.setFaction("Roman");
        assert (humanPlayer1.recruitUnit("Legionary"));

    }

    @Test
    public void testBerserkerBasedOnFaction() {

        Province province_1 = new Province("Province1", humanPlayer1);
        humanPlayer1.setSelectedProvince(province_1);
        assert (humanPlayer1.recruitUnit("Berserker"));
        humanPlayer1.setFaction("Gallic");
        assert (humanPlayer1.recruitUnit("Berserker"));
        humanPlayer1.actionEndTurn();
        humanPlayer2.actionEndTurn();
        humanPlayer1.setFaction("Celtic Briton");
        assert (humanPlayer1.recruitUnit("Berserker"));
        humanPlayer1.setFaction("Germanic");
        assert (humanPlayer1.recruitUnit("Berserker"));

    }

    @Test
    public void MoraleTooLow() {
        Unit u = new Unit(currentProvince, humanPlayer1.getFaction(), humanPlayer1);
        u.setMorale(-1);
        assert(u.getMorale() == 1);
    }

    @Test
    public void updateRoutedtoReady(){
        Elephant u = new Elephant(currentProvince, humanPlayer1.getFaction(), humanPlayer1);
        humanPlayer1.addUnit(u);
        u.setUnitState(new UnitRoutedState(u));
        assert(u.getUnitState() instanceof UnitRoutedState);
        humanPlayer1.sequential();
        assert(u.getUnitState() instanceof UnitReadyState);
    }

    @Test
    public void applySpecialAbilityNoEffect() {
        Unit u = new Unit(currentProvince, humanPlayer1.getFaction(), humanPlayer1);
        u.applySpecialAbility();
        //Nothing Happens
    }


    

    ///////////////////////////////////////////////////////////////////////////////////////////
    /////////////// BATTLE RESOLVER TESTS ////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void unitRoutingTest() {

        Province province_1 = new Province("Province1", humanPlayer1);
        Unit unit1 = new Unit(province_1, humanPlayer1.getFaction(), humanPlayer1);
        unit1.routeUnit();
        assert (unit1.getUnitState() instanceof UnitRoutedState);

    }

    @Test
    public void unitStateTest() {

        Province province_1 = new Province("Province1", humanPlayer1);
        Unit unit1 = new Unit(province_1, humanPlayer1.getFaction(), humanPlayer1);
        unit1.setUnitState(new UnitRoutedState(unit1));

    }

    @Test
    public void unitDestroyTest() {
        Province province_1 = new Province("Province1", humanPlayer1);
        Unit unit1 = new Unit(province_1, humanPlayer1.getFaction(), humanPlayer1);
        humanPlayer1.addUnit(unit1);
        assert (humanPlayer1.getUnitsUnderControl().size() == 1);
        assert (province_1.getUnitsInProvince().size() == 1);
        unit1.destroyUnit();
        assert (humanPlayer1.getUnitsUnderControl().size() == 0);
        assert (province_1.getUnitsInProvince().size() == 0);

    }

    @Test
    public void testMeleeEngagementStrategy() {

        //for (int i = 0; i < 1000; i++) {
            Province dude = new Province("dood", humanPlayer2);
            Pikeman unitOne = new Pikeman(currentProvince, humanPlayer1.getFaction(), humanPlayer1);
            unitOne.changeUnitState(new UnitReadyState(unitOne));
            Pikeman unitTwo = new Pikeman(dude, humanPlayer2.getFaction(), humanPlayer2);
            unitTwo.changeUnitState(new UnitReadyState(unitTwo));
            unitOne.setNumTroops(100);
            currentProvince.addAdjacentProvince(dude);
            MeleeEngagement m = new MeleeEngagement();
            m.unitEngagement(unitOne, unitTwo);
        //}
    }

    @Test
    public void testMeleeEngagementStrategy2() {

        //for (int i = 0; i < 1000; i++) {
            Province dude = new Province("dood", humanPlayer2);
            Pikeman unitOne = new Pikeman(currentProvince, humanPlayer1.getFaction(), humanPlayer1);
            unitOne.changeUnitState(new UnitReadyState(unitOne));
            Pikeman unitTwo = new Pikeman(dude, humanPlayer2.getFaction(), humanPlayer2);
            unitTwo.changeUnitState(new UnitReadyState(unitTwo));
            unitTwo.setMeleeAttack(0);
            unitOne.setNumTroops(100);
            currentProvince.addAdjacentProvince(dude);
            MeleeEngagement m = new MeleeEngagement();
            m.unitEngagement(unitOne, unitTwo);
        //}
    }

    @Test
    public void testMissileEngagementStrategy() {
        for (int i = 0; i < 1000; i++) {
            Province dude = new Province("dood", humanPlayer2);
            HorseArcher unitOne = new HorseArcher(currentProvince, humanPlayer1.getFaction(), humanPlayer1);
            unitOne.changeUnitState(new UnitReadyState(unitOne));
            HorseArcher unitTwo = new HorseArcher(dude, humanPlayer2.getFaction(), humanPlayer2);
            unitTwo.changeUnitState(new UnitReadyState(unitTwo));
            unitOne.setNumTroops(100);
            currentProvince.addAdjacentProvince(dude);
            MissileEngagement m = new MissileEngagement();
            m.unitEngagement(unitOne, unitTwo);
        }
    }

    @Test
    public void testMissileEngagementStrategy2() {
        for (int i = 0; i < 1000; i++) {
            Province dude = new Province("dood", humanPlayer2);
            HorseArcher unitOne = new HorseArcher(currentProvince, humanPlayer1.getFaction(), humanPlayer1);
            unitOne.changeUnitState(new UnitReadyState(unitOne));
            unitOne.setNumTroops(1);
            HorseArcher unitTwo = new HorseArcher(dude, humanPlayer2.getFaction(), humanPlayer2);
            unitTwo.changeUnitState(new UnitReadyState(unitTwo));
            unitTwo.setNumTroops(100);
            currentProvince.addAdjacentProvince(dude);
            MissileEngagement m = new MissileEngagement();
            m.unitEngagement(unitOne, unitTwo);
        }
    }

    @Test
    public void testInvadeAttemptSuccess() {
        Province dude = new Province("dood", humanPlayer2);
        
        for (int i = 0; i < 100; i++) {
            Pikeman unit1Player = new Pikeman(currentProvince, humanPlayer1.getFaction(), humanPlayer1);
            unit1Player.changeUnitState(new UnitReadyState(unit1Player));
            unit1Player.setNumTroops(100);
        }   
        
        HorseArcher unitEnemy = new HorseArcher(dude, humanPlayer2.getFaction(), humanPlayer2);
        unitEnemy.changeUnitState(new UnitReadyState(unitEnemy));
        unitEnemy.setNumTroops(100);
 
        currentProvince.addAdjacentProvince(dude);
        humanPlayer1.setDestinationProvince(dude);
        assert(humanPlayer1.actionInvadeAttempt());
    }

    @Test
    public void testInvadeAttemptDraw() {
        Province dude = new Province("dood", humanPlayer2);
        
        for (int i = 0; i < 300; i++) {
            Pikeman unit1Player = new Pikeman(currentProvince, humanPlayer1.getFaction(), humanPlayer1);
            unit1Player.changeUnitState(new UnitReadyState(unit1Player));
            unit1Player.setNumTroops(100);
        }   
        
        for (int i = 0; i < 300; i++) {
            HorseArcher unitEnemy = new HorseArcher(dude, humanPlayer2.getFaction(), humanPlayer2);
            unitEnemy.changeUnitState(new UnitReadyState(unitEnemy));
            unitEnemy.setNumTroops(100);
        }       
 
        currentProvince.addAdjacentProvince(dude);
        humanPlayer1.setDestinationProvince(dude);
        assert(!humanPlayer1.invadeAttempt());
    }

    @Test
    public void testInvadeAttemptFail() {
        Province dude = new Province("dood", humanPlayer2);
        Pikeman unitPlayer = new Pikeman(currentProvince, humanPlayer1.getFaction(), humanPlayer1);
        unitPlayer.changeUnitState(new UnitReadyState(unitPlayer));
        unitPlayer.setNumTroops(100);

        for (int i = 0; i < 100; i++) {
            HorseArcher unitEnemy = new HorseArcher(dude, humanPlayer2.getFaction(), humanPlayer2);
            unitEnemy.changeUnitState(new UnitReadyState(unitEnemy));
            unitEnemy.setNumTroops(100);
        }       
 
        currentProvince.addAdjacentProvince(dude);
        humanPlayer1.setDestinationProvince(dude);
        assert(!humanPlayer1.invadeAttempt());
    }


    @Test
    public void testInvadeAttemptNoUnits() {
        Province dude = new Province("dood", humanPlayer2);    
 
        currentProvince.addAdjacentProvince(dude);
        humanPlayer1.setDestinationProvince(dude);
        assert(humanPlayer1.invadeAttempt());
    }

    @Test
    public void testInvadeAttemptMeleeChanceCap() {
        for (int i = 0 ; i < 100 ; i++) {
            Province dude = new Province("dood", humanPlayer2);
            
            Pikeman unit1Player = new Pikeman(currentProvince, humanPlayer1.getFaction(), humanPlayer1);
            unit1Player.changeUnitState(new UnitReadyState(unit1Player));
            unit1Player.setNumTroops(100);
            unit1Player.setSpeed(996);
            
            HorseArcher unitEnemy = new HorseArcher(dude, humanPlayer2.getFaction(), humanPlayer2);
            unitEnemy.changeUnitState(new UnitReadyState(unitEnemy));
            unitEnemy.setNumTroops(100);
            unitEnemy.setSpeed(1);
    
            currentProvince.addAdjacentProvince(dude);
            humanPlayer1.setDestinationProvince(dude);
            humanPlayer1.invadeAttempt();
            // randomly reachable
        }
    }

    @Test
    public void testInvadeAttemptMissileChanceCap() {
            Province dude = new Province("dood", humanPlayer2);
            
            Pikeman unit1Player = new Pikeman(currentProvince, humanPlayer1.getFaction(), humanPlayer1);
            unit1Player.changeUnitState(new UnitReadyState(unit1Player));
            unit1Player.setNumTroops(100);
            unit1Player.setSpeed(50);
            
            HorseArcher unitEnemy = new HorseArcher(dude, humanPlayer2.getFaction(), humanPlayer2);
            unitEnemy.changeUnitState(new UnitReadyState(unitEnemy));
            unitEnemy.setNumTroops(100);
            unitEnemy.setSpeed(100);
    
            currentProvince.addAdjacentProvince(dude);
            humanPlayer1.setDestinationProvince(dude);
            humanPlayer1.invadeAttempt();
            // randomly reachable
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    /////////////// SPECIAL ABILITIES TESTS /////////////
    ///////////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void SpecialAbilty_1_RomanLegionary1() {
        
        // Defender with 5 Roman Legionaries loses province and loses morale for other units across faction
        Province dude = new Province("dood", humanPlayer2);
        
        //Disjointed province
        Province bro = new Province("bru", humanPlayer2);
        humanPlayer2.setFaction("Roman");
        assert(Objects.equals(dude.getOwner(),humanPlayer2));

        Unit unitEnemyMoraleChange = new Legionary(bro, humanPlayer2.getFaction(), humanPlayer2);
        unitEnemyMoraleChange.changeUnitState(new UnitReadyState(unitEnemyMoraleChange));
        humanPlayer2.addUnit(unitEnemyMoraleChange);
        assert(unitEnemyMoraleChange.getMorale() == 10); 

        for (int i = 0; i < 100; i++) {
            Pikeman unit1Player = new Pikeman(currentProvince, humanPlayer1.getFaction(), humanPlayer1);
            unit1Player.changeUnitState(new UnitReadyState(unit1Player));
            unit1Player.setNumTroops(100);
        }   
        
        for (int i = 0; i < 5; i++) {
            Legionary unitEnemy = new Legionary(dude, humanPlayer2.getFaction(), humanPlayer2);
            unitEnemy.changeUnitState(new UnitReadyState(unitEnemy));
            unitEnemy.setNumTroops(100);
        } 
 
        currentProvince.addAdjacentProvince(dude);
        humanPlayer1.setDestinationProvince(dude);
        assert(humanPlayer1.invadeAttempt());
        assert(unitEnemyMoraleChange.getMorale() == 9); 
        assert(Objects.equals(dude.getOwner(),humanPlayer1));

        // Region province and regain morale for units across faction
        Province peanutButter = new Province("peanutButter", humanPlayer2);
        for (int i = 0; i < 100; i++) {
            Unit unitEnemy = new Unit(peanutButter, humanPlayer2.getFaction(), humanPlayer2);
            unitEnemy.changeUnitState(new UnitReadyState(unitEnemy));
            unitEnemy.setNumTroops(100);
        } 

        System.out.println("--------------------------------------------------------------");
        peanutButter.addAdjacentProvince(dude);

        humanPlayer2.setSelectedProvince(peanutButter);
        humanPlayer2.setDestinationProvince(dude);

        assert(humanPlayer2.invadeAttempt());
        assert(Objects.equals(dude.getOwner(),humanPlayer2));

        System.out.println("--------------------------------------------------------------");
        System.out.println(unitEnemyMoraleChange.getMorale());
        assert(unitEnemyMoraleChange.getMorale() == 10); 
    }

    @Test
    public void SpecialAbilty_1_RomanLegionary2NotRoman() {
        
        // Defender with 5 Roman Legionaries loses province and loses morale for other units across faction
        Province dude = new Province("dood", humanPlayer2);
        
        //Disjointed province
        Province bro = new Province("bru", humanPlayer2);
        humanPlayer2.setFaction("NotRoman");
        assert(Objects.equals(dude.getOwner(),humanPlayer2));

        Unit unitEnemyMoraleChange = new Legionary(bro, humanPlayer2.getFaction(), humanPlayer2);
        unitEnemyMoraleChange.changeUnitState(new UnitReadyState(unitEnemyMoraleChange));
        humanPlayer2.addUnit(unitEnemyMoraleChange);
        assert(unitEnemyMoraleChange.getMorale() == 10); 

        for (int i = 0; i < 100; i++) {
            Pikeman unit1Player = new Pikeman(currentProvince, humanPlayer1.getFaction(), humanPlayer1);
            unit1Player.changeUnitState(new UnitReadyState(unit1Player));
            unit1Player.setNumTroops(100);
        }   
        
        for (int i = 0; i < 5; i++) {
            Legionary unitEnemy = new Legionary(dude, humanPlayer2.getFaction(), humanPlayer2);
            unitEnemy.changeUnitState(new UnitReadyState(unitEnemy));
            unitEnemy.setNumTroops(100);
        } 
 
        currentProvince.addAdjacentProvince(dude);
        humanPlayer1.setDestinationProvince(dude);
        assert(humanPlayer1.invadeAttempt());
        assert(unitEnemyMoraleChange.getMorale() == 10); 
        assert(Objects.equals(dude.getOwner(),humanPlayer1));

        // Region province and regain morale for units across faction
        Province peanutButter = new Province("peanutButter", humanPlayer2);
        for (int i = 0; i < 100; i++) {
            Unit unitEnemy = new Unit(peanutButter, humanPlayer2.getFaction(), humanPlayer2);
            unitEnemy.changeUnitState(new UnitReadyState(unitEnemy));
            unitEnemy.setNumTroops(100);
        } 

        System.out.println("--------------------------------------------------------------");
        peanutButter.addAdjacentProvince(dude);

        humanPlayer2.setSelectedProvince(peanutButter);
        humanPlayer2.setDestinationProvince(dude);

        assert(humanPlayer2.invadeAttempt());
        assert(Objects.equals(dude.getOwner(),humanPlayer2));

        System.out.println("--------------------------------------------------------------");
        System.out.println(unitEnemyMoraleChange.getMorale());
        assert(unitEnemyMoraleChange.getMorale() == 10); 
    }


    @Test
    public void SpecialAbilty_3_MeleeCavalry1() {
        
        Province dude = new Province("dood", humanPlayer2);
        
        MeleeCavalry unitPlayer = new MeleeCavalry(currentProvince, humanPlayer1.getFaction(), humanPlayer1);
        System.out.println(unitPlayer.getMeleeAttack());
        unitPlayer.changeUnitState(new UnitReadyState(unitPlayer));
        
        MeleeCavalry unitEnemy = new MeleeCavalry(dude, humanPlayer2.getFaction(), humanPlayer2);
        unitEnemy.changeUnitState(new UnitReadyState(unitEnemy));
        
        System.out.println(unitPlayer.getMeleeAttack());
        assert(unitPlayer.getMeleeAttack() == 110);
        assert(unitEnemy.getMeleeAttack() == 110);
        
        currentProvince.addAdjacentProvince(dude);
        humanPlayer1.setDestinationProvince(dude);
        humanPlayer1.invadeAttempt();

    
        assert(unitPlayer.getMeleeAttack() == 110);
        assert(unitEnemy.getMeleeAttack() == 110);
    }

    @Test
    public void SpecialAbilty_3_MeleeCavalry2() {
        
        Province dude = new Province("dood", humanPlayer2);
        
        MeleeCavalry unitPlayer = new MeleeCavalry(currentProvince, humanPlayer1.getFaction(), humanPlayer1);
      
        unitPlayer.changeUnitState(new UnitReadyState(unitPlayer));
        
        Unit unitEnemy1 = new Unit(dude, humanPlayer2.getFaction(), humanPlayer2);
        unitEnemy1.changeUnitState(new UnitReadyState(unitEnemy1));
        Unit unitEnemy2 = new Unit(dude, humanPlayer2.getFaction(), humanPlayer2);
        unitEnemy2.changeUnitState(new UnitReadyState(unitEnemy2));
        Unit unitEnemy3 = new Unit(dude, humanPlayer2.getFaction(), humanPlayer2);
        unitEnemy3.changeUnitState(new UnitReadyState(unitEnemy3));
        humanPlayer1.setDestinationProvince(dude);
        
        assert(unitPlayer.getMeleeAttack() == 110);
        
        currentProvince.addAdjacentProvince(dude);
        humanPlayer1.invadeAttempt();

        assert(unitPlayer.getMeleeAttack() == 120);
 

    }

    @Test
    public void SpecialAbilty_3_MeleeCavalry3() {
        MeleeCavalry unitPlayer = new MeleeCavalry(currentProvince, humanPlayer1.getFaction(), humanPlayer1);
        assert(unitPlayer.getMorale() == 10);
        unitPlayer.applySpecialAbility();
        assert(unitPlayer.getMorale() == 15);

    }

    @Test
    public void SpecialAbilty_3_MeleeCavalry4() {
        
        Province dude = new Province("dood", humanPlayer2);
        
        Unit unitPlayer1 = new Unit(currentProvince, humanPlayer1.getFaction(), humanPlayer1);
        unitPlayer1.changeUnitState(new UnitReadyState(unitPlayer1));
        Unit unitPlayer2= new Unit(currentProvince, humanPlayer1.getFaction(), humanPlayer1);
        unitPlayer2.changeUnitState(new UnitReadyState(unitPlayer2));
        Unit unitPlayer3 = new Unit(currentProvince, humanPlayer1.getFaction(), humanPlayer1);
        unitPlayer3.changeUnitState(new UnitReadyState(unitPlayer3));
        Unit unitPlayer4 = new Unit(currentProvince, humanPlayer1.getFaction(), humanPlayer1);
        unitPlayer4.changeUnitState(new UnitReadyState(unitPlayer3));

        MeleeCavalry unitEnemy = new MeleeCavalry(dude, humanPlayer2.getFaction(), humanPlayer2);
        unitEnemy.changeUnitState(new UnitReadyState(unitEnemy));
        
        humanPlayer1.setDestinationProvince(dude);      
        
        assert(unitEnemy.getMeleeAttack() == 110);
        
        currentProvince.addAdjacentProvince(dude);
        humanPlayer1.invadeAttempt();
        System.out.println(unitEnemy.getMeleeAttack());
        assert(unitEnemy.getMeleeAttack() >= 120);

    }

    @Test
    public void SpecialAbility_6_Elephant1() {
        for (int i = 0; i < 100; i++) {
            Province dude = new Province("dood", humanPlayer2);
            Elephant unitOne = new Elephant(currentProvince, humanPlayer1.getFaction(), humanPlayer1);
            unitOne.changeUnitState(new UnitReadyState(unitOne));
            Elephant unitTwo = new Elephant(dude, humanPlayer2.getFaction(), humanPlayer2);
            unitTwo.changeUnitState(new UnitReadyState(unitTwo));
            //dude.addUnit(unitTwo);
            unitOne.setNumTroops(100);
            currentProvince.addAdjacentProvince(dude);
            MeleeEngagement m = new MeleeEngagement();
            m.unitEngagement(unitOne, unitTwo);
        }
    }

    @Test
    public void SpecialAbilty_8_Druid1() {

        Province dude = new Province("dood", humanPlayer2);
        
        Druid unitPlayer = new Druid(currentProvince, humanPlayer1.getFaction(), humanPlayer1);
        int originalMorale = unitPlayer.getMorale();

        unitPlayer.changeUnitState(new UnitReadyState(unitPlayer));
        
        Druid unitEnemy1 = new Druid(dude, humanPlayer2.getFaction(), humanPlayer2);
        unitEnemy1.changeUnitState(new UnitReadyState(unitEnemy1));
        
        humanPlayer1.setDestinationProvince(dude);
          
        currentProvince.addAdjacentProvince(dude);
        humanPlayer1.invadeAttempt();

        assert(unitPlayer.getMorale() == (int)(originalMorale*0.95*1.1));

    }

    @Test
    public void SpecialAbilty_8_Druid2() {

        Province dude = new Province("dood", humanPlayer2);
        
        for(int i = 0; i < 10 ; i++) {
            Druid unitPlayer = new Druid(currentProvince, humanPlayer1.getFaction(), humanPlayer1);
            unitPlayer.changeUnitState(new UnitReadyState(unitPlayer));
        }

        Unit unitEnemy = new Unit(dude, humanPlayer2.getFaction(), humanPlayer2);
        unitEnemy.changeUnitState(new UnitReadyState(unitEnemy));

        int originalMorale = unitEnemy.getMorale();

        humanPlayer1.setDestinationProvince(dude);
          
        currentProvince.addAdjacentProvince(dude);
        humanPlayer1.invadeAttempt();

        assert(unitEnemy.getMorale() == (int)(originalMorale*0.95*5));

    }

    @Test
    public void SpecialAbilty_8_Druid3() {

        Province dude = new Province("dood", humanPlayer2);
        
        for(int i = 0; i < 10 ; i++) {
            Druid enemy = new Druid(dude, humanPlayer2.getFaction(), humanPlayer2);
            enemy.changeUnitState(new UnitReadyState(enemy));
        }

        Unit unitPlayer = new Unit(currentProvince, humanPlayer2.getFaction(), humanPlayer1);
        unitPlayer.changeUnitState(new UnitReadyState(unitPlayer));

        int originalMorale = unitPlayer.getMorale();

        humanPlayer1.setDestinationProvince(dude);
          
        currentProvince.addAdjacentProvince(dude);
        humanPlayer1.invadeAttempt();

        assert(unitPlayer.getMorale() == (int)(originalMorale*0.95*5));

    }





    ///////////////////////////////////////////////////////////////////////////////////////////
    /////////////// WEALTH AND TAXES TESTS ////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void taxRateTest() {

        Province province_1 = new Province("Province1", humanPlayer1);
        humanPlayer1.setSelectedProvince(province_1);
        assert (province_1.getWealth() == 500);
        humanPlayer1.endTurn();
        humanPlayer2.endTurn();
        assert (province_1.getWealth() == 510);
        humanPlayer1.actionAssignTaxRate("Low Tax");
        assert (province_1.getTaxRate() == 0.10);
        humanPlayer1.assignTaxRate("Normal Tax");
        assert (province_1.getTaxRate() == 0.15);
        humanPlayer1.assignTaxRate("High Tax");
        assert (province_1.getTaxRate() == 0.20);
        humanPlayer1.assignTaxRate("Very High Tax");
        assert (province_1.getTaxRate() == 0.25);

        // humanPlayer1.actionAssignTaxRate(0.15);

    }

    @Test
    public void testGetTaxRate() {
        currentProvince.setTaxRate(5.0);
        assert (currentProvince.getTaxRate() == 5.0);
    }

    @Test
    public void testSetTaxRate() {
        currentProvince.setTaxRate(5.0);
        assert (currentProvince.getTaxRate() == 5.0);
    }

    @Test
    public void testGetWealthGrowthRate() {
        currentProvince.setWealthGrowthRate(150);
        assert (currentProvince.getWealthGrowthRate() == 150);
    }

    @Test
    public void testSetWealthGrowthRate() {
        currentProvince.setWealthGrowthRate(200);
        assert (currentProvince.getWealthGrowthRate() == 200);
    }

    @Test
    public void testCollectTaxIncome10percent() {
        currentProvince.setTaxRate(0.10);
        currentProvince.setWealthGrowthRate(200);
        currentProvince.collectTaxIncome();
        assert (currentProvince.getWealthGrowthRate() == 210);
    }

    @Test
    public void testCollectTaxIncome20percent() {
        currentProvince.setTaxRate(0.20);
        currentProvince.setWealthGrowthRate(200);
        currentProvince.collectTaxIncome();
        assert (currentProvince.getWealthGrowthRate() == 190);
    }

    @Test
    public void testCollectTaxIncome25percent() {
        currentProvince.setTaxRate(0.25);
        currentProvince.setWealthGrowthRate(200);
        unit.setMorale(5);
        currentProvince.collectTaxIncome();
        assert (currentProvince.getWealthGrowthRate() == 170);
        assert (unit.getMorale() == 4);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    /////////////// CAMPAIGN VICTORY TESTS ////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void testCampaignGoalVictoryCondition1() {

        humanPlayer1.getProvincesUnderControl().clear();
        humanPlayer1.getPlayerGoals().set(0, "CONQUEST");
        humanPlayer1.getPlayerGoals().set(1, "WEALTH");
        humanPlayer1.getPlayerGoals().set(2, "TREASURY");

        assert (!humanPlayer1.isMainGoal());
        assert (!humanPlayer1.isSubGoal1());
        assert (!humanPlayer1.isSubGoal2());
        assert (!humanPlayer1.isVictor());

        assert (humanPlayer1.getProvincesUnderControl().size() == 0);

        // Satisfy CONQUEST GOAL
        for (int i = 1; i <= 53; i++) {
            new Province("NewProvince", humanPlayer1);
        }
        assert (humanPlayer1.getProvincesUnderControl().size() == 53);
        humanPlayer1.checkIfVictoryExists();
        assert (humanPlayer1.isMainGoal());
        assert (!humanPlayer1.isVictor());

        // Satify WEALTH GOAL
        humanPlayer1.getProvincesUnderControl().get(1).setWealth(400000);
        humanPlayer1.checkIfVictoryExists();
        assert (humanPlayer1.isSubGoal1());
        assert (humanPlayer1.isVictor());

    }

    @Test
    public void testCampaignGoalVictoryCondition2() {
        humanPlayer1.getProvincesUnderControl().clear();
        humanPlayer1.getPlayerGoals().set(0, "CONQUEST");
        humanPlayer1.getPlayerGoals().set(1, "WEALTH");
        humanPlayer1.getPlayerGoals().set(2, "TREASURY");

        assert (!humanPlayer1.isMainGoal());
        assert (!humanPlayer1.isSubGoal1());
        assert (!humanPlayer1.isSubGoal2());
        humanPlayer1.checkIfVictoryExists();
        assert (!humanPlayer1.isVictor());

        assert (humanPlayer1.getProvincesUnderControl().size() == 0);

        // Satisfy CONQUEST GOAL
        for (int i = 1; i <= 53; i++) {
            new Province("NewProvince", humanPlayer1);
        }
        assert (humanPlayer1.getProvincesUnderControl().size() == 53);
        humanPlayer1.checkIfVictoryExists();
        assert (humanPlayer1.isMainGoal());
        assert (!humanPlayer1.isVictor());

        // Satify WEALTH GOAL
        humanPlayer1.setGoldAmount(100000);
        humanPlayer1.checkIfVictoryExists();
        assert (humanPlayer1.isSubGoal2());
        assert (humanPlayer1.isVictor());

    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    /////////////// PLAYER ACTIONS TESTS ////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Test attempts to give a province to a player
     */
    @Test
    public void givePlayerProvince() {
        try {
            game.saveGame();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Province province_normal = new Province("Normal", humanPlayer1);
        Province province_sea = new Province("Sea", humanPlayer1);
        assert(humanPlayer1.getProvincesUnderControl().contains(province_normal));
        assert(humanPlayer1.getProvincesUnderControl().contains(province_sea));
    }



    @Test
    public void removePlayerProvince() {
        Province province_normal = new Province("Normal", humanPlayer1);
        Province province_sea = new Province("Sea", humanPlayer1);
        humanPlayer1.actionRemoveProvince(province_sea);
        assert(humanPlayer1.getProvincesUnderControl().contains(province_sea) == false);
    }

    @Test
    public void testEndTurn() {
        //Initially it will be humanPlayer1's turn. Turn counter should be 1
        assert(game.getTurnCounter() == 1);
        humanPlayer1.actionEndTurn();
        //Sequential steps will be run until it is player's turn again. So the following code should work
        Province province_sea = new Province("Sea", humanPlayer1);
        assert(humanPlayer1.getProvincesUnderControl().contains(province_sea));
    }

    @Test
    public void PlayerStandbyState() {
        // Nothing should happen
        humanPlayer1.setPlayerState(new PlayerStandbyState(humanPlayer1));
        humanPlayer1.actionAssignTaxRate("Low Tax");
        Province newTown = new Province("newTown", humanPlayer1);
        humanPlayer1.actionSetSelectedProvince(currentProvince);
        assert(!Objects.equals(newTown, humanPlayer1.getSelectedProvince()));
        humanPlayer1.actionRemoveProvince(currentProvince);
        assert(humanPlayer1.actionMoveUnits() == false);
        humanPlayer1.actionRecruitUnit("ANY");
        assert(humanPlayer1.actionInvadeAttempt() == false);
        humanPlayer1.actionEndTurn();

    }

///////////////////////////////////////////////////////////////////////////////////////////
///////////////                   PROVINCE TESTS                        ////////////////////
///////////////////////////////////////////////////////////////////////////////////////////


    @Test
    public void testProvinceAdjacency() {
        Province province_1 = new Province("Province1" , humanPlayer1);
        province_1.setNodeNumber(62);
        Province province_2 = new Province("Province2" , humanPlayer1);
        province_2.setNodeNumber(63);
        assert(!province_1.isAdjacent(province_2));
        province_1.addAdjacentProvince(province_2);

        assert(province_1.isAdjacent(province_2));

    }

    @Test
    public void getAdjacentProvinceTest() {
        Province province_1 = new Province("Province1" , humanPlayer1);
        province_1.setNodeNumber(62);
        Province province_2 = new Province("Province2" , humanPlayer1);
        province_2.setNodeNumber(63);
        assert(province_1.getAdjacentProvinces().size() == 0);
        assert(province_2.getAdjacentProvinces().size() == 0);
        province_1.addAdjacentProvince(province_2);
        assert(province_1.getAdjacentProvinces().size() == 1);
        assert(province_2.getAdjacentProvinces().size() == 1);
    }

    @Test
    public void getAdjacent2ProvinceTest() {
        Province province_1 = new Province("Province1" , humanPlayer1);
        Province province_2 = new Province("Province2" , humanPlayer1);
        province_1.addAdjacentProvince(province_2);
        for(Province p : province_1.getAdjacentProvinces()) {
            assert(p instanceof Province);
        }
    }
 
    @Test
    public void testAddUnitToProvince() {
        Unit testUnit = new Unit(currentProvince, humanPlayer1.getFaction(), humanPlayer1);
        currentProvince.addUnit(testUnit);
        assert(currentProvince.getUnitsInProvince().contains(testUnit));
    }

    @Test
    public void testRemoveUnitFromProvince() {
        // first add a unit to the province
        Unit testUnit = new Unit(currentProvince, humanPlayer1.getFaction(), humanPlayer1);
        // then remove the unit from the province
        currentProvince.removeUnit(testUnit);
        assert(currentProvince.getUnitsInProvince().contains(testUnit) == false);
    }

    @Test
    public void testGetNumberOfUnitsInProvince() {
        Unit testUnit = new Unit(currentProvince, humanPlayer1.getFaction(), humanPlayer1);
        // we already added one unit in the setup
        assert(currentProvince.getNumberOfUnits() == currentProvince.getUnitsInProvince().size());
        assert(currentProvince.getNumberOfUnits() == 2);

    }

    @Test
    public void testNumberOfAvailabelUnits() {
        assert(currentProvince.numberOfAvailableUnit() == 0);
        Unit testUnit = new Unit(currentProvince, humanPlayer1.getFaction(), humanPlayer1);
        Unit testUnit2 = new Unit(currentProvince, humanPlayer1.getFaction(), humanPlayer1);
        Unit testUnit3 = new Unit(currentProvince, humanPlayer1.getFaction(), humanPlayer1);
        assert(currentProvince.numberOfAvailableUnit() == 0);
        testUnit2.setUnitState(new UnitReadyState(testUnit2));
        assert(currentProvince.numberOfAvailableUnit() == 1);


    }

}