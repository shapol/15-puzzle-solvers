package Puzzle;

import java.awt.Point;
import java.util.Vector;

public class PuzzleNode implements Comparable{

    private PuzzleNode theParent = null; /*This Node Parent*/

    private int[][] puzzle;
    private Point spaceCell;
    private int movesFromStart = 0;     /*Node Depth (Optimize to Minimum That Number)*/ 
    private int movesToGoal = 0;        /*Estimate based on The heuristic*/
    private Vector<Point> validMoves;   /*Space Cell can (Valid Move) move to any of this Points*/


    public PuzzleNode(int[][] puzzle, PuzzleNode theParent, Point spaceCell) {

        /*Init The puzzle*/
        this.puzzle = puzzle;

        /*Init The Current Parent*/
        this.theParent = theParent;

        /*Init The Space Cell postion*/
        this.spaceCell = spaceCell;

        /*Calculate Current Valid moves*/
        this.validMoves = generateValidMoves();

        /*Update Current movesFromStart*/
        if (this.theParent != null) {
            movesFromStart = this.theParent.movesFromStart + 1;
        }

        /*Estimate moves to goal from current State*/
        this.movesToGoal = getMovesToGoal();
    }

    private Vector<Point> generateValidMoves() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    public Vector<PuzzleNode> expandNode(){
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    public int getMovesToGoal() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public int compareTo(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getMovesFromStart() {
        return movesFromStart;
    }

    public void setMovesFromStart(int movesFromStart) {
        this.movesFromStart = movesFromStart;
    }

    public void setMovesToGoal(int movesToGoal) {
        this.movesToGoal = movesToGoal;
    }

}
