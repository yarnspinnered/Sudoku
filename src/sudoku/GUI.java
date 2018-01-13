/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/** * 
 *
 * @author User
 */
public class GUI extends Frame implements ActionListener {
    private final Board mainBoard;
    private final boolean[][] fixedVals;
    private final int[][] actualBoardVals;
    
    private final JSplitPane DoublePane;
    private final JPanel topPanel;
    private final JPanel midPanel;
    private final JPanel btmPanel;
    private final JPanel UIPanel;
    
    private final JTextField[][] textCells;
    private final Button buttonCheck;
    private final Button buttonSolve;
    private final JTextField outputText;
    
    public static final Color OPEN_CELL_COLOR = Color.GRAY;

    
    GUI(Board mainBoard){
        this.mainBoard = mainBoard;
        this.fixedVals = mainBoard.getFixedVals();
        this.setName("Sudoku");
        
        this.textCells = new JTextField[9][9];
        this.actualBoardVals = this.mainBoard.getBoardVals();
        
        this.topPanel = new JPanel();
        this.topPanel.setLayout(new GridLayout(9,9));
        this.topPanel.setSize(new Dimension(60*9, 60*9));
        
        for(int i=0; i < 9; i++){
            for(int j=0; j < 9; j++){
                this.textCells[i][j] = new JTextField();
                this.textCells[i][j].setText("" + this.actualBoardVals[i][j]);
                if(this.fixedVals[i][j]){
                    this.textCells[i][j].setEditable(false);
                } else {
                    this.textCells[i][j].setEditable(true);
                }
                this.topPanel.add(textCells[i][j]);
                int[] addr = new int[]{i,j};
            }
        }
        
        this.midPanel = new JPanel();
        this.midPanel.setLayout(new GridLayout(1,3));
        this.midPanel.setSize(new Dimension(150, 30));
        
        buttonCheck = new Button("Check");
        buttonCheck.addActionListener(this);
        this.midPanel.add(buttonCheck);
        
        buttonSolve = new Button("Solve");
        buttonSolve.addActionListener(this);
        this.midPanel.add(buttonSolve);
        
        this.btmPanel = new JPanel();
        this.btmPanel.setLayout(new FlowLayout());
        this.btmPanel.setSize(new Dimension(150,30));
        this.outputText = new JTextField();
        this.outputText.setPreferredSize(new Dimension(150, 30));
        this.outputText.setEditable(false);
        this.btmPanel.add(this.outputText);
                
        this.UIPanel = new JPanel();
        this.UIPanel.add(midPanel);
        this.UIPanel.add(btmPanel);
        this.UIPanel.setLayout(new GridLayout(2,1));
        
        this.DoublePane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, this.topPanel, 
                this.UIPanel);
        this.DoublePane.setDividerLocation(0.9);
        this.DoublePane.setSize(new Dimension(60*10, 60*9));

        add(DoublePane);
        setVisible(true);
        pack();

    }
    
   public void actualBoardToGUIUpdate(){
       for(int i=0; i < 9; i++){
            for(int j=0; j < 9; j++){
                this.textCells[i][j].setText("" + this.actualBoardVals[i][j]);
            }
        }
   }
   @Override
   public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == this.buttonCheck){
            mainBoard.updateBoard(this.textCells);
            if (!mainBoard.isBoardFull()){
                this.outputText.setText("Board is not yet full!");
            } else if (!mainBoard.checkBoard()){
                this.outputText.setText("This solution is wrong");
            } else {
                this.outputText.setText("You have solved the puzzle!");
            }
        }
        
        if (evt.getSource() == this.buttonSolve){
            boolean solved = mainBoard.solveBoard(0);
            this.actualBoardToGUIUpdate();
            if(solved){
                this.outputText.setText("Here's the solved puzzle!");
            } else {
                this.outputText.setText("Puzzle could not be solved.");
            }
        }
   }
   
   
}
