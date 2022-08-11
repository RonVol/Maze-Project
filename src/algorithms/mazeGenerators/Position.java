package algorithms.mazeGenerators;

import java.io.Serializable;

/**
 * This class represents a position in the maze
 */
public class Position implements Serializable {
    private int row;
    private int col;

    public Position(int r, int c)
    {
        this.row=r;
        this.col=c;
    }
    public int getRowIndex()
    {
        return this.row;
    }
    public int getColumnIndex()
    {
        return this.col;
    }
    @Override
    public String toString()
    {
        return "{"+this.row+","+this.col+"}";
    }
}
