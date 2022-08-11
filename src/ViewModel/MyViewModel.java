package ViewModel;

import Model.*;
import algorithms.mazeGenerators.Maze;
import javafx.scene.input.KeyEvent;
import java.io.File;
import java.util.Observable;
import java.util.Observer;
/**
 * Using MVVM architecture, this Class represents the ViewModel Module.
 */
public class MyViewModel extends Observable implements Observer {
    private IModel model;

    public MyViewModel(IModel model)
    {
        this.model = model;
        this.model.assignObserver(this);
    }
    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof IModel)
        {
            setChanged();
            notifyObservers(arg);
        }
    }
    public int getCharRow() {return model.getCharRow();}
    public int getCharCol() {return model.getCharCol();}
    public int[] getGoalPosition() {return model.getGoalPosition();}
    public Maze getMaze() {return model.getMaze();}
    public void exitGame() {model.exitGame();}
    public int[][] getSolution() {return model.getSolution();}
    public void loadGame(File f) {model.loadGame(f.getAbsolutePath());}
    public void generateMaze(int rows,int cols) {this.model.generateMaze(rows,cols);}
    public void saveGame(File file) {this.model.saveGame(file.getAbsolutePath());}
    public void requestSolution() {model.requestSolution();}

    public void moveCharacter(KeyEvent e)
    {
        //Translate character movement to something the Model understands.
        MovementDirection direction;
        switch (e.getCode()){
            case UP, NUMPAD8 -> direction = MovementDirection.UP;
            case DOWN,NUMPAD2 -> direction = MovementDirection.DOWN;
            case LEFT,NUMPAD4 -> direction = MovementDirection.LEFT;
            case RIGHT,NUMPAD6 -> direction = MovementDirection.RIGHT;
            case NUMPAD7 -> direction = MovementDirection.UPLEFT;
            case NUMPAD9 -> direction = MovementDirection.UPRIGHT;
            case NUMPAD1 -> direction = MovementDirection.DOWNLEFT;
            case NUMPAD3 -> direction = MovementDirection.DOWNRIGHT;
            default -> {
                // no need to move the player...
                return;
            }
        }
        model.updateCharLocation(direction);
    }
}
