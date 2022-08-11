package algorithms.search;

import java.io.Serializable;

/**
 * This class represents a state of a maze(position and cost)
 */
public class MazeState extends AState implements Serializable {
    public MazeState(Object o, int cost) {
        super(o, cost);
    }
}
