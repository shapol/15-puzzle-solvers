package AStar;

import java.util.Vector;


public class AStar {

    Vector<PuzzleNode> openList;

    public AStar() {
        openList = new Vector<PuzzleNode>();
    }
    
    
    private int sortNodes(PuzzleNode first, PuzzleNode second) {
        
        int firstValue = first.getMovesToGoal()+first.getMovesFromStart();
        int secondValue = second.getMovesToGoal()+second.getMovesFromStart();
        
        if (firstValue>secondValue)
            return 1;
        if (firstValue<secondValue)
            return -1;
        return 0;                        
    }

    
}
