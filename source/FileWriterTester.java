import javafx.application.*;
import javafx.stage.*;

public class FileWriterTester extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Level testLevel = FileReader.readLevel("Group7-A2/source/levelTest2.txt", 1);
        Profile testProfile = new Profile("testUser", 2, 1);

        Cell[][] map = testLevel.getMap();
        for (Cell[] row : map) {
            for (Cell cell : row) {
                System.out.println(cell);
            }
        }

        FileWriter.saveLevel(testLevel, testProfile);

        System.exit(0);
    }
}
