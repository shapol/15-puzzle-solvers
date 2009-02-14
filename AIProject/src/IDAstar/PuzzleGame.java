package IDAstar;

import java.awt.Point;
import java.util.Vector;

public class PuzzleGame {

     
    
    private int nodesSearched = 0; /*The number of nodes we searched so far*/

    /* we keep the previous positions and insure we never get there again.
    This keeps us from moving the same tile back and forth ,and forces the moves taken 
    to advance towrds the goal. */
    public static Vector<PuzzleNode> prevPos = new Vector<PuzzleNode>();
    /*In our tree of puzzle States this will hold the solution puzzle Node-State*/
    private PuzzleNode currSolutionNode = null;
    
  

    public PuzzleGame(int[][] puzzle) throws Exception {


    }

 
    private void solveGameIDAStar() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

  

    public void initializePrevPos(){
        prevPos.clear();
    }
    
   
   
}
