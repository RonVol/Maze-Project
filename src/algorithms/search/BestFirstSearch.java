package algorithms.search;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * BreadthFirstSearch using a priority queue(by cost)
 */
public class BestFirstSearch extends BreadthFirstSearch {
    public BestFirstSearch() {
        super();
        Comparator<AState> comp = new Comparator<AState>() {
            @Override
            public int compare(AState op1, AState op2) {
                if(op1.getCost() > op2.getCost())
                    return 1;
                if(op1.getCost() < op2.getCost())
                    return -1;
                return 0;
            }
        };
       this.queue = new PriorityQueue<AState>(comp);
    }

    @Override
    public String getName()
    {
        return "Best First Search";
    }
}
