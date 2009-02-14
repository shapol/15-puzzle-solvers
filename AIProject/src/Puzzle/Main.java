package Puzzle;

public class Main {

    public static void main(String[] args) {

        int[][][] puzzels = {
            //Puzzle 0
            {{1, 9, 3, 7},
                {4, 0, 6, 15},
                {5, 13, 2, 11},
                {8, 12, 14, 10}
            },
            //Puzzle 1
            {{1, 3, 6, 7},
                {13, 14, 9, 15},
                {4, 0, 8, 11},
                {12, 5, 2, 10}
            },
            //Puzzle 2
            {{1, 14, 6, 7},
                {13, 15, 0, 11},
                {4, 5, 3, 9},
                {12, 2, 8, 10}
            }
        };
        int puzzleNumber = 1;        
        try {
            
            /*Astar Solver*/
       //     AStar.PuzzleGame puzzleGame = new AStar.PuzzleGame(puzzels[puzzleNumber]);
      //      puzzleGame.solveGame();                                    
            
            /*IDAStar Solver*/
            IDAstar.IDAStar idaStar = new IDAstar.IDAStar(puzzels[puzzleNumber]);
            idaStar.solveGame();
           // System.out.println(idaStar.getManahtanDistance(puzzels[2]));
            
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }


    }
}
