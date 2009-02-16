package AStar;

import java.awt.Point;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Vector;

public class PuzzleGame {

    public static int puzzleDimension = 4; /*4X4 Puzzle*/
    public static int maxValue = 15;
        
    private PuzzleNode goalNode; /*Final Goal*/
    
    private PuzzleNode root;   
    LinkedList<PuzzleNode> openList;
    public static LinkedList<PuzzleNode> prevPos = new LinkedList<PuzzleNode>(); /* we keep the previous positions and insure we never get there again. This keeps us from moving the same square back and forth ,and forces the moves taken  to advance towrds the goal. */   
    
    
           
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
               
         /*Create Goal State*/
        int[][] goalPuzzle = {
            {0, 1, 2, 3},
            {4, 5, 6, 7},
            {8, 9, 10, 11},
            {12, 13, 14, 15}
        };
        goalNode = new PuzzleNode(goalPuzzle, null, new java.awt.Point(0, 0));
               
        /*Create Puzzle*/
        this.root = new PuzzleNode(puzzle, null, spaceCell);
        PuzzleGame.prevPos.clear();

        openList = new LinkedList<PuzzleNode>();               
    }
            
    public void solveGame() {
            
        openList.add(root); /*Add Root to Open List*/                
        while (openList.size()>0)
        {                        
            PuzzleNode currSolutionNode = openListExtrectMin();
            if (isDone(currSolutionNode))
            {
                System.out.println("Done");
                return;
            }
            else
            {
                //expend not yet seen childs to open list.
                LinkedList<PuzzleNode> childs = currSolutionNode.getMyChilds();
                for (int i=0;i<childs.size();i++)                
                    openList.add(childs.get(i));                
            }
        }
        System.out.println("Solution Not Found");
    }
            
    private boolean isDone(PuzzleNode currNode) {
        if (currNode.equals(goalNode)) {
            return true;
        }
        return false;
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
        Collections.sort(openList, new SmallerPuzzleNode());        
        PuzzleNode res = openList.remove();
        System.out.println("Extrecting : "+res.getMovesToGoal()+res.getMovesFromStart());
        return res;        
    }

}
