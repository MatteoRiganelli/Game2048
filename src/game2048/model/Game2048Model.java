/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game2048.model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Random;
 
//implementa il gioco vero e proprio
public class Game2048Model {
     
    //attivare a true per avere il debug del gioco
    private static final boolean DEBUG = false;
     
    private static final int SPAZIATURA_CELLE = 6; //spazio fra le celle ->prima era 16
    private static final int NUM_CELLE = 4; //numero di celle
     
    private boolean arrowActive;
     
    private int highScore; //punteggio più alto
    private int highCell; //casella più alta
    private int currentScore; //punteggio più alto corrente
    private int currentCell; //casella più alta corrente
     
    private Cell[][] grid;
     
    private Random random;
     
    public Game2048Model() {
        this.grid = new Cell[NUM_CELLE][NUM_CELLE];
        this.random = new Random();
        this.highScore = 0;
        this.highCell = 0;
        this.currentScore = 0;
        this.currentCell = 0;
        this.arrowActive = false;
        initializeGrid();
    }
     
    public void initializeGrid() {
        int xx = SPAZIATURA_CELLE;
        for (int x = 0; x < NUM_CELLE; x++) {
            int yy = SPAZIATURA_CELLE;
            for (int y = 0; y < NUM_CELLE; y++) {
                Cell cell = new Cell(0);
                cell.setCellLocation(xx, yy);
                grid[x][y] = cell;
                yy += SPAZIATURA_CELLE + Cell.getCellWidth();
            }
            xx += SPAZIATURA_CELLE + Cell.getCellWidth();
        }
    }
     
    public void setHighScores() {
        highScore = (currentScore > highScore) ? 
                currentScore : highScore;
        highCell = (currentCell > highCell) ?
                currentCell : highCell;
        currentScore = 0;
        currentCell = 0;
    }
     
    public boolean isGameOver() {
        return isGridFull() && !isMovePossible();
    }
     
    private boolean isGridFull() {
        for (int x = 0; x < NUM_CELLE; x++) {
            for (int y = 0; y < NUM_CELLE; y++) {
                if (grid[x][y].isZeroValue()) {
                    return false;
                }
            }
        }
        return true;
    }
     
    private boolean isMovePossible() {
        for (int x = 0; x < NUM_CELLE; x++) {
            for (int y = 0; y < (NUM_CELLE - 1); y++) {
                int yy = y + 1;
                if (grid[x][y].getValue() == grid[x][yy].getValue()) {
                    return true;
                }
            }
        }
         
        for (int y = 0; y < NUM_CELLE; y++) {
            for (int x = 0; x < (NUM_CELLE - 1); x++) {
                int xx = x + 1;
                if (grid[x][y].getValue() == grid[xx][y].getValue()) {
                    return true;
                }
            }
        }
         
        return false;
    }
     
    public void addNewCell() {
        int value = (random.nextInt(10) < 9) ?  2 : 4;
         
        boolean locationFound = false;
        while(!locationFound) {
            int x = random.nextInt(NUM_CELLE);
            int y = random.nextInt(NUM_CELLE);
            if (grid[x][y].isZeroValue()) {
                grid[x][y].setValue(value);
                locationFound = true;
                if (DEBUG) {
                    System.out.println(displayAddCell(x, y));
                }
            }
        }
         
        updateScore(0, value);
    }
     
    private String displayAddCell(int x, int y) {
        StringBuilder builder = new StringBuilder();
        builder.append("Cell added at [");
        builder.append(x);
        builder.append("][");
        builder.append(y);
        builder.append("].");
         
        return builder.toString();
    }
     
    public boolean moveCellsUp() {
        boolean dirty = false;
         
        if (moveCellsUpLoop())  dirty = true;
         
        for (int x = 0; x < NUM_CELLE; x++) {
            for (int y = 0; y < (NUM_CELLE - 1); y++) {
                int yy = y + 1;
                dirty = combineCells(x, yy, x, y, dirty);
            }
        }
         
        if (moveCellsUpLoop())  dirty = true;
         
        return dirty;
    }
     
    private boolean moveCellsUpLoop() {
        boolean dirty = false;
         
        for (int x = 0; x < NUM_CELLE; x++) {
            boolean columnDirty = false;
            do {
                columnDirty = false;
                for (int y = 0; y < (NUM_CELLE - 1); y++) {
                    int yy = y + 1;
                    boolean cellDirty = moveCell(x, yy, x, y);
                    if (cellDirty) {
                        columnDirty = true;
                        dirty = true;
                    }
                }
            } while (columnDirty);      
        }
         
        return dirty;
    }
     
    public boolean moveCellsDown() {
        boolean dirty = false;
         
        if (moveCellsDownLoop())    dirty = true;
         
        for (int x = 0; x < NUM_CELLE; x++) {
            for (int y = NUM_CELLE - 1; y > 0; y--) {
                int yy = y - 1;
                dirty = combineCells(x, yy, x, y, dirty);
            }
        }
         
        if (moveCellsDownLoop())    dirty = true;
         
        return dirty;
    }
     
    private boolean moveCellsDownLoop() {
        boolean dirty = false;
         
        for (int x = 0; x < NUM_CELLE; x++) {
            boolean columnDirty = false;
            do {
                columnDirty = false;
                for (int y = NUM_CELLE - 1; y > 0; y--) {
                    int yy = y - 1;
                    boolean cellDirty = moveCell(x, yy, x, y);
                    if (cellDirty) {
                        columnDirty = true;
                        dirty = true;
                    }
                }
            } while (columnDirty);      
        }
         
        return dirty;
    }
     
    public boolean moveCellsLeft() {
        boolean dirty = false;
         
        if (moveCellsLeftLoop())    dirty = true;
         
        for (int y = 0; y < NUM_CELLE; y++) {
            for (int x = 0; x < (NUM_CELLE - 1); x++) { //faccio fino a (NUM_CELLE-1) perchè sennò sfora sul combine
                int xx = x + 1; //per poter far bene il combine cell
                dirty = combineCells(xx, y, x, y, dirty);
            }
        }
         
        if (moveCellsLeftLoop())    dirty = true;
         
        return dirty;
    }
     
    private boolean moveCellsLeftLoop() { 
        boolean dirty = false;
         
        for (int y = 0; y < NUM_CELLE; y++) {
            boolean rowDirty = false;
            do {
                rowDirty = false;
                for (int x = 0; x < (NUM_CELLE - 1); x++) {
                    int xx = x + 1;
                    boolean cellDirty = moveCell(xx, y, x, y);
                    if (cellDirty) {
                        rowDirty = true;
                        dirty = true;
                    }
                }
            } while (rowDirty); //fintanto che non ho due celle con val !=0 vicine 
        }
         
        return dirty;
    }
     
    public boolean moveCellsRight() {
        boolean dirty = false;
         
        if (moveCellsRightLoop())   dirty = true;
         
        for (int y = 0; y < NUM_CELLE; y++) {
            for (int x = (NUM_CELLE - 1); x > 0; x--) {
                int xx = x - 1;
                dirty = combineCells(xx, y, x, y, dirty);
            }
        }
         
        if (moveCellsRightLoop())   dirty = true;
         
        return dirty;
    }
 
    private boolean moveCellsRightLoop() {
        boolean dirty = false;
         
        for (int y = 0; y < NUM_CELLE; y++) {
            boolean rowDirty = false;
            do {
                rowDirty = false;
                for (int x = (NUM_CELLE - 1); x > 0; x--) {
                    int xx = x - 1;
                    boolean cellDirty = moveCell(xx, y, x, y);
                    if (cellDirty) {
                        rowDirty = true;
                        dirty = true;
                    }
                }
            } while (rowDirty);     
        }
         
        return dirty;
    }
     
    private boolean combineCells(int x1, int y1, int x2, int y2,
            boolean dirty) {
        if (!grid[x1][y1].isZeroValue()) {
            int value = grid[x1][y1].getValue();
            if (grid[x2][y2].getValue() == value) { //quindi posso unirle
                int newValue = value + value;
                grid[x2][y2].setValue(newValue);
                grid[x1][y1].setValue(0);
                updateScore(newValue, newValue);
                dirty = true;
            }
        }
        return dirty;
    }
     
    private boolean moveCell(int x1, int y1, int x2, int y2) {
        boolean dirty = false;
        if (!grid[x1][y1].isZeroValue() && (grid[x2][y2].isZeroValue())) {
            if (DEBUG) {
                System.out.println(displayMoveCell(x1, y1, x2, y2));
            }
            int value = grid[x1][y1].getValue();
            grid[x2][y2].setValue(value);
            grid[x1][y1].setValue(0);
            dirty = true;
        }
        return dirty;
    }
     
    private String displayMoveCell(int x1, int y1, int x2, int y2) {
        StringBuilder builder = new StringBuilder();
        builder.append("Moving cell [");
        builder.append(x1);
        builder.append("][");
        builder.append(y1);
        builder.append("] to [");
        builder.append(x2);
        builder.append("][");
        builder.append(y2);
        builder.append("].");
         
        return builder.toString();
    }
     
    private void updateScore(int value, int cellValue) {
        currentScore += value;
        currentCell = (cellValue > currentCell) ? 
                cellValue : currentCell;
    }
     
    public Cell getCell(int x, int y) {
        return grid[x][y];
    }
     
    public int getHighScore() {
        return highScore;
    }
 
    public int getHighCell() {
        return highCell;
    }
 
    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }
 
    public void setHighCell(int highCell) {
        this.highCell = highCell;
    }
 
    public int getCurrentScore() {
        return currentScore;
    }
 
    public int getCurrentCell() {
        return currentCell;
    }
 
    public boolean isArrowActive() {
        return arrowActive;
    }
 
    public void setArrowActive(boolean arrowActive) {
        this.arrowActive = arrowActive;
    }
 
    public Dimension getPreferredSize() {
        int width = NUM_CELLE * Cell.getCellWidth() + 
                SPAZIATURA_CELLE * 5;
        return new Dimension(width, width);
    }
     
    public void draw(Graphics g) {//disegna la tabella che contiene la griglia
        g.setColor(new Color(0xbbada0)); //prima era g.setColor(Color.DARK_GREY)
        Dimension d = getPreferredSize();
        g.fillRect(0, 0, d.width, d.height);
        //g.fillRoundRect(0, 0, d.width, d.height, 15, 15);

        for (int x = 0; x < NUM_CELLE; x++) {
            for (int y = 0; y < NUM_CELLE; y++) {
                grid[x][y].draw(g); //disegna la cella
            }
        }
    }
 
}
