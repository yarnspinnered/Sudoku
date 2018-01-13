/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

/**
 *
 * @author User
 */
public class Sudoku {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       int[][] boardArgs = new int[9][9];
        
        boardArgs[0] = new int[]{5,3,0,0,0,0,0,0,0};
        boardArgs[1] = new int[]{6,0,0,1,9,5,0,0,0};
        boardArgs[2] = new int[]{0,9,8,0,0,0,0,6,0};
        boardArgs[3] = new int[]{8,0,0,0,6,0,0,0,3};
        boardArgs[4] = new int[]{4,0,0,8,0,3,0,0,1};
        boardArgs[5] = new int[]{7,0,0,0,2,0,0,0,6};
        boardArgs[6] = new int[]{0,6,0,0,0,0,2,8,0};
        boardArgs[7] = new int[]{0,0,0,4,1,9,0,0,5};
        boardArgs[8] = new int[]{0,0,0,0,8,0,0,7,9};
        
        Board actualBoard = new Board(boardArgs);
//        System.out.println(actualBoard.checkBoard());
//        actualBoard.solveBoard(0);
        GUI x = new GUI(actualBoard);
        
    }
    
}
