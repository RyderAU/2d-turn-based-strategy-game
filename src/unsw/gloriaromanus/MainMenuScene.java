package unsw.gloriaromanus;

import java.io.IOException;
import java.nio.file.Paths;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class MainMenuScene {

    private Stage stage;
    private String title;
    private MainMenuController controller;
    private Scene scene;
    public static MediaPlayer music;

    /**
     * Creates a main menu
     */
    public MainMenuScene(Stage stage) throws IOException {
        this.stage = stage;
        title = "Main Menu";

        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainmenu.fxml"));
        controller = new MainMenuController();
        loader.setController(controller);
        
        // load into a Parent node called root
        Parent root = loader.load();
        scene = new Scene(root, 500, 300);
    }

    /**
     * Displays the mainmenu
     */
    public void start() {
        
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
        getController().setBackground();
        playGameMusic();
        
    }


    /**
     * Plays music fairy fountain
     */
    public void playGameMusic() {
  
        String musicFileName = "/home/comp2511-student/Music/fairyFountain.wav";
  
        music = new MediaPlayer(new Media(Paths.get(musicFileName).toUri().toString()));
        try {
            GloriaRomanusScene.music.stop();
        } catch (Exception ex) {
        }

        music.setAutoPlay(true);
        music.setCycleCount(MediaPlayer.INDEFINITE);
        music.play();
  
    }

/////////////////////////////////////////////////////////////////////////////////
/////////////////             GETTERS/SETTERS                ////////////////////
/////////////////////////////////////////////////////////////////////////////////


    public MainMenuController getController() {
        return controller;
    }
}




