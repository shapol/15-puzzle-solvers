package IDAstar;

import java.awt.Point;

public class PuzzleNode implements Comparable {

    private int[][] puzzle;
    private Point spaceCell;
    private int movesFromStart = 0;     /*Node Depth (Optimize to Minimum That Number)*/


    public PuzzleNode(int[][] puzzle, int movesFromStart,Point spaceCell) {

        /*Init The puzzle*/
        this.puzzle = puzzle;

        /*Init The Space Cell postion*/
        this.spaceCell = spaceCell;

       this.movesFromStart = movesFromStart;
    } 
    
   public int compareTo(Object o) {

        PuzzleNode other = (PuzzleNode) o;
        for (int i = 0; i < getPuzzle().length; i++) {
            for (int j = 0; j < getPuzzle().length; j++) {
                int[][] otherPuzzle = other.getPuzzle();
                if (this.getPuzzle()[i][j] != otherPuzzle[i][j]) {
                    return 1; /*Diffrent*/
                }
            }
        }
        return 0; /*The Same*/
    }

    public int getMovesFromStart() {
        return movesFromStart;
    }

    public void setMovesFromStart(int movesFromStart) {
        this.movesFromStart = movesFromStart;
    }

    public int[][] getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(int[][] puzzle) {
        this.setPuzzle(puzzle);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        return (this.compareTo(other) == 0);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + (this.puzzle != null ? calculateHashCodeForPuzzle() : 0);
        return hash;
    }
    
    private int calculateHashCodeForPuzzle(){
        int result = 17;
        int[] puzzleRaw;
        for(int i=0;i<puzzle.length;i++){
           puzzleRaw = puzzle[i];
            for(int j=0;j<puzzleRaw.length;j++){
                result += result*37 + puzzle[i][j];
            }
        }
        return result;
    }

    public Point getSpaceCell() {
        return spaceCell;
    }

    public void setSpaceCell(Point spaceCell) {
        this.spaceCell = spaceCell;
    }
    
    @Override
    public String toString(){
        String result = "";
        int[] puzzleRaw;
        for(int i=0;i<puzzle.length;i++){
            puzzleRaw = puzzle[i];
            result += "| ";
            for(int j=0;j<puzzleRaw.length;j++){
                result += puzzleRaw[j] +" ";
            }
            result += "|\n";
        }
        
        return result;
    }
    
    
}
