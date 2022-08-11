package algorithms.search;

public abstract class ASearchingAlgorithm implements ISearchingAlgorithm {
    protected Solution solution;
    protected int nodesEvaluated;

    @Override
    public abstract Solution solve(ISearchable searchable);
    @Override
    public abstract String getName();
    @Override
    public int getNumberOfNodesEvaluated()
    {
        return nodesEvaluated;
    }

}
