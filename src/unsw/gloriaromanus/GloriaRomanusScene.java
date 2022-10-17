package unsw.gloriaromanus;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import unsw.gloriaromanus.classes.Game;
import unsw.gloriaromanus.classes.Player;

public class GloriaRomanusScene {


    private Stage stage;
    private GloriaRomanusController controller;
    private Scene scene;
    public static MediaPlayer music;
    private static Game runningGame;

    /**
     * Creates the game map
     */
    public GloriaRomanusScene(Stage stage) throws IOException {
        this.stage = stage;
        //title = "Main Menu";

        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        this.scene = new Scene(root);

        stage.setWidth(800);
        stage.setHeight(700);
        

    }

    /**
     * Starts the game given optional parameters
     */
    public void start(String p1Faction,  String p2Faction, String gameName, String mainGoal, String subgoal1, String subgoal2) throws JsonParseException, JsonMappingException, IOException {
        
        playGameMusic();
        stage.setTitle("Gloria Romanus");
        stage.setScene(scene);
        
        Game newGame = new Game(gameName);
        controller.setGame(newGame);    
        runningGame = newGame;  
        Player p1 = newGame.getHumanPlayer1();
        Player p2 = newGame.getHumanPlayer2();

        // Assigns player goals
        boolean mainGoalProvided = false;
        boolean subgoal1Provided = false;
        boolean subgoal2Provided = false;

        ArrayList<String> goals = new ArrayList<String>();
        goals.add("CONQUEST");
        goals.add("WEALTH");
        goals.add("TREASURY");
        
        if (!mainGoal.equals("")) {
            goals.remove(mainGoal);
            p1.getPlayerGoals().set(0, mainGoal);
            p2.getPlayerGoals().set(0, mainGoal);
            mainGoalProvided = true;
        }
        if (!subgoal1.equals("")) {
            goals.remove(subgoal1);
            p1.getPlayerGoals().set(1, subgoal1);
            p2.getPlayerGoals().set(1, subgoal1);
            subgoal1Provided = true;
        } 
        if (!subgoal2.equals("")) {
            goals.remove(subgoal2);
            p1.getPlayerGoals().set(2, subgoal2);
            p2.getPlayerGoals().set(2, subgoal2);
            subgoal2Provided = true;
        } 
        
       // int goalsLeftToAssign = goals.size();
        
        Collections.shuffle(goals);

        for (String s : goals) {
            System.out.println("YES:" + s);
            if (!mainGoalProvided) {
                System.out.println("YES: MAIN");
                p1.getPlayerGoals().set(0, s.toUpperCase());
                p2.getPlayerGoals().set(0, s.toUpperCase());
                mainGoalProvided = true;
                continue;
            }

            if (!subgoal1Provided) {
                System.out.println("YES: 1");
                p1.getPlayerGoals().set(1, s.toUpperCase());
                p2.getPlayerGoals().set(1, s.toUpperCase());
                subgoal1Provided = true;
                continue;
            }

            if (!subgoal2Provided) {
                System.out.println("YES: 2");
                p1.getPlayerGoals().set(2, s.toUpperCase());
                p2.getPlayerGoals().set(2, s.toUpperCase());
                subgoal2Provided = true;
                continue;
            }
        }

        controller.setup();
        stage.show();

    }

    /**
     * Loads a given game
     * @param game The given Game
     */
    public void load(Game game) throws JsonParseException, JsonMappingException, IOException {
        playGameMusic();
        runningGame = game;
        stage.setTitle("Gloria Romanus");
        stage.setScene(scene);
        controller.setGame(game);
        controller.loadsetup();
        stage.show();
    }
    
    /** 
     * Plays music 
     */
    public void playGameMusic() {

        String musicFileName = "/home/comp2511-student/Music/DragonbonWAV.wav";
        
        
        music = new MediaPlayer(new Media(Paths.get(musicFileName).toUri().toString()));
        MainMenuScene.music.stop();
        music.setAutoPlay(true);
        music.setCycleCount(MediaPlayer.INDEFINITE);
        music.play();
  
    }
    

/////////////////////////////////////////////////////////////////////////////////
/////////////////             GETTERS/SETTERS                ////////////////////
/////////////////////////////////////////////////////////////////////////////////

    public static Game getRunningGame() {
        return runningGame;
    }

    public GloriaRomanusController getController() {
        return controller;
    }


}
