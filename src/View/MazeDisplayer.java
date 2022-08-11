package View;

import algorithms.mazeGenerators.Maze;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

/**
 * This class extends from Canvas and is doing the drawing.
 */
public class MazeDisplayer extends Canvas
{
    private Maze maze;
    private int[][] solution;
    private boolean isSolutionDrawn=false;
    // player position and color
    private int playerRow = 0;
    private int playerCol = 0;
    private Color playerColor=Color.WHITE;
    private int orientation;// player orientation, 0->up,1->down,2->right,3->left
    // wall and player images:
    GraphicsContext graphicsContext;
    @FXML
    StringProperty imageFileNameWall1 = new SimpleStringProperty();
    @FXML
    StringProperty imageFileNameWall2 = new SimpleStringProperty();
    @FXML
    StringProperty imageFileNamePlayerUp = new SimpleStringProperty();
    @FXML
    StringProperty imageFileNamePlayerDown = new SimpleStringProperty();
    @FXML
    StringProperty imageFileNamePlayerRight = new SimpleStringProperty();
    @FXML
    StringProperty imageFileNamePlayerLeft = new SimpleStringProperty();
    @FXML
    StringProperty imageFileNameSolution = new SimpleStringProperty();

    Image wallImage1,wallImage2,solutionImage;
    double canvasHeight,canvasWidth,cellHeight,cellWidth;


    /**
     * Drawing functions
     * for optimization, player movement does not cause the whole maze to be re-drawn
     */
    public void drawMaze(Maze maze)
    {
        this.maze = maze;
        playerRow = maze.getStartPosition().getRowIndex();
        playerCol = maze.getStartPosition().getColumnIndex();
        draw();
    }
    public void draw()
    {
        if(maze!= null) {
            updateSizes();
            graphicsContext = getGraphicsContext2D();
            graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
            drawMazeWalls();
            drawPlayer(-1,-1);
        }
    }

    private void drawMazeWalls() {
        graphicsContext.setFill(Color.RED);
        initImages();
        for (int i = 0; i < this.maze.getRowsNum(); i++) {
            for (int j = 0; j < this.maze.getColumnsNum(); j++) {
                if(maze.getCellValue(i,j) == 1){
                    double x = j * cellWidth;
                    double y = i * cellHeight;
                    Image im = randomImage();

                    if(im == null)
                        graphicsContext.fillRect(x, y, cellWidth, cellHeight);
                    else
                        graphicsContext.drawImage(im, x, y, cellWidth, cellHeight);
                }
            }
        }
    }

    /**
     * To avoid redrawing whole maze when player moves, we save the previous
     * player position, to clear it.
     * parameters will be -1,-1 if the drawing occurs not from player movement.
     */
    private void drawPlayer(int prevRow,int prevCol) {

        double prevX = prevCol * cellWidth;
        double prevY = prevRow * cellHeight;

        double x = getPlayerCol() * cellWidth;
        double y = getPlayerRow() * cellHeight;
        graphicsContext.setFill(playerColor);

        Image playerImage = getOrienation();

        if(playerImage == null){
            if(prevRow!=-1)
                graphicsContext.clearRect(prevX,prevY,cellWidth,cellHeight);
            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
        }
        else {
            if(prevRow!=-1)
                graphicsContext.clearRect(prevX,prevY,cellWidth,cellHeight);
            graphicsContext.drawImage(playerImage, x, y, cellWidth, cellHeight);
        }
    }

    private Image getOrienation()
    {
        Image im = null;
        try {
            switch (orientation) {
                case 0:
                    im = new Image(new FileInputStream(getImageFileNamePlayerUp()));
                    break;
                case 1:
                    im = new Image(new FileInputStream(getImageFileNamePlayerDown()));
                    break;
                case 2:
                    im = new Image(new FileInputStream(getImageFileNamePlayerLeft()));
                    break;
                case 3:
                    im = new Image(new FileInputStream(getImageFileNamePlayerRight()));
                    break;
            }
        }catch (Exception e){}
        return im;
    }
    private void drawSolution()
    {
        if(solution!= null)
        {
            isSolutionDrawn=true;
            graphicsContext.setFill(Color.RED);
            for (int i = 0; i < solution.length ; i++) {
                if(solutionImage == null)
                    graphicsContext.fillRect(solution[i][1] * cellWidth, solution[i][0] * cellHeight, cellWidth, cellHeight);
                else
                    graphicsContext.drawImage(solutionImage,solution[i][1] * cellWidth, solution[i][0] * cellHeight, cellWidth, cellHeight);

            }
        }
    }
    private void removeSolution()
    {
        if(solution!= null && isSolutionDrawn)
        {
            isSolutionDrawn=false;
            for (int i = 0; i < solution.length ; i++) {
                graphicsContext.clearRect(solution[i][1] * cellWidth, solution[i][0] * cellHeight, cellWidth, cellHeight);

            }
        }
    }

    /**
     * Helper functions
     */
    //return a random image from the wall images
    public Image randomImage()
    {
        if(wallImage1 != null && wallImage2 != null)
        {
            Random rand = new Random();
            int r = rand.nextInt(10);
            if(r<5)
                return wallImage1;
            else
                return wallImage2;
        }
        return null;
    }

    /**
     * Setters and Getters
     */

    public String getImageFileNameWall1() {
        return imageFileNameWall1.get();
    }
    public void setImageFileNameWall1(String s) {
        this.imageFileNameWall1.set(s);
    }
    public String getImageFileNameWall2() {
        return imageFileNameWall2.get();
    }
    public void setImageFileNameWall2(String s) {
        this.imageFileNameWall2.set(s);
    }

    public String getImageFileNameSolution() {
        return imageFileNameSolution.get();
    }
    public void setImageFileNameSolution(String s) {

        this.imageFileNameSolution.set(s);
    }
    public String getImageFileNamePlayerUp() {
        return imageFileNamePlayerUp.get();
    }
    public String getImageFileNamePlayerDown() {
        return imageFileNamePlayerDown.get();
    }
    public void setImageFileNamePlayerUp(String s) {

        this.imageFileNamePlayerUp.set(s);
    }
    public void setImageFileNamePlayerDown(String s) {

        this.imageFileNamePlayerDown.set(s);
    }

    public String getImageFileNamePlayerRight() {
        return imageFileNamePlayerRight.get();
    }
    public void setImageFileNamePlayerRight(String s) {

        this.imageFileNamePlayerRight.set(s);
    }
    public String getImageFileNamePlayerLeft() {
        return imageFileNamePlayerLeft.get();
    }
    public void setImageFileNamePlayerLeft(String s) {

        this.imageFileNamePlayerLeft.set(s);
    }

    public int getPlayerRow() {
        return playerRow;
    }
    public int getPlayerCol() {
        return playerCol;
    }
    public void setPlayerPosition(int r, int c)
    {
        int prevY = this.playerRow;
        int prevX = this.playerCol;
        this.playerCol = c;
        this.playerRow = r;
        checkOrientation(prevY,prevX,r,c);

        removeSolution();
        drawPlayer(prevY,prevX);
    }
    // player orientation, 0->up,1->down,2->right,3->left
    private void checkOrientation(int prevY,int prevX,int r,int c)
    {
        if(prevY==r-1)
            orientation=1;
        else if(prevY==r+1)
            orientation=0;
        else if(prevX==c-1)
            orientation=3;
        else if(prevX==c+1)
            orientation=2;
    }

    private void updateSizes()
    {
        canvasHeight = getHeight();
        canvasWidth = getWidth();
        int rows = maze.getRowsNum();
        int cols = maze.getColumnsNum();
        cellHeight = canvasHeight / rows;
        cellWidth = canvasWidth / cols;
    }
    public void setSolution(int[][] sol)
    {
        this.solution=sol;
        updateSizes();
        drawSolution();
    }

    private void initImages()
    {
        try {
            wallImage1 = new Image(new FileInputStream(getImageFileNameWall1()));
            wallImage2 = new Image(new FileInputStream(getImageFileNameWall2()));
            solutionImage = new Image(new FileInputStream(getImageFileNameSolution()));
        }catch (FileNotFoundException e)
        {
        }
    }

}
