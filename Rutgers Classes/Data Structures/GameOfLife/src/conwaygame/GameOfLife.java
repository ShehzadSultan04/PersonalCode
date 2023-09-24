package conwaygame;

import java.util.ArrayList;


/**
 * Conway's Game of Life Class holds various methods that will
 * progress the state of the game's board through it's many iterations/generations.
 *
 * Rules 
 * Alive cells with 0-1 neighbors die of loneliness.
 * Alive cells with >=4 neighbors die of overpopulation.
 * Alive cells with 2-3 neighbors survive.
 * Dead cells with exactly 3 neighbors become alive by reproduction.

 * @author Seth Kelley 
 * @author Maxwell Goldberg
 */
public class GameOfLife {

    // Instance variables
    
    private static final boolean ALIVE = true;
    private static final boolean  DEAD = false;
    public int rows; 
    public int columns; 

    private boolean[][] grid;    // The board has the current generation of cells
    // boolean[][] newGenGrid; //The board that has the next generation
    
    private int totalAliveCells; // Total number of alive cells in the grid (board)

    //ADDED CODE
    WeightedQuickUnionUF uf;
    ArrayList<Integer> parents = new ArrayList<Integer>();
    private boolean[] parentID;
    //ADDED CODE

    private int[][] aliveNeighbors;
    /**
    * Default Constructor which creates a small 5x5 grid with five alive cells.
    * This variation does not exceed bounds and dies off after four iterations.
    */
    public GameOfLife() {
        grid = new boolean[5][5];
        totalAliveCells = 5;
        grid[1][1] = ALIVE;
        grid[1][3] = ALIVE;
        grid[2][2] = ALIVE;
        grid[3][2] = ALIVE;
        grid[3][3] = ALIVE;
    }

    /**
    * Constructor used that will take in values to create a grid with a given number
    * of alive cells
    * @param file is the input file with the initial game pattern formatted as follows:
    * An integer representing the number of grid rows, say r
    * An integer representing the number of grid columns, say c
    * Number of r lines, each containing c true or false values (true denotes an ALIVE cell)
    */
    public GameOfLife (String file) {

        StdIn.setFile(file);
        rows = StdIn.readInt();
        columns = StdIn.readInt();
        grid = new boolean[rows][columns];
        aliveNeighbors = new int[rows][columns];
        parentID = new boolean[rows * columns];
        uf = new WeightedQuickUnionUF(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                grid[i][j] = StdIn.readBoolean();
            }
        }

        
        calculateNeightbors();
    }

    /**
     * Returns grid
     * @return boolean[][] for current grid
     */
    public boolean[][] getGrid () {
        return grid;
    }
    
    /**
     * Returns totalAliveCells
     * @return int for total number of alive cells in grid
     */
    public int getTotalAliveCells () {
        return totalAliveCells;
    }

    /**
     * Returns the status of the cell at (row,col): ALIVE or DEAD
     * @param row row position of the cell
     * @param col column position of the cell
     * @return true or false value "ALIVE" or "DEAD" (state of the cell)
     */
    public boolean getCellState (int row, int col) {

        // WRITE YOUR CODE HERE
        return grid[row][col]; // update this line, provided so that code compiles
    }

    /**
     * Returns true if there are any alive cells in the grid
     * @return true if there is at least one cell alive, otherwise returns false
     */
    public boolean isAlive () {

        // WRITE YOUR CODE HERE
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (grid[i][j]){
                    totalAliveCells++;
                }
                    
            }
        } 
        return totalAliveCells > 0; 

    }

    /**
     * Determines the number of alive cells around a given cell.
     * Each cell has 8 neighbor cells which are the cells that are 
     * horizontally, vertically, or diagonally adjacent.
     * 
     * @param col column position of the cell
     * @param row row position of the cell
     * @return neighboringCells, the number of alive cells (at most 8).
     */
    public int numOfAliveNeighbors (int row, int col) {
        
        return aliveNeighbors[row][col];
    }

    private void calculateNeightbors(){
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {

                if (i > 0 && i < rows -1 && j >0 && j < columns -1) {
                    //This is one of the center boxes
                    aliveNeighbors[i][j] = 0;
                    aliveNeighbors[i][j] += (grid[i-1][j-1]) ? 1 :0; //diagonal top left
                    aliveNeighbors[i][j] += (grid[i-1][j]) ? 1 :0; //top
                    aliveNeighbors[i][j] += (grid[i-1][j+1]) ? 1 :0; //diagonal top right
                    aliveNeighbors[i][j] += (grid[i][j-1]) ? 1 :0; //left
                    aliveNeighbors[i][j] += (grid[i][j+1]) ? 1 :0; //right
                    aliveNeighbors[i][j] += (grid[i+1][j-1]) ? 1 :0; //diagonal bottom left
                    aliveNeighbors[i][j] += (grid[i+1][j]) ? 1 :0; //bottom
                    aliveNeighbors[i][j] += (grid[i+1][j+1]) ? 1 :0; //diagonal bottom right
                }

                else if (i == 0 && j == 0) {
                    //Top left box
                    aliveNeighbors[i][j] = 0;
                    aliveNeighbors[i][j] += (grid[rows-1][columns-1]) ? 1 :0; //diagonal top left
                    aliveNeighbors[i][j] += (grid[rows-1][j]) ? 1 :0; //top
                    aliveNeighbors[i][j] += (grid[rows-1][j+1]) ? 1 :0; //diagonal top right
                    aliveNeighbors[i][j] += (grid[i][columns-1]) ? 1 :0; //left
                    aliveNeighbors[i][j] += (grid[i][j+1]) ? 1 :0; //right
                    aliveNeighbors[i][j] += (grid[i+1][columns-1]) ? 1 :0; //diagonal bottom left
                    aliveNeighbors[i][j] += (grid[i+1][j]) ? 1 :0; //bottom
                    aliveNeighbors[i][j] += (grid[i+1][j+1]) ? 1 :0; //diagonal bottom right
                }

                else if (i == 0 && (j > 0 && j < columns -1)){
                    //Top edge boxes
                    aliveNeighbors[i][j] = 0;
                    aliveNeighbors[i][j] += (grid[rows-1][j-1]) ? 1 :0; //diagonal top left
                    aliveNeighbors[i][j] += (grid[rows-1][j]) ? 1 :0; //top
                    aliveNeighbors[i][j] += (grid[rows-1][j+1]) ? 1 :0; //diagonal top right
                    aliveNeighbors[i][j] += (grid[i][j-1]) ? 1 :0; //left
                    aliveNeighbors[i][j] += (grid[i][j+1]) ? 1 :0; //right
                    aliveNeighbors[i][j] += (grid[i+1][j-1]) ? 1 :0; //diagonal bottom left
                    aliveNeighbors[i][j] += (grid[i+1][j]) ? 1 :0; //bottom
                    aliveNeighbors[i][j] += (grid[i+1][j+1]) ? 1 :0; //diagonal bottom right
                }

                else if (i == 0 && j == columns -1) {
                    //Top right box
                    aliveNeighbors[i][j] = 0;
                    aliveNeighbors[i][j] += (grid[rows-1][columns-2]) ? 1 :0; //diagonal top left
                    aliveNeighbors[i][j] += (grid[rows-1][columns -1]) ? 1 :0; //top
                    aliveNeighbors[i][j] += (grid[rows-1][0]) ? 1 :0; //diagonal top right
                    aliveNeighbors[i][j] += (grid[i][j-1]) ? 1 :0; //left
                    aliveNeighbors[i][j] += (grid[i][0]) ? 1 :0; //right
                    aliveNeighbors[i][j] += (grid[i+1][j-1]) ? 1 :0; //diagonal bottom left
                    aliveNeighbors[i][j] += (grid[i+1][j]) ? 1 :0; //bottom
                    aliveNeighbors[i][j] += (grid[i+1][0]) ? 1 :0; //diagonal bottom right
                }

                else if (j == columns -1 && i > 0 && i < rows-1) {
                    //Right Edge Boxes
                    aliveNeighbors[i][j] = 0;
                    aliveNeighbors[i][j] += (grid[i-1][j-1]) ? 1 :0; //diagonal top left
                    aliveNeighbors[i][j] += (grid[i-1][j]) ? 1 :0; //top
                    aliveNeighbors[i][j] += (grid[i-1][0]) ? 1 :0; //diagonal top right
                    aliveNeighbors[i][j] += (grid[i][j-1]) ? 1 :0; //left
                    aliveNeighbors[i][j] += (grid[i][0]) ? 1 :0; //right
                    aliveNeighbors[i][j] += (grid[i+1][j-1]) ? 1 :0; //diagonal bottom left
                    aliveNeighbors[i][j] += (grid[i+1][j]) ? 1 :0; //bottom
                    aliveNeighbors[i][j] += (grid[i+1][0]) ? 1 :0; //diagonal bottom right
                }

                else if (j == columns -1 && i == rows-1) {
                    //Bottom Right Box
                    aliveNeighbors[i][j] = 0;
                    aliveNeighbors[i][j] += (grid[i-1][j-1]) ? 1 :0; //diagonal top left
                    aliveNeighbors[i][j] += (grid[i-1][j]) ? 1 :0; //top
                    aliveNeighbors[i][j] += (grid[i-1][0]) ? 1 :0; //diagonal top right
                    aliveNeighbors[i][j] += (grid[i][j-1]) ? 1 :0; //left
                    aliveNeighbors[i][j] += (grid[i][0]) ? 1 :0; //right
                    aliveNeighbors[i][j] += (grid[0][j-1]) ? 1 :0; //diagonal bottom left
                    aliveNeighbors[i][j] += (grid[0][j]) ? 1 :0; //bottom
                    aliveNeighbors[i][j] += (grid[0][0]) ? 1 :0; //diagonal bottom right
                }

                else if (i == rows -1 && j > 0 && j < columns -1) {
                    //Bottom Edge Boxes
                    aliveNeighbors[i][j] = 0;
                    aliveNeighbors[i][j] += (grid[i-1][j-1]) ? 1 :0; //diagonal top left
                    aliveNeighbors[i][j] += (grid[i-1][j]) ? 1 :0; //top
                    aliveNeighbors[i][j] += (grid[i-1][j+1]) ? 1 :0; //diagonal top right
                    aliveNeighbors[i][j] += (grid[i][j-1]) ? 1 :0; //left
                    aliveNeighbors[i][j] += (grid[i][j+1]) ? 1 :0; //right
                    aliveNeighbors[i][j] += (grid[0][j-1]) ? 1 :0; //diagonal bottom left
                    aliveNeighbors[i][j] += (grid[0][j]) ? 1 :0; //bottom
                    aliveNeighbors[i][j] += (grid[0][j+1]) ? 1 :0; //diagonal bottom right
                }

                else if (i == rows -1 && j == 0) {
                    //Bottom Left Box
                    aliveNeighbors[i][j] = 0;
                    aliveNeighbors[i][j] += (grid[i-1][columns-1]) ? 1 :0; //diagonal top left
                    aliveNeighbors[i][j] += (grid[i-1][j]) ? 1 :0; //top
                    aliveNeighbors[i][j] += (grid[i-1][j+1]) ? 1 :0; //diagonal top right
                    aliveNeighbors[i][j] += (grid[i][columns-1]) ? 1 :0; //left
                    aliveNeighbors[i][j] += (grid[i][j+1]) ? 1 :0; //right
                    aliveNeighbors[i][j] += (grid[0][columns-1]) ? 1 :0; //diagonal bottom left
                    aliveNeighbors[i][j] += (grid[0][j]) ? 1 :0; //bottom
                    aliveNeighbors[i][j] += (grid[0][j+1]) ? 1 :0; //diagonal bottom right
                }

                if (j == 0 && i > 0 && i < rows -1) {
                    //Left edge boxes
                    aliveNeighbors[i][j] = 0;
                    aliveNeighbors[i][j] += (grid[i-1][columns-1]) ? 1 :0; //diagonal top left
                    aliveNeighbors[i][j] += (grid[i-1][j]) ? 1 :0; //top
                    aliveNeighbors[i][j] += (grid[i-1][j+1]) ? 1 :0; //diagonal top right
                    aliveNeighbors[i][j] += (grid[i][columns-1]) ? 1 :0; //left
                    aliveNeighbors[i][j] += (grid[i][j+1]) ? 1 :0; //right
                    aliveNeighbors[i][j] += (grid[i+1][columns-1]) ? 1 :0; //diagonal bottom left
                    aliveNeighbors[i][j] += (grid[i+1][j]) ? 1 :0; //bottom
                    aliveNeighbors[i][j] += (grid[i+1][j+1]) ? 1 :0; //diagonal bottom right
                }
                
            }
        }
    }

    /**
     * Creates a new grid with the next generation of the current grid using 
     * the rules for Conway's Game of Life.
     * 
     * @return boolean[][] of new grid (this is a new 2D array)
     */
    public boolean[][] computeNewGrid () {
        boolean newGenGrid[][];
        newGenGrid = new boolean[rows][columns];
        newGenGrid = grid;
        

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                //if, alive cell && num of neighbors is 0 or 1, cell dies
                //else if, dead cell && number of alive neighbors is exac 3, cell becomes alive
                //else if, alive cell && num of neighbors is 2 or 3, cell remains alive
                //else if, alive cell && num of neighbors is 4 or more, cell dies

                if (getCellState(i, j) && (aliveNeighbors[i][j] <= 1 || (aliveNeighbors[i][j]) >= 4)){
                    //if cell is alive and has 0, 1, or 4 or more neighbors
                    newGenGrid[i][j] = false; //cell dies
                }

                else if (!getCellState(i, j) && aliveNeighbors[i][j] == 3){
                    newGenGrid[i][j] = true;
                }
            }
        }
        calculateNeightbors();
        // WRITE YOUR CODE HERE
        return newGenGrid;// update this line, provided so that code compiles
    }

    /**
     * Updates the current grid (the grid instance variable) with the grid denoting
     * the next generation of cells computed by computeNewGrid().
     * 
     * Updates totalAliveCells instance variable
     */
    public void nextGeneration () {
    
        grid = computeNewGrid(); 
        
    }

    /**
     * Updates the current grid with the grid computed after multiple (n) generations. 
     * @param n number of iterations that the grid will go through to compute a new grid
     */
    
    public void nextGeneration (int n) {

        // WRITE YOUR CODE HERE
        for (int i = 0; i < n; i++){
            grid = computeNewGrid(); 
        }
    }

    private boolean isIDPresent(int id){
        for (int x = 0; x < parents.size(); x++){
            if (x == id){
                return true;
            }
        }
        return false; 

    }

    /**
     * Determines the number of separate cell communities in the grid
     * @return the number of communities in the grid, communities can be formed from edges
     */
    int communities = 0;
    public int numOfCommunities() {
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < columns; j++){
                if (grid[i][j] && !isIDPresent(uf.find(i, j))){
                    parents.add(uf.find(i, j));
                    if (j-1 > 0 && grid[i][j-1]){
                        uf.union(i, j, i, j-1);
                    }
                    if (i-1 > 0 && grid[i-1][j]){
                        uf.union(i, j, i-1, j);
                    }
                    if (j < columns && grid[i][j+1]){
                        uf.union(i, j, i, j+1);
                    }
                    if (i < rows && grid[i+1][j]){
                        uf.union(j, j, i+1, j);
                    }
                }
            }
        }

        for (int i = 0; i < parents.size(); i++){
            if(!parentID[parents.get(i)]){
                parentID[parents.get(i)] = true;
                communities++;
            }
        }
        return communities;
    }
}
