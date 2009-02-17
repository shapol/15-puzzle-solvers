package IDAstar;

import Puzzle.PuzzleGame;
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
    private int currentDepth; // This variable will only be used for having information of the current space that the IDA* takes
                                                                //    (the  stack size).
    /****************************************************************************************/
    
    
    public IDAStar(int[][] puzzle) throws Exception {
        puzzleDimension = Puzzle.PuzzleGame.puzzleDimension;
        if ((puzzle.length != puzzleDimension) | (puzzle[0].length != puzzleDimension)) {
            throw new Exception("InValid Game Width != " + puzzleDimension);
        }
        Point spaceCell = PuzzleGame.getSpacePoint(puzzle);
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
     *  This function will calculate the cost function.
     * @param g - the number of moves made from the start state to the current position.
    
     * @param puzzle the puzzle matrix
     * @return the cost of the current state.
     */
    public int f(int g, int[][] puzzle) {
        return g + h(puzzle);
    }
    public int h(int[][] puzzle) {
        return Puzzle.PuzzleGame.getManahtanDistance(puzzle);
    }

    public void solveGame() {
        Point spacePoint = PuzzleGame.getSpacePoint(this.root);
        IDAStarAlgorithm(PuzzleGame.cloneSquareMatrix(this.root), spacePoint.x, spacePoint.y);

    //Run this in order to save the duplicates also
    //IDAStarAlgorithmWithDoubleVerticiesSaves(new PuzzleNode(cloneSquareMatrix(this.root)), spacePoint.x, spacePoint.y);
    }

    /**
     *  This function will run the IDA* algorithm, it will use the IDAStartAuxiliary function for each iteration.
     * @param root the root puzzle for the algorithm
     * @param spaceI the i value of the space of the root puzzle
     * @param spaceJ the j value of the space of the root puzzle
     * @return the depth/number of stpes to the goal node
     */
    public int IDAStarAlgorithm(int[][] root, int spaceI, int spaceJ) {
        _isFinished = false;
        int threshold = h(root);
        int temp;
        currentDepth = 0;
        long startAlgoDtime = System.currentTimeMillis();
        while (!_isFinished && threshold < 100) {
            parentSpaceI = spaceI;
            parentSpaceJ = spaceJ;
            temp = IDAStartAuxiliary(root, spaceI, spaceJ, 0, threshold);
            if (_isFinished) {
                 /* A solution has been found! End the iterations and return the depth of solution. */
                long endAlgoTime = System.currentTimeMillis();
                System.out.println("Run Time : " + (endAlgoTime - startAlgoDtime)+" millisec");
                System.out.println("Solution Depth: "+temp);
                System.out.println("Number of Nodes That has Been expended: " + toatlNumberOfNodesExpansions);
                System.out.println("Number of Nodes That has Been Generated:" + totalNumberOfNodes);                
                return temp;
            }
            
            /* A solution has not been found for this threshold.
             * Increase the threshold to the minimal cost of the last iteration
             * generated nodes which hadn't been expanded..
             */
            threshold = temp;
        }
        return -1;
    }
    /**
     *  This function is an auxiliary function for the IDAStarAlgorithm function.
     * @param puzzle the puzzle matrix
     * @param spaceI the i value of the current puzzle space location
     * @param spaceJ the j value of the current puzzle space location
     * @param g the distance of the current state from the root state.
     * @param threshold the current threshold of the current iteration.
     * @return the depth/number of stpes to the goal node
     */
    public int IDAStartAuxiliary(int[][] puzzle, int spaceI, int spaceJ, int g, int threshold) {

        totalNumberOfNodes++;

        /* Check to see if a solution has been found. */
        if (h(puzzle) == 0) {
            /* Solution found! Recurse back up the tree, ending all further search. */
            _isFinished = true;
            return f(g, puzzle);
        }

        if (f(g, puzzle) > threshold) {
            /* State cannot be solved in this iteration. No further
             * search effort is needed. */
            return f(g, puzzle);
        }

        int min = Integer.MAX_VALUE;
        int temp;
        /* Must now consider the moves in this position. Identify
  	 * all legal moves.  notice that the reverse of the move that took to
         * this state will not be generated with generateValidSpacePoints function.
	 */
        Vector<Point> validSpacePoints = generateValidSpacePoints(spaceI, spaceJ);
        if (validSpacePoints.size() > 0) {
            toatlNumberOfNodesExpansions++;
        }
        for (Point validSpacePoint : validSpacePoints) {
            parentSpaceI = spaceI;
            parentSpaceJ = spaceJ;

            /* Modify state by playing "move" */
            makeMove(puzzle, spaceI, spaceJ, validSpacePoint.x, validSpacePoint.y);
            currentDepth++;
            /* Recurse on the new state.  one more move down
            * the search tree has been made, hence a value of "g+1" is passed to IDAStartAuxiliary.
             * If _isFinished will be updated to true then a solution 
             * has been found and the search is stopped.
             */
            temp = IDAStartAuxiliary(puzzle, validSpacePoint.x, validSpacePoint.y, (g + 1), threshold);
            currentDepth--;
            if (_isFinished) {
            /* A solution has been found. Curtail search 
             * at this node, and pass back the solution flag to the parent state.*/
                return temp;
            }
            if (temp < min) { //update the minimum state cost that have been generated but not expanded.
                min = temp;
            }

            /* Modify state by undoing "move" */
            makeMove(puzzle, validSpacePoint.x, validSpacePoint.y, spaceI, spaceJ);
        }
        
        /* All moves have been considered and no solution was found.
	 * Return the minimum cost for the next iteration.*/
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
        Vector<Point> validSpacePoints = generateValidSpacePoints(spaceI, spaceJ);
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
      
    private Vector<Point> generateValidSpacePoints(int spaceI, int spaceJ) {

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
    /**
     * This function will make a move on the puzzle.
     * notice that this function will return new cloned puzzle and that it will be used in the IDAStartAuxiliaryWithDoubleVerticiesSaves
     *function.
     */
    private int[][] makeMoveWithClone(int[][] puzzle, int spaceI, int spaceJ, int newSpaceI, int newSpaceJ) {
        int[][] newPuzzle = PuzzleGame.cloneSquareMatrix(puzzle);
        newPuzzle[spaceI][spaceJ] = puzzle[newSpaceI][newSpaceJ];
        newPuzzle[newSpaceI][newSpaceJ] = 0;
        return newPuzzle;
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

    
    /**
     * This class will be used only for the statistics measurements. it will be used for saving the dupilcates states in an efficient way.
     * 
     * @author Tomer Peled & Al Yaros
     */
    public class PuzzleNode {

        private int[][] puzzle;

        public PuzzleNode(int[][] puzzle) {
            this.puzzle = puzzle;
        }

        public int[][] getPuzzle() {
            return puzzle;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 11 * hash + (this.puzzle != null ? calculateHashCodeForPuzzle() : 0);
            return hash;
        }
        @Override
        public boolean equals(Object otherObj) {

            if (otherObj == this) {
                return true;
            }
            PuzzleNode other = (PuzzleNode) otherObj;
            for (int i = 0; i < getPuzzle().length; i++) {
                for (int j = 0; j < getPuzzle().length; j++) {
                    int[][] otherPuzzle = other.getPuzzle();
                    if (this.getPuzzle()[i][j] != otherPuzzle[i][j]) {
                        return false;
                    }
                }
            }
            return true;
        }
        private int calculateHashCodeForPuzzle() {
            int result = 17;
            int[] puzzleRaw;
            for (int i = 0; i < puzzle.length; i++) {
                puzzleRaw = puzzle[i];
                for (int j = 0; j < puzzleRaw.length; j++) {
                    result += result * 37 + puzzle[i][j];
                }
            }
            return result;
        }
        
    }
    
}
