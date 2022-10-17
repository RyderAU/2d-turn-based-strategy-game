package unsw.gloriaromanus;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EndScreenScene {

    private Stage stage;
    private String title;
    private EndScreenController controller;
    private Scene scene;

    /**
     * Intialises an end screen
     */
    public EndScreenScene(Stage stage) throws IOException {
        this.stage = stage;
        title = "Defeat Screen";

        FXMLLoader loader = new FXMLLoader(getClass().getResource("endscreen.fxml"));
        controller = new EndScreenController();
        loader.setController(controller);
        
        // load into a Parent node called root
        Parent root = loader.load();
        scene = new Scene(root, 300, 500);
        
    }

    /** 
     * Display the end screen
     */
    public void start() {
        
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
        getController().setBackground();
        
    }

/////////////////////////////////////////////////////////////////////////////////
/////////////////             GETTERS/SETTERS                ////////////////////
/////////////////////////////////////////////////////////////////////////////////


    public EndScreenController getController() {
        return controller;
    }
}




