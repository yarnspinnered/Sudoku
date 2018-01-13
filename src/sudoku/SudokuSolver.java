/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.CountDownLatch;

/**
 *
 * @author j
 */
public class SudokuSolver implements Runnable{
    private final Board solvingBoard;
    private CountDownLatch latch;
    private boolean[] solved;
    
    SudokuSolver(Board solvingBoard){
        this.solvingBoard = solvingBoard;
    }
    
    SudokuSolver(Board solvingBoard, CountDownLatch latch, boolean[] solved){
        this.solvingBoard = solvingBoard;
        this.latch = latch;
        this.solved = solved;
    }
    
/*  We shuffle an array of 1 to 9. When solveBoard is called, it works by 
    trying values in this array from left to right and backtracking if all 
    fail. By having a randomly shuffled array, our solutions are more random
    and since we generate boards by solving a board then removing elements,
    we generate boards that are more random.
*/
    private static int[] getShuffledArrayOfOneToNine(){
        int index, temp;
        int[] arr = {1,2,3,4,5,6,7,8,9};
        
        for (int i = arr.length - 1; i > 0; i--){
            index = ThreadLocalRandom.current().nextInt(i + 1);
            temp = arr[index];
            arr[index] = arr[i];
            arr[i] = temp;
        }
        
        return arr;
    }
    
    /*
    We solve the board by iterating through a shuffled array of 1 to 9. We try 
    different values in a cell, if it works, we move on to the next. If all 
    values don't work, we change the value back to the original value then 
    backtrack.
    */
    public boolean solveBoard(int cellIndex){
        int row = cellIndex / 9;
        int col = cellIndex % 9;
        int[][] boardVals = this.solvingBoard.getBoardVals();
        boolean[][] fixedVals = this.solvingBoard.getFixedVals();
        int originalVal = boardVals[row][col];
        int[] answers = getShuffledArrayOfOneToNine();
        
        if(fixedVals[row][col]){
            if(cellIndex<80){
                return this.solveBoard(cellIndex + 1);
            } else {
                return solvingBoard.checkBoard();
            }
        }
        
        if(cellIndex==80){
            for(int i: answers){
                boardVals[row][col] = i;
                if(solvingBoard.checkBoard()){
                    return true;
                } 
            }
            return false;
        }

        for(int i: answers){
            boardVals[row][col] = i;
            if(solvingBoard.checkBoard()){
                boolean next = this.solveBoard(cellIndex + 1);
                if(next){
                    return true;
                }
            }
        }
        
        boardVals[row][col] = originalVal;
        return false;
    }
    
//    Used to limit runtime when solving boards for generate board. Does not
//    limit run time if we use solve board in the client itself.
    @Override
    public void run(){
        this.solved[0] = this.solveBoard(0);
        this.latch.countDown();
    }
    
}
