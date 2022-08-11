package algorithms.search;

public interface ISearchingAlgorithm {
    /**
     * This function takes a searchable(adapted maze) instance and returns a solution to it.
     * @param searchable
     * @return
     */
    Solution solve(ISearchable searchable);

    /**
     *
     * @return The name of the algorithem used to get the solution
     */
    String getName();

    /**
     *
     * @return Number of nodes that have been evaluated
     */
    int getNumberOfNodesEvaluated();
}
