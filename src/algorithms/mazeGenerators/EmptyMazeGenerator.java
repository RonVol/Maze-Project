package algorithms.mazeGenerators;

/**
 * This class will generate an empty maze(no walls)
 */
public class EmptyMazeGenerator extends AMazeGenerator {
    @Override
    public Maze generate(int rows, int columns) {
        Maze maze = new Maze(rows,columns);
        //make all cells empty
        int mazeRows = maze.getRowsNum();
        int mazeCols = maze.getColumnsNum();
        for(int i=0;i<mazeRows;i++){
            for(int j=0;j<mazeCols;j++){
                maze.setCellValue(i,j,0);
            }
        }
        return maze;
    }
}
