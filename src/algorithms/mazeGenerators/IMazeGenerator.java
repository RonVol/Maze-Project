package algorithms.mazeGenerators;

public interface IMazeGenerator {
    /**
     * generate will create a maze
     * @param rows and columns is the size of the maze
     * @return a maze instance
     */
    Maze generate(int rows, int columns);

    /**
     * Measure the running time of the generate function
     * @param rows and columns is the size of the maze
     * @return
     */
    long measureAlgorithmTimeMillis(int rows, int columns);

}
