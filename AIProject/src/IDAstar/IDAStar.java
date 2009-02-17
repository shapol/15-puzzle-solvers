package IDAstar;

import java.awt.Point;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

/**
 * This class will implement IDA* algorithm.
 * 
 * Iterative deepening A*.  Find a lower bound on the solution 
 * and then try and verify whether the bound is the correct answer.
 * If the search fails, then the bound was too low and has to be
 * increased.  Repeat the process (iterate) until the bound has been
 * increased (deepened) large enough to find the solution.
 * 
 * @author Tomer Peled & Al Yaros
 */
public class IDAStar {

    private int puzzleDimension; // The dimension of the puzzle    
 
    private int[][] root; // The starting state of the algorithm.   

    // The space position of the parent state
    private int parentSpaceI;
    private int parentSpaceJ;    
    public static boolean _isFinished; // is the algorithm has finished - meanining got to the goal node
    
    /*Statistics variables */
    /****************************************************************************************/
    private int totalNumberOfNodes;
    private int toatlNumberOfNodesExpansions;
    private HashMap<PuzzleNode, Integer> _statesOccurrence;
    private int numberOfDuplicates;
    private int currentDepth;
    /****************************************************************************************/

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
        
        /*Statistics initialization*/
        _isFinished = false;
        totalNumberOfNodes = 0;
        toatlNumberOfNodesExpansions = 0;
        _statesOccurrence = new HashMap<PuzzleNode, Integer>();
        numberOfDuplicates = 0;
        currentDepth = 0;
    }

    /**
     * This function will return the position point of the space in the given puzzle
     * @param puzzle a puzzle matrix
     * @return the Point position of the space in the given puzzle
     */
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
        IDAStarAlgorithm(cloneSquareMatrix(this.root), spacePoint.x, spacePoint.y);
        
        //Run this in order to save the duplicates also
        //IDAStarAlgorithmWithDoubleVerticiesSaves(new PuzzleNode(cloneSquareMatrix(this.root)), spacePoint.x, spacePoint.y);
    }

    public int IDAStarAlgorithm(int[][] root, int spaceI, int spaceJ) {
        _isFinished = false;
        int threshold = h(root);
        int temp;
        long startAlgoDtime = System.currentTimeMillis();
         currentDepth = 0;
        while (!_isFinished && threshold < 100) {
          //  System.out.println("(" + threshold + ")");
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
             currentDepth++;
             System.out.println(currentDepth);
            temp = IDAStartAuxiliary(puzzle, validSpacePoint.x, validSpacePoint.y, (g + 1), threshold);
             currentDepth--;
            if (_isFinished) {
                return temp;
            }
            if (temp < min) {
                min = temp;
            }
             
            //undo move
            makeMove(puzzle, validSpacePoint.x, validSpacePoint.y, spaceI, spaceJ);
        }
        return min;
    }

    /**
     * This two function are actually the same as the above function only that they will measure the duplicates also.
     * notice that we didn't want to mix this operation in the regular running of the algorithm, because we wanted that the time
     * measurement will be the most accurate as possible.
     */
    public int IDAStarAlgorithmWithDoubleVerticiesSaves(PuzzleNode root, int spaceI, int spaceJ) {
        _isFinished = false;
        int threshold = h(root.getPuzzle());
        int temp;
        int currentIterationNumberOfDuplicates = 0;
        while (!_isFinished && threshold < 100) {
            System.out.println("(" + threshold + ")");
            parentSpaceI = spaceI;
            parentSpaceJ = spaceJ;
            temp = IDAStartAuxiliaryWithDoubleVerticiesSaves(root, spaceI, spaceJ, 0, threshold);
            currentIterationNumberOfDuplicates = getStatesOccurences();
            System.out.println("Number of doubled/or more than doubled occurences for this iteration is " + currentIterationNumberOfDuplicates);
            this.numberOfDuplicates += currentIterationNumberOfDuplicates;
            if (_isFinished) {
                System.out.println("Number Of duplicates at the whole algorithm is " + this.numberOfDuplicates);
                return temp;
            }
            if (temp == -1) {
                return -1;
            }
            threshold = temp;
            _statesOccurrence.clear();
        }
        return -1;
    }
    public int IDAStartAuxiliaryWithDoubleVerticiesSaves(PuzzleNode puzzle, int spaceI, int spaceJ, int g, int threshold) {

        totalNumberOfNodes++;
        Integer stateOccurence = _statesOccurrence.get(puzzle);
        if (stateOccurence != null) {
            _statesOccurrence.put(puzzle, stateOccurence + 1);
        } else {
            _statesOccurrence.put(puzzle, 1);
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

    private int getStatesOccurences() {
        Iterator statesIter = _statesOccurrence.values().iterator();
        int result = 0;
        Integer stateOccurence = null;
        while (statesIter.hasNext()) {
            stateOccurence = (Integer) statesIter.next();
            if (stateOccurence > 1) {
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

    /**
     * This function will make a move on the given puzzle
     * 
     * @param puzzle the puzzle matrix
     * @param spaceI the i value of the current space
     * @param spaceJ the j value of the current space
     * @param newSpaceI the i value of the new space place
     * @param newSpaceJ the j value of the new space place
     */
    private void makeMove(int[][] puzzle, int spaceI, int spaceJ, int newSpaceI, int newSpaceJ) {
        puzzle[spaceI][spaceJ] = puzzle[newSpaceI][newSpaceJ];
        puzzle[newSpaceI][newSpaceJ] = 0;
    }

    /*
     * This function will make a move on the puzzle.
     * notice that this function will return new cloned puzzle and that it will be used in the IDAStartAuxiliaryWithDoubleVerticiesSaves
     *function.
     */
    private int[][] makeMoveWithClone(int[][] puzzle, int spaceI, int spaceJ, int newSpaceI, int newSpaceJ) {
        int[][] newPuzzle = cloneSquareMatrix(puzzle);
        newPuzzle[spaceI][spaceJ] = puzzle[newSpaceI][newSpaceJ];
        newPuzzle[newSpaceI][newSpaceJ] = 0;
        return newPuzzle;
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
