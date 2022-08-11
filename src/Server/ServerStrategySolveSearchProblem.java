package Server;

import algorithms.mazeGenerators.Maze;
import algorithms.search.*;

import java.io.*;

/**
 * This Strategy gets as input a maze Instance, and returns a solution instance to the maze.
 * Each solution is saved in the temp directory, and before solving we will check if a solution already exists.
 */
public class ServerStrategySolveSearchProblem implements IServerStrategy {
    @Override
    public void applyStrategy(InputStream inFromClient, OutputStream outToClient) {
        Solution sol;
        try {
            ObjectInputStream input = new ObjectInputStream(inFromClient);
            ObjectOutputStream output = new ObjectOutputStream(outToClient);

            Maze maze = (Maze) input.readObject();
            String name = maze.toString();//instance name as file name
            String tempDirectoryPath = System.getProperty("java.io.tmpdir");

            //Check temp directory for existing solution,else solve and save it.
            File file = new File(tempDirectoryPath,name);
            if(file.exists())
            {
                FileInputStream fileIn = new FileInputStream(file);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                sol = (Solution) in.readObject();
                fileIn.close();
                in.close();

            }else{
                SearchableMaze searchableMaze = new SearchableMaze(maze);
                Configurations config = Configurations.getInstance();
                ISearchingAlgorithm searchAlgo = config.getSearchingAlgorithm();

                sol = searchAlgo.solve(searchableMaze);
                FileOutputStream fileOut = new FileOutputStream(file);
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(sol);
                fileOut.flush();
                fileOut.close();
                out.flush();
                out.close();
            }
            output.writeObject(sol);
            output.flush();

            input.close();
            output.close();

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
