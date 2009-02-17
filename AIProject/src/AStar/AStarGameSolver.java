package AStar;

import java.awt.Point;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;

public class AStarGameSolver {

    public static int puzzleDimension = Puzzle.PuzzleGame.puzzleDimension;
    public static int maxValue = Puzzle.PuzzleGame.gameSlotsNumber;
    private PuzzleNode root;
    LinkedList<PuzzleNode> openList;
    /*  we keep the previous positions and insure we never get there again. This keeps us from moving the same
    square back and forth ,and forces the moves taken  to advance towrds the goal. */
    private static HashSet<PuzzleNode> prevPos = new HashSet<PuzzleNode>();
    long startTime = 0;
    long endTime = 0;

    public AStarGameSolver(int[][] puzzle) throws Exception {

        /*Validate Size*/
        if ((puzzle.length != puzzleDimension) | (puzzle[0].length != puzzleDimension)) {
            throw new Exception("InValid Game Width");
        }

        /*Make Sure it has Space in it And Get It*/
        Point spaceCell = getSpacePoint(puzzle);
        if (spaceCell == null) {
            throw new Exception("where is the space?  :-) ");
        }

        /*Create Puzzle*/
        this.root = new PuzzleNode(puzzle, null, spaceCell);

        AStarGameSolver.prevPos.clear();
        openList = new LinkedList<PuzzleNode>();
    }

    public void solveGame() {

        startTime = System.currentTimeMillis();
        openList.add(root); /*Add Root to Open List*/
        while (openList.size() > 0) {
            PuzzleNode currSolutionNode = openListExtrectMin();
            if (isDone(currSolutionNode)) {
                endTime = System.currentTimeMillis();
                printResultInfo(currSolutionNode);
                return;
            } else {
                //expend not yet seen childs to open list.
                LinkedList<PuzzleNode> childs = currSolutionNode.getMyChilds();
                for (int i = 0; i < childs.size(); i++) {
                    openList.add(childs.get(i));
                }
            }
        }
        endTime = System.currentTimeMillis();
        printResultInfo(null);
    }

    private boolean isDone(PuzzleNode currNode) {
        return (currNode.getMovesToGoal() == 0);
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

    private PuzzleNode openListExtrectMin() {
        Collections.sort(openList, new PuzzleNodeRelaxCompartor());
        PuzzleNode res = openList.remove();
        return res;
    }

    private void printResultInfo(PuzzleNode resultNode) {

        long runTime = endTime - startTime;
        System.out.println("Run Time: " + runTime + " miliSec");

        if (resultNode == null) {
            System.out.println("No Solution Found");
            return;
        }

        System.out.println("Solution Found");
        System.out.println("Solution Depth :" + resultNode.getNodeDepth());
        System.out.println("Number of Nodes That has Been expended: " + prevPos.size());
        if (puzzleDimension == 3) {
            System.out.println("Number Of Nodes in The Search Space: 9!/2=181440");
        }
        if (puzzleDimension == 4) {
            System.out.println("Number Of Nodes in The Search Space: 16!/2=Approx " + Math.pow(10, 13));
        }
    }

    private class PuzzleNodeRelaxCompartor implements Comparator {

        public int compare(Object first, Object second) {

            int firstValue = ((PuzzleNode) first).getMovesToGoal() + ((PuzzleNode) first).getMovesFromStart();
            int secondValue = ((PuzzleNode) second).getMovesToGoal() + ((PuzzleNode) second).getMovesFromStart();

            if (firstValue > secondValue) {
                return 1;
            }
            if (firstValue < secondValue) {
                return -1;
            }
            return 0;
        }
    }

    private class PuzzleNode {

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
                node = node.theParent;
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
            int[][] result = cloneSquareMatrix(this.getPuzzle());
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

        private int[][] cloneSquareMatrix(int[][] matrix) {
            int[][] result = new int[matrix.length][matrix.length];
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix.length; j++) {
                    result[i][j] = matrix[i][j];
                }
            }
            return result;
        }
    }
}//End of AStarGameSolver Class

