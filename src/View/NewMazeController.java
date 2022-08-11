package View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.util.Random;

/**
 * Controller for the New Maze window
 */
public class NewMazeController
{
    @FXML
    public Button randomButton;
    @FXML
    private TextField rowsTextField;
    @FXML
    private TextField colsTextField;

    private int maxRandomMaze=50;

    public int[] getInput()
    {
        int r,c;
        try {
            r = Integer.parseInt(rowsTextField.getText().trim());
            c = Integer.parseInt(colsTextField.getText().trim());
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please input proper Rows and Columns numbers");
            alert.showAndWait();
            r=-1;
            c=-1;
        }
        return new int[]{r,c};
    }

    public void randButtonOnAction(ActionEvent actionEvent) {
        Random rand = new Random();
        int r = rand.nextInt(maxRandomMaze) + 2;
        rowsTextField.setText(String.valueOf(r));
        colsTextField.setText(String.valueOf(r));
    }

}
