/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Puzzle;

import AStar.PuzzleGame;
import AStar.PuzzleGame.Algorithm;

/**
 *
 * @author USER
 */
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
        int puzzleNumber = 0;
        try {
            PuzzleGame puzzleGame = new PuzzleGame(puzzels[puzzleNumber]);
            puzzleGame.solveGame(Algorithm.AStar);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }


    }
    
}
