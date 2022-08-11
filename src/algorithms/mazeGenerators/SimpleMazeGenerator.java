package algorithms.mazeGenerators;
import java.util.Random;

/**
 * This class will generate a maze with randomized walls and a single simple path
 */
public class SimpleMazeGenerator extends AMazeGenerator {

    @Override
    public Maze generate(int rows, int columns) {
        Maze maze = new Maze(rows, columns);

        int currR = maze.getStartPosition().getRowIndex();
        int currC = maze.getStartPosition().getColumnIndex();
        int endR = maze.getGoalPosition().getRowIndex();
        int endC = maze.getGoalPosition().getColumnIndex();
        //set starting position as 0
        maze.setCellValue(currR, currC, 0);

        Random rand = new Random();
        int random;
        //first we create a simple path( of 0's )from start to end
        while (currR < endR || currC < endC) {
            random = rand.nextInt(2);
            if (random == 1 && currR < endR)
            {
                currR++;
            } else if (currC<endC)
            {
                currC++;
            }
            maze.setCellValue(currR, currC, 0);
        }
        //now we will iterate on all cells, but we care about only walls
        //randomly choose if this wall should become a path(0)
        for(int i=0;i<maze.getRowsNum();i++){
            for(int j=0;j<maze.getColumnsNum();j++){
                if(maze.getCellValue(i,j)==1 && rand.nextInt(2) == 1){
                        maze.setCellValue(i, j, 0);
                }
            }
        }
        maze.setCellValue(endR, endC, 0);
        return maze;


    }
}
