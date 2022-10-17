package unsw.gloriaromanus.classes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.io.Serializable;

import unsw.gloriaromanus.states.PlayerActiveState;
import unsw.gloriaromanus.states.PlayerStandbyState;

public class Game implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final int numberOfGameProvinces = 53;
    private String provinceFile = "src/unsw/gloriaromanus/province_adjacency_matrix_fully_connected.json";

    private String gameName;
    private HumanPlayer humanPlayer1;
    private HumanPlayer humanPlayer2;
    private int turnCounter = 1;
    private Player currentPlayer;
    private ArrayList<Province> listOfGameProvinces;
    private HashMap<Integer, Province> nodeNumberToProvince; 
    private String engagementStyle = "BasicBattleResolver";
    private boolean gameEnded = false;

    /**
     * Create a basic game
     * @param gameName
     */
    public Game(String gameName) {
        this.gameName = gameName;
        this.humanPlayer1 = new HumanPlayer(this, "Rome");
        humanPlayer1.setPlayerState(new PlayerActiveState(humanPlayer1));
        this.humanPlayer2 = new HumanPlayer(this, "Gaul");

        // both players have the same goals
        humanPlayer2.setPlayerGoals(humanPlayer1.getPlayerGoals());

        humanPlayer2.setPlayerState(new PlayerStandbyState(humanPlayer2));
        this.currentPlayer = this.humanPlayer1;
        this.listOfGameProvinces = new ArrayList<Province>();
        this.nodeNumberToProvince = new HashMap<Integer, Province>();
        setUpProvinces();

      }

    /**
     * Create a game, specifying desired player factions and battleResolver
     * @param gameName
     * @param player1Faction
     * @param player2Faction
     * @param engagementStyle
     */
    public Game(String gameName, String player1Faction, String player2Faction, String engagementStyle) {
        this.gameName = gameName;
        this.humanPlayer1 = new HumanPlayer(this, player1Faction);
        humanPlayer1.setPlayerState(new PlayerActiveState(humanPlayer1));
        this.humanPlayer2 = new HumanPlayer(this, player2Faction);
        humanPlayer2.setPlayerState(new PlayerStandbyState(humanPlayer2));
        this.currentPlayer = this.humanPlayer1;
        this.listOfGameProvinces = new ArrayList<Province>();
        this.nodeNumberToProvince = new HashMap<Integer, Province>();
        this.engagementStyle = engagementStyle;
        
        setUpProvinces();
     
        
    }

    /**
     * Advances to next player 
     */
    public void advancePlayer() {

        // If human change to computer
        if (currentPlayer == humanPlayer1) {
            humanPlayer2.sequential();
            currentPlayer = humanPlayer2;
        }

        // If computer change to computer1
        else {
            currentPlayer = humanPlayer1;
            humanPlayer1.sequential();
            turnCounter++;
        }
    }

    /**
     * Setup intial game provinces and allocates them randomly to players
     */
    public void setUpProvinces() {

        ArrayList<String> listOfProvinces = new ArrayList<String>();
       
        try {
            String contents = new String((Files.readAllBytes(Paths.get(provinceFile))));
            JSONObject provinces = new JSONObject(contents);
            // Obtain list of all province names
            for (String provinceName : provinces.keySet()) {

                listOfProvinces.add(provinceName);
            }

            // Shuffle the province name list for random province allocation
            Collections.shuffle(listOfProvinces);
            assignProvincesToPlayers(listOfProvinces);
            addAllProvinceAdjacencies(provinces);

        } catch (IOException e) {
            System.out.println("File Does Not Exist");
        }
        
    }

    /**
     * Assign provinces to players randomly
     * @param listOfProvinces
     */
    public void assignProvincesToPlayers(ArrayList<String> listOfProvinces) {

        int i = 0;
        int nodeNumber = 0;
        for (String provinceName : listOfProvinces) {

            Province newProvince;

            if (i % 2 == 0) {
                System.out.println("Player 1 gets:" + provinceName + i);
                newProvince = new Province(provinceName, humanPlayer1);
                newProvince.setNodeNumber(nodeNumber);
                nodeNumberToProvince.put(i, newProvince);
                //humanPlayer1.addProvince(newProvince);

            } else {
                System.out.println("Player 2 gets:" + provinceName);
                newProvince = new Province(provinceName, humanPlayer2);
                newProvince.setNodeNumber(nodeNumber);
                nodeNumberToProvince.put(i, newProvince);
                //humanPlayer2.addProvince(newProvince);

            }
            listOfGameProvinces.add(newProvince);
            nodeNumber++;
            i++;
        }
    }

    /**
     * Creates a connected graph of adjacent provinces 
     * @param provinces
     */
    public void addAllProvinceAdjacencies(JSONObject provinces) {

        for (String provinceName : provinces.keySet()) {
            JSONObject provinceAdjacency = provinces.getJSONObject(provinceName);
            // System.out.println(provinceName);

            for (String province : provinceAdjacency.keySet()) {
                Boolean isAdjacent = (boolean) provinceAdjacency.get(province);
                if (isAdjacent) {
                    // System.out.println("YAY");
                    Province p = getGameProvince(provinceName);
                    p.addAdjacentProvince(getGameProvince(province));
                }
            }
        }
    }

    /**
     * Saves the game
     * @throws IOException
     */
    public void saveGame() throws IOException {

        String gameSaveFilename = gameName + ".txt";
        
        
        ObjectOutputStream save = new ObjectOutputStream(new FileOutputStream(gameSaveFilename));
        save.writeObject(this);
        save.close();
       
    }


/////////////////////////////////////////////////////////////////////////////////
/////////////////           GETTERS/SETTERS                  ////////////////////
/////////////////////////////////////////////////////////////////////////////////



    public HumanPlayer getHumanPlayer1() {
        return humanPlayer1;
    }
    
    public HumanPlayer getHumanPlayer2() {
        return humanPlayer2;
    }
    
    public HashMap<Integer, Province> getNodeNumberToProvince() {
        return nodeNumberToProvince;
    }

    public ArrayList<Province> getListOfGameProvinces() {
        return listOfGameProvinces;
    }

    public String getEngagementStyle() {
        return engagementStyle;
    }

    public void setProvinceFile(String provinceFile) {
        this.provinceFile = provinceFile;
    }

    public Province getProvinceFromMap(int key) {
        return nodeNumberToProvince.get(key);
    }


    public Province getGameProvince(String provinceName) {
        
        for (Province p : listOfGameProvinces) {
            if  (provinceName.equals(p.getName())) {
                return p;
            }
        }

        return null;
    }


    public int getTurnCounter() {
        return turnCounter;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    public void setGameEnded(boolean gameEnded) {
        this.gameEnded = gameEnded;
    }
/*
    public static void main(String[] args) throws IOException {
        Game game = new Game("test");
        game.saveGame();
        Province p = game.getNodeNumberToProvince().get(1);
        System.out.println(p);

    }
*/
    
}

