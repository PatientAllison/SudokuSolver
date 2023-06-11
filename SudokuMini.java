public class SudokuMini {
    private SudokuCell[] mini;
    public SudokuMini(SudokuSquare square, int coordinateWithinSquare, boolean row) {
        this.mini = new SudokuCell[3];
        for (SudokuCell cell: square.getAllCells()) {
            if (row) {
                if (((cell.getY() - 1) % 3) == coordinateWithinSquare) mini[(cell.getX() - 1) % 3] = cell;
            }
            else {
                if (((cell.getX() - 1) % 3) == coordinateWithinSquare) mini[(cell.getY() - 1) % 3] = cell;
            }
        }
    }
    public SudokuCell[] getCells() {
        return this.mini;
    }
}
