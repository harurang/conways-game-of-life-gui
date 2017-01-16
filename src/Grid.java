import java.io.Serializable;

/**
 * This class represents the Grid of the game.
 *
 * Skeleton of class was provided.
 *
 * Grid implements Serializable because Grid will be written to a
 * file using Object Serialization
 **/
public class Grid implements Serializable{

    private Cell[][] itsCurrentState;
    private Cell[][] itsNextState;

    /**
     @ensures grid of cells is constructed
     @requires max number of rows and columns of grid
     **/
    public Grid(int numRows, int numColumns) {
        //instantiate two arrays of type Cell
        itsCurrentState = new Cell[numRows][numColumns];
        itsNextState = new Cell[numRows][numColumns];
        for (int row=0; row< itsCurrentState.length; row++) {
            for (int column=0; column < itsCurrentState[row].length; column++) {
                //put a cell in each index
                itsCurrentState[row][column] = new Cell();
                itsNextState[row][column] = new Cell();
            }
        }
    }

    /**
     @ensures if cell is alive or not
     @requires row and column of cell
     **/
    public boolean cellIsAlive(int x, int y) {
        return itsCurrentState[x][y].isAlive();
    }

    /**
     @ensures state of specified index is returned
     @requires 2 int parameters that represent Grid indices
     **/
    public Cell getCell(int x, int y){
        return itsCurrentState[x][y];
    }

    /**
     @ensures states are updated
     **/
    public void update() {
        //loop over rows
        for (int row=0; row < itsCurrentState.length; row++) {
            //loop over columns
            for (int column=0; column < itsCurrentState[row].length; column++) {
                //find out if the cell should be alive next round
                boolean isAliveNextRound = aliveNextRound(row,column);
                //update it's value based on return value
                itsNextState[row][column].setAlive(isAliveNextRound);
            }
        }
        //swap arrays
        swapStates();
    }

    /**
     @ensures if a cell will be alive in the next state based on live neighbors
     @requires row and column of cell that is being evaluated
     **/
    private boolean aliveNextRound(int row, int column) {
        //assigns currentAliveState alive or dead based on parameter
        boolean currentAliveState = itsCurrentState[row][column].isAlive();
        boolean aliveNextRound = currentAliveState;

        int liveNeighbors = getCountOfLiveNeighbors(row,column);

        if(currentAliveState == true && (liveNeighbors != 2 && liveNeighbors !=3))
            aliveNextRound = false;
        else if(currentAliveState == false && liveNeighbors == 3)
            aliveNextRound = true;
        //return whether the cell should be alive next round or not
        return aliveNextRound;
    }

    /**
     @ensures number of live cells around current cell is obtained using normalization
     @requires row and column in order to determine which cell we need the live neighbors of
     **/
    private int getCountOfLiveNeighbors(int row, int column) {
        int numLiveNeighbors = 0;

        int up = row-1;
        int down = row+1;
        int left = column-1;
        int right = column+1;
        // top edge case
        if (row == 0) {
            up = itsCurrentState.length-1;
        // bottom edge case
        } else if (row == itsCurrentState.length-1) {
            down = 0;
        }
        // left edge case
        if (column == 0) {
            left = itsCurrentState[0].length-1;
        // right edge case
        } else if (column == itsCurrentState[0].length-1) {
            right = 0;
        }

        int[][] neighborsToConsider = { {up,left},{up,column}, {up,right}, {row,left},{row,right},
        {down,left},{down,column},{down,right} };

        for (int neighborIndex = 0; neighborIndex < neighborsToConsider.length; neighborIndex++) {
            if (itsCurrentState[neighborsToConsider[neighborIndex][0]][neighborsToConsider[neighborIndex][1]].isAlive())
                numLiveNeighbors++;
        }
        return numLiveNeighbors;
    }

    /**
     @ensures current state is swapped with next state
     **/
    private void swapStates() {
        Cell[][] temp = itsCurrentState;
        itsCurrentState = itsNextState;
        itsNextState = temp;
    }

    @Override
    public String toString() {
        String returnVal = "";
        for (int i=0; i<itsCurrentState.length; i++) {
            for (int j=0; j<itsCurrentState[i].length; j++) {
                returnVal += itsCurrentState[i][j];
                returnVal += " ";
            }
            returnVal += "\n";
        }
        return returnVal;
    }

    /**
     @ensures certain cells are alive when program begins
     **/
    public void gliderSetup() {
        itsCurrentState[5][5].setAlive(true);
        itsCurrentState[6][5].setAlive(true);
        itsCurrentState[7][5].setAlive(true);
        itsCurrentState[7][4].setAlive(true);
        itsCurrentState[6][3].setAlive(true);
    }
}
