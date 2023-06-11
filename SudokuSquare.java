/**
 * A 3x3 square of cells.
 */
public class SudokuSquare {
    /**
     * The cells within the square.
     */
    private SudokuCell[] square;
    /**
     * The coordinates of the top left cell in the square.
     */
    private int[] topLeftCoordinates;
    private SudokuMini[] miniRows;
    private SudokuMini[] miniColumns;
    /**
     * Creates the square.
     * @param square The array of cells in the square.
     * @param topLeftCoordinates The coordinates of the top left cell.
     */
    public SudokuSquare(SudokuCell[] square, int[] topLeftCoordinates) {
        this.topLeftCoordinates = topLeftCoordinates;
        this.square = square;
        this.miniRows = new SudokuMini[3];
        this.miniColumns = new SudokuMini[3];
        for (int i = 0; i < 3; i++) {
            this.miniRows[i] = new SudokuMini(this, i, true);
            this.miniColumns[i] = new SudokuMini(this, i, false);
        }
    }
    /**
     * @param coordinates The coordinates of a desired cell.
     * @return The specified cell.
     */
    public SudokuCell getCell(int[] coordinates) {
        int cell = coordinates[0] * 3 + coordinates[1];
        return this.square[cell];
    }
    public SudokuCell[] getAllCells() {
        return this.square;
    }
    /**
     * @return An array of all confirmed values in a square.
     */
    public int[] getValues() {
        int[] values = new int[0];
        for (SudokuCell cell: this.square) {
            int value = cell.getValue();
            if (value != 0) {
                int[] newValues = new int[values.length + 1];
                System.arraycopy(values, 0, newValues, 0, values.length);
                newValues[values.length] = value;
                values = newValues;
            }
        }
        return values;
    }
    /**
     * @param y The y value of a row, within the square.
     *          The first row of the square is 0, regardless
     *          of the square's position within the puzzle.
     * @return A "mini row" containing only the cells within
     * one row of the square.
     */
    public SudokuMini getMiniRow(int y) {
        return miniRows[y];
    }
    /**
     * @param x The x value of a column, within the square.
     *          The first column of the square is 0, regardless
     *          of the square's position within the puzzle.
     * @return A "mini column" containing only the cells within
     * one column of the square.
     */
    public SudokuMini getMiniColumn(int x) {
        return miniColumns[x];
    }
    /**
     * @return The coordinates of the top left cell.
     */
    public int[] getCoordinates() {
        return this.topLeftCoordinates;
    }
    /**
     * @return The x coordinate of the top left cell.
     */
    public int getX() {
        return this.topLeftCoordinates[0];
    }
    /**
     * @return the y coordinate of the top left cell.
     */
    public int getY() {
        return this.topLeftCoordinates[1];
    }

}
