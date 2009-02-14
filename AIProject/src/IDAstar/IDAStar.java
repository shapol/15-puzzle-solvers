package IDAstar;

import java.awt.Point;
import java.util.HashMap;
import java.util.Vector;

public class IDAStar {

    private static int[][][] manhatanDistance;
    private boolean _isFinished;
    private PuzzleNode root;
    public static final int gameWidth = 15;
    public static int puzzleDimension = 4;
    private PuzzleNode goalNode;
    private HashMap<PuzzleNode, Integer> _pathNodes;
    private Vector<PuzzleNode> _childs;

    public IDAStar(int[][] puzzle) throws Exception {
        if ((puzzle.length != puzzleDimension) | (puzzle[0].length != puzzleDimension)) {
            throw new Exception("InValid Game Width != " + puzzleDimension);
        }
        Point spaceCell = getSpacePoint(puzzle);
        if (spaceCell == null) {
            throw new Exception("where is the space?  :-) ");
        }
        this.root = new PuzzleNode(puzzle, null, spaceCell);
        int[][] goalPuzzle = {
            {0, 1, 2, 3},
            {4, 5, 6, 7},
            {8, 9, 10, 11},
            {12, 13, 14, 15}
        };
        goalNode = new PuzzleNode(goalPuzzle, null, new java.awt.Point(0, 0));
        initializeManhatanDistance();
        _isFinished = false;
        _pathNodes = new HashMap<PuzzleNode, Integer>();
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
        return getManahtanDistance(puzzleNode);
    }

    public void solveGame() {
        System.out.println(IDA(this.root));
    }

    public int IDA(PuzzleNode root) {
        _isFinished = false;
        int threshold = h(root);
        PuzzleNode node;
        int temp;
        while (!_isFinished) {
            _pathNodes.put(root, 0);
            temp = DFAux(root, threshold);
            if (_isFinished) {
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

    public int DFAux(PuzzleNode node, int threshold) {
        if (isDone(node)) {
            _isFinished = true;
            return f(node);
        }

        if (f(node) > threshold) {
            return f(node);
        }

        int min = Integer.MAX_VALUE;
        int temp;
        Vector<PuzzleNode> childs = generateValidNodes(node);
        for (PuzzleNode child : childs) {
            _pathNodes.put(child, 0);
            //        System.out.println(child);
            //          System.out.println();
            temp = DFAux(child, threshold);
            if (_isFinished) {
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

    public void initializeManhatanDistance() {
        manhatanDistance = new int[gameWidth + 1][puzzleDimension][puzzleDimension];
        for (int value = 0; value <= gameWidth; value++) {
            for (int i = 0; i < puzzleDimension; i++) {
                for (int j = 0; j < puzzleDimension; j++) {
                    manhatanDistance[value][i][j] = Math.abs(value / puzzleDimension - i) + Math.abs(value % puzzleDimension - j);
                }
            }
        }
    }

    public static int getManahtanDistance(PuzzleNode puzzleNode) {
        int distance = 0;
        int[][] puzzle = puzzleNode.getPuzzle();
        int[] puzzleRaw;
        int tValue;
        for (int i = 0; i < puzzle.length; i++) {
            puzzleRaw = puzzle[i];
            for (int j = 0; j < puzzleRaw.length; j++) {
                tValue = puzzle[i][j];
                if (tValue != 0) {
                    distance += manhatanDistance[tValue][i][j];
                }

            }
        }
        return distance;
    }

    public static int getManahtanDistance(int[][] puzzle) {
        int distance = 0;
        int[] puzzleRaw;
        int tValue;
        for (int i = 0; i < puzzle.length; i++) {
            puzzleRaw = puzzle[i];
            for (int j = 0; j < puzzleRaw.length; j++) {
                tValue = puzzle[i][j];
                if (tValue != 0) {
                    distance += manhatanDistance[tValue][i][j];
                }
            }
        }
        return distance;
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
            if (!_pathNodes.containsKey(tPuzzleNode)) {
                result.add(tPuzzleNode);
            }
        }
        if (spaceCell.x + 1 < puzzleDimension) {
            point = new Point(spaceCell.x + 1, spaceCell.y);
            tPuzzleNode = generateNewPuzzleNode(puzzle, spaceCell, point, puzzleNode);
            if (!_pathNodes.containsKey(tPuzzleNode)) {
                result.add(tPuzzleNode);
            }
        }
        if (spaceCell.y - 1 >= 0) {
            point = new Point(spaceCell.x, spaceCell.y - 1);
            tPuzzleNode = generateNewPuzzleNode(puzzle, spaceCell, point, puzzleNode);
            if (!_pathNodes.containsKey(tPuzzleNode)) {
                result.add(tPuzzleNode);
            }
        }
        if (spaceCell.y + 1 < puzzleDimension) {
            point = new Point(spaceCell.x, spaceCell.y + 1);
            tPuzzleNode = generateNewPuzzleNode(puzzle, spaceCell, point, puzzleNode);
            if (!_pathNodes.containsKey(tPuzzleNode)) {
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

    public PuzzleNode generateNewPuzzleNode(int[][] puzzle, Point spaceCell, Point moveToMake, PuzzleNode parentNode) {
        return new PuzzleNode(makeMoveOnPuzzle(puzzle, spaceCell, moveToMake), parentNode, moveToMake);
    }
}
