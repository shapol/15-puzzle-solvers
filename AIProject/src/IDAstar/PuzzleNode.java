package IDAstar;

import java.awt.Point;

public class PuzzleNode implements Comparable {

    private PuzzleNode theParent = null; /*This Node Parent*/

    private int[][] puzzle;
    private Point spaceCell;
    private int movesFromStart = 0;     /*Node Depth (Optimize to Minimum That Number)*/

    private int movesToGoal = 0;        /*Estimate based on The heuristic*/

    public PuzzleNode(int[][] puzzle, PuzzleNode theParent, Point spaceCell) {

        /*Init The puzzle*/
        this.puzzle = puzzle;

        /*Init The Current Parent*/
        this.theParent = theParent;

        /*Init The Space Cell postion*/
        this.spaceCell = spaceCell;

        /*Update Current movesFromStart*/
        if (this.theParent != null) {
            movesFromStart = this.theParent.movesFromStart + 1;
        }
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

 
    public boolean isVisitedBefore() {

        /*If its the solution Then its ok to stuck on it*/
        if (this.movesToGoal == 0) {
            return false;
        }

        boolean isVisited = PuzzleGame.prevPos.contains(this);
        if (!isVisited) {
            PuzzleGame.prevPos.add(this); /*Not visited Then add it And return False*/
            return false;
        } else {
            return true; /*Visited Before*/
        }    
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
