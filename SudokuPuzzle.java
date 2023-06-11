import edu.duke.FileResource; //needed to read cells
import java.util.*;

/**
 * Generates and holds all the data for the puzzle.
 */
public class SudokuPuzzle {
    /**
     * Stores the current state of the puzzle.
     */
    private ArrayList<SudokuCell> cells;
    /**
     * Stores all cells with confirmed answers for the puzzle,
     * including the original cells and any confirmed while solving.
     */
    private int confirmedAnswers;
    /**
     * Stores all the rows in this puzzle.
     */
    private SudokuRow[] rows;
    /**
     * Stores all the column in this puzzle.
     */
    private SudokuColumn[] columns;
    /**
     * Stores all the 3x3 squares in this puzzle.
     */
    private SudokuSquare[] squares;
    //private int generation;

    /**
     * Generates the puzzle from the input file.
     * @param key The sudoku key chosen by the user.
     * @throws IllegalArgumentException if the
     * key file is not exactly 9 lines that have 9
     * characters, or if any character is not a digit.
     */
    public SudokuPuzzle(FileResource key) {
        this.cells = new ArrayList<SudokuCell>();
        this.confirmedAnswers = 0;
        this.rows = new SudokuRow[9];
        this.columns = new SudokuColumn[9];
        this.squares = new SudokuSquare[9];
        setRows(key);
        setColumns();
        setSquares();
    }
    private void setRows(FileResource key) {
        int currentRow = 0;
        SudokuCell cell;
        for (String line: key.lines()) {
            if (line.length() != 9) throw new IllegalArgumentException("All rows must be 9 cells long!");
            currentRow++;
            if (currentRow > 9) throw new IllegalArgumentException("Key must contain 9 rows!");
            SudokuCell[] row = new SudokuCell[9];
            int value;
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                if (Character.isDigit(c)) {
                    value = Integer.parseInt(Character.toString(c));
                }
                else {
                    throw new IllegalArgumentException("All cells must contain a digit 0-9!");
                }
                int[] coordinates = new int[2];
                coordinates[0] = i + 1;
                coordinates[1] = currentRow;
                if (value == 0) {
                    cell = new SudokuCell(coordinates);
                }
                else {
                    cell = new SudokuCell(coordinates,value);
                    confirmedAnswers++;
                }
                this.cells.add(cell);
                row[i] = cell;
            }
            SudokuRow suRow = new SudokuRow(row,currentRow);
            this.rows[currentRow - 1] = suRow;
        }
        if (currentRow != 9) throw new IllegalArgumentException("Key must contain 9 rows!");
    }
    private void setColumns() {
        for (int i = 0; i < this.columns.length; i++) {
            SudokuCell[] column = new SudokuCell[9];
            for (int k = 0; k < column.length; k++) {
                column[k] = this.rows[k].getCell(i);
            }
            SudokuColumn suColumn = new SudokuColumn(column,i+1);
            this.columns[i] = suColumn;
        }
    }
    private void setSquares() {
        SudokuCell cell;
        for (int i = 0; i < this.squares.length; i++) {
            SudokuCell[] square = new SudokuCell[9];
            int[] topLeftCoordinates = new int[2];
            topLeftCoordinates[0] = (i % 3) * 3 + 1;
            topLeftCoordinates[1] = (i / 3) * 3 + 1;
            for (SudokuCell sudokuCell : cells) {
                cell = sudokuCell;
                int x = cell.getX() - 1;
                int y = cell.getY() - 1;
                if (x >= topLeftCoordinates[0] - 1 && x <= topLeftCoordinates[0] + 1) {
                    if (y >= topLeftCoordinates[1] - 1 && y <= topLeftCoordinates[1] + 1) {
                        int index = (x % 3 + y * 3) % 9;
                        square[index] = cell;
                    }
                }
            }
            SudokuSquare suSquare = new SudokuSquare(square,topLeftCoordinates);
            this.squares[i] = suSquare;
        }
    }
    public ArrayList<SudokuCell> getCells() {
        return this.cells;
    }
    public int getConfirmedAnswers() {
        return this.confirmedAnswers;
    }
    public void incrementConfirmedAnswers() {
        this.confirmedAnswers++;
    }
    public SudokuRow[] getRows() {
        return this.rows;
    }
    public SudokuColumn[] getColumns() {
        return this.columns;
    }
    public SudokuSquare[] getSquares() {
        return this.squares;
    }
    /**
     * Formats the puzzle string. Spaces are used to make the
     * printed output roughly square. Separators are added to
     * mark the boundaries of the puzzle itself and each square.
     * Unknown values are replaced with asterisks.
     * @return The formatted string.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" --------------------------------- \n");
        for (int i = 0; i < this.rows.length; i++) {
            sb.append("| ");
            for (int k = 0; k < 9; k++) {
                int value = this.rows[i].getCell(k).getValue();
                if (value == 0) sb.append("*  ");
                else sb.append(value).append("  ");
                if (k % 3 == 2 && k < 8) sb.append("|  ");
            }
            sb.append("\b|\n");
            if (i %3 == 2 && i < 8) sb.append("|---------------------------------|\n");
        }
        sb.append(" --------------------------------- \n");
        return sb.toString();
    }
}
