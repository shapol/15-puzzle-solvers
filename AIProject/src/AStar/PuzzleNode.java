package AStar;

import java.awt.Point;
import java.util.LinkedList;

public class PuzzleNode {

    private PuzzleNode theParent = null; /*This Node Parent*/

    private int[][] puzzle;
    private Point spaceCell;
    private int movesFromStart = 0;     /*Steps Made Up To Here (Optimize to Minimum That Number)*/

    private int movesToGoal = 0;        /*Estimate based on The heuristic*/

    private LinkedList<Point> validMoves;   /*Space Cell can (Valid Move) move to any of this Points*/


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

    public LinkedList<PuzzleNode> getMyChilds() {

        LinkedList<PuzzleNode> res = new LinkedList<PuzzleNode>();
        while (validMoves.size() > 0) {
            Point currMove = validMoves.remove();
            PuzzleNode child = new PuzzleNode(makeMoveOnPuzzle(currMove), this, currMove);
            if (!child.isVisitedBefore()) {
                res.add(child);
            }
        }
        return res;
    }

    public int getMovesToGoal() {
        return getManahtanDistance(this);
    }

    private int getManahtanDistance(PuzzleNode node) {
        return Puzzle.PuzzleGame.getManahtanDistance(node.getPuzzle());
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (this.puzzle != null ? calculateHashCodeForPuzzle() : 0);
        return hash;
    }

    private int calculateHashCodeForPuzzle() {
        int result = 17;
        int[] puzzleRaw;
        for (int i = 0; i < this.puzzle.length; i++) {
            puzzleRaw = this.puzzle[i];
            for (int j = 0; j < puzzleRaw.length; j++) {
                result += result * 37 + this.puzzle[i][j];
            }
        }
        return result;
    }

    public int[][] getPuzzle() {
        return puzzle;
    }

    public int getNodeDepth() {
        int res = 0;
        PuzzleNode node = this;
        while (node != null) {
            node = node.getTheParent();
            res++;
        }
        return res;
    }

    public int getMovesFromStart() {
        return movesFromStart;
    }

    public LinkedList<Point> getValidMoves() {
        return validMoves;
    }

    private LinkedList<Point> generateValidMoves() {

        /*The SpaceCell can move either left , right , up , down*/
        LinkedList<Point> result = new LinkedList<Point>();

        if (spaceCell.x - 1 >= 0) {
            result.add(new Point(spaceCell.x - 1, spaceCell.y));
        }
        if (spaceCell.x + 1 < AStarGameSolver.puzzleDimension) {
            result.add(new Point(spaceCell.x + 1, spaceCell.y));
        }
        if (spaceCell.y - 1 >= 0) {
            result.add(new Point(spaceCell.x, spaceCell.y - 1));
        }
        if (spaceCell.y + 1 < AStarGameSolver.puzzleDimension) {
            result.add(new Point(spaceCell.x, spaceCell.y + 1));
        }
        return result;
    }

    private int[][] makeMoveOnPuzzle(Point moveToMake) {
        int[][] result = Puzzle.PuzzleGame.cloneSquareMatrix(this.getPuzzle());
        result[spaceCell.x][spaceCell.y] = getPuzzle()[moveToMake.x][moveToMake.y];
        result[moveToMake.x][moveToMake.y] = 0;
        return result;
    }

    private boolean isVisitedBefore() {

        /*If its the solution Then its ok to stuck on it*/
        if (this.movesToGoal == 0) {
            return false;
        }

        boolean isVisited = AStarGameSolver.prevPos.contains(this);
        if (!isVisited) {
            AStarGameSolver.prevPos.add(this); /*Not visited Then add it And return False*/
            return false;
        } else {
            return true; /*Visited Before*/
        }
    }
    
    public PuzzleNode getTheParent() {
        return theParent;
    }

}
