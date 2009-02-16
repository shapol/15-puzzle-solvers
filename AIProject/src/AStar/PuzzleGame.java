package AStar;

import java.awt.Point;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Vector;

public class PuzzleGame {

    public static int puzzleDimension = Puzzle.PuzzleGame.puzzleDimension;
    public static int maxValue = Puzzle.PuzzleGame.gameSlotsNumber;
    
    private PuzzleNode root;   
    LinkedList<PuzzleNode> openList;
    public static LinkedList<PuzzleNode> prevPos = new LinkedList<PuzzleNode>(); /* we keep the previous positions and insure we never get there again. This keeps us from moving the same square back and forth ,and forces the moves taken  to advance towrds the goal. */   
    
    
           
    public PuzzleGame(int[][] puzzle) throws Exception {

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
        return (currNode.getMovesToGoal()==0);
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
        return res;        
    }

}
