package unsw.gloriaromanus;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import unsw.gloriaromanus.classes.MainMenu;

public class MainMenuController {

    private GloriaRomanusScene map;

    @FXML
    private TextField player1FactionTB;

    @FXML
    private TextField player2FactionTB;

    @FXML
    private TextField gameNameTB;

    @FXML
    private TextField loadGameNameTB;

    @FXML 
    private Label loadgamelabel;

    @FXML 
    private Label title;

    @FXML
    private Button startButton;
    
    @FXML
    private Button loadButton;

    @FXML
    private Button goalDescriptionButton;

    @FXML
    private Button factionDetailsButton;

    @FXML
    private Label p1faction;

    @FXML
    private Label p2faction;

    @FXML
    private Label gameName;
   
    @FXML
    private CheckBox battleResolverCB;

    @FXML
    private Label extraresolvers;

    @FXML
    private Label gameNameError;

    @FXML
    private Label maingoal;

    @FXML
    private TextField mainGoalTB;

    @FXML
    private TextField subgoal1TB;

    @FXML
    private TextField subgoal2TB;

    @FXML
    private Label subgoal1;

    @FXML
    private Label subgoal2;

    @FXML
    private Label notValidName;
    
    @FXML
    private Label player1factionError;

    @FXML
    private Label player2factionError;

    @FXML
    private Label mainGoalError;

    @FXML
    private Label subgoal1Error;

    @FXML
    private Label subgoal2Error;

    @FXML
    private VBox factionList;

    @FXML
    private VBox goals;

    @FXML 
    private AnchorPane root;

    /**
     * Sets background for mainmenu
     */
    public void setBackground() {
        root.setStyle("-fx-background-image:url('file:images/background.jpg')");
    }
 
    /**
     * Attempts to start a new campaign provided correct parameters
     */
    @FXML
    public void clickedStartCampaignButton(ActionEvent e) throws JsonParseException, JsonMappingException, IOException {
        
        if (!validGoalProvided()) {
            System.out.println("RERWEWE");
            return;
        }
        
        
        if (gameNameTB.getText().equals("")) {
            // LABEL ERROR MESSAGE
            gameNameError.setVisible(true);

        } else {
            
            String p1Faction = player1FactionTB.getText();
            String p2Faction = player2FactionTB.getText();

            if (!validFactionName(p1Faction) && !validFactionName(p2Faction)) {
                player1factionError.setVisible(true);
                player2factionError.setVisible(true);
                return;
            }

            if (!validFactionName(p1Faction)) {
                player1factionError.setVisible(true);
                return;
            }

            if (!validFactionName(p2Faction)) {
                player2factionError.setVisible(true);
                return;
            }

            if (p1Faction.equals("") && p2Faction.equals("")) {
                p1Faction = "Rome";
                p2Faction = "Gaul";
            }
            
            if (player1FactionTB.getText().equals("") && !player2FactionTB.getText().equals("")) {
                p1Faction = "Rome";
                if (p1Faction.equals(p2Faction)) {
                    p1Faction = "Gaul";
                }
            }

            if (!player1FactionTB.getText().equals("") && player2FactionTB.getText().equals("")) {
                p2Faction = "Rome";
                if (p1Faction.equals(p2Faction)) {
                    p2Faction = "Gaul";
                }
            }

            map.getController().setP1Faction(p1Faction);
            map.getController().setP2Faction(p2Faction);

            map.start(player1FactionTB.getText(), player2FactionTB.getText(), gameNameTB.getText(), mainGoalTB.getText().toUpperCase(), subgoal1TB.getText().toUpperCase(), subgoal2TB.getText().toUpperCase());
        }

        
    }

    /**
     * Attempts to load a given game if available
     */
    @FXML
    public void clickedLoadGameButton(ActionEvent e) throws JsonParseException, JsonMappingException, IOException {
        
        MainMenu m = new MainMenu(); 
        try {
            map.load(m.loadGame(loadGameNameTB.getText() + ".txt"));
        } catch (Exception ex) {
            notValidName.setVisible(true);
        }
        
    }

  
    /**
     * Changes diplay of main menu
     */
    @FXML
    public void clickedFactionDetailsButton(ActionEvent e) {
        
        factionList.setVisible(true);

        factionDetailsButton.setVisible(false);
        goalDescriptionButton.setVisible(false);
        loadgamelabel.setVisible(false);
        player1FactionTB.setVisible(false);
        player2FactionTB.setVisible(false);
        gameNameTB.setVisible(false);
        loadGameNameTB.setVisible(false);
        title.setVisible(false);
        startButton.setVisible(false);
        loadButton.setVisible(false);
        p1faction.setVisible(false);
        p2faction.setVisible(false);
        gameName.setVisible(false);
        battleResolverCB.setVisible(false);
        extraresolvers.setVisible(false);
        gameNameError.setVisible(false);
        maingoal.setVisible(false);
        mainGoalTB.setVisible(false);
        subgoal1TB.setVisible(false);
        subgoal2TB.setVisible(false);
        subgoal1.setVisible(false);
        subgoal2.setVisible(false);
        notValidName.setVisible(false);
        player1factionError.setVisible(false);
        player2factionError.setVisible(false);
        mainGoalError.setVisible(false);
        subgoal1Error.setVisible(false);
        subgoal2Error.setVisible(false);

    }

    /**
     * Changes diplay of main menu
     */
    @FXML
    public void clickedGoalDescriptionButton(ActionEvent e) {
        
        goals.setVisible(true);

        factionDetailsButton.setVisible(false);
        goalDescriptionButton.setVisible(false);
        loadgamelabel.setVisible(false);
        player1FactionTB.setVisible(false);
        player2FactionTB.setVisible(false);
        gameNameTB.setVisible(false);
        loadGameNameTB.setVisible(false);
        title.setVisible(false);
        startButton.setVisible(false);
        loadButton.setVisible(false);
        p1faction.setVisible(false);
        p2faction.setVisible(false);
        gameName.setVisible(false);
        battleResolverCB.setVisible(false);
        extraresolvers.setVisible(false);
        gameNameError.setVisible(false);
        maingoal.setVisible(false);
        mainGoalTB.setVisible(false);
        subgoal1TB.setVisible(false);
        subgoal2TB.setVisible(false);
        subgoal1.setVisible(false);
        subgoal2.setVisible(false);
        notValidName.setVisible(false);
        player1factionError.setVisible(false);
        player2factionError.setVisible(false);
        mainGoalError.setVisible(false);
        subgoal1Error.setVisible(false);
        subgoal2Error.setVisible(false);
        
    }

    /**
     * Changes diplay of main menu
     */
    @FXML
    public void clickedFactionBackButton(ActionEvent e) {

        factionList.setVisible(false);

        factionDetailsButton.setVisible(true);
        goalDescriptionButton.setVisible(true);
        loadgamelabel.setVisible(true);
        player1FactionTB.setVisible(true);
        player2FactionTB.setVisible(true);
        gameNameTB.setVisible(true);
        loadGameNameTB.setVisible(true);
        title.setVisible(true);
        startButton.setVisible(true);
        loadButton.setVisible(true);
        p1faction.setVisible(true);
        p2faction.setVisible(true);
        gameName.setVisible(true);
        battleResolverCB.setVisible(true);
        extraresolvers.setVisible(true);
        //gameNameError.setVisible(true);
        maingoal.setVisible(true);
        mainGoalTB.setVisible(true);
        subgoal1TB.setVisible(true);
        subgoal2TB.setVisible(true);
        subgoal1.setVisible(true);
        subgoal2.setVisible(true);
        //notValidName.setVisible(true);
        //player1factionError.setVisible(true);
        //player2factionError.setVisible(true);
        //mainGoalError.setVisible(true);
        //subgoal1Error.setVisible(true);
        //subgoal2Error.setVisible(true);
        
    }
    
    /**
     * Changes diplay of main menu
     */
    @FXML
    public void clickedGoalbackButton(ActionEvent e) {

        goals.setVisible(false);

        factionDetailsButton.setVisible(true);
        goalDescriptionButton.setVisible(true);
        loadgamelabel.setVisible(true);
        player1FactionTB.setVisible(true);
        player2FactionTB.setVisible(true);
        gameNameTB.setVisible(true);
        loadGameNameTB.setVisible(true);
        title.setVisible(true);
        startButton.setVisible(true);
        loadButton.setVisible(true);
        p1faction.setVisible(true);
        p2faction.setVisible(true);
        gameName.setVisible(true);
        battleResolverCB.setVisible(true);
        extraresolvers.setVisible(true);
        //gameNameError.setVisible(true);
        maingoal.setVisible(true);
        mainGoalTB.setVisible(true);
        subgoal1TB.setVisible(true);
        subgoal2TB.setVisible(true);
        subgoal1.setVisible(true);
        subgoal2.setVisible(true);
        //notValidName.setVisible(true);
        //player1factionError.setVisible(true);
        //player2factionError.setVisible(true);
        //mainGoalError.setVisible(true);
        //subgoal1Error.setVisible(true);
        //subgoal2Error.setVisible(true);
    }

    /**
     * Checks if the given faction name is valid
     * @param factionName
     * @return
     */
    public boolean validFactionName(String factionName) {

        if (factionName.equals("") ||
            factionName.equals("Rome") ||
            factionName.equals("Gaul") ||
            factionName.equals("Carthaginians") ||
            factionName.equals("Celtic Briton") ||
            factionName.equals("Spanish") ||
            factionName.equals("Numidians") ||
            factionName.equals("Egyptians") ||
            factionName.equals("Seleucid Empire") ||
            factionName.equals("Pontus") ||
            factionName.equals("Amenians") ||
            factionName.equals("Parthians") ||
            factionName.equals("Germanics") ||
            factionName.equals("Greek City States") ||
            factionName.equals("Macedonians") ||
            factionName.equals("Thracians") ||
            factionName.equals("Dacians")) {
                return true;
            } else {
                return false;
            }

    }

    /**
     * Checks if the provided goals are valid
     * @return
     */
    public boolean validGoalProvided() {
        
        if (("CONQUEST".equals(mainGoalTB.getText().toUpperCase()) ||
            "WEALTH".equals(mainGoalTB.getText().toUpperCase()) ||
            "TREASURY".equals(mainGoalTB.getText().toUpperCase()) ||
            "".equals(mainGoalTB.getText())) &&
            
            ("CONQUEST".equals(subgoal1TB.getText().toUpperCase()) ||
            "WEALTH".equals(subgoal1TB.getText().toUpperCase()) ||
            "TREASURY".equals(subgoal1TB.getText().toUpperCase())||
            "".equals(subgoal1TB.getText())) &&
            
            ("CONQUEST".equals(subgoal2TB.getText().toUpperCase()) ||
            "WEALTH".equals(subgoal2TB.getText().toUpperCase()) ||
            "TREASURY".equals(subgoal2TB.getText().toUpperCase())||
            "".equals(subgoal2TB.getText()))) {
            
            System.out.println("SFSSD");
            int goalsUnprovided = 0;
            if ("".equals(mainGoalTB.getText())) goalsUnprovided++;
            if ("".equals(subgoal1TB.getText())) goalsUnprovided++;
            if ("".equals(subgoal2TB.getText())) goalsUnprovided++;

            if (goalsUnprovided == 2 || goalsUnprovided == 3) return true;

            if (mainGoalTB.getText().toUpperCase() != subgoal1TB.getText().toUpperCase() &&
                mainGoalTB.getText().toUpperCase() != subgoal2TB.getText().toUpperCase() &&
                subgoal1TB.getText().toUpperCase() != subgoal2TB.getText().toUpperCase()) {
                    return true;
                }    
        
        }

        if (!"CONQUEST".equals(mainGoalTB.getText().toUpperCase()) &&
            !"WEALTH".equals(mainGoalTB.getText().toUpperCase()) &&
            !"TREASURY".equals(mainGoalTB.getText().toUpperCase()) &&
            !"".equals(mainGoalTB.getText())) {
                mainGoalError.setVisible(true);
        }
        
        if (!"CONQUEST".equals(subgoal1TB.getText().toUpperCase()) &&
            !"WEALTH".equals(subgoal1TB.getText().toUpperCase()) &&
            !"TREASURY".equals(subgoal1TB.getText().toUpperCase()) &&
            !"".equals(subgoal1TB.getText())) {
                subgoal1Error.setVisible(true);
        }

        if (!"CONQUEST".equals(subgoal2TB.getText().toUpperCase()) &&
            !"WEALTH".equals(subgoal2TB.getText().toUpperCase()) &&
            !"TREASURY".equals(subgoal2TB.getText().toUpperCase()) &&
            !"".equals(subgoal2TB.getText())) {
                subgoal2Error.setVisible(true);
        }


        return false;

    }

    public void setAllErrorsToInvisible() {
        gameNameError.setVisible(false);
        notValidName.setVisible(false);
        player1factionError.setVisible(false);
        player2factionError.setVisible(false);
        mainGoalError.setVisible(false);
        subgoal1Error.setVisible(false);
        subgoal2Error.setVisible(false);
    }

/////////////////////////////////////////////////////////////////////////////////
/////////////////             GETTERS/SETTERS                ////////////////////
/////////////////////////////////////////////////////////////////////////////////


    public void setMap(GloriaRomanusScene map) {
        this.map = map;
    }  
}
