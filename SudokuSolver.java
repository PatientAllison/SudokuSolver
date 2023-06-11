//needed to import key, haven't learned proper File class yet
import edu.duke.FileResource;

/**
 * The rules of Sudoku: Every row, column, and 3x3 square must have
 * exactly one of each digit 1-9.
 */
public class SudokuSolver {
    /**
     * The main solver method. Loops over every cell in the puzzle,
     * gets its row, column, and square, and determines possible
     * values for the cell. Most of the logic is in helper methods.
     * @see #checkFull(SudokuCell, int[])
     * @see #checkImpossibleValues(SudokuCell, SudokuCell[])
     * @see #checkMiniRows(SudokuSquare, int)
     * @see #checkMiniColumns(SudokuSquare, int)
     * @param puzzle The puzzle input by the user.
     * @throws RuntimeException if the square fails to initialize.
     */
    public static void solve(SudokuPuzzle puzzle) {
        int generation = 0;
        SudokuRow[] rows = puzzle.getRows();
        SudokuColumn[] columns = puzzle.getColumns();
        SudokuSquare[] squares = puzzle.getSquares();
        while (puzzle.getConfirmedAnswers() < 81) {
            generation++;
            System.out.println("Changed Cells: \n");
            for (SudokuCell cell : puzzle.getCells()) {
                if (cell.getValue() != 0) continue;
                int x = cell.getX();
                int y = cell.getY();
                SudokuRow row = rows[y - 1];
                SudokuColumn column = columns[x - 1];
                SudokuSquare square = null;
                for (SudokuSquare currentSquare : squares) {
                    if (x >= currentSquare.getX() && x < currentSquare.getX() + 3) {
                        if (y >= currentSquare.getY() && y < currentSquare.getY() + 3) {
                            square = currentSquare;
                            break;
                        }
                    }
                }
                if (square == null) {
                    throw new RuntimeException("The square shouldn't be null anymore!");
                }
                int[] rowValues = row.getValues();
                int[] columnValues = column.getValues();
                int[] squareValues = square.getValues();
                SudokuCell[] rowCells = row.getAllCells();
                SudokuCell[] columnCells = column.getAllCells();
                SudokuCell[] squareCells = square.getAllCells();
                if (checkFull(cell, rowValues) || checkFull(cell, columnValues) || checkFull(cell, squareValues)) {
                    puzzle.incrementConfirmedAnswers();
                    System.out.printf("%s, %s (value: %s column: %s row: %s square: %s, %s)\n", cell.getX(), cell.getY(), cell.getValue(), column.getX(), row.getY(), (square.getX() / 3) + 1, (square.getY() / 3) + 1);
                    continue;
                }
                if (checkWhenLastPossibleCell(rowCells) || checkWhenLastPossibleCell(columnCells) || checkWhenLastPossibleCell(squareCells) || checkWhenLastPossibleValue(cell)) {
                    puzzle.incrementConfirmedAnswers();
                    System.out.printf("%s, %s (value: %s column: %s row: %s square: %s, %s)\n", cell.getX(), cell.getY(), cell.getValue(), column.getX(), row.getY(), (square.getX() / 3) + 1, (square.getY() / 3) + 1);
                    continue;
                }
                if (checkImpossibleValues(cell, rowCells) || checkImpossibleValues(cell, columnCells) || checkImpossibleValues(cell, squareCells)) {
                    puzzle.incrementConfirmedAnswers();
                    System.out.printf("%s, %s (value: %s column: %s row: %s square: %s, %s)\n", cell.getX(), cell.getY(), cell.getValue(), column.getX(), row.getY(), (square.getX() / 3) + 1, (square.getY() / 3) + 1);
                    continue;
                }
                for (int i = 1; i < 10; i++) {
                    if (!cell.getPossibleValues()[i - 1]) continue;
                    if (checkMiniRows(square, i) == 0) {
                        row.setAllOtherMiniRowsImpossible(square.getX(), i);
                    }
                    if (checkMiniColumns(square, i) == 0) {
                        column.setAllOtherMiniColumnsImpossible(square.getY(), i);
                    }
                }
            }
            System.out.printf("\nGeneration %s:\n", generation);
            System.out.println(puzzle);
        }
    }
    public static boolean checkWhenLastPossibleValue(SudokuCell cell) {
        int count = 0;
        int value = 0;
        boolean currentValue;
        for (int i = 0; i < 9; i++) {
            currentValue = cell.getPossibleValues()[i];
            if (currentValue) {
                count++;
                value = i + 1;
            }
        }
        if (count == 1) {
            cell.setValue(value);
            return true;
        }
        return false;
    }
    public static boolean checkWhenLastPossibleCell(SudokuCell[] cells) {
        SudokuCell cell = null;
        int trueCount;
        for (int i = 1; i < 10; i++) {
            trueCount = 0;
            for (SudokuCell currentCell : cells) {
                boolean[] possibleValues = currentCell.getPossibleValues();
                    if (possibleValues[i-1]) {
                        trueCount++;
                        cell = currentCell;
                    }
                }
            if (trueCount == 1 && cell.getValue() == 0) {
                cell.setValue(i);
                return true;
            }
        }
        return false;
    }
        /**
         * Checks how many "mini rows" within the square have a possible value.
         * @param square The square of the current cell.
         * @param value The possible value to check.
         * @return The number of "mini rows" that have the possible value.
         */
    public static int checkMiniRows(SudokuSquare square, int value) {
        int count = 0;
        for (int i = 0; i < 2; i++) {
            SudokuMini miniRow = square.getMiniRow(i);
            for (SudokuCell checkCell: miniRow.getCells()) {
                boolean[] possibleValues = checkCell.getPossibleValues();
                    if (possibleValues[value - 1]) {
                        count++;
                        break;
                    }
            }
        }
        return count;
    }
    /**
     * Checks how many "mini columns" within the square have a possible value.
     * @param square The square of the current cell.
     * @param value The possible value to check.
     * @return The number of "mini columns" that have the possible value.
     */
    public static int checkMiniColumns(SudokuSquare square, int value) {
        int count = 0;
        for (int i = 0; i < 2; i++) {
            SudokuMini miniColumn = square.getMiniColumn(i);
            for (SudokuCell checkCell: miniColumn.getCells()) {
                boolean[] possibleValues = checkCell.getPossibleValues();
                if (possibleValues[value - 1]) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }
    /**
     * Checks other confirmed values within the row, column,
     * or square, and sets the possible value for those
     * values to false for the current square. If there is
     * only one possible value remaining after eliminating
     * the others, sets the confirmed value for the cell to
     * that value.
     * @param cell The current cell.
     * @param otherCells A row, square, or column.
     * @return Whether the method found only one possible value
     * remaining after all others, and set the value accordingly.
     * The main solver method continues if this is true.
     */
    public static boolean checkImpossibleValues(SudokuCell cell, SudokuCell[] otherCells) {
        for (SudokuCell otherCell: otherCells) {
            int value = otherCell.getValue();
            if (value != 0) {
                cell.setImpossibleValue(value);
            }
        }
        if (cell.getPossibleCount() == 1) {
            boolean[] possibleValues = cell.getPossibleValues();
            for (int i = 0; i < possibleValues.length; i++) {
                if (possibleValues[i]) {
                    cell.setValue(i + 1);
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Checks if there is only one value left in the row,
     * column, or cell. If so, sets the value for the cell
     * to that value.
     * @param cell The current cell.
     * @param values The values of all other cells in the
     *               row, column, or square.
     * @return Whether the method found only one value missing
     * from the row, column, or square, and set the value accordingly.
     * The main solver method continues if this is true.
     */
    public static boolean checkFull(SudokuCell cell, int[] values) {
        if (values.length == 8) {
            int valueCount = 0;
            for (int i = 1; i < values.length; i++) {
                int lastValueCount = valueCount;
                for (int value : values) {
                    if (value == i) {
                        valueCount++;
                        break;
                    }
                }
                if (valueCount == lastValueCount) {
                    cell.setValue(i);
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Prompts the user for a puzzle file, generates
     * the puzzle, and attempts to solve it.
     * @param args The user can specify a file to use on
     *             the command line instead of at runtime.
     */
    public static void main(String[] args) {
    //FileResource key = new FileResource(args[0]); //open key file
    FileResource key = new FileResource();
    SudokuPuzzle puzzle = new SudokuPuzzle(key);
    System.out.println("Key:");
    System.out.println(puzzle);
    //System.out.println("Changed Cells: \n");
    solve(puzzle);
    //System.out.println("\nGeneration 1:");
    //System.out.println(puzzle);
    //System.out.println("What the fuck");
    }
}
