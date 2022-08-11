package algorithms.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Stack;

public class DepthFirstSearch extends  ASearchingAlgorithm{
    private Stack<AState> stack;

    public DepthFirstSearch(){this.stack = new Stack<>();}

    /**
     * Standart DFS algorithem using a stack
     * @param startState - the state we start search from
     * @param searchable - the searchable maze instance
     * @return a hashmap of calculated states given by DFS
     */
    private HashMap<String, AState> DFS(AState startState, ISearchable searchable){
        HashMap<String, AState> visited = new HashMap<>();
        this.stack.push(startState);

        while(!stack.isEmpty())
        {
            AState s = stack.pop();
            //if found goal
            if(Objects.equals(s.toString(), searchable.getGoalState().toString()))
            {
                this.nodesEvaluated++;
                visited.put(s.toString(),s);
                return visited;
            }
            if(!visited.containsKey(s.toString()))
            {
                visited.put(s.toString(),s);
                ArrayList<AState> neighbors = searchable.getAllPossibleStates(s);

                for (AState neighbor : neighbors) {
                    if (!visited.containsKey(neighbor.toString())) {
                        neighbor.predecessor  = s;
                        this.stack.push(neighbor);
                    }
                }
            }
            this.nodesEvaluated++;
        }
        return visited;
    }
    @Override
    public Solution solve(ISearchable searchable) {
        //Use DFS to get states
        HashMap<String, AState> states = DFS(searchable.getStartState(),searchable);
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

    @Override
    public String getName() {
        return "Depth First Search";
    }
}
