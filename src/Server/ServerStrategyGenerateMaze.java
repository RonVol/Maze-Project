package Server;

import IO.MyCompressorOutputStream;
import algorithms.mazeGenerators.*;

import java.io.*;

/**
 * This strategy gets as input stream an int array of size 2 representing dimensions of a maze
 * The strategy will generate a maze,compress the data, and send the compressed data
 */
public class ServerStrategyGenerateMaze implements IServerStrategy {
    @Override
    public void applyStrategy(InputStream inFromClient, OutputStream outToClient) {
        try {
            ObjectInputStream input = new ObjectInputStream(inFromClient);
            ObjectOutputStream output = new ObjectOutputStream(outToClient);

            ByteArrayOutputStream byteArr = new ByteArrayOutputStream();
            MyCompressorOutputStream compressor = new MyCompressorOutputStream(byteArr);

            Configurations config = Configurations.getInstance();
            IMazeGenerator mazeGenerator = config.getMazeGenerator();

            int [] sizeOfMaze = (int[]) input.readObject();
            int rows = sizeOfMaze[0],columns =sizeOfMaze[1];
            Maze maze = mazeGenerator.generate(rows, columns); //creates the maze
            byte[] mazeByteArr = maze.toByteArray();

            compressor.write(mazeByteArr);
            output.writeObject(byteArr.toByteArray());

            output.flush();
            compressor.flush();
            byteArr.flush();

            input.close();
            output.close();
            compressor.close();
            byteArr.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
