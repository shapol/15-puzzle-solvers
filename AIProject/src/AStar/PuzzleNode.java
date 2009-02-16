package AStar;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Vector;

public class PuzzleNode{

    
    
    
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
    
    public LinkedList<PuzzleNode> getMyChilds(){
        
        LinkedList<PuzzleNode> res = new LinkedList<PuzzleNode>();
        while (validMoves.size()>0)
        {
            Point currMove = validMoves.remove();
            PuzzleNode child = new PuzzleNode(makeMoveOnPuzzle(currMove), this, currMove);
            if (!child.isVisitedBefore())
                res.add(child);            
        }
        return res;        
    }
        
    public int getMovesToGoal() {
        return getManahtanDistance(this);
    }
    
    private int getManahtanDistance(PuzzleNode node) {
        
        int destinationI;
        int destinationJ;
        int distance =0;
        
        for (int i=0;i<PuzzleGame.puzzleDimension;i++)
            for (int j=0;j<PuzzleGame.puzzleDimension;j++)
            {
                int currValue = node.puzzle[i][j];
                if (currValue==0)
                {
                    destinationI = PuzzleGame.puzzleDimension-1;
                    destinationJ = PuzzleGame.puzzleDimension-1;
                }
                else
                {                    
                    destinationI = (currValue-1)/PuzzleGame.puzzleDimension;
                    destinationJ = (currValue-1)%PuzzleGame.puzzleDimension;                                        
                }
                distance = distance + Math.abs(i-destinationI) + Math.abs(j-destinationJ);
            }
        
        return distance;                    
    }
    
    @Override
    public boolean equals(Object otherObj) {
        
        if (otherObj == this)
            return true;
                
        PuzzleNode other = (PuzzleNode)otherObj;
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
        if (spaceCell.x + 1 < PuzzleGame.puzzleDimension) {
            result.add(new Point(spaceCell.x + 1, spaceCell.y));
        }
        if (spaceCell.y - 1 >= 0) {
            result.add(new Point(spaceCell.x, spaceCell.y - 1));
        }
        if (spaceCell.y + 1 < PuzzleGame.puzzleDimension) {
            result.add(new Point(spaceCell.x, spaceCell.y + 1));
        }
        return result;
    }

    private int[][] makeMoveOnPuzzle(Point moveToMake) {
        int[][] result = (int[][]) getPuzzle().clone(); /*Copy Current*/
        result[spaceCell.x][spaceCell.y] = getPuzzle()[moveToMake.x][moveToMake.y];
        result[moveToMake.x][moveToMake.y] = 0;
        return result;
    }
            
    private boolean isVisitedBefore() {

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
   
    
    
}
