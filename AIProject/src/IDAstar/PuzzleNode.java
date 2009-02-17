package IDAstar;

public class PuzzleNode {

    private int[][] puzzle;

    public PuzzleNode(int[][] puzzle) {
        this.puzzle = puzzle;
    }

    public int[][] getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(int[][] puzzle) {
        this.puzzle = puzzle;
    }

    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + (this.puzzle != null ? calculateHashCodeForPuzzle() : 0);
        return hash;
    }

    private int calculateHashCodeForPuzzle() {
        int result = 17;
        int[] puzzleRaw;
        for (int i = 0; i < puzzle.length; i++) {
            puzzleRaw = puzzle[i];
            for (int j = 0; j < puzzleRaw.length; j++) {
                result += result * 37 + puzzle[i][j];
            }
        }
        return result;
    }
}
