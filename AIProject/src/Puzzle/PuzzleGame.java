package Puzzle;

import java.awt.Point;
import java.util.Vector;

public class PuzzleGame {

    public static enum Algorithm {
        AStar, IDAStar
    }
    
    private PuzzleNode root;
    private final int gameWidth = 15;
    private int nodesSearched = 0; /*The number of nodes we searched so far*/

    /* we keep the previous positions and insure we never get there again.
    This keeps us from moving the same tile back and forth ,and forces the moves taken 
    to advance towrds the goal. Each time we do binary search on it */
    public static Vector<PuzzleNode> prevPos = new Vector<PuzzleNode>();
    
    /*In our tree of puzzle States this will hold the solution puzzle Node-State*/
    private PuzzleNode currSolutionNode = null;

    public PuzzleGame(int[][] puzzle, Point spaceCell) throws Exception {

        if ((puzzle.length != gameWidth) | (puzzle[0].length != gameWidth)) {
            throw new Exception("InValid Game Width != 15");
        }
        this.root = new PuzzleNode(puzzle, null, spaceCell);
    }

    public void solveGame(Algorithm algo) {

        switch (algo) {
            case AStar:
                System.out.println("Solving With AStar");
                solveGameAStar();
                break;

            case IDAStar:
                System.out.println("Solving With IDAStar");
                solveGameIDAStar();
                break;

            default:
                System.out.println("Algorithm Not Found");
                break;
        }
    }

    private void solveGameAStar() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void solveGameIDAStar() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private boolean isDone(PuzzleNode currNode){
        throw new UnsupportedOperationException("Not supported yet.");
    }
        
}
