package Model;

import algorithms.mazeGenerators.Maze;

import java.util.Observer;

public interface IModel {
    void generateMaze(int rows, int cols);
    int getCharRow();
    int getCharCol();
    void updateCharLocation(MovementDirection direction);
    Maze getMaze();
    void saveGame(String path);
    void loadGame(String path);
    void requestSolution();
    int[][] getSolution();
    int[] getGoalPosition();
    void  assignObserver(Observer o);
    void exitGame();
}
