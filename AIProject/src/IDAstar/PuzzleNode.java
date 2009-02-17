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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + (this.puzzle != null ? calculateHashCodeForPuzzle() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object otherObj) {

        if (otherObj == this) {
            return true;
        }
        PuzzleNode other = (PuzzleNode) otherObj;
        for (int i = 0; i < getPuzzle().length; i++) {
            for (int j = 0; j < getPuzzle().length; j++) {
                int[][] otherPuzzle = other.getPuzzle();
                if (this.getPuzzle()[i][j] != otherPuzzle[i][j]) {
                    return false;
                }
            }
        }
        return true;
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
