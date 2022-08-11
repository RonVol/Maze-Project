package algorithms.search;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents a solution(array of states) to a searchable maze.
 */
public class Solution implements Serializable {

    private ArrayList<AState> solution;

    public Solution()
    {
        this.solution = new ArrayList<>();
    }

    public ArrayList<AState> getSolutionPath()
    {
        return solution;
    }

    public boolean addState(AState state)
    {
        if(state==null){
            return false;
        }
        solution.add(0, state);
        return true;
    }
}
