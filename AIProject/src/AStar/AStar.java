package AStar;

import java.util.Vector;


public class AStar {

    Vector<PuzzleNode> openList;
    PuzzleNode root;

    public AStar(PuzzleNode root) {
        openList = new Vector<PuzzleNode>();
        this.root = root;        
    }
        
    private int getMinNodes(PuzzleNode first, PuzzleNode second) {
        
        int firstValue = first.getMovesToGoal()+first.getMovesFromStart();
        int secondValue = second.getMovesToGoal()+second.getMovesFromStart();
        
        if (firstValue>secondValue)
            return 2;
        
        if (firstValue<secondValue)
            return 1;
        
        return 0;                        
    }

    
    /*Missing*/
    public void run(){
        
        /*Add Root to Open List*/
        openList.add(root);
        
        
    }
}
