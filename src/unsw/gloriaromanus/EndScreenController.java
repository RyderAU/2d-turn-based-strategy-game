package unsw.gloriaromanus;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class EndScreenController {

    private GloriaRomanusScene map;
    private MainMenuScene mainmenu;

    @FXML
    private Button exitGameButton;

    @FXML
    private Button returnButton;

    @FXML 
    private Label victoryLabel;

    @FXML 
    private Label defeatLabel;

    @FXML
    private AnchorPane root;

    /**
     * Sets the background of the endscreen
     */
    public void setBackground() {
        //ImageView iv = new ImageView();
        //iv.setImage(new Image("file:images/background.jpg"));
        root.setStyle("-fx-background-image:url('file:images/background.jpg')");
    }
 
    /**
     * Returns to main menu
     */
    @FXML
    public void clickedExitButton(ActionEvent e) throws JsonParseException, JsonMappingException, IOException {
        mainmenu.start();     
    }

    /**
     * Returns to game
     */
    @FXML
    public void clickedReturnButton(ActionEvent e) throws JsonParseException, JsonMappingException, IOException {
        map.load(GloriaRomanusScene.getRunningGame());
    }

/////////////////////////////////////////////////////////////////////////////////
/////////////////             GETTERS/SETTERS                ////////////////////
/////////////////////////////////////////////////////////////////////////////////

    public void setMap(GloriaRomanusScene map) {
        this.map = map;
    }  

    public void setMainMenu(MainMenuScene mainmenu) {
        this.mainmenu = mainmenu;
    }

    public void setVictor(String victor) {
        victoryLabel.setText("VICTORY: " + victor);
    }

    public void setLoser(String loser) {
        defeatLabel.setText("DEFEAT: " + loser);
    }
}
