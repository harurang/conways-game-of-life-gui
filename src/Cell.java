import java.io.Serializable;

/**
 * This class represents a component of a the Grid.
 *
 * Cell implements Serializable because Grid will be written to a
 * file using Object Serialization and Grid is composed of Cells.
**/
public class Cell implements Serializable{

    private boolean isAlive;

    /**
    @ensures cells are instantiated to false
    **/
    public Cell() {
        this.isAlive = false;
    }

    /**
    @requires boolean based on if cell is alive or dead
    **/
    public boolean isAlive() {
        return this.isAlive;
    }

    /**
    @requires boolean value for cell state
    @ensures cell state is set
    **/
    public void setAlive(boolean val) {
        this.isAlive = val;
    }

    @Override
    public String toString() {
        String returnVal = "-";
        if (isAlive)
           returnVal = "O";
        return returnVal;
    }

    @Override
    public boolean equals(Object o) {
        boolean returnVal = false;
        if (o instanceof Cell)
            returnVal = (this.isAlive == ((Cell)o).isAlive());
        return returnVal;
    }
}







