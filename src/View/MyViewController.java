package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import Server.Configurations;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Using MVVM architecture, this Class represents the View Module.
 */
public class MyViewController implements IView, Initializable, Observer {
    @FXML
    public BorderPane MainPane;
    @FXML
    public Label mainText;
    @FXML
    private MazeDisplayer mazeDisplayer;

    private MyViewModel viewModel;
    private Stage stage;
    private int[] goalPos;

    //music related
    private Media media;
    private MediaPlayer mediaPlayer;
    private boolean isMusicOn = false;

    //input related
    private double sensitivity = 0.002;//scroll sensitivity
    private double maximumZoom = 2; // maximum zoom scaling
    private String menuMusicPath = "resources/Audio/MenuMusic.mp3";
    private String gameMusicPath = "resources/Audio/GameMusic.mp3";
    private String winMusicPath = "resources/Audio/WinMusic.mp3";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        playMusic(menuMusicPath);
        setMainText("Welcome to Space Maze Game !");
    }


    @Override
    public void update(Observable o, Object arg) {
        String change = (String) arg;
        switch (change){
            case "maze generated" -> mazeGenerated();
            case "player moved" -> playerMoved();
            case "got solution" -> mazeSolved();
            default -> System.out.println("Not implemented change: " + change);
        }
    }

    private void mazeGenerated()
    {
        mainText.setVisible(false);
        playMusic(gameMusicPath);
        this.goalPos = viewModel.getGoalPosition();
        mazeDisplayer.drawMaze(viewModel.getMaze());
        mazeDisplayer.requestFocus();
    }

    private void playerMoved() {
        if(viewModel.getMaze()!= null && viewModel.getCharRow()==goalPos[0] && viewModel.getCharCol() == goalPos[1])
        {
            playMusic(winMusicPath);
            raiseAlert(Alert.AlertType.CONFIRMATION,"Game Result","WOW !","YOU WIN !");
        }
        setPlayerPosition(viewModel.getCharRow(), viewModel.getCharCol());
    }

    private void mazeSolved() {
        mazeDisplayer.setSolution(viewModel.getSolution());
    }

    private void playMusic(String path)
    {
        stopMusic();
        media = new Media(new File(path).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(0.5);
        mediaPlayer.play();
        isMusicOn = true;
    }

    private void stopMusic()
    {
        if(isMusicOn)
        {
            isMusicOn=false;
            mediaPlayer.stop();
        }
    }

    /**
     * Setters
     */
    private void setMainText(String s)
    {
        mainText.setText(s);
    }

    public void setPlayerPosition(int row, int col){

        mazeDisplayer.setPlayerPosition(row, col);
    }
    @Override
    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void setStage(Stage s)
    {
        this.stage = s;
        stage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
    }
    void setResize(Scene scene) {
        scene.widthProperty().addListener((observableValue, oldSceneWidth, newSceneWidth) -> {
            mazeDisplayer.setWidth(newSceneWidth.doubleValue());
            mazeDisplayer.draw();
        });
        scene.heightProperty().addListener((observableValue, oldSceneHeight, newSceneHeight) -> {
            mazeDisplayer.setHeight(newSceneHeight.doubleValue());
            mazeDisplayer.draw();
        });

        //stage.minHeightProperty().bind(scene.widthProperty().divide(2));
    }

    /**
     * On Action Functions
     */
    public void keyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode().compareTo(KeyCode.SPACE)==0){
            viewModel.requestSolution();
        }
        viewModel.moveCharacter(keyEvent);
        keyEvent.consume();
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

    public void saveOnAction() {
        if(viewModel.getMaze()==null)
        {
            raiseAlert(Alert.AlertType.ERROR,"Save Game","Save Game Error:","Cant save a maze that doesn't exist !");
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Game");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("text files", "*.txt"));
        File fileToSave = fileChooser.showSaveDialog(this.stage);
        if (fileToSave != null)
            viewModel.saveGame(fileToSave);
    }
    public void loadOnAction()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Game");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("text files","*.txt"));
        File file = fileChooser.showOpenDialog(this.stage);
        if(file != null) {
            viewModel.loadGame(file);
        }
    }

    public void newOnAction(ActionEvent actionEvent) {
        //create a dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        //load the fxml
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("NewMaze.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (Exception e)
        {}
        //add buttons
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            NewMazeController controller = fxmlLoader.getController();
            int[] newMazeSize = controller.getInput();
            try {
                //generate a new maze, reset scaling of the zoom
                viewModel.generateMaze(newMazeSize[0], newMazeSize[1]);
                mazeDisplayer.setScaleX(0.8);
                mazeDisplayer.setScaleY(0.8);
            } catch (Exception e) {
            }
        }
    }

    public void mouseScrolled(ScrollEvent scrollEvent)
    {
        double delta = scrollEvent.getDeltaY() * sensitivity;

        //Control the limits of the zoom
        if( mazeDisplayer.getScaleX()>maximumZoom) {
            mazeDisplayer.setScaleX(maximumZoom);
            mazeDisplayer.setScaleY(maximumZoom);
        }
        else if(mazeDisplayer.getScaleX()<0) {
            mazeDisplayer.setScaleY(0.5);
            mazeDisplayer.setScaleX(0.5);
        }

        if (scrollEvent.isControlDown() &&  mazeDisplayer.getScaleX()<=maximumZoom &&  mazeDisplayer.getScaleX()> 0) {
            mazeDisplayer.setScaleX(mazeDisplayer.getScaleX()+delta);
            mazeDisplayer.setScaleY(mazeDisplayer.getScaleY()+delta);
        }
    }

    public void instructionsOnAction(ActionEvent actionEvent) {
        raiseAlert(Alert.AlertType.INFORMATION,"Instructions","Instructions","Click File->New to start a new game." +
                "\nUse arrows or numpad to move the Spaceship around the asteroids." +
                "\nPress space to get a HINT for the solution");
    }

    public void gameRulesOnAction(ActionEvent actionEvent) {
        raiseAlert(Alert.AlertType.INFORMATION,"Game Rules","Game Rules","Solve the maze by finding the hidden exit.");
    }

    //exit game by menu button
    public void exitOnAction(ActionEvent actionEvent) {
         closeGame();
    }
    //exit game by clicking the X
    private void closeWindowEvent(WindowEvent event) {
        closeGame();
    }

    private void closeGame()
    {
        //create a dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        //add buttons
        dialog.setContentText("Are you sure you wanna exit?");
        dialog.setHeaderText("Exit Game");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            viewModel.exitGame();
        }
    }

    public void propertiesOnAction(ActionEvent actionEvent)
    {
        raiseAlert(Alert.AlertType.INFORMATION,"Properties","Properties",
                "Number Of Threads:"+ Configurations.getInstance().getThreadPoolSize()+"\n" +
                "Maze Generating Algorithem:"+Configurations.getInstance().getMazeGenerator().getClass().getSimpleName()+"\n" +
                "Maze Solving Algorithem:"+Configurations.getInstance().getSearchingAlgorithm().getName());
    }

    public void raiseAlert(Alert.AlertType type,String title, String header, String text)
    {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(text);
        alert.showAndWait();
        if(text.equals("YOU WIN !"))
        {
            newOnAction(null);
        }
    }
}
