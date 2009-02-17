package AStar;

import Puzzle.PuzzleGame;
import java.awt.Point;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class AStarGameSolver {

    public static int puzzleDimension = Puzzle.PuzzleGame.puzzleDimension;
    public static int maxValue = Puzzle.PuzzleGame.gameSlotsNumber;
    private PuzzleNode root;
    PriorityQueue<PuzzleNode> openList;
    private int addedToOpenCounter=0;
    /*  we keep the previous positions and insure we never get there again. This keeps us from moving the same
    square back and forth ,and forces the moves taken  to advance towrds the goal. */
    public static HashSet<PuzzleNode> prevPos = new HashSet<PuzzleNode>();
    long startTime = 0;
    long endTime = 0;

    public AStarGameSolver(int[][] puzzle) throws Exception {

        /*Validate Size*/
        if ((puzzle.length != puzzleDimension) | (puzzle[0].length != puzzleDimension)) {
            throw new Exception("InValid Game Width");
        }

        /*Make Sure it has Space in it And Get It*/
        Point spaceCell = PuzzleGame.getSpacePoint(puzzle);
        if (spaceCell == null) {
            throw new Exception("where is the space?  :-) ");
        }

        /*Create Puzzle*/
        this.root = new PuzzleNode(puzzle, null, spaceCell);

        AStarGameSolver.prevPos.clear();
        openList = new PriorityQueue<PuzzleNode>(100,new PuzzleNodeRelaxCompartor());
    }

    public void solveGame() {

        startTime = System.currentTimeMillis();
        openList.add(root); /*Add Root to Open List*/
        addedToOpenCounter++;
        while (openList.size() > 0) {
            PuzzleNode currSolutionNode = openListExtrectMin();
            if (isDone(currSolutionNode)) {
                endTime = System.currentTimeMillis();
                printResultInfo(currSolutionNode);
                return;
            } else {
                //expend not yet seen childs to open list.
                LinkedList<PuzzleNode> childs = currSolutionNode.getMyChilds();
                for (int i = 0; i < childs.size(); i++) {
                    openList.add(childs.get(i));
                    addedToOpenCounter++;
                }
            }
        }
        endTime = System.currentTimeMillis();
        printResultInfo(null);
    }

    private boolean isDone(PuzzleNode currNode) {
        return (currNode.getMovesToGoal() == 0);
    }

    private PuzzleNode openListExtrectMin() {
        //Collections.sort(openList, new PuzzleNodeRelaxCompartor());
        PuzzleNode res = openList.remove();
        return res;
    }

    private void printResultInfo(PuzzleNode resultNode) {

        long runTime = endTime - startTime;
        System.out.println("Run Time: " + runTime + " miliSec");

        if (resultNode == null) {
            System.out.println("No Solution Found");
            return;
        }

        System.out.println("Solution Found");
        System.out.println("Solution Depth :" + (resultNode.getNodeDepth()-1));
        System.out.println("Number of Nodes That has Been expended: " + prevPos.size());
        System.out.println("Number of Nodes That has Been Generated: " + addedToOpenCounter);
        
        if (puzzleDimension == 3) {
            System.out.println("Number Of Nodes in The Search Space: 9!/2=181440");
        }
        if (puzzleDimension == 4) {
            System.out.println("Number Of Nodes in The Search Space: 16!/2=Approx " + Math.pow(10, 13));
        }
    }

    private class PuzzleNodeRelaxCompartor implements Comparator {

        public int compare(Object first, Object second) {

            int firstValue = ((PuzzleNode) first).getMovesToGoal() + ((PuzzleNode) first).getMovesFromStart();
            int secondValue = ((PuzzleNode) second).getMovesToGoal() + ((PuzzleNode) second).getMovesFromStart();

            if (firstValue > secondValue) {
                return 1;
            }
            if (firstValue < secondValue) {
                return -1;
            }
            return 0;
        }
    }  

            
}//End of AStarGameSolver Class

