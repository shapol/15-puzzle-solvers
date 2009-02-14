package BFS;

import Puzzle.PuzzleGame;
import Puzzle.PuzzleNode;
import java.util.Vector;

public class IDAStar {

    private PuzzleGame puzzleGame;
    private boolean _isFinished;

    public IDAStar(PuzzleGame puzzleGame) {
        this.puzzleGame = puzzleGame;
        _isFinished = false;
    }

    public int DFAux(PuzzleNode node, int threshold) {
        if (f(node) > threshold) {
            return f(node);
        }
        //  if(puzzleGame.isDone(node)){
        //       return f(node);
        //    }
        int min = Integer.MAX_VALUE;
        int temp;
        Vector<PuzzleNode> childs = node.expandNode();
        for (PuzzleNode child : childs) {
            temp = DFAux(child, threshold);
            if (this.puzzleGame.isDone(child)) {
                _isFinished = true;
                return temp;
            }
            if (temp < min) {
                min = temp;
            }
        }
        return min;
    }

    public int f(PuzzleNode puzzleNode) {
        return puzzleNode.getMovesFromStart() + puzzleNode.getMovesToGoal();
    }
    
    public int g(PuzzleNode puzzleNode){
        return puzzleNode.getMovesFromStart();
    }

    public int h(PuzzleNode puzzleNode) {
        return PuzzleGame.getManahtanDistance(puzzleNode);
    }

    public int ida(PuzzleNode root) {
        _isFinished = false;
        int threshold = h(root);
        PuzzleNode node;
        int temp;
        while (!_isFinished) {
            temp = DFAux(root, threshold);
            if (_isFinished) {
                return temp;
            }
            if (temp == -1) {
                return -1;
            }
            threshold = temp;
        }
        return -1;
    }
}
