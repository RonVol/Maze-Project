package algorithms.search;

import java.util.ArrayList;

public interface ISearchable {
    /**
     *
     * @return the starting state
     */
    AState getStartState();

    /**
     *
     * @return the goal state
     */
    AState getGoalState();

    /**
     * this function calculates all possible neighbouring states of a state clock-wise
     * @param state to be calculated around
     * @return array list of states
     */
    ArrayList<AState> getAllPossibleStates(AState state);

}
