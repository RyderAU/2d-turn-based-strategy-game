package unsw.gloriaromanus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import unsw.gloriaromanus.classes.Game;
import unsw.gloriaromanus.classes.Player;
import unsw.gloriaromanus.classes.Province;
import unsw.gloriaromanus.states.UnitReadyState;
import unsw.gloriaromanus.states.UnitRoutedState;
import unsw.gloriaromanus.states.UnitTrainingState;
import unsw.gloriaromanus.classes.units.Unit;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.data.GeoPackage;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyLayerResult;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol.HorizontalAlignment;
import com.esri.arcgisruntime.symbology.TextSymbol.VerticalAlignment;
import com.esri.arcgisruntime.data.Feature;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.geojson.FeatureCollection;
import org.geojson.LngLatAlt;

import org.json.JSONArray;
import org.json.JSONObject;

public class GloriaRomanusController{

  // FXML VARIABLES
  @FXML
  private MapView mapView;
  @FXML
  private TextField invading_province;
  @FXML
  private TextField opponent_province;
  @FXML
  private TextArea output_terminal;
  @FXML
  private TextField troop_to_train;
  @FXML
  private ScrollPane units_in_province;
  @FXML
  private VBox provinceActions;
  @FXML
  private Label yearLabel;
  @FXML
  private Label playerLabel;
  @FXML
  private Label goldLabel;
  @FXML
  private Label mainGoalLabel;
  @FXML
  private Label subGoal1Label;
  @FXML
  private Label subGoal2Label;
  @FXML
  private Label conqueredProvincesLabel;
  @FXML
  private Label factionWealthLabel;
  @FXML 
  private TextField unitIDTextbox;
  @FXML 
  private VBox unitDisplayVBox;
  @FXML
  private CheckBox lowTaxCheckbox;
  @FXML
  private CheckBox normalTaxCheckbox;
  @FXML
  private CheckBox highTaxCheckbox;
  @FXML


  private CheckBox veryHighTaxCheckbox;
  private MainMenuScene mainmenu;
  private EndScreenScene endScreen;
  private ArcGISMap map;
  private Map<String, String> provinceToOwningFactionMap;
  private Map<String, Integer> provinceToNumberTroopsMap;
  private Feature currentlySelectedHumanProvince;
  private Feature currentlySelectedEnemyProvince;
  private FeatureLayer featureLayer_provinces;
  private boolean fromProvinceSelected = false;
  private boolean destinationProvinceSelected = false;

  Game game;
  Player humanPlayer1; 
  Player humanPlayer2; 

  String p1Faction;
  String p2Faction;

  Province selectedProvince;
  Province destinationProvince;

  // METHODS

  @FXML
  private void initialize() throws JsonParseException, JsonMappingException, IOException {
  }

  /**
   * Sets up the game environment
   * @throws JsonParseException
   * @throws JsonMappingException
   * @throws IOException
   */
  public void setup() throws JsonParseException, JsonMappingException, IOException {
    
    humanPlayer1 = game.getHumanPlayer1();
    humanPlayer2 = game.getHumanPlayer2();
    humanPlayer1.setFaction(p1Faction);
    humanPlayer2.setFaction(p2Faction);

    yearLabel.setText("YEAR: " + game.getTurnCounter());
    // humanPlayer1.setGoldAmount(99999);
    mainGoalLabel.setText("MAIN GOAL: " + humanPlayer1.getPlayerGoals().get(0));
    subGoal1Label.setText("SUBGOAL 1: " + humanPlayer1.getPlayerGoals().get(1));
    subGoal2Label.setText("SUBGOAL 2: " + humanPlayer1.getPlayerGoals().get(2));

    goldLabel.setText("GOLD: " + humanPlayer1.getGoldAmount());
    factionWealthLabel.setText("FACTION WEALTH: " + humanPlayer1.getFactionWealth());
    conqueredProvincesLabel.setText("PROVINCES CONQUERED: " + humanPlayer1.getProvincesUnderControl().size() + "/53");

    provinceToOwningFactionMap = getProvinceToOwningFactionMap();

    provinceToNumberTroopsMap = new HashMap<String, Integer>();
    for (String provinceName : provinceToOwningFactionMap.keySet()) {
      provinceToNumberTroopsMap.put(provinceName, 0);
    }
    
    currentlySelectedHumanProvince = null;
    currentlySelectedEnemyProvince = null;

    initializeProvinceLayers();
    changeUnitDisplay();
    output_terminal.setText("");
  }

  /**
   * Set up for the game environment when loading a game instead of creating a new one
   * @throws JsonParseException
   * @throws JsonMappingException
   * @throws IOException
   */
  public void loadsetup() throws JsonParseException, JsonMappingException, IOException {
    
    humanPlayer1 = game.getHumanPlayer1();
    humanPlayer2 = game.getHumanPlayer2();
    p1Faction = humanPlayer1.getFaction();
    p2Faction = humanPlayer2.getFaction();

    yearLabel.setText("YEAR: " + game.getTurnCounter());

    mainGoalLabel.setText("MAIN GOAL: " + humanPlayer1.getPlayerGoals().get(0));
    subGoal1Label.setText("SUBGOAL 1: " + humanPlayer1.getPlayerGoals().get(1));
    subGoal2Label.setText("SUBGOAL 2: " + humanPlayer1.getPlayerGoals().get(2));
    
    if (game.getCurrentPlayer() == humanPlayer1) {
      goldLabel.setText("GOLD: " + humanPlayer1.getGoldAmount());
      factionWealthLabel.setText("FACTION WEALTH: " + humanPlayer1.getFactionWealth());
      conqueredProvincesLabel.setText("PROVINCES CONQUERED: " + humanPlayer1.getProvincesUnderControl().size() + "/53");
    } else {
      goldLabel.setText("GOLD: " + humanPlayer2.getGoldAmount());
      factionWealthLabel.setText("FACTION WEALTH: " + humanPlayer2.getFactionWealth());
      conqueredProvincesLabel.setText("PROVINCES CONQUERED: " + humanPlayer2.getProvincesUnderControl().size() + "/53");
    }

    provinceToOwningFactionMap = getProvinceToOwningFactionMap();

    provinceToNumberTroopsMap = new HashMap<String, Integer>();
    for (String provinceName : provinceToOwningFactionMap.keySet()) {
      provinceToNumberTroopsMap.put(provinceName, 0);
    }
  
    currentlySelectedHumanProvince = null;
    currentlySelectedEnemyProvince = null;

    initializeProvinceLayers();
    changeUnitDisplay();
    output_terminal.setText("");

  }

  /**
   * Run the necessary algorithm when choosing to raid an enemy province
   * @param e
   * @throws JsonParseException
   * @throws JsonMappingException
   * @throws IOException
   */
  @FXML
  public void clickedRaidButton(ActionEvent e) throws JsonParseException, JsonMappingException, IOException {
    // Check if provinces are valid to raid
    if (!selectedProvince.isAdjacent(destinationProvince)) {
      output_terminal.setText("Provinces Not Adjacent");
      return;
    } else if (game.getCurrentPlayer() == humanPlayer1) {
      if (selectedProvince.getOwner() != humanPlayer1 || destinationProvince.getOwner() != humanPlayer2) {
        output_terminal.setText("Invalid Raid Attempt");
        return;
      }
    } else if (game.getCurrentPlayer() == humanPlayer2) {
      if (selectedProvince.getOwner() != humanPlayer2 || destinationProvince.getOwner() != humanPlayer1) {
        output_terminal.setText("Invalid Raid Attempt");
        return;
      }
    }

    if (selectedProvince.numberOfAvailableUnit() == 0 || destinationProvince.numberOfAvailableUnit() == 0) {
      output_terminal.setText("Raid Unsuccessful");
    } else if (game.getCurrentPlayer().raid()) {
      output_terminal.setText("Raid Successful!");
    } else {
      output_terminal.setText("Already raided this turn");
    }
    
    changeUnitDisplay();
    resetSelections();  // reset selections in UI
  } 

  @FXML
  public void clickedStartCampaignButton(ActionEvent e) {
    
  }

  /**
   * Save the game and exit to the main menu
   * @param e
   * @throws IOException
   */
  @FXML
  public void clickedSaveAndQuitButton(ActionEvent e) throws IOException {
      game.saveGame();
      mainmenu.getController().setAllErrorsToInvisible();
      mainmenu.start();
  }

  /**
   * Run the code to invade an enemy province from a selected friendly province
   * @param e
   * @throws IOException
   */
  @FXML
  public void clickedInvadeButton(ActionEvent e) throws IOException {
    // Check if provinces are valid to invade
    if (!selectedProvince.isAdjacent(destinationProvince)) {
      output_terminal.setText("Provinces Not Adjacent");
      return;
    } else if (game.getCurrentPlayer() == humanPlayer1) {
      if (selectedProvince.getOwner() != humanPlayer1 || destinationProvince.getOwner() != humanPlayer2) {
        output_terminal.setText("Invalid Invade Attempt");
        return;
      }
    } else if (game.getCurrentPlayer() == humanPlayer2) {
      if (selectedProvince.getOwner() != humanPlayer2 || destinationProvince.getOwner() != humanPlayer1) {
        output_terminal.setText("Invalid Invade Attempt");
        return;
      }
    }

    output_terminal.setText("========= LET THE BATTLE BEGIN! =========\n");


    if (game.getCurrentPlayer().invadeAttempt()) {

      Player.engagementMessages += "\nInvasion Successful!\n";
      output_terminal.setText(Player.engagementMessages);
      //output_terminal.appendText("Invasion Successful!\n");
      // update provinces visually
      provinceToOwningFactionMap.put(destinationProvince.getName(), game.getCurrentPlayer().getFaction());
      conqueredProvincesLabel.setText("PROVINCES CONQUERED: " + game.getCurrentPlayer().getProvincesUnderControl().size() + "/53");
      checkVictory(game.getCurrentPlayer());

    } else {
      Player.engagementMessages += "\nInvasion Unsuccessful!\n";
      output_terminal.setText(Player.engagementMessages);
      //output_terminal.setText("Invasion Unsuccessful\n");
    }
    //output_terminal.appendText("=================================");
    changeUnitDisplay();
    resetSelections();  // reset selections in UI
  }

  /**
   * End the current players' turn, switching control to the other player
   * @param e
   * @throws JsonParseException
   * @throws JsonMappingException
   * @throws IOException
   */
  @FXML
  public void clickedEndTurnButton(ActionEvent e) throws JsonParseException, JsonMappingException, IOException {
    System.out.println("player 1 goal to win: " + humanPlayer1.getPlayerGoals());
    System.out.println("player 2 goal to win: " + humanPlayer2.getPlayerGoals());
    System.out.println("human player 1 num provinces controlled " + humanPlayer1.getProvincesUnderControl().size());
    System.out.println("human player 2 num provinces controlled " + humanPlayer2.getProvincesUnderControl().size());

    if (game.getCurrentPlayer() == humanPlayer1) {
      humanPlayer1.endTurn();
      playerLabel.setText("CURRENT PLAYER: Player 2");
      goldLabel.setText("GOLD: " + humanPlayer2.getGoldAmount());
      System.out.println("goldddddd" + humanPlayer2.getGoldAmount());
      factionWealthLabel.setText("FACTION WEALTH: " + humanPlayer2.getFactionWealth());
      conqueredProvincesLabel.setText("PROVINCES CONQUERED: " + humanPlayer2.getProvincesUnderControl().size() + "/53");
      output_terminal.setText(humanPlayer2.getUnitsTrainingFinishedMessage());
      checkVictory(humanPlayer2);

    } else {
      humanPlayer2.endTurn();
      yearLabel.setText("YEAR: " + game.getTurnCounter());
      playerLabel.setText("CURRENT PLAYER: Player 1");
      goldLabel.setText("GOLD: " + humanPlayer1.getGoldAmount());
      factionWealthLabel.setText("FACTION WEALTH: " + humanPlayer1.getFactionWealth());
      conqueredProvincesLabel.setText("PROVINCES CONQUERED: " + humanPlayer1.getProvincesUnderControl().size() + "/53");
      output_terminal.setText(humanPlayer1.getUnitsTrainingFinishedMessage());
      checkVictory(humanPlayer1);
    }

    // set units to ready if appropriate or update their turns to become ready
    for (String provinceName : provinceToOwningFactionMap.keySet()) {
      Province province = game.getGameProvince(provinceName);
  
      int troopsInProvince = 0;
        for (Unit unit : province.getUnitsInProvince()) {
          if (unit.getUnitState() instanceof UnitReadyState) {
              troopsInProvince += unit.getNumTroops();
          }
        }
      updateUnitsInProvince(province);
      provinceToNumberTroopsMap.put(provinceName, troopsInProvince);
    }


    //resetSelections();
    initializeProvinceLayers();
    addAllPointGraphics();
    
  }
  
  /**
   * Select a unit to move to another province
   * @param e
   */
  @FXML
  public void clickedUnitSelectButton(ActionEvent e) {
      int unitID = Integer.parseInt(unitIDTextbox.getText());
      int counter = 0;
      for (Unit u : selectedProvince.getUnitsInProvince()) {
          if (u.getUnitState() instanceof UnitReadyState) {
              if (unitID == counter) {
                  Label l = (Label) unitDisplayVBox.getChildren().get(unitID);
                  l.setStyle("-fx-text-fill: green");
                  game.getCurrentPlayer().selectUnit(u);
                  break;
              } else {
                  counter++;
              }
          } 
      }

  }

  /**
   * Unselect a unit that was previously added to the group of units to move to a destination province
   * @param e
   */
  @FXML
  public void clickedUnitUnselectButton(ActionEvent e) {
    
    int unitID = Integer.parseInt(unitIDTextbox.getText());
      
    int counter = 0;

    for (Unit u : selectedProvince.getUnitsInProvince()) {
        
        if (u.getUnitState() instanceof UnitReadyState) {
            if (unitID == counter) {

                Label l = (Label) unitDisplayVBox.getChildren().get(unitID);
                l.setStyle("-fx-text-fill: black");
                game.getCurrentPlayer().unselectUnit(u);
                break;
                
            } else {
                counter++;
            }
        } 
    }
  }

  /**
   * Confirm a group of units or single unit to move to a destination province
   * @param e
   * @throws JsonParseException
   * @throws JsonMappingException
   * @throws IOException
   */
  @FXML
  public void clickedMoveButton(ActionEvent e) throws JsonParseException, JsonMappingException, IOException {
      if (game.getCurrentPlayer().getSelectedUnitSize() == 0) {
        output_terminal.setText("No units selected");
      } else if (game.getCurrentPlayer().moveUnits()) {
          output_terminal.setText("Successfully moved units");
      } else {
          output_terminal.setText("Unsuccessfully move");
      }
      
      changeUnitDisplay();
  }

  /**
   * Select the low tax option for a province, deselecting all the other options and setting the tax rate accordingly
   * @param e
   */
  @FXML
  public void lowTaxSelected(ActionEvent e) {

      normalTaxCheckbox.setSelected(false);
      highTaxCheckbox.setSelected(false);
      veryHighTaxCheckbox.setSelected(false);

      lowTaxCheckbox.setSelected(true);
      selectedProvince.setTaxRate(0.10);

  }

  /**
   * Select the normal tax option for a province
   * @param e
   */
  @FXML
  public void normalTaxSelected(ActionEvent e) {

      lowTaxCheckbox.setSelected(false);
      highTaxCheckbox.setSelected(false);
      veryHighTaxCheckbox.setSelected(false);

      normalTaxCheckbox.setSelected(true);
      selectedProvince.setTaxRate(0.15);  
      
  }

  /**
   * Select the high tax option for a province
   * @param e
   */
  @FXML
  public void highTaxSelected(ActionEvent e) {

      normalTaxCheckbox.setSelected(false);
      lowTaxCheckbox.setSelected(false);
      veryHighTaxCheckbox.setSelected(false);

      highTaxCheckbox.setSelected(true);
      selectedProvince.setTaxRate(0.20);
    
  }

  /**
   * Select the very high tax option for a province
   * @param e
   */
  @FXML
  public void veryHighTaxSelected(ActionEvent e) {

      normalTaxCheckbox.setSelected(false);
      highTaxCheckbox.setSelected(false);
      lowTaxCheckbox.setSelected(false);

      veryHighTaxCheckbox.setSelected(true);
      selectedProvince.setTaxRate(0.25);

  }

  /**
   * Acquire the unit that the user wishes to train from the textbox, and add that unit to the selected friendly province
   * @param e
   * @throws IOException
   */
  @FXML
  public void clickedTrainButton(ActionEvent e) throws IOException {
    // get string from textbox
    String t = troop_to_train.getText();

    // create the unit

    if (game.getCurrentPlayer().recruitUnit(t)) {

      output_terminal.setText("Successfully recruited " + t);

      goldLabel.setText("GOLD: " + game.getCurrentPlayer().getGoldAmount());

      updateUnitsInProvince(selectedProvince);
      // System.out.println(selectedProvince.getUnitsInProvince());
      // get recently added unit and add to selected province list of units
      // selectedProvince.addUnit(game.getCurrentPlayer().getUnitsUnderControl().get(game.getCurrentPlayer().getUnitsUnderControl().size() - 1));

    } else {
      output_terminal.setText("Not able to recruit " + t);
    }
  }

  /**
   * Initially run to update province owner, change feature in each
   * FeatureLayer to be visible/invisible depending on owner. Can also update
   * graphics initially
   * @throws JsonParseException
   * @throws JsonMappingException
   * @throws IOException
   */
  private void initializeProvinceLayers() throws JsonParseException, JsonMappingException, IOException {

    currentlySelectedEnemyProvince = null;
    currentlySelectedHumanProvince = null;
    invading_province.setText("");
    opponent_province.setText("");
    fromProvinceSelected = false;
    destinationProvinceSelected = false;
    provinceActions.setVisible(false);

    Basemap myBasemap = Basemap.createImagery();
    // myBasemap.getReferenceLayers().remove(0);
    map = new ArcGISMap(myBasemap);
    mapView.setMap(map);

    // note - tried having different FeatureLayers for AI and human provinces to
    // allow different selection colors, but deprecated setSelectionColor method
    // does nothing
    // so forced to only have 1 selection color (unless construct graphics overlays
    // to give color highlighting)
    GeoPackage gpkg_provinces = new GeoPackage("src/unsw/gloriaromanus/provinces_right_hand_fixed.gpkg");
    gpkg_provinces.loadAsync();
    gpkg_provinces.addDoneLoadingListener(() -> {
      if (gpkg_provinces.getLoadStatus() == LoadStatus.LOADED) {
        // create province border feature
        featureLayer_provinces = createFeatureLayer(gpkg_provinces);
        map.getOperationalLayers().add(featureLayer_provinces);

      } else {
        System.out.println("load failure");
      }
    });

    addAllPointGraphics();
  }

  /**
   * Retrieves and creates the on screen graphics
   */
  private void addAllPointGraphics() throws JsonParseException, JsonMappingException, IOException {
    mapView.getGraphicsOverlays().clear();

    InputStream inputStream = new FileInputStream(new File("src/unsw/gloriaromanus/provinces_label.geojson"));
    FeatureCollection fc = new ObjectMapper().readValue(inputStream, FeatureCollection.class);

    GraphicsOverlay graphicsOverlay = new GraphicsOverlay();

    for (org.geojson.Feature f : fc.getFeatures()) {
      if (f.getGeometry() instanceof org.geojson.Point) {
        org.geojson.Point p = (org.geojson.Point) f.getGeometry();
        LngLatAlt coor = p.getCoordinates();
        Point curPoint = new Point(coor.getLongitude(), coor.getLatitude(), SpatialReferences.getWgs84());
        PictureMarkerSymbol s = null;
        String province = (String) f.getProperty("name");
        String faction = provinceToOwningFactionMap.get(province);

        TextSymbol t = new TextSymbol(10,
            faction + "\n" + province + "\n" + provinceToNumberTroopsMap.get(province), 0xFFFF0000,
            HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);

        // Assigns sprites based on the chosen factions
        if ("Rome".equals(faction)) {
          s = new PictureMarkerSymbol("images/legionary.png");
          s.setHeight(100);
          s.setWidth(30);
        } else if ("Carthaginians".equals(faction)) {
          s = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Crossbowman/Crossbowman_NB.png");
          s.setHeight(100);
          s.setWidth(80);
        } else if ("Celtic Briton".equals(faction)) {
          s = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/ArcherMan/Egyptian/Egyptian_Archer_NB.png");
          s.setHeight(100);
          s.setWidth(80);
        } else if ("Spanish".equals(faction)) {
          s = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Flagbearer/FlagBearer_NB.png");
          s.setHeight(100);
          s.setWidth(80);
        } 
        
        else if ("Numidians".equals(faction)) {
          s = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Hoplite/Hoplite_NB.png");
          s.setHeight(100);
          s.setWidth(80);
        } 
        else if ("Egyptians".equals(faction)) {
          s = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Pikeman/Pikeman_NB.png");
          s.setHeight(100);
          s.setWidth(80);
        }
         else if ("Seleucid Empire".equals(faction)) {
          s = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Slingerman/Slinger_Man_NB.png");
          s.setHeight(100);
          s.setWidth(80);
        } 
        else if ("Pontus".equals(faction)) {
          s = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Spearman/Spearman_NB.png");
          s.setHeight(100);
          s.setWidth(80);
        } else if ("Amenians".equals(faction)) {
          s = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Swordsman/Egyptian/Swordsman_Alt_NB.png");
          s.setHeight(100);
          s.setWidth(80);
        } else if ("Parthians".equals(faction)) {
          s = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Swordsman/Swordsman_NB.png");
          s.setHeight(100);
          s.setWidth(80);
        } else if ("Germanics".equals(faction)) {
          s = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Flagbearer/Egyptian/Egyptian_Flagbearer_NB.png");
          s.setHeight(100);
          s.setWidth(80);
        } else if ("Greek City States".equals(faction)) {
          s = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Flagbearer/Roman/RomanFlagbearer_NB.png");
          s.setHeight(100);
          s.setWidth(80);
        } else if ("Macedonians".equals(faction)) {
          s = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Horse/Horse_Archer/Horse_Archer_NB.png");
          s.setHeight(80);
          s.setWidth(60);
        } else if ("Thracians".equals(faction)) {
          s = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Chariot/Chariot_NB.png");
          s.setHeight(80);
          s.setWidth(60);
        } else if ("Dacians".equals(faction)) {
          s = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Camel/CamelArcher/CamelArcher_NB.png");
          s.setHeight(80);
          s.setWidth(60);
        } 

         // Remaining is Gaul
         else {
          s = new PictureMarkerSymbol(new Image((new File("images/Celtic_Druid.png")).toURI().toString()));
          s.setHeight(100);
          s.setWidth(30);
        } 


        t.setHaloColor(0xFFFFFFFF);
        t.setHaloWidth(2);
        Graphic gPic = new Graphic(curPoint, s);
        Graphic gText = new Graphic(curPoint, t);
        graphicsOverlay.getGraphics().add(gPic);
        graphicsOverlay.getGraphics().add(gText);
      } else {
        System.out.println("Non-point geo json object in file");
      }

    }

    inputStream.close();
    mapView.getGraphicsOverlays().add(graphicsOverlay);
  }

  private FeatureLayer createFeatureLayer(GeoPackage gpkg_provinces) {
    FeatureTable geoPackageTable_provinces = gpkg_provinces.getGeoPackageFeatureTables().get(0);

    // Make sure a feature table was found in the package
    if (geoPackageTable_provinces == null) {
      System.out.println("no geoPackageTable found");
      return null;
    }

    // Create a layer to show the feature table
    FeatureLayer flp = new FeatureLayer(geoPackageTable_provinces);

    // https://developers.arcgis.com/java/latest/guide/identify-features.htm
    // listen to the mouse clicked event on the map view
    mapView.setOnMouseClicked(e -> {
      // was the main button pressed?
      if (e.getButton() == MouseButton.PRIMARY) {
        // get the screen point where the user clicked or tapped
        Point2D screenPoint = new Point2D(e.getX(), e.getY());

        // specifying the layer to identify, where to identify, tolerance around point,
        // to return pop-ups only, and
        // maximum results
        // note - if select right on border, even with 0 tolerance, can select multiple
        // features - so have to check length of result when handling it
        final ListenableFuture<IdentifyLayerResult> identifyFuture = mapView.identifyLayerAsync(flp,
            screenPoint, 0, false, 25);

        // add a listener to the future
        identifyFuture.addDoneListener(() -> {
          try {


            // get the identify results from the future - returns when the operation is
            // complete
            IdentifyLayerResult identifyLayerResult = identifyFuture.get();
            // a reference to the feature layer can be used, for example, to select
            // identified features
            if (identifyLayerResult.getLayerContent() instanceof FeatureLayer) {
              FeatureLayer featureLayer = (FeatureLayer) identifyLayerResult.getLayerContent();
              // select all features that were identified
              List<Feature> features = identifyLayerResult.getElements().stream().map(f -> (Feature) f).collect(Collectors.toList());
              

              if (fromProvinceSelected == true && destinationProvinceSelected == true) {
                featureLayer.unselectFeature(currentlySelectedHumanProvince);
                featureLayer.unselectFeature(currentlySelectedEnemyProvince);
                invading_province.setText("");
                opponent_province.setText("");
                fromProvinceSelected = false;
                destinationProvinceSelected = false;
                provinceActions.setVisible(false);
                humanPlayer1.setSelectedProvince(null);
                humanPlayer2.setSelectedProvince(null);
                humanPlayer1.setDestinationProvince(null);
                humanPlayer2.setDestinationProvince(null);
                selectedProvince = null;
                destinationProvince = null;
                unitDisplayVBox.getChildren().clear();
                game.getCurrentPlayer().unselectAllUnits();
              }


              if (features.size() > 1){
                printMessageToTerminal("Have more than 1 element - you might have clicked on boundary!");
              }
              else if (features.size() == 1){
                // note maybe best to track whether selected...
                Feature f = features.get(0);
                String province = (String)f.getAttributes().get("name");
                Province gameProvince = game.getGameProvince(province);

                
                if (fromProvinceSelected == true && destinationProvinceSelected == false){
                  
                  //if (currentlySelectedEnemyProvince != null){
                  //  featureLayer.unselectFeature(currentlySelectedEnemyProvince);
                  //}
                  currentlySelectedEnemyProvince = f;
                  opponent_province.setText(province);
                  destinationProvinceSelected = true;
                  featureLayer.selectFeature(f); 
                  humanPlayer1.setDestinationProvince(gameProvince);
                  humanPlayer2.setDestinationProvince(gameProvince);
                  destinationProvince = gameProvince;
                  

                } else if (fromProvinceSelected == false && destinationProvinceSelected == false){
                  // province owned by human
                    if (game.getCurrentPlayer() == humanPlayer1){
                        if (humanPlayer2.getProvincesUnderControl().contains(gameProvince)) {
                            output_terminal.setText("Please select Friendly Province");
                            return;
                        }
                    }

                    else if (game.getCurrentPlayer() == humanPlayer2){
                      if (humanPlayer1.getProvincesUnderControl().contains(gameProvince)) {
                          output_terminal.setText("Please select Friendly Province");
                          return;
                      }
                  }

                  currentlySelectedHumanProvince = f;
                  invading_province.setText(province);
                  fromProvinceSelected = true;
                  provinceActions.setVisible(true);
                  featureLayer.selectFeature(f); 
                  humanPlayer1.setSelectedProvince(gameProvince);
                  humanPlayer2.setSelectedProvince(gameProvince);
                  selectedProvince = gameProvince;
                  output_terminal.setText("");
                  
                  updateTaxOptions(gameProvince);
                  updateUnitsInProvince(gameProvince);
                  

                }

              }
              
            }
          } catch (InterruptedException | ExecutionException ex) {
            // ... must deal with checked exceptions thrown from the async identify
            // operation
            System.out.println("InterruptedException occurred");
          }
        });
      }
    });
    return flp;
  }

  /**
   * Updates the tax checkboxes for a given province based on the rate
   * @param gameProvince The province to check the tax rate in
   */
  private void updateTaxOptions(Province gameProvince) {
    
    double taxRate = selectedProvince.getTaxRate();

    lowTaxCheckbox.setSelected(false);
    normalTaxCheckbox.setSelected(false);
    highTaxCheckbox.setSelected(false);
    veryHighTaxCheckbox.setSelected(false);

    if (taxRate == 0.10) lowTaxCheckbox.setSelected(true);
    if (taxRate == 0.15) normalTaxCheckbox.setSelected(true);
    if (taxRate == 0.20) highTaxCheckbox.setSelected(true);
    if (taxRate == 0.25) veryHighTaxCheckbox.setSelected(true);

    
  }

  /**
   * Checks if a victory exists
   * @param player The player whose victory condition is being checked
   */
  private void checkVictory(Player player) {
    player.checkIfVictoryExists();
    System.out.println("player 1 victor? " + humanPlayer1.isVictor());
    System.out.println("player 2 victor? " + humanPlayer2.isVictor());
    if (player.isVictor() && game.isGameEnded() == false) {
      System.out.println("someone's won");
      // player has won
      if (player.equals(humanPlayer1)) {
        output_terminal.setText("player 1 has won!");
        endScreen.getController().setVictor("Player 1");
        endScreen.getController().setLoser("Player 2");
        game.setGameEnded(true);
        endScreen.start();

      } else {
        // player.equals(humanPlayer2)
        output_terminal.setText("player 2 has won!");
        endScreen.getController().setVictor("Player 2");
        endScreen.getController().setLoser("Player 1");
        game.setGameEnded(true);
        endScreen.start();
      }
    }
  }

  /**
   * Updates the unit output in a selected province
   * @param gameProvince The province in which the display is to be outputted to
   */
  private void updateUnitsInProvince(Province gameProvince) {
    
    unitDisplayVBox.setStyle("-fx-text-fill: black");
    unitDisplayVBox.getChildren().clear();

    int unitID = 0;

    for (Unit u : gameProvince.getUnitsInProvince()) {
      if (u.getUnitState() instanceof UnitReadyState) {
        Label l = new Label("UNIT_ID " + unitID + ": " + u.getName() + " Ready. MovPoints = " + u.getMovPointsLeft());
        l.setStyle("-fx-text-fill: black");
        unitDisplayVBox.getChildren().add(l);
        unitID++;
      } 
    }

    
    for (Unit u : gameProvince.getUnitsInProvince()) {
      if (u.getUnitState() instanceof UnitTrainingState) {
        Label l = new Label("Training: " + u.getName() + ", Time Left: " + u.getTrainingTime() + " turn/s");
        l.setStyle("-fx-text-fill: blue");
        unitDisplayVBox.getChildren().add(l);
      } 
    }

    for (Unit u : gameProvince.getUnitsInProvince()) {
      if (u.getUnitState() instanceof UnitRoutedState) {
        Label l = new Label(u.getName() + " Routed");
        l.setStyle("-fx-text-fill: red");
        unitDisplayVBox.getChildren().add(l);
      } 
    }
  } 

  /**
   * Changes the unit display on screen to match backend
   */
  public void changeUnitDisplay() throws JsonParseException, JsonMappingException, IOException {
      
      
      for (Province province : game.getListOfGameProvinces()) {
          int numTroopsInProvince = 0;
          for (Unit u : province.getUnitsInProvince()) {
              if (u.getUnitState() instanceof UnitReadyState) {
                numTroopsInProvince += u.getNumTroops();
              }
          }
          provinceToNumberTroopsMap.put(province.getName(), numTroopsInProvince);
      }

      addAllPointGraphics();
  }


 
  /**
   * Assigns the provinve to the faction it is controlled by
   */
  private Map<String, String> getProvinceToOwningFactionMap() throws IOException {
    String content = Files.readString(Paths.get("src/unsw/gloriaromanus/initial_province_ownership.json"));
    JSONObject ownership = new JSONObject(content);
    Map<String, String> m = new HashMap<String, String>();
    for (String key : ownership.keySet()) {
      // key will be the faction name
      JSONArray ja = ownership.getJSONArray(key);
      // value is province name
      for (int i = 0; i < ja.length(); i++) {
        String value = ja.getString(i);
        if(humanPlayer1.getProvincesUnderControl().contains(game.getGameProvince(value))) {
            //m.put(value, humanPlayer1.getFaction());
            m.put(value, p1Faction);
        }  else {
           // m.put(value, humanPlayer2.getFaction());
            m.put(value, p2Faction);
        }
        
      }
    }
    return m;
  }

  /**
   * Resets province selections
   */
  private void resetSelections(){
    featureLayer_provinces.unselectFeatures(Arrays.asList(currentlySelectedEnemyProvince, currentlySelectedHumanProvince));

    currentlySelectedEnemyProvince = null;
    currentlySelectedHumanProvince = null;
    invading_province.setText("");
    opponent_province.setText("");
    fromProvinceSelected = false;
    destinationProvinceSelected = false;
    provinceActions.setVisible(false);

  }

  /**
   * Prints a given message to the terminal
   * @param message The given message to print
   */
  private void printMessageToTerminal(String message){
    output_terminal.appendText(message+"\n");
  }

  /**
   * Stops and releases all resources used in application.
   */
  void terminate() {

    if (mapView != null) {
      mapView.dispose();
    }
  }

/////////////////////////////////////////////////////////////////////////////////
/////////////////             GETTERS/SETTERS                ////////////////////
/////////////////////////////////////////////////////////////////////////////////

  public void setMainMenu(MainMenuScene mn) {
    this.mainmenu = mn;
  }

  public void setGame(Game game) {
    this.game = game;
  }

  public void setP1Faction(String p1Faction) {
    this.p1Faction = p1Faction;
  }

  public void setP2Faction(String p2Faction) {
    this.p2Faction = p2Faction;
  }

  public void setEndScreen(EndScreenScene endScreen) {
    this.endScreen = endScreen;
  }
}
