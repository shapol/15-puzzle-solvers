package AStar;

import java.awt.Point;
import java.util.Vector;

public class PuzzleGame {

    public static int puzzleDimension = 4; /*4X4 Puzzle*/

    public static int maxValue = 15;
    private PuzzleNode root;
    
    private int nodesSearched = 0; /*The number of nodes we searched so far*/
    
    /* we keep the previous positions and insure we never get there again.
    This keeps us from moving the same square back and forth ,and forces the moves taken 
    to advance towrds the goal. */
    public static Vector<PuzzleNode> prevPos = new Vector<PuzzleNode>();
    private PuzzleNode currSolutionNode = null; /* this will hold the Current Node*/

    private static int[][][] manhatanDistance; /*Matrix of pre-calculated manhatan Distances*/    
    private PuzzleNode goalNode; /*Final Goal*/


    public PuzzleGame(int[][] puzzle) throws Exception {

        /*Validate Size*/
        if ((puzzle.length != puzzleDimension) | (puzzle[0].length != puzzleDimension)) {
            throw new Exception("InValid Game Width != 15");
        }
        
        /*Make Sure it has Space in it And Get It*/
        Point spaceCell = getSpacePoint(puzzle);
        if (spaceCell == null) {
            throw new Exception("where is the space?  :-) ");
        }
        
        /*Create Puzzle*/
        this.root = new PuzzleNode(puzzle, null, spaceCell);                
        PuzzleGame.prevPos.clear();
        
        /*Create Goal State*/
        int[][] goalPuzzle = {
            {0, 1, 2, 3},
            {4, 5, 6, 7},
            {8, 9, 10, 11},
            {12, 13, 14, 15}
        };
        goalNode = new PuzzleNode(goalPuzzle, null, new java.awt.Point(0, 0));
                
        /*Init The Manhatan Distances Matrix*/
        initializeManhatanDistance();
    }

    public boolean isDone(PuzzleNode currNode) {
        if (currNode.compareTo(goalNode) == 0) {
            return true;
        }
        return false;
    }

    public static int getManahtanDistance(PuzzleNode puzzleNode) {
        int distance = 0;
        int[][] puzzle = puzzleNode.getPuzzle();
        int[] puzzleRaw;
        int tValue;
        for (int i = 0; i < puzzle.length; i++) {
            puzzleRaw = puzzle[i];
            for (int j = 0; j < puzzleRaw.length; j++) {
                tValue = puzzle[i][j];
                if (tValue != 0) {
                    distance += manhatanDistance[tValue][i][j];
                }
            }
        }
        return distance;
    }

    
    
    /*Missing*/
    public void solveGame(){
        
    }


    
    
    private Point getSpacePoint(int[][] puzzle) {
        for (int i = 0; i < puzzleDimension; i++) {
            for (int j = 0; j < puzzleDimension; j++) {
                if (puzzle[i][j] == 0) {
                    return new java.awt.Point(i, j);
                }
            }
        }
        return null;
    }

    private void initializeManhatanDistance() {
        PuzzleGame.manhatanDistance = new int[maxValue][puzzleDimension][puzzleDimension];
        for (int value = 0; value < maxValue; value++) {
            for (int i = 0; i < puzzleDimension; i++) {
                for (int j = 0; j < puzzleDimension; j++) {
                    manhatanDistance[value][i][j] = Math.abs(value / puzzleDimension - i) + Math.abs(value % puzzleDimension - j);
                }
            }
        }
    }
}
