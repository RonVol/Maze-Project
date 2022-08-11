package algorithms.search;

import java.util.*;

public class BreadthFirstSearch extends ASearchingAlgorithm{

    protected Queue<AState> queue;

    public BreadthFirstSearch()
    {
        this.queue = new LinkedList<>();
    }

    @Override
    public String getName()
    {
        return "Breadth First Search";
    }

    @Override
    public Solution solve(ISearchable searchable)
    {
        //Use BreathFS to get states
        HashMap<String, AState> states = BFS(searchable.getStartState(),searchable);

        Solution sol = new Solution();
        //re-trace the solution using predecessor's from goal to start
        AState currState = states.get(searchable.getGoalState().toString());
        while(currState.getPredecessor() != null){
            sol.addState(currState);
            currState = currState.getPredecessor() ;
        }
        //need to add the starting state after the loop
        sol.addState(currState);

        this.solution = sol;
        return sol;
    }

    /**
     * Standard BFS algorithem, we will use a HashMap where the key is Position(as it is unique to a maze)
     * @param startState - the state to begin the search from
     * @param searchable - the searchable maze instance
     * @return a hashmap of calculated states given by BreathFS
     */
    private HashMap<String, AState> BFS(AState startState, ISearchable searchable){
        HashMap<String, AState> visited = new HashMap<>();

        visited.put(startState.toString(),startState);
        queue.add(startState);

        while(this.queue.size() != 0){
            AState s = this.queue.remove();
            //if found goal
            if(Objects.equals(s.toString(), searchable.getGoalState().toString()))
            {
                this.nodesEvaluated++;
                visited.put(s.toString(),s);
                return visited;
            }
            ArrayList<AState> neighbors = searchable.getAllPossibleStates(s);

            for (AState neighbor : neighbors) {
                if (!visited.containsKey(neighbor.toString())) {
                    neighbor.setPredecessor(s);
                    visited.put(neighbor.toString(), neighbor);
                    this.queue.add(neighbor);
                }
            }
            this.nodesEvaluated++;

        }
        return visited;
    }


}
