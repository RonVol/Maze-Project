package algorithms.search;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;

import java.util.ArrayList;

/**
 * Object Adapter Design pattern
 * Adapt Maze to ISearchable
 */
public class SearchableMaze  implements ISearchable{
    private Maze maze;
    private ArrayList <AState> states;

    public SearchableMaze(Maze maze)
    {
        this.maze=maze;
    }

    @Override
    public AState getStartState() {
        return new MazeState(this.maze.getStartPosition(), 0);
    }

    @Override
    public AState getGoalState() {
        return new MazeState(this.maze.getGoalPosition(), 0);
    }

    @Override
    public ArrayList<AState> getAllPossibleStates(AState state) {
        this.states = new ArrayList<>();
        if(state == null){
            return states;
        }

        Position p = (Position)state.getData();
        int pRow = p.getRowIndex();
        int pCol = p.getColumnIndex();

        boolean up = addState(new Position(pRow - 1,pCol),10);
        boolean right = addState(new Position(pRow ,pCol + 1),10);
        boolean down = addState(new Position(pRow + 1,pCol),10);
        boolean left = addState(new Position(pRow ,pCol - 1),10);

        //check diagonal positions with higher cost,but less than 2 straight steps
        if(up || right)
        {
            addState(new Position(pRow -1,pCol + 1),15);
        }
        if(down || right)
        {
            addState(new Position(pRow +1,pCol + 1),15);
        }
        if(down || left)
        {
            addState(new Position(pRow + 1,pCol - 1),15);
        }
        if(up || left)
        {
            addState(new Position(pRow -1,pCol - 1),15);
        }

        return states;
    }

    //helper function for getAllPossibleStates function
    private boolean addState (Position p, int cost){
        int pRow = p.getRowIndex();
        int pCol = p.getColumnIndex();
        boolean cantAdd = pRow < 0 || pRow > maze.getRowsNum() -1 || pCol < 0 || pCol> maze.getColumnsNum() - 1;
        if(!cantAdd && maze.getCellValue(pRow,pCol)==0){
            this.states.add(new MazeState(p, cost));
            return true;
        }
        return false;
    }
}
