package algorithms.mazeGenerators;

import java.io.Serializable;

/**
 * Maze Class represents a maze, with a start position and goal position
 * where 0 indicates free cell and 1 indicates a wall
 */


public class Maze implements Serializable {
    private int[][] maze;
    private int rows;
    private int columns;
    private Position startPosition;
    private Position goalPosition;
    Maze(int rows,int columns) {
        // if rows and columns is wrong, make it 2x2
        if (rows <= 0 || columns <= 0) {
            this.rows = 2;
            this.columns = 2;
        } else {
            this.rows = rows;
            this.columns = columns;
        }
        this.maze = new int[this.rows][this.columns];
        //default starting and goal position
        this.startPosition = new Position(0, 0);

        this.goalPosition = new Position(this.rows - 1, this.columns - 1);
        //default maze values is full of walls.
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                maze[i][j] = 1;
            }
        }

    }

    /**
     * Byte array constructor, should receive byte arr by format described at toByteArray Function
     */
    public Maze(byte[] arr)
    {
        //if data received isn't at least by format for example empty arr
        //construct a default 2x2 maze with walls
        if(arr.length < 12)
        {
            this.rows=2;
            this.columns=2;
            this.maze = new int[this.rows][this.columns];
            //default starting and goal position
            this.startPosition = new Position(0, 0);
            this.goalPosition = new Position(this.rows - 1, this.columns - 1);
            //default maze values is full of walls.
            for (int i = 0; i < this.rows; i++) {
                for (int j = 0; j < this.columns; j++) {
                    maze[i][j] = 1;
                }
            }
        }
        else
        {
            int rows = combineInt(arr[0], arr[1]);
            int cols = combineInt(arr[2], arr[3]);
            int startRow = combineInt(arr[4], arr[5]);
            int startCol = combineInt(arr[6], arr[7]);
            int goalRow = combineInt(arr[8], arr[9]);
            int goalCol = combineInt(arr[10], arr[11]);
            this.rows = rows;
            this.columns = cols;
            this.maze = new int[rows][cols];
            this.startPosition = new Position(startRow, startCol);
            this.goalPosition = new Position(goalRow, goalCol);
            int count = 12;
            for (int i = 0; i < this.rows; i++) {
                for (int j = 0; j < this.columns; j++) {
                    maze[i][j] = arr[count];
                    count++;
                }
            }
        }

    }
    public int getRowsNum(){
        return this.rows;
    }
    public int getColumnsNum(){
        return this.columns;
    }
    public int[][] getMazeArr() {return this.maze;}
    public Position getStartPosition(){
        return this.startPosition;
    }
    public void setStartPosition(int row, int col){
        this.startPosition = new Position(row,col);
    }
    public Position getGoalPosition(){
        return this.goalPosition;
    }
    public void setGoalPosition(int row, int col){
        this.goalPosition = new Position(row,col);
    }


    public boolean setCellValue(int row,int col, int value){
        //if illegal input, don't change anything and return false
        if(row<0 || col<0 || row >= this.rows || col >= this.columns){
            return false;
        }
        this.maze[row][col] = value;
        return true;
    }

    public int getCellValue(int row, int col){
        return this.maze[row][col];
    }

    public void print(){
        for(int i=0;i<this.rows;i++){
            for(int j=0;j<this.columns;j++) {
                if (i == startPosition.getRowIndex() && j == startPosition.getColumnIndex()) {
                    System.out.print("S ");
                } else if (i == goalPosition.getRowIndex() && j == goalPosition.getColumnIndex()) {
                    System.out.print("E ");
                } else{
                    System.out.print(this.maze[i][j] + " ");
              }
            }
            System.out.println();
        }
    }

    //split an int number to two byte-sized int's(each a byte,8bits)
    //assumption is that the highest number for dimension is 2^16-1, so an int is 2 bytes.
    //it's possible to lower dimension limit and calculations by calculating difference and remainder of 127 instead
    private int[] splitInt(int num)
    {
        int[] res = new int[2];
        String binaryStr = padLeftZeros(Integer.toBinaryString(num),16);
        res[0] =Integer.parseInt( binaryStr.substring(0,8),2);
        res[1] =Integer.parseInt( binaryStr.substring(8,16),2);
        return res;
    }
    //reverse the splitInt function
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

    /**
     * Convert the maze to a byte array using the following format:
     * Index 0,1 - two bytes representing maze number of rows
     * Index 2,3 - two bytes representing maze number of columns
     * Index 4,5 - two bytes representing the START row
     * Index 6,7 - two bytes representing the START column
     * Index 8,9 - two bytes representing the GOAL row
     * Index 10,11 - two bytes representing the GOAL column
     * Index 12 up to rows*colums - the maze itself(0's and 1's)
     * @return byte array
     */
    public byte[] toByteArray(){
        int[] startRow = splitInt(this.getStartPosition().getRowIndex());
        int[] startCol = splitInt(this.getStartPosition().getColumnIndex());
        int[] goalRow = splitInt(this.getGoalPosition().getRowIndex());
        int[] goalCol = splitInt(this.getGoalPosition().getColumnIndex());
        int[] mazeRows = splitInt(this.getRowsNum());
        int[] mazeCols = splitInt(this.getColumnsNum());

        byte[] b = new byte[this.getRowsNum()*this.getColumnsNum() + 12];
        b[0] = (byte) mazeRows[0];
        b[1] = (byte)mazeRows[1];
        b[2] = (byte)mazeCols[0];
        b[3] = (byte)mazeCols[1];
        b[4] = (byte)startRow[0];
        b[5] = (byte)startRow[1];
        b[6] = (byte)startCol[0];
        b[7] = (byte)startCol[1];
        b[8] = (byte)goalRow[0];
        b[9] = (byte)goalRow[1];
        b[10] = (byte)goalCol[0];
        b[11] = (byte)goalCol[1];

        int count =12;
        for(int i=0;i<this.getRowsNum();i++)
        {
            for(int j=0;j<this.getColumnsNum();j++)
            {
                b[count] = (byte) (this.maze[i][j]);
                count++;
            }
        }
        return b;
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
