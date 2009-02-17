package IDAstar;

import java.awt.Point;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class IDAStar {

    public static boolean _isFinished;
    private int[][] root;
    private int puzzleDimension;
    private int totalNumberOfNodes;
    private int toatlNumberOfNodesExpansions;
    private int parentSpaceI;
    private int parentSpaceJ;
    private HashMap<PuzzleNode,Integer> _statesOccurences;

    public IDAStar(int[][] puzzle) throws Exception {
        puzzleDimension = Puzzle.PuzzleGame.puzzleDimension;
        if ((puzzle.length != puzzleDimension) | (puzzle[0].length != puzzleDimension)) {
            throw new Exception("InValid Game Width != " + puzzleDimension);
        }
        Point spaceCell = getSpacePoint(puzzle);
        if (spaceCell == null) {
            throw new Exception("where is the space?  :-) ");
        }
        this.root = puzzle;
        _isFinished = false;
        totalNumberOfNodes = 0;
        toatlNumberOfNodesExpansions = 0;
        _statesOccurences = new HashMap<PuzzleNode,Integer>();
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

    public int f(int g, int[][] puzzle) {
        return g + h(puzzle);
    }

    public int h(int[][] puzzle) {
        return Puzzle.PuzzleGame.getManahtanDistance(puzzle);
    }

    public void solveGame() {
        Point spacePoint = getSpacePoint(this.root);
        System.out.println(IDAStarAlgorithm(cloneSquareMatrix(this.root), spacePoint.x, spacePoint.y));
       IDAStarAlgorithmWithDoubleVerticiesSaves(new PuzzleNode(cloneSquareMatrix(this.root)),spacePoint.x, spacePoint.y);
    }

    public int IDAStarAlgorithm(int[][] root, int spaceI, int spaceJ) {
        _isFinished = false;
        int threshold = h(root);
        int temp;
        long startAlgoDtime = System.currentTimeMillis();
        while (!_isFinished && threshold < 100) {
            System.out.println("(" + threshold + ")");
            parentSpaceI = spaceI;
            parentSpaceJ = spaceJ;
            temp = IDAStartAuxiliary(root, spaceI, spaceJ, 0, threshold);
            if (_isFinished) {
                long endAlgoTime = System.currentTimeMillis();
                System.out.println("Time : " + (endAlgoTime - startAlgoDtime));
                System.out.println("Total number of nodes : " + totalNumberOfNodes);
                System.out.println("Total number of node expansions " + toatlNumberOfNodesExpansions);
                return temp;
            }
            if (temp == -1) {
                return -1;
            }
            threshold = temp;
        }
        return -1;
    }
    public int IDAStartAuxiliary(int[][] puzzle, int spaceI, int spaceJ, int g, int threshold) {

        totalNumberOfNodes++;
        
        if (h(puzzle) == 0) {
            _isFinished = true;
            return f(g, puzzle);
        }

        if (f(g, puzzle) > threshold) {
            return f(g, puzzle);
        }

        int min = Integer.MAX_VALUE;
        int temp;
        Vector<Point> validSpacePoints = generateValidNodes(spaceI, spaceJ);
        if (validSpacePoints.size() > 0) {
            toatlNumberOfNodesExpansions++;
        }
        for (Point validSpacePoint : validSpacePoints) {
            parentSpaceI = spaceI;
            parentSpaceJ = spaceJ;

            makeMove(puzzle, spaceI, spaceJ, validSpacePoint.x, validSpacePoint.y);
            temp = IDAStartAuxiliary(puzzle, validSpacePoint.x, validSpacePoint.y, (g + 1), threshold);
            if (_isFinished) {
                //        System.out.println(child);
                //     System.out.println();
                return temp;
            }
            if (temp < min) {
                min = temp;
            }
            undoMove(puzzle, validSpacePoint.x, validSpacePoint.y, spaceI, spaceJ);
        }
        return min;
    }
    
     public int IDAStarAlgorithmWithDoubleVerticiesSaves(PuzzleNode root, int spaceI, int spaceJ) {
        _isFinished = false;
        int threshold = h(root.getPuzzle());
        int temp;
        while (!_isFinished && threshold < 100) {
            System.out.println("(" + threshold + ")");
            parentSpaceI = spaceI;
            parentSpaceJ = spaceJ;
            temp = IDAStartAuxiliaryWithDoubleVerticiesSaves(root, spaceI, spaceJ, 0, threshold);
            System.out.println("Number of doubled/or more than doubled occurences for this iteration is "+getStatesOccurences());
            if (_isFinished) {
                return temp;
            }
            if (temp == -1) {
                return -1;
            }
            threshold = temp;
            _statesOccurences.clear();
        }
        return -1;
    }
    public int IDAStartAuxiliaryWithDoubleVerticiesSaves(PuzzleNode puzzle, int spaceI, int spaceJ, int g, int threshold) {

        totalNumberOfNodes++;
        Integer stateOccurence = _statesOccurences.get(puzzle);
        if(stateOccurence!=null){
             _statesOccurences.put(puzzle,stateOccurence+1);
        }
        else{
             _statesOccurences.put(puzzle,1);
        }
       
        if (h(puzzle.getPuzzle()) == 0) {
            _isFinished = true;
            return f(g, puzzle.getPuzzle());
        }

        if (f(g, puzzle.getPuzzle()) > threshold) {
            return f(g, puzzle.getPuzzle());
        }

        int min = Integer.MAX_VALUE;
        int temp;
        Vector<Point> validSpacePoints = generateValidNodes(spaceI, spaceJ);
        if (validSpacePoints.size() > 0) {
            toatlNumberOfNodesExpansions++;
        }
        for (Point validSpacePoint : validSpacePoints) {
            parentSpaceI = spaceI;
            parentSpaceJ = spaceJ;

            int[][] newPuzzle = makeMoveWithClone(puzzle.getPuzzle(), spaceI, spaceJ, validSpacePoint.x, validSpacePoint.y);
            PuzzleNode newPuzzleNode = new PuzzleNode(newPuzzle);
            temp = IDAStartAuxiliaryWithDoubleVerticiesSaves(newPuzzleNode, validSpacePoint.x, validSpacePoint.y, (g + 1), threshold);
            if (_isFinished) {
                return temp;
            }
            if (temp < min) {
                min = temp;
            }
        }
        return min;
    }
    
    private int getStatesOccurences(){
        Iterator statesIter = _statesOccurences.values().iterator();
        int result = 0;
        Integer stateOccurence = null;
        while(statesIter.hasNext()){
            stateOccurence = (Integer)statesIter.next();
            if(stateOccurence>1){
                result += stateOccurence;
            }            
        }
        return result;
    }
    
    private Vector<Point> generateValidNodes(int spaceI, int spaceJ) {

        /*The SpaceCell can move either left , right , up , down*/
        Vector<Point> result = new Vector<Point>();
        Point spacePoint;

        if (spaceI - 1 >= 0 && !(parentSpaceI == spaceI - 1 && parentSpaceJ == spaceJ)) {
            spacePoint = new Point(spaceI - 1, spaceJ);
            result.add(spacePoint);
        }
        if (spaceI + 1 < puzzleDimension && !(parentSpaceI == spaceI + 1 && parentSpaceJ == spaceJ)) {
            spacePoint = new Point(spaceI + 1, spaceJ);
            result.add(spacePoint);
        }
        if (spaceJ - 1 >= 0 && !(parentSpaceI == spaceI && parentSpaceJ == spaceJ - 1)) {
            spacePoint = new Point(spaceI, spaceJ - 1);
            result.add(spacePoint);
        }
        if (spaceJ + 1 < puzzleDimension && !(parentSpaceI == spaceI && parentSpaceJ == spaceJ + 1)) {
            spacePoint = new Point(spaceI, spaceJ + 1);
            result.add(spacePoint);
        }
        return result;
    }

    private void makeMove(int[][] puzzle, int spaceI, int spaceJ, int newSpaceI, int newSpaceJ) {
        puzzle[spaceI][spaceJ] = puzzle[newSpaceI][newSpaceJ];
        puzzle[newSpaceI][newSpaceJ] = 0;
    }
    
     private int[][] makeMoveWithClone(int[][] puzzle, int spaceI, int spaceJ, int newSpaceI, int newSpaceJ) {
        int[][] newPuzzle = cloneSquareMatrix(puzzle);
         newPuzzle[spaceI][spaceJ] = puzzle[newSpaceI][newSpaceJ];
        newPuzzle[newSpaceI][newSpaceJ] = 0;
        return newPuzzle;
    }

    private void undoMove(int[][] puzzle, int spaceI, int spaceJ, int oldSpaceI, int oldSpaceJ) {
        puzzle[spaceI][spaceJ] = puzzle[oldSpaceI][oldSpaceJ];
        puzzle[oldSpaceI][oldSpaceJ] = 0;
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
