package Model;

import Client.*;
import IO.*;
import Server.*;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.application.Platform;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
/**
 * Using MVVM architecture, this Class represents the Model Module.
 */
public class MyModel extends Observable implements IModel {

    private Maze maze;
    private int charRow;
    private int charCol;
    private int[][] solution;
    private Server mazeGeneratingServer;
    private Server solveSearchProblemServer;

    public MyModel()
    {
        this.maze = null;
        this.solution=null;
        this.charRow=0;
        this.charCol=0;
        mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        mazeGeneratingServer.start();
        solveSearchProblemServer.start();
    }

    @Override
    public void updateCharLocation(MovementDirection direction) {
        if(this.maze != null) {
            switch (direction) {
                case UP:
                    if (inBoundsAndNotWall(charRow-1,charCol))
                        charRow--;
                    break;
                case DOWN:
                    if (inBoundsAndNotWall(charRow+1,charCol))
                        charRow++;
                    break;
                case LEFT:
                    if (inBoundsAndNotWall(charRow,charCol-1))
                        charCol--;
                    break;
                case RIGHT:
                    if (inBoundsAndNotWall(charRow,charCol+1))
                        charCol++;
                    break;
                case UPRIGHT:
                    if(  (inBoundsAndNotWall(charRow-1,charCol+1)) &&((inBoundsAndNotWall(charRow-1,charCol)) || (inBoundsAndNotWall(charRow,charCol+1))) )
                    {charRow--;charCol++;}
                    break;
                case UPLEFT:
                    if((inBoundsAndNotWall(charRow-1,charCol-1)) && (  inBoundsAndNotWall(charRow-1,charCol) || inBoundsAndNotWall(charRow,charCol-1)           ))
                    {charRow--;charCol--;}
                    break;
                case DOWNLEFT:
                    if((inBoundsAndNotWall(charRow+1,charCol-1)) && ( inBoundsAndNotWall(charRow+1,charCol) || inBoundsAndNotWall(charRow,charCol-1)    )   )
                    {charRow++;charCol--;}
                    break;
                case DOWNRIGHT:
                    if( (inBoundsAndNotWall(charRow+1,charCol+1)) && ( inBoundsAndNotWall(charRow+1,charCol) || inBoundsAndNotWall(charRow,charCol+1)   )     )
                    {charRow++;charCol++;}
                    break;
                default:
                    //do nothing
            }
        }
        setChanged();
        notifyObservers("player moved");
    }

    private boolean inBoundsAndNotWall(int row,int col)
    {
        return (row>=0) && (col>=0) && (row<this.maze.getRowsNum()) &&(col<this.maze.getColumnsNum()) && (this.maze.getCellValue(row,col) == 0);
    }

    @Override
    public void generateMaze(int rows, int cols) {
        final Maze[] m = new Maze[1];
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy()
            {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer)
                {
                    try
                    {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{rows, cols};
                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed withMyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[rows*cols + 12]; //allocating byte[] for the decompressed maze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        m[0] = new Maze(decompressedMaze);
                    } catch (Exception e) {}
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {}
        this.maze = m[0];
        this.charCol = this.maze.getStartPosition().getColumnIndex();
        this.charRow = this.maze.getStartPosition().getRowIndex();
        this.solution = null;

        setChanged();
        notifyObservers("maze generated");
    }

    public void saveGame(String path) {
        MyCompressorOutputStream myCompressorOutputStream;
        try {
            myCompressorOutputStream = new MyCompressorOutputStream(new FileOutputStream(path));
            myCompressorOutputStream.write(maze.toByteArray());
            myCompressorOutputStream.close();
            notifyObservers("SaveGame");
        } catch (Exception e) {;
        }
    }

    public void loadGame(String path)
    {
        try {
            // need to read the format seperatly to get the maze size first
            MyDecompressorInputStream formatDecompress = new MyDecompressorInputStream(new FileInputStream(path));
            byte[] formatBytes = new byte[12];
            formatDecompress.read(formatBytes);
            formatDecompress.close();
            int rows = combineInt(formatBytes[0],formatBytes[1]);
            int cols = combineInt(formatBytes[2],formatBytes[3]);

            MyDecompressorInputStream in = new MyDecompressorInputStream(new FileInputStream(path));
            byte[] savedMazeBytes = new byte[rows*cols + 12];
            in.read(savedMazeBytes);
            in.close();
            maze = new Maze(savedMazeBytes);
            setChanged();
            notifyObservers("maze generated");
        } catch (IOException e) {
        }
    }

    public void requestSolution()
    {
        if(maze==null)
            return;
        if(solution==null){
            try {
                Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                    @Override
                    public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                        try {
                            ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                            ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                            toServer.flush();
                            toServer.writeObject(maze); //send maze to server
                            toServer.flush();
                            Solution sol = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                            ArrayList<AState> mazeSolutionSteps = sol.getSolutionPath();
                            solution = new int[sol.getSolutionPath().size()][2];
                            for (int i = 0; i < mazeSolutionSteps.size(); ++i) {
                                solution[i][0] = ((Position)((MazeState)mazeSolutionSteps.get(i)).getData()).getRowIndex();
                                solution[i][1] = ((Position)((MazeState)mazeSolutionSteps.get(i)).getData()).getColumnIndex();
                            }
                            setChanged();
                            notifyObservers("got solution");
                        } catch (Exception e) {
                            //
                        }
                    }
                });
                client.communicateWithServer();
            } catch (UnknownHostException e) {
                //
            }

        }else{
            //solution already exists
            setChanged();
            notifyObservers("got solution");
        }
    }


    public void exitGame()
    {
        mazeGeneratingServer.stop();
        solveSearchProblemServer.stop();
        Platform.exit();
        System.exit(0);
    }

    /**
     * Getters and Setters
     */
    @Override
    public void assignObserver(Observer o) {this.addObserver(o);}
    public int[][] getSolution() {return this.solution;}
    public Maze getMaze() {return maze;}
    public int getCharRow(){
        return this.charRow;
    }
    public int getCharCol(){
        return this.charCol;
    }

    public int[] getGoalPosition()
    {
        if(maze==null){
            return null;
        }
        return new int[]{this.maze.getGoalPosition().getRowIndex(),this.maze.getGoalPosition().getColumnIndex()};
    }
    /**
     * Helper functions to load a maze
     */
    private int combineInt(int a, int b)
    {
        if(a<0)
            a = (a & 0xFF);
        if(b<0)
            b = (b & 0xFF);
        String num1 = padLeftZeros(Integer.toBinaryString(a), 8);
        String num2 = padLeftZeros(Integer.toBinaryString(b), 8);
        int res = Integer.parseInt(num1.concat(num2), 2);
        return res;
    }
    private String padLeftZeros(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }
        sb.append(inputString);

        return sb.toString();
    }


}
