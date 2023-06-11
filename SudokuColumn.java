/**
 * A column of cells, all with the same x coordinate.
 */
public class SudokuColumn {
    /**
     * An array of cells, from top to bottom within the column.
     */
    private SudokuCell[] column;
    /**
     * The x coordinate of the column.
     */
    private int x;
    /**
     * Creates the column.
     * @param column The array of cells in the column.
     * @param x The x value of the column.
     */
    public SudokuColumn(SudokuCell[] column, int x) {
        this.column = column;
        this.x = x;
    }
    /**
     * @param cell The y coordinate of the cell within the column.
     * @return The cell at that location.
     */
    public SudokuCell getCell(int cell) {
        return this.column[cell];
    }
    public SudokuCell[] getAllCells() {
        return this.column;
    }
    /**
     * @return An array of all confirmed values in a column.
     */
    public int[] getValues() {
        int[] values = new int[0];
        for (SudokuCell cell: this.column) {
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
     * the value in the column, all cells outside that column have the
     * value set to impossible.
     * @param y The y value of the top left cell in a square.
     * @param value The value to set to impossible.
     */
    public void setAllOtherMiniColumnsImpossible(int y, int value) {
        y = (y - 1) / 3;
        for (SudokuCell cell: this.column) {
            int cellY = (cell.getY() - 1) / 3;
            if (cellY < y || cellY / 3 > y) {
                cell.setImpossibleValue(value);
            }
        }
    }
    /**
    /**
     * @return The x coordinate of the column.
     */
    public int getX() {
        return x;
    }
}
