/**
 * A row of cells, all with the same y coordinate.
 */
public class SudokuRow {
    /**
     * An array of cells, from left to right on the row.
     */
    private SudokuCell[] row;
    /**
     * The y value of the row.
     */
    private int y;
    /**
     * Creates the row.
     * @param row The array of cells in the row.
     * @param y The y value of the row.
     */
    public SudokuRow(SudokuCell[] row, int y) {
        this.row = row;
        this.y = y;
    }
    /**
     * @param cell The x coordinate of the cell within the row.
     * @return The cell at that location.
     */
    public SudokuCell getCell(int cell) {
        return row[cell];
    }
    /**
     * @return All cells in the row
     */
    public SudokuCell[] getAllCells() {
        return this.row;
    }
    /**
     * @return An array of all confirmed values in a row.
     */
    public int[] getValues() {
        int[] values = new int[0];
        for (SudokuCell cell: this.row) {
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
     * If the solver has determined that one square must contain
     * the value in the row, all cells outside that row have the
     * value set to impossible.
     * @param x The x value of the top left cell in a square.
     * @param value The value to set to impossible.
     */
    public void setAllOtherMiniRowsImpossible(int x, int value) {
        x = (x - 1) / 3;
        for (SudokuCell cell: this.row) {
            int cellX = (cell.getX() - 1) / 3;
            if (cellX < x || cellX / 3 > x) {
                cell.setImpossibleValue(value);
            }
        }
    }
    /**
     * @return The y value of the row.
     */
    public int getY() {
        return y;
    }
}
