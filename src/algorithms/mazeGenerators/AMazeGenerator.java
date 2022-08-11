package algorithms.mazeGenerators;

public abstract class AMazeGenerator implements IMazeGenerator {
    @Override
    public abstract Maze generate(int rows, int columns);

    @Override
    public long measureAlgorithmTimeMillis(int rows, int columns){
        //use generate
        long before = System.currentTimeMillis();
        generate(rows,columns);
        long after = System.currentTimeMillis();
        return after-before;
    }
}
