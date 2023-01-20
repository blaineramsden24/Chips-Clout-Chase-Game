import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;


/**
 * This class is responsible for creating the main menu.
 * @author Blaine Ramsden
 * @version 1.1
 *
 */

public class Main extends Application{

    private double screenWidth;
    private int levelChosen = 1;
    private  String levelPath;
    private double screenHeight;
    private final String L_STYLE = "-fx-font-weight: bold;-fx-text-fill: White;-fx-font-size: 20;";
    private Boolean musicPlay = true;
    private Stage window;
    private Scene mainMenu;
    private Boolean fullScreen = false;
    private  boolean playing = false;
    private boolean loggedin = false;
    private int count = 0;
    private BorderPane mainPane;
    private Label lTitle;
    private Label lone;
    private Label ltwo;
    private Label lfour;
    private Label lfive;
    private Label lsix;
    private Label lseven;
    private Label leight;
    private Label lnine;
    private Label lten;
    private Label pad1;
    private Profile profile;
    private String inputStream2 = "background.png";
    private Profile newProfile;
    private Button playButton, settingsButton, quitButton, tutorialButton, newGameButton, mainCharacter1Button, mainCharacter2Button;
    protected Engine engine = new Engine();
    //private LeaderBoard leaderboard = new LeaderBoard();

    public void play() throws Exception {

        /**
         * A switch statement to get the level path from the levelNumber
         */
        switch (levelChosen){
            case 0:
                levelPath = "";
                break;
            case 1:
                levelPath = "Resources/Levels/level1.txt";
                break;
            case 2:
                levelPath = "Resources/Levels/level2.txt";
                break;
            case 3:
                levelPath = "Resources/Levels/level3.txt";
                break;
            case 4:
                levelPath = "Resources/Levels/level4.txt";
                break;
            case 5:
                levelPath = "Resources/Levels/level5.txt";
                break;
            default:
                levelPath = "Group7-A2/source/Resources/Levels/level1.txt";
                break;
        }
        if (profile == null){
            Label error = new Label("Please Log in before playing!");
            error.setId("error");
            error.setAlignment(Pos.CENTER);
            mainPane.setBottom(error);

        }else {
            engine.runLevel(window, levelPath, profile);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void playMusic(boolean play, double volume)  {

        AudioClip song = new AudioClip(this.getClass().getResource("Resources\\Music\\backgroundmusic.mp3").toString());

        if (playing != true && play == true){
            song.play(volume);
            song.setCycleCount(100);
            playing = true;
        }else if (playing == true && play == true){
            song.setVolume(volume);
        }else if(play == false){
            song.stop();
            playing = false;
        }

    }

    private void settingsChange(Boolean fs, double sw, double sh, boolean music, double volume){
        playMusic(music, volume);
        screenHeight = sh;
        screenWidth = sw;
        window.setHeight(screenHeight);
        window.setWidth(screenWidth);
        fullScreen = fs;
        window.setFullScreen(fullScreen);


    }

    private void login(){

        GridPane loginGrid = new GridPane();
        StackPane center = new StackPane();

        loginGrid.getStylesheets().add("buttons.css");
        BackgroundImage myBI= new BackgroundImage(new Image(inputStream2   ,screenWidth,screenHeight,false,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        loginGrid.setBackground(new Background(myBI));

        TextField newProf = new TextField("Username");
        newProf.setId("profile");
        newProf.setMaxWidth(100);


        Button rightArrow = new Button();
        rightArrow.setId("arrow");
        rightArrow.setMinSize(50,50);
        Button leftArrow = new Button();
        leftArrow.setId("arrow");
        leftArrow.setRotate(180);
        leftArrow.setMinSize(50,50);

        ArrayList<Image> characters = new ArrayList<Image>();
        Image character = new Image("player1.png");
        Image character2 = new Image("player2.png");
        characters.add(character);
        characters.add(character2);
        ImageView characterImage = new ImageView(characters.get(count));

        rightArrow.setOnAction(e-> {
            if (count == 1){
                characterImage.setImage(characters.get(0));
                count = 0;
            }else{
            characterImage.setImage(characters.get(count + 1));
            count ++;}
        });
        leftArrow.setOnAction(e-> {
            if (count == 0){
                characterImage.setImage(characters.get(1));
                count = 1;
            }else{
                characterImage.setImage(characters.get(count - 1));
                count --;}
        });
        Button saveButton = new Button("Save");
        saveButton.setId("retroYellow");

        saveButton.setOnAction(e-> {
            window.setScene(mainMenu);
            window.setFullScreen(fullScreen);

        });



        ArrayList<Profile> profileList = ProfileIOUtils.readProfiles("profile.txt");
        int proflen = profileList.size();

        System.out.println(proflen);

        ComboBox profileSelect = new ComboBox();;
        for (int i = 0; i < proflen; i++){
            profileSelect.getItems().add(profileList.get(i).getName());

        }
        profileSelect.setOnAction(e-> {
            profile = ProfileIOUtils.findProfileInList(profileList, profileSelect.getValue().toString());
            characterImage.setImage(characters.get(profile.getVariant() - 1));
        });
        profileSelect.setId("combox");



        Button createPorifle = new Button("Create Profile");
        createPorifle.setId("retroOrange");
        createPorifle.setOnAction(e-> {
            if (newProf.getText() != ""){
                Profile newProfile = new Profile(newProf.getText(), 0, characters.indexOf(characterImage.getImage()) + 1);
                profileList.add(newProfile);
                profileSelect.getItems().add(newProfile.getName());
            }
        });

        Label loginpad2 = new Label(" ");
        loginpad2.setMinHeight(200);

        characterImage.setFitWidth(200);
        characterImage.setFitHeight(300);

        loginGrid.add(newProf, 3,1);
        loginGrid.add(leftArrow, 1,3);
        loginGrid.add(rightArrow, 3,3);
        loginGrid.add(characterImage,2,3);
        loginGrid.add(saveButton, 3,4);
        loginGrid.add(createPorifle, 3,2);
        loginGrid.add(profileSelect, 1,1,2,1);

        center.getChildren().add(loginGrid);
        loginGrid.setAlignment(Pos.CENTER);
        Scene login = new Scene(center, screenWidth, screenHeight);
        window.setScene(login);

    }

    private void logOut(){

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Window Creation
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = screenSize.getWidth();
        screenHeight = screenSize.getHeight();
        window = primaryStage;
        window.setTitle("Chips Clout Chase");

        // Title Image
        //FileInputStream inputStream = new FileInputStream("C:\\betterlogo.png");
        Image image = new Image("betterlogo.png");
        ImageView title = new ImageView(image);
        title.setScaleX(2.0);
        title.setScaleY(2.0);
        title.setPreserveRatio(true);


        Text Title = new Text(400, 400, "Welcome to Chips Clout Chase");
        Title.setId("motd");
        Title.setStyle("-fx-font: 42 arial;");


        //Image tutImage = new Image("index.png");
        //ImageView tutorial = new ImageView(tutImage);


        Label tutLabel = new Label(      "============================================================================\n" +
                "Hello and welcome to our game, this project was developed by Victor, Blaine, Jim, Nick, Morgan, Aleks, Jozef \n" +
                "We hope you enjoy playing, here are some instructions\n\n" +
                "You are chip a simple man looking for some clout(score)\n" +
                "====How To Play====\n" +
                "The only keys you need to remember for this game is are W,A,S,D. Use these keys to move around the level\n" +
                "Your objective? Reach the goal in one piece!\n\n" +
                "====Information====" +
                "\n along your quest for the drip many obstacles lie in your way, Bouncers, drunks the Feds even Lava!\n" +
                "Theses obstacles can be avoided with your wit and various items that can help you across the level\n\n " +
                "-Boots-\n" +
                "'Flippers' and 'Fireboots' will protect you from Lava and Water respectively, letting you walk over the hazard without a scratch!\n\n " +
                " -Keys-\n  " +
                "Sometimes rooms or items will be separated by coloured doors for these you will need the corresponding key to gain access.\n\n" +
                "-Tokens-\n" +
                "These will be your main form of Clout throughout the game not only will these get your score up they also raise your status\n" +
                "you'll find that more doors are open for you due to the amount of tokens you have\n\n" +
                "-Teleporters- \n " +
                "Through the magic of modern science you are able to break yourself down and shift to an entirely new location in a matter of seconds\n" +
                "use these to avoid enemies and find interesting ways to traverse the level to your advantage \n\n" +
                "tip: enemies will only move when you do, use this information work out their movement and evade capture.\n" +
                "=========================================================================="

        );

        tutLabel.setStyle("-fx-background-color:WHITE");



        Scene scene = new Scene(tutLabel, 200, 100);



        Button backTut = new Button("Back");
        backTut.setOnAction(e-> {
            window.setScene(mainMenu);
            window.setFullScreen(fullScreen);
        });
        backTut.setId("retroYellow");

        Button tutLevel = new Button("Play tutorial");
        tutLevel.setOnAction(e-> {
            levelChosen = 0;
            try {
                play();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        tutLevel.setId("retroOrange");
        GridPane tutorialGrid  = new GridPane();
        Scene tutorialScene = new Scene(tutorialGrid, screenWidth, screenHeight);

        //tutorialGrid.add(tutorial,1,2);
        tutorialGrid.add(backTut, 1,1);
        tutorialGrid.add(tutLabel, 4, 2);
        tutorialGrid.add(Title, 4, 1);
        tutorialGrid.add(tutLevel,6,6);


        // Settings


        GridPane settingsGrid = new GridPane();
        settingsGrid.getStylesheets().add("buttons.css");


        BackgroundImage myBI = new BackgroundImage(new Image(inputStream2, screenWidth, screenHeight, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        settingsGrid.setBackground(new Background(myBI));
        Label settingsLabeltitle = new Label("Settings");
        settingsLabeltitle.setId("motd");

        Label resolution = new Label("Resolution: ");
        resolution.setId("motd");
        Label Xtext = new Label("x");
        Xtext.setId("motd");
        TextField custWidth = new TextField(String.valueOf(screenWidth));
        TextField custHeight = new TextField(String.valueOf(screenHeight));

        Label fullscreen = new Label("Fullscreen");
        fullscreen.setId("motd");
        CheckBox fsCbox = new CheckBox();

        Label textMusic = new Label("Music: ");
        CheckBox musicBox = new CheckBox();
        textMusic.setId("motd");
        musicBox.setSelected(true);
        Slider musicVol = new Slider();
        musicVol.setMax(10);
        musicVol.setMin(0);
        musicVol.setBlockIncrement(1);
        musicVol.setValue(5);
        Label textVol = new Label("Volume: ");
        textVol.setId("motd");

        Button save = new Button("Save");
        save.setOnAction(e -> settingsChange(fsCbox.isSelected(), Double.parseDouble(custWidth.getText()),
                Double.parseDouble(custHeight.getText()), musicBox.isSelected(), musicVol.getValue()));
        save.setId("retroGreen");
        Button back = new Button("Back");
        back.setOnAction(e -> {
            window.setScene(mainMenu);
            window.setFullScreen(fullScreen);

        });
        back.setId("retroOrange");


        settingsGrid.add(settingsLabeltitle, 1, 1);
        settingsGrid.add(resolution, 1, 2);
        settingsGrid.add(custWidth, 2, 2);
        settingsGrid.add(Xtext, 3, 2);
        settingsGrid.add(custHeight, 4, 2);
        settingsGrid.add(fullscreen, 1, 3);
        settingsGrid.add(fsCbox, 2, 3);
        settingsGrid.add(save, 1, 5);
        settingsGrid.add(back, 5, 6);
        settingsGrid.add(textMusic, 1, 4);
        settingsGrid.add(musicBox, 2, 4);
        settingsGrid.add(textVol, 3, 4);
        settingsGrid.add(musicVol, 4, 4);

        Scene settingsScene = new Scene(settingsGrid, screenWidth, screenHeight);
        //Buttons

        playButton = new Button("Play");

        playButton.setOnMouseClicked(e -> {
            try {
                play();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        playButton.setId("retroRed");
        playButton.setMinWidth(200);

        settingsButton = new Button("Settings");

        settingsButton.setAlignment(Pos.CENTER);
        settingsButton.setOnAction(e -> {
                    window.setScene(settingsScene);
                    window.setFullScreen(fullScreen);
                }
        );
        settingsButton.setId("retroOrange");
        settingsButton.setMinWidth(100);
        settingsButton.setPadding(new Insets(0, 0, 0, 0));

        Button profileButton = new Button("User: " + profile);
        profileButton.setOnAction(e -> logOut());

        if (!loggedin) {
            profileButton.setText("Log In");
            profileButton.setOnAction(e -> login());
            profileButton.setId("retroBlueLogin");

            quitButton = new Button("Exit Game");

            quitButton.setId("retroBlue");
            quitButton.setMinWidth(100);
            quitButton.setAlignment(Pos.CENTER);
            quitButton.setOnAction(e -> System.exit(0));

            tutorialButton = new Button("Tutorial");
            tutorialButton.setOnAction(e -> {
                window.setScene(tutorialScene);
                window.setFullScreen(fullScreen);
            });
            tutorialButton.setMinWidth(100);
            tutorialButton.setId("retroYellow");
            tutorialButton.setAlignment(Pos.CENTER);

            newGameButton = new Button("New Game");
            newGameButton.setMinWidth(100);
            newGameButton.setAlignment(Pos.CENTER);
            newGameButton.setId("retroGreen");

            VBox Motdarea = new VBox();
            Motdarea.setPadding(new Insets(50,0,0,0));
            Motdarea.setAlignment(Pos.CENTER_LEFT);


            Motd.updateMessage();

            Button messenger = new Button(Motd.getMessage());
            messenger.maxWidth(200);
            messenger.setId("motd");
            messenger.setOnAction(e -> {
                try {
                    Motd.updateMessage();
                    messenger.setText(Motd.getMessage());
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            });
            Motdarea.getChildren().add(messenger);


            Label pad6 = new Label("");
            pad6.setPrefHeight(50);

            Label pad8 = new Label("");
            pad8.setPrefWidth(125);

            mainPane = new BorderPane();
            mainPane.getStylesheets().add("buttons.css");

            VBox box1 = new VBox();
            VBox box2 = new VBox();
            HBox box3 = new HBox();
            VBox box4 = new VBox();
            VBox box5 = new VBox();


            box1.getChildren().addAll(newGameButton, settingsButton);
            box1.setAlignment(Pos.CENTER);
            box2.getChildren().addAll(tutorialButton, quitButton);
            box2.setAlignment(Pos.CENTER);
            box3.getChildren().addAll(box1, box2);
            box4.setAlignment(Pos.CENTER);
            box4. getChildren().addAll(playButton, box3);
            box4.setMaxSize(200,100);
            box5.setAlignment(Pos.CENTER);
            box5.setPadding(new Insets(0, 50, 0, 0));
            box5.getChildren().addAll(title, box4);

            mainPane.setTop(Motdarea);
            mainPane.setCenter(box5);
            profileButton.setMinSize(100,100);
            mainPane.setLeft(profileButton);



            BackgroundImage myBI2 = new BackgroundImage(new Image(inputStream2, screenWidth, screenHeight, false,
                    true),
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT);
            mainPane.setBackground(new Background(myBI2));


            //Scene MainMenu
            mainMenu = new Scene(mainPane, screenWidth, screenHeight);
            window.setScene(mainMenu);
            window.setFullScreen(fullScreen);
            playMusic(musicPlay, 5);
            playing = true;

            window.show();
        }

    }

}