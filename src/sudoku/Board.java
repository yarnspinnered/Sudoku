/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;
import javax.swing.*;
import java.util.concurrent.*;



/**
 *
 * @author User
 */
public class Board {
    private int[][] boardVals;
    private boolean[][] fixedVals;
    private SudokuSolver solver;
    
    Board(int[][] boardVals){
        this.boardVals = boardVals;
        solver = new SudokuSolver(this);
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
    
//    We have this method so that it is not necessary to directly access the
//    solver
    public boolean callSolveBoard(int idx){
        return this.solver.solveBoard(idx);
    }
    
    /*
    We randomly enter seed values onto an empty board. We try to solve it.
    We use a countdownlatch with an await property. Once it takes too long 
    to solve, we retry with a different seed. When solved, we then remove 
    values randomly to create a mostly empty board and finally use the
    board object to update the GUI.
    */
    public static Board generateBoard(){
        boolean valid = false;
        Board candidateBoard = new Board(new int[9][9]);
        int attemptCounter = 0;
        double p;
        
        while(!valid){
            int[][] newBoardVals = new int[9][9];
            for(int i = 0; i < 9; i++){
                for(int j = 0; j < 9; j++){
                    p = ThreadLocalRandom.current().nextDouble(1);
                    if(p<=0.15){
                        newBoardVals[i][j] = 
                                ThreadLocalRandom.current().nextInt(1, 10);
                    }
                }
            }
            
            candidateBoard = new Board(newBoardVals);
            CountDownLatch latch = new CountDownLatch(1);
            boolean[] solved = new boolean[1];
/*          Call the constructor variant of SudokuSolver which takes in a 
            a latch so that it can decrement the latch when it finishes
            solving. If it takes more than a second to solve, the latch will
            time out and we proceed to a new seed.
*/
            SudokuSolver solver = new SudokuSolver(
                    candidateBoard, latch, solved);
            
            try {
                Thread t1 = new Thread(solver);
                t1.start();
                latch.await(1, TimeUnit.SECONDS);  
                // wait until latch counted down to 0
                
            } catch (InterruptedException e) {
            }
            valid = solved[0];
        }
        
//        Removing values from the filled board
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                p = ThreadLocalRandom.current().nextDouble(1);
                if(p<=0.6){
                    candidateBoard.boardVals[i][j] = 0;
                    candidateBoard.fixedVals[i][j] = false;
                } else {
                    candidateBoard.fixedVals[i][j] = true;
                }
            }
        }
        
        return candidateBoard;
    }
    
    public void actualGUIToBoardUpdate(JTextField[][] textCells){
        for(int i = 0; i < 9; i++){
          for(int j = 0; j < 9; j++){
                String txt = textCells[i][j].getText().trim();
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
                if (this.boardVals[i][j] == 0){
                    return false;
                }
            }
        }
        return true;
    }
    
//  Check if the board is in a valid state. i.e. It conforms to the rules
//  of Sudoku.
    public boolean checkBoard(){
        boolean[][] rowChecks = new boolean[9][9];
        boolean[][] colChecks = new boolean[9][9];
        boolean[][] boxChecks = new boolean[9][9];
        
        for(int i=0; i<this.boardVals.length;i++){
            for(int j=0; j<this.boardVals.length;j++){
                int val = this.boardVals[i][j] - 1;
                int box = i/3*3 + j/3;
                if(val != -1){
                    if(rowChecks[i][val] | colChecks[j][val] 
                            | boxChecks[box][val]){
                        return false;
                    } else {
                        rowChecks[i][val] = true;
                        colChecks[j][val] = true;
                        boxChecks[box][val] = true;
                    }
                }
            }
        }
        
        return true;
    }
    
//    Clear entries which are not fixed. i.e. Values entered by player only.
//    Initial values of the board are not cleared.
    public void clearEntries(JTextField[][] textCells){
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(!this.fixedVals[i][j]){
                    textCells[i][j].setText("0");
                    this.boardVals[i][j] = 0;
                }
            }
        }
    }
    
}
