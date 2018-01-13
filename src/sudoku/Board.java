/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.concurrent.ThreadLocalRandom;


/**
 *
 * @author User
 */
public class Board {
    private int[][] boardVals;
    private boolean[][] fixedVals;
    
    Board(int[][] boardVals){
        this.boardVals = boardVals;
        
        fixedVals = new boolean[9][9];
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                fixedVals[i][j] = boardVals[i][j] > 0;
            }
        }
    }
    public boolean[][] getFixedVals() {
        return fixedVals;
    }

    public void setFixedVals(boolean[][] fixedVals) {
        this.fixedVals = fixedVals;
    }
    
    public int[][] getBoardVals() {
        return this.boardVals;
    }

    public void setBoardVals(int[][] boardVals) {
        this.boardVals = boardVals;
    }
    
    public static Board generateBoard(){
        boolean valid = false;
        Board result = new Board(new int[9][9]);
        int attemptCounter = 0;
        double p;
        
        while(!valid){
            System.out.println("new attempt " + attemptCounter++);
            int[][] newBoardVals = new int[9][9];
            for(int i = 0; i < 9; i++){
                for(int j = 0; j < 9; j++){
                    p = ThreadLocalRandom.current().nextDouble(1);
                    if(p<=0.20){
                        newBoardVals[i][j] = 
                                ThreadLocalRandom.current().nextInt(1, 10);
                    }
                }
            }
            result = new Board(newBoardVals);
            valid = result.solveBoard(0);
        }
        
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                p = ThreadLocalRandom.current().nextDouble(1);
                if(p<=0.65){
                    result.boardVals[i][j] = 0;
                    result.fixedVals[i][j] = false;
                } else {
                    result.fixedVals[i][j] = true;
                }
            }
        }
        
        return result;
    }
    public void actualGUIToBoardUpdate(JTextField[][] textCells){
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                String txt = textCells[i][j].getText().trim();
//                System.out.println("Location: " + i + "," + j + " with value " + txt);
                if(!txt.equals("")){
                    this.boardVals[i][j] = Integer.parseInt(txt);
                } else {
                    this.boardVals[i][j] = 0;
                }
            }
        }
    }
    
    public boolean isBoardFull(){
        for(int i=0; i<this.boardVals.length;i++){
            for(int j=0; j<this.boardVals.length;j++){
                System.out.println(i + " " + j);
                if (this.boardVals[i][j] == 0){
                    return false;
                }
            }
        }
        return true;
    }
    public boolean checkBoard(){
        boolean[][] rowChecks = new boolean[9][9];
        boolean[][] colChecks = new boolean[9][9];
        boolean[][] boxChecks = new boolean[9][9];
        
        for(int i=0; i<this.boardVals.length;i++){
            for(int j=0; j<this.boardVals.length;j++){
                int x = this.boardVals[i][j] - 1;
                int box = i/3*3 + j/3;
                if(x != -1){
                    if(rowChecks[i][x] | colChecks[j][x] | boxChecks[box][x]){
//                        System.out.println(rowChecks[i][x]);
//                        System.out.println(colChecks[j][x]);
//                        System.out.println(boxChecks[box][x]);
//                        System.out.println("i " + i + "j " + j);
                        return false;
                        
                    } else {
//                        System.out.println("Setting i " + i + " .j " + j + " .x " + x + " box " + box);
                        rowChecks[i][x] = true;
                        colChecks[j][x] = true;
                        boxChecks[box][x] = true;
                    }
                }
            }
        }
        
        return true;
    }
    
    
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
    
    public boolean solveBoard(int cellIndex){
        int row = cellIndex / 9;
        int col = cellIndex % 9;
        int originalVal = this.boardVals[row][col];
        int[] answers = getShuffledArrayOfOneToNine();
        
//        System.out.println(row + " " + col);
        if(this.fixedVals[row][col]){
            if(cellIndex<80){
                return solveBoard(cellIndex + 1);
            } else {
                return this.checkBoard();
            }
        }
        
        if(cellIndex==80){
            for(int i: answers){
                this.boardVals[row][col] = i;
                if(this.checkBoard()){
                    return true;
                } 
            }

            return false;
        }
        

        for(int i: answers){
            this.boardVals[row][col] = i;
            if(this.checkBoard()){
                boolean next = solveBoard(cellIndex + 1);
                if(next){
                    return true;
                }
            }
        }
        
        
        this.boardVals[row][col] = originalVal;
        return false;
    }
    
    public void clearEntries(JTextField[][] textCells){
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(this.fixedVals[i][j]){
                    ;
                } else {
                    textCells[i][j].setText("0");
                    this.boardVals[i][j] = 0;
                }
            }
        }
    }
    
}
