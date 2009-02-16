package AStar;

import java.util.Comparator;


public class SmallerPuzzleNode implements Comparator {

    public int compare(Object first, Object second) {
        
        int firstValue = ((PuzzleNode)first).getMovesToGoal()+((PuzzleNode)first).getMovesFromStart();
        int secondValue = ((PuzzleNode)second).getMovesToGoal()+((PuzzleNode)second).getMovesFromStart();
        
        if (firstValue>secondValue)
            return -1;
        
        if (firstValue<secondValue)
            return 1;
        
        return 0;    
    }

}
