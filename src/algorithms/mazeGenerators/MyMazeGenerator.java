

package algorithms.mazeGenerators;
import java.util.*;
import java.awt.Point;
/**
 * This class will generate a maze using randomzied prim's algorithem.
 */
public class MyMazeGenerator extends AMazeGenerator {
    @Override
    public Maze generate(int rows, int columns) {
        // Create new Maze.
        Maze maze = new Maze(rows, columns);
        // Each element in the arrayList is a List holding two Points in the maze.
        ArrayList<List<Point>> arrayList = new ArrayList<>();
        // Using Random to choose random element from the arrayList.
        Random rand = new Random();
        // Use GoalPosition to build the maze as a start point.
        Point endPoint = new Point(maze.getGoalPosition().getRowIndex(),maze.getGoalPosition().getColumnIndex());
        // Add Element to arrayList.
        List<Point> WallOption = new ArrayList<Point>();
        WallOption.add(endPoint);
        WallOption.add(endPoint);
        arrayList.add(WallOption);

        while (!arrayList.isEmpty()) {
            // Choose random element from the arrayList.
            List<Point> CurrPoints = arrayList.get(rand.nextInt(arrayList.size()));
            Point CurrPoint = CurrPoints.get(1);
            Point CurrPointNeighbor = CurrPoints.get(0);

            // Delete the element from arrayList.
            arrayList.remove(CurrPoints);

            // Check if CurrPoint is a wall.
            if (maze.getCellValue(CurrPoint.x, CurrPoint.y) == 1) {
                maze.setCellValue(CurrPointNeighbor.x, CurrPointNeighbor.y, 0);
                maze.setCellValue(CurrPoint.x, CurrPoint.y,0);

                // If left of the Point is a wall, Add the neighbor to arrayList.
                if (CurrPoint.y - 2 >= 0 && maze.getCellValue(CurrPoint.x,CurrPoint.y - 2) == 1) {
                    Point left = new Point(CurrPoint.x, CurrPoint.y - 2);
                    Point Near = new Point(CurrPoint.x, CurrPoint.y - 1);
                    List<Point> WallOp = new ArrayList<Point>();
                    WallOp.add(Near);
                    WallOp.add(left);
                    arrayList.add(WallOp);
                }

                // If right of the Point is a wall, Add the neighbor to arrayList.
                if (CurrPoint.y + 2 < maze.getColumnsNum() && maze.getCellValue(CurrPoint.x,CurrPoint.y + 2) == 1) {
                    Point right = new Point(CurrPoint.x, CurrPoint.y + 2);
                    Point Near = new Point(CurrPoint.x, CurrPoint.y + 1);
                    List<Point> WallOptio = new ArrayList<Point>();
                    WallOptio.add(Near);
                    WallOptio.add(right);
                    arrayList.add(WallOptio);
                }

                // If up(north) of the Point is a wall, Add the neighbor to arrayList.
                if (CurrPoint.x - 2 >= 0 && maze.getCellValue(CurrPoint.x - 2, CurrPoint.y) == 1) {
                    Point up = new Point(CurrPoint.x - 2, CurrPoint.y);
                    Point Near = new Point(CurrPoint.x - 1, CurrPoint.y);
                    List<Point> WallOpti = new ArrayList<Point>();
                    WallOpti.add(Near);
                    WallOpti.add(up);
                    arrayList.add(WallOpti);
                }

                // If down(south) of the Point is a wall, Add the neighbor to arrayList.
                if (CurrPoint.x + 2 < maze.getRowsNum() && maze.getCellValue(CurrPoint.x + 2,CurrPoint.y) == 1) {
                    Point down = new Point(CurrPoint.x + 2, CurrPoint.y);
                    Point Near = new Point(CurrPoint.x + 1, CurrPoint.y);
                    List<Point> WallOpt = new ArrayList<Point>();
                    WallOpt.add(Near);
                    WallOpt.add(down);
                    arrayList.add(WallOpt);

                }
            }
        }

        // Make sure there is a Path from start point of the Maze.
        maze.setCellValue(1,0,0);
        maze.setCellValue(0,1,0);
        maze.setCellValue(0,0,0);
        return maze;
    }
}
