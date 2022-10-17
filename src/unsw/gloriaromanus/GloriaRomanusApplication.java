package unsw.gloriaromanus;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;

public class GloriaRomanusApplication extends Application {

  private MainMenuController mainMenuController;
  private GloriaRomanusController mapController;
  private EndScreenController endScreenController;

  @Override
  public void start(Stage stage) throws IOException {
    

    // set up the scene
    MainMenuScene mainmenu = new MainMenuScene(stage);
    GloriaRomanusScene map = new GloriaRomanusScene(stage);
    EndScreenScene endScreen = new EndScreenScene(stage);

    mainMenuController = mainmenu.getController();
    mapController = map.getController();
    endScreenController = endScreen.getController();

    mainMenuController.setMap(map);
    mapController.setMainMenu(mainmenu);
    mapController.setEndScreen(endScreen);
    endScreenController.setMap(map);
    endScreenController.setMainMenu(mainmenu);

    mainmenu.start();
  
    

  }
  
  /**
   * Stops and releases all resources used in application.
   */
  @Override
  public void stop() {
    mapController.terminate();
  }

  /**
   * Opens and runs application.
   *
   * @param args arguments passed to this application
   */
  public static void main(String[] args) {
  
    Application.launch(args);
  }
}

