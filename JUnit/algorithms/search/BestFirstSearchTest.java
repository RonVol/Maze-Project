package algorithms.search;

import algorithms.mazeGenerators.EmptyMazeGenerator;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BestFirstSearchTest {

    @Test
    void testName() {
        ISearchingAlgorithm algo = new BestFirstSearch();
        assertEquals("Best First Search",algo.getName());
    }

    @Test
    void testIllegalInputRow() {
        IMazeGenerator mg = new MyMazeGenerator();
        Maze maze = mg.generate(-1, 30);
        SearchableMaze searchableMaze = new SearchableMaze(maze);
        ISearchingAlgorithm algo = new BestFirstSearch();
        Solution solution = algo.solve(searchableMaze);
        assertEquals(2,solution.getSolutionPath().size());
    }
    @Test
    void testIllegalInputCol(){
        IMazeGenerator mg = new MyMazeGenerator();
        Maze maze = mg.generate(30, -1);
        SearchableMaze searchableMaze = new SearchableMaze(maze);
        ISearchingAlgorithm algo = new BestFirstSearch();
        Solution solution = algo.solve(searchableMaze);
        assertEquals(2,solution.getSolutionPath().size());
    }
    @Test
    void testCost(){
        IMazeGenerator mg2 = new EmptyMazeGenerator();
        Maze maze2 = mg2.generate(2, 2);
        maze2.setCellValue(1,0,1);
        SearchableMaze searchableMaze2 = new SearchableMaze(maze2);
        ISearchingAlgorithm algo2 = new BestFirstSearch();
        Solution solution2 = algo2.solve(searchableMaze2);
        assertEquals(2,solution2.getSolutionPath().size());
    }
    @Test
    void testStatesDidentChange(){
        IMazeGenerator mg = new MyMazeGenerator();
        Maze maze = mg.generate(30, 50);
        SearchableMaze searchableMaze = new SearchableMaze(maze);
        ISearchingAlgorithm algo = new BestFirstSearch();
        Solution solution = algo.solve(searchableMaze);
        assertEquals("{0,0}",maze.getStartPosition().toString());
        assertEquals("{"+29+","+49+"}",maze.getGoalPosition().toString());
    }
}