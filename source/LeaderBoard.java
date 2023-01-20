import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class LeaderBoard {
    public ArrayList<String> leaderboard = new ArrayList<String>();

    public LeaderBoard(File leaderboardfile) throws FileNotFoundException {
        Scanner in = new Scanner(leaderboardfile);


            leaderboard.add(in.nextLine());



    }






}
