import java.util.Arrays;

/**
 * All the cells that make up the puzzle.
 */
public class SudokuCell {
    /**
     * An array of two coordinates for the
     * cell. The first coordinate is the X
     * value, the second coordinate is the
     * Y value.
     */
    private final int[] coordinates;
    /**
     * An array of possible values for the cell.
     * The array is always size 9. The value at
     * an array index indicates whether a cell's
     * answer can be that index + 1. If a final
     * value is determined by the solver, that
     * value is set to true, while all others are
     * set to false.
     */
    private final boolean[] possibleValues;
    /**
     * The final value for the cell. If it is zero,
     * the final value has not been determined yet.
     */
    private int value;
    /**
     * Generates the cell with an unknown value.
     * Sets possible values to all be true. The
     * solver will read and manipulate the values
     * later.
     * @param coordinates The coordinates of the cell.
     */
    public SudokuCell(int[] coordinates) {
        this.coordinates = coordinates;
        this.value = 0;
        boolean[] possibleValues = new boolean[9];
        Arrays.fill(possibleValues, true);
        this.possibleValues = possibleValues;
    }

    /**
     * Generates a cell with a known value. Sets the possible
     * values for that value to true, all others are set to false.
     * @param coordinates The coordinates of the cell.
     * @param value The known value of the cell.
     */
    public SudokuCell(int[] coordinates, int value) {
        this.coordinates = coordinates;
        this.value = value;
        boolean[] possibleValues = new boolean[9];
        for (int i = 0; i < possibleValues.length; i++) {
            possibleValues[i] = i == value - 1;
        }
        this.possibleValues = possibleValues;
    }

    /**
     * @return The X and Y values of the cell.
     */
    public int[] getCoordinates() {
        return this.coordinates;
    }
    /**
     * @return Just the X value of the cell.
     */
    public int getX() {
        return this.coordinates[0];
    }
    /**
     * @return Just the Y value of the cell.
     */
    public int getY() {
        return this.coordinates[1];
    }
    /**
     * @return The array of possible values for the cell.
     */
    public boolean[] getPossibleValues() {
        return this.possibleValues;
    }
    /**
     * @return The number of possible values for the cell.
     */
    public int getPossibleCount() {
        int count = 0;
        for (boolean possibleValue : this.possibleValues) {
            if (possibleValue) count++;
        }
        return count;
    }
    public void setImpossibleValue(int value) {
        this.possibleValues[value - 1] = false;
    }
    /**
     * @return The known value of the cell.
     * If the value is not yet known, returns 0.
     */
    public int getValue() {
        return this.value;
    }
    /**
     * Sets the known value for the cell.
     * Used by the solver.
     * @param value The known value of the cell.
     */
    public void setValue(int value) {
        this.value = value;
        for (int i = 0; i < 8; i++) {
            if (i + 1 != value) {
                possibleValues[i] = false;
            }
        }
    }
}

