package IDAstar;

import java.awt.Point;
import java.util.HashSet;
import java.util.Vector;

public class IDAStar {

    public static boolean _isFinished;
    private PuzzleNode root;
    private int puzzleDimension;
    private PuzzleNode goalNode;
    private HashSet<PuzzleNode> _pathNodes;
    private int totalNumberOfNodes;
    private int toatlNumberOfNodesExpansions;

    public IDAStar(int[][] puzzle) throws Exception {
        puzzleDimension = Puzzle.PuzzleGame.puzzleDimension;
        if ((puzzle.length != puzzleDimension) | (puzzle[0].length != puzzleDimension)) {
            throw new Exception("InValid Game Width != " + puzzleDimension);
        }
        Point spaceCell = getSpacePoint(puzzle);
        if (spaceCell == null) {
            throw new Exception("where is the space?  :-) ");
        }
        this.root = new PuzzleNode(puzzle, 0, spaceCell);
        int[][] goalPuzzle = {
            {0, 1, 2, 3},
            {4, 5, 6, 7},
            {8, 9, 10, 11},
            {12, 13, 14, 15}
        };
        goalNode = new PuzzleNode(goalPuzzle, 0, new java.awt.Point(0, 0));
        _isFinished = false;
        totalNumberOfNodes = 0;
        toatlNumberOfNodesExpansions = 0;
        _pathNodes = new HashSet<PuzzleNode>();
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

    public int f(PuzzleNode puzzleNode) {
        return g(puzzleNode) + h(puzzleNode);
    }
    public int g(PuzzleNode puzzleNode) {
        return puzzleNode.getMovesFromStart();
    }
    public int h(PuzzleNode puzzleNode) {
        return Puzzle.PuzzleGame.getManahtanDistance(puzzleNode);
    }

    public void solveGame() {
        System.out.println(IDAStarAlgorithm(this.root));
    }
    public int IDAStarAlgorithm(PuzzleNode root) {
        _isFinished = false;
        int threshold = h(root);
        int temp;        
        long startAlgoDtime = System.currentTimeMillis();
        while (!_isFinished && threshold<100) {
            System.out.println("("+threshold+")");
            _pathNodes.add(root);
            temp = IDAStartAuxiliary(root, threshold);
            if (_isFinished) {
                long endAlgoTime = System.currentTimeMillis();
                System.out.println("Time : "+(endAlgoTime-startAlgoDtime));
                System.out.println("Total number of nodes : "+totalNumberOfNodes);
                System.out.println("Total number of node expansions "+toatlNumberOfNodesExpansions);
                return temp;
            }
            if (temp == -1) {
                return -1;
            }
            threshold = temp;
            _pathNodes.clear();
        }
        return -1;
    }
    public int IDAStartAuxiliary(PuzzleNode node, int threshold) {
        
          //     if (isDone(node)) {
        if(h(node)==0){
            _isFinished = true;
            return f(node);
        }  
        
        if (f(node) > threshold) {
            return f(node);
        }    

        int min = Integer.MAX_VALUE;
        int temp;
        Vector<PuzzleNode> childs = generateValidNodes(node);
        totalNumberOfNodes += childs.size();
        if(childs.size()>0){
            toatlNumberOfNodesExpansions++;
        }
        for (PuzzleNode child : childs) {
            _pathNodes.add(child);
            temp = IDAStartAuxiliary(child, threshold);
            if (_isFinished) {
                System.out.println(child);
                System.out.println();
                return temp;
            }
            if (temp < min) {
                min = temp;
            }
           _pathNodes.remove(child);
        }
        return min;
    }

    public boolean isDone(PuzzleNode currNode) {
        if (currNode.compareTo(goalNode) == 0) {
            return true;
        }
        return false;
    }
    
    public PuzzleNode generateNewPuzzleNode(int[][] puzzle, Point spaceCell, Point moveToMake, PuzzleNode parentNode) {
        return new PuzzleNode(makeMoveOnPuzzle(puzzle, spaceCell, moveToMake), parentNode.getMovesFromStart()+1, moveToMake);
    }
    private Vector<PuzzleNode> generateValidNodes(PuzzleNode puzzleNode) {

        int[][] puzzle = puzzleNode.getPuzzle();
        Point spaceCell = puzzleNode.getSpaceCell();

        /*The SpaceCell can move either left , right , up , down*/
        Vector<PuzzleNode> result = new Vector<PuzzleNode>();
        Point point;
        PuzzleNode tPuzzleNode;

        if (spaceCell.x - 1 >= 0) {
            point = new Point(spaceCell.x - 1, spaceCell.y);
            tPuzzleNode = generateNewPuzzleNode(puzzle, spaceCell, point, puzzleNode);
            if (!_pathNodes.contains(tPuzzleNode)) {
                result.add(tPuzzleNode);
            }
        }
        if (spaceCell.x + 1 < puzzleDimension) {
            point = new Point(spaceCell.x + 1, spaceCell.y);
            tPuzzleNode = generateNewPuzzleNode(puzzle, spaceCell, point, puzzleNode);
            if (!_pathNodes.contains(tPuzzleNode)) {
                result.add(tPuzzleNode);
            }
        }
        if (spaceCell.y - 1 >= 0) {
            point = new Point(spaceCell.x, spaceCell.y - 1);
            tPuzzleNode = generateNewPuzzleNode(puzzle, spaceCell, point, puzzleNode);
            if (!_pathNodes.contains(tPuzzleNode)) {
                result.add(tPuzzleNode);
            }
        }
        if (spaceCell.y + 1 < puzzleDimension) {
            point = new Point(spaceCell.x, spaceCell.y + 1);
            tPuzzleNode = generateNewPuzzleNode(puzzle, spaceCell, point, puzzleNode);
            if (!_pathNodes.contains(tPuzzleNode)) {
                result.add(tPuzzleNode);
            }
        }
        return result;
    }
    private int[][] makeMoveOnPuzzle(int[][] puzzle, Point spaceCell, Point moveToMake) {
        int[][] result = clonePuzzle(puzzle);
        result[spaceCell.x][spaceCell.y] = puzzle[moveToMake.x][moveToMake.y];
        result[moveToMake.x][moveToMake.y] = 0;
        return result;
    }
    private int[][] clonePuzzle(int[][] puzzle) {
        int[] puzzleRaw;
        int[][] newPuzzle = new int[puzzleDimension][puzzleDimension];
        for (int i = 0; i < puzzle.length; i++) {
            puzzleRaw = puzzle[i];
            for (int j = 0; j < puzzleRaw.length; j++) {
                newPuzzle[i][j] = puzzle[i][j];
            }
        }
        return newPuzzle;
    }
   
}
