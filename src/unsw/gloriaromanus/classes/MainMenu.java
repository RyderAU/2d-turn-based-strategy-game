package unsw.gloriaromanus.classes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainMenu {

    /**
     * Loads and returns a game provided the game filepath
     * 
     * @param gameSave
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Game loadGame(String gameSave) throws ClassNotFoundException, IOException {

       // try {
            Path path = Paths.get(gameSave);
            System.out.println(path.toAbsolutePath().toString());
            ObjectInputStream save = new ObjectInputStream(new FileInputStream(gameSave));
            Game game;
            game = (Game) save.readObject();
            save.close();
            return game;

        //} catch (Exception e) {
            // TODO Auto-generated catch block
       //     System.out.println("FILE NOT FOUND");
        //}
        
        //return null;
    }

    /**
     * Start a new game with defualt faction allocation
     * @param gameName
     * @return
     */
    public Game startNewConquest(String gameName) {
        return new Game(gameName);
    }

    /**
     * Start a new game with specified player factions
     * @param gameName
     * @param player1Faction
     * @param player2Faction
     * @return
     */
    public Game startNewConquest(String gameName, String player1Faction, String player2Faction, String engagementStyle) {
        return new Game(gameName, player1Faction, player2Faction, engagementStyle);      
    }
}






/** 
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        MainMenu g = new MainMenu();
        Game n = new Game("D");
        System.out.println(n.getListOfGameProvinces().size());
        Path path = Paths.get("s_gameSave.txt");
        System.out.println(path.toAbsolutePath().toString());
        //Game game = g.loadGame("s_gameSave.txt");
        //Path pathToFile = Paths.get("Test1_gameSave.txt");
        //System.out.println(pathToFile.toAbsolutePath());
        //Province p = game.getNodeNumberToProvince().get(0);
        
    }
*/

