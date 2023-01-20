import javafx.application.*;
import javafx.stage.*;

public class FileReaderMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        String path = "Group7-A2/source/level2.txt";
        Level level = FileReader.readLevel(path, 1);
        Cell[][] map = level.getMap();

        for (Cell[] row : map) {
            for (Cell cell : row) {
                System.out.println(cell);
            }
        }
    }
}
