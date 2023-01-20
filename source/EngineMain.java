import javafx.application.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.media.*;
import javafx.scene.text.*;
import javafx.stage.*;

import java.io.*;

public class EngineMain extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        String filePath = "Group7-A2/source/Resources/Levels/levelMaze.txt";
        //String filePath = "savedGames/Jozef-level2-saved-2019-12-06-21-31-05Z.txt";
        Profile profile = new Profile("Jozef", 0, 1);
        Engine.runLevel(primaryStage, filePath, profile);
    }
}
