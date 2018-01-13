/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Arrays;

/** * 
 *
 * @author User
 */
public class GUI extends JFrame implements ActionListener {
    private Board mainBoard;
    private boolean[][] fixedVals;
    private int[][] actualBoardVals;
    
    private final JSplitPane DoublePane;
    private final JPanel topPanel;
    private final JPanel[] miniTopPanels = new JPanel[9];
    private final JPanel midPanel;
    private final JPanel btmPanel;
    private final JPanel UIPanel;
    
    private final JTextField[][] textCells;
    private final MyVerifier verifier;
    private final Button buttonCheck;
    private final Button buttonSolve;
    private final Button buttonClear;
    private final Button buttonGenerate;
    private final JTextField outputText;
    
    public static final Color OPEN_CELL_COLOR = Color.GRAY;

    
    GUI(Board mainBoard){
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.mainBoard = mainBoard;
        this.fixedVals = mainBoard.getFixedVals();
        this.setName("Sudoku");
        
        this.textCells = new JTextField[9][9];
        this.actualBoardVals = this.mainBoard.getBoardVals();
        this.verifier = new MyVerifier();
        
        this.topPanel = new JPanel();
        this.topPanel.setLayout(new GridLayout(3,3));
//        this.topPanel.setSize(new Dimension(120*18, 120 * 18));
        for(int i=0; i < 9; i++){
            this.miniTopPanels[i] = new JPanel();
            this.miniTopPanels[i].setLayout(new GridLayout(3,3));
        }
        
        for(int i=0; i < 9; i++){
            for(int j=0; j < 9; j++){
                this.textCells[i][j] = new JTextField();
                this.textCells[i][j].setText("" + this.actualBoardVals[i][j]);
                this.textCells[i][j].setInputVerifier(this.verifier);
                this.textCells[i][j].setPreferredSize(new Dimension(30, 30));
                this.textCells[i][j].setFont(new Font("Default", Font.PLAIN, 20));
                this.textCells[i][j].setHorizontalAlignment(JTextField.CENTER);
                this.textCells[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
                
                if(this.fixedVals[i][j]){
                    this.textCells[i][j].setEditable(false);
                } else {
                    this.textCells[i][j].setEditable(true);
                }
                
                int miniPanelIdx = (i/3) * 3 + j/3;
                this.miniTopPanels[miniPanelIdx].add(textCells[i][j]);
            }
        }
        
        for(int i=0; i < 9; i++){
            this.topPanel.add(this.miniTopPanels[i]);
            this.miniTopPanels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
        }
        
        this.midPanel = new JPanel();
        this.midPanel.setLayout(new GridLayout(2,2));
        this.midPanel.setSize(new Dimension(150, 60));
        
        this.buttonCheck = new Button("Check");
        this.buttonCheck.addActionListener(this);
        this.midPanel.add(buttonCheck);
        
        this.buttonSolve = new Button("Solve");
        this.buttonSolve.addActionListener(this);
        this.midPanel.add(buttonSolve);
        
        this.buttonClear = new Button("Clear");
        this.buttonClear.addActionListener(this);
        this.midPanel.add(buttonClear);
        
        this.buttonGenerate = new Button("Generate");
        this.buttonGenerate.addActionListener(this);
        this.midPanel.add(buttonGenerate);
        
        this.btmPanel = new JPanel();
        this.btmPanel.setLayout(new FlowLayout());
//        this.btmPanel.setSize(new Dimension(150,30));
        this.outputText = new JTextField();
        this.outputText.setPreferredSize(new Dimension(270, 30));
        this.outputText.setEditable(false);
        this.btmPanel.add(this.outputText);
                
        this.UIPanel = new JPanel();
        this.UIPanel.add(midPanel);
        this.UIPanel.add(btmPanel);
        this.UIPanel.setLayout(new GridLayout(2,1));
        
        this.DoublePane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, this.topPanel, 
                this.UIPanel);
        this.DoublePane.setDividerLocation(0.9);

        add(DoublePane);
        setVisible(true);
        pack();
        
        this.setTitle("Sudoku");
    }
    
   class MyVerifier extends InputVerifier{
       HashSet<String> permittedStrings =new HashSet<String>(
               Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9"));
       
       @Override
       public boolean verify(JComponent input) {
            JTextField textCell = (JTextField) input;
            String enteredText = textCell.getText();
            if(!enteredText.equals("0")){
                enteredText = enteredText.replaceFirst("^0+(?!$)", "");
                enteredText = enteredText.replaceAll("0+$", "");
            }
            if(enteredText == ""){ enteredText = "0";}
            if(permittedStrings.contains(enteredText)){
                textCell.setText(enteredText);
                return true;
            } else {
                textCell.setText("0");
                
                return false;
            }
        }
   } 
    
   public void actualBoardToGUIUpdate(boolean fullReset){
       for(int i=0; i < 9; i++){
            for(int j=0; j < 9; j++){
                int actualVal = this.actualBoardVals[i][j];
                if(fullReset){
                
                    if(actualVal > 0){
                        this.textCells[i][j].setText("" + this.actualBoardVals[i][j]);
                        this.textCells[i][j].setEditable(false);
                    }else{
                        this.textCells[i][j].setText("0");
                        this.textCells[i][j].setEditable(true);
                    } 
                } else {
                    this.textCells[i][j].setText("" + this.actualBoardVals[i][j]);
                }
            }
        }
   }
   
   @Override
   public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == this.buttonCheck){
            mainBoard.actualGUIToBoardUpdate(this.textCells);
            if (!mainBoard.isBoardFull()){
                this.outputText.setText("Board is not yet full!");
            } else if (!mainBoard.checkBoard()){
                this.outputText.setText("This solution is wrong");
            } else {
                this.outputText.setText("You have solved the puzzle!");
            }
        }
        
        if (evt.getSource() == this.buttonSolve){
            this.mainBoard.actualGUIToBoardUpdate(this.textCells);
            boolean solved = this.mainBoard.callSolveBoard(0);
            this.actualBoardToGUIUpdate(false);
            if(solved){
                this.outputText.setText("Here's the solved puzzle!");
            } else {
                this.outputText.setText("Puzzle cannot be solved from current state.");
            }
        }
        
        if (evt.getSource() == this.buttonClear){
            mainBoard.clearEntries(this.textCells);
            this.outputText.setText("Cleared!");
        }
        
        if (evt.getSource() == this.buttonGenerate){
            this.outputText.setText("Generating...");
            this.mainBoard = Board.generateBoard();
            this.actualBoardVals = this.mainBoard.getBoardVals();
            this.fixedVals = this.mainBoard.getFixedVals();
            actualBoardToGUIUpdate(true);
            this.outputText.setText("Generated!");
        }
   }
   
   
}
