/**
 * Filename: GameState.java
 * Name: Lucas Wan
 * Login: cs8bwi20rj
 * Date: Feb 6, 2020
 * Sources of Help: notes
 *
 * This file contains methods for the game of 2048
 */

import java.util.Random;

/**
 * This class is to perform basic error cheking and make the game of 2048
 * possible.
 */
public class GameState {
    private Random rng;
    private int[][] board;
    private int score;

    /**
     * toString method that make a string representation of the a object
     *
     * @return a string representation of a GameState object
     */
    @Override
    public String toString () {
    StringBuilder outputString = new StringBuilder();
    outputString.append(String.format("Score: %d\n", getScore()));
    for (int row = 0; row < getBoard().length; row++) {
        for (int column = 0; column < getBoard()[0].length; column++) {
            outputString.append(getBoard()[row][column] == 0 ? "    -" :
                String.format("%5d", getBoard()[row][column]));
        }
        outputString.append("\n");
    }
    return outputString.toString();
    }

    /**
     * constructor that takes in number of rows and columns of the board
     *
     * @param numRows number of rows of the board
     * @param numCols number of columns of the board
     */
    public GameState(int numRows, int numCols) {
        this.board = new int[numRows][numCols];
        this.score = 0;
    }

    /**
     * getter method that make a deep copy of the board
     *
     * @return a int[][] copy of the board
     */
    public int[][] getBoard() {
        int[][] copy = new int[board.length][board[0].length];
        for (int row = 0; row < board.length; ++row) {
            for (int col = 0; col < board[0].length; ++col) {
                copy[row][col] = this.board[row][col];
            }
        }
        return copy;
    }

    /**
     * Settermethod that set the board
     *
     * @param newBoard a 2D int array
     */
    public void setBoard (int[][] newBoard) {
        if (newBoard == null) {
            return;
        }
        int[][] copy = new int[newBoard.length][newBoard[0].length];
        for (int row = 0; row < copy.length; ++row) {
            for (int col = 0; col < copy[0].length; ++col) {
                copy[row][col] = newBoard[row][col];
            }
        }
        this.board = copy;
    }

    /**
     * getter method that gets the current score
     *
     * @return int of current score
     */
    public int getScore () {
        return this.score;
    }

    /**
     * setter method that sets score
     *
     * @param newScore the int that wanted to set
     */
    public void setScore (int newScore) {
        this.score = newScore;
    }

    /**
     * a method to make a random number in a bound
     *
     * @param bound the bound of the intended random number
     * @return the random number
     */
    protected int rollRNG (int bound) {
        rng = new Random();
        return rng.nextInt(bound);
    }

    /**
     * a method that generates a new tile with known possibility of 2 and 4
     *
     * @return the number of the new tile
     */
    protected int randomTile () {
        if (rollRNG(100) < Config.TWO_PROB ) {
            return Config.TWO_TILE;
        }
        else {
            return Config.FOUR_TILE;
        }
    }

    /**
     * a method that counts the current empty tiles
     *
     * @return the number of empty tiles on the board
     */
    protected int countEmptyTiles() {
        int count = 0;
        for (int row = 0; row < board.length; ++row) {
            for (int col = 0; col < board[row].length; ++col) {
                if (board[row][col] == 0) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * a method that adds a new number on a random empty tile of board
     *
     * @return the number added
     */
    protected int addTile() {
        int noEmptyTile = 0;
        if (this.countEmptyTiles() == 0) {
            return noEmptyTile;
        }
        else {
            int row;
            int col;
            // find a empty tile
            do {
            row = rollRNG(this.board.length);
            col = rollRNG(this.board[row].length);
            }
            while (this.board[row][col] != 0);
            int add = this.randomTile();
            this.board[row][col] = add;
            return add;
        }
    }

    /**
     * a method that rotate the whole board counterclockwise
     *
     */
    protected void rotateCounterClockwise() {
        int[][] originalBoard = this.getBoard();
        int[][] rotatedBoard = new int[board[0].length][board.length];
        for (int row = 0; row < board.length; ++row) {
            for (int col = 0; col < board[row].length; ++col) {
                rotatedBoard[rotatedBoard.length-1-col][row] =
                originalBoard[row][col];
            }
        }
        this.board = rotatedBoard;
    }

    /**
     * a method that check if it possible to slide all tiles down
     *
     * @return True it is possible to slide down, vice vsesa
     */
    protected boolean canSlideDown() {
        for (int row = 0; row < board.length - 1; ++row) {
            for (int col = 0; col < board[row].length; ++col) {
                if(board[row+1][col] == board[row][col] && board[row][col]!=0) {
                    return true;
                }
                if (board[row][col] != 0 && board[row+1][col] == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * a method that checks if game is over
     *
     * @return True if game is over
     */
    public boolean isGameOver() {
        GameState copy = new GameState(board.length, board[0].length);
        copy.board = this.getBoard();
        for (int i = 0; i < 4; ++i) {
            if (copy.canSlideDown()) {
                return false;
            }
            else {
                copy.rotateCounterClockwise();
            }
        }
        return true;
    }

    /**
     * a method that slide down all tiles on board according to the rules
     *
     * @return True if sliding is successful, false if not
     */
    protected boolean slideDown() {
        if (this.canSlideDown()) {
            // make all 0 to the upper part of the board.
            for (int row = board.length-1; row > 0; --row) {
                for (int col = 0; col < board[row].length; ++col) {
                    if (board[row][col] == 0) {
                        // find the row index of a non-zero number above
                        int rowOfNum = 0;
                        for (int i = row; i>=0; i--) {
                            if (board[i][col] !=0) {
                                rowOfNum = i;
                                break;
                            }
                        }
                        // change the number and empty tile
                        board[row][col] = board[rowOfNum][col];
                        board[rowOfNum][col] = 0;
                    }
                }
            }

            // emerge all the nearby same numbers
            for (int row = board.length-1; row > 0; --row) {
                for (int col = 0; col < board[row].length; ++col) {
                    if (board[row][col] == board[row-1][col]) {
                        board[row][col] *= 2;
                        board[row-1][col] = 0;
                        this.score += board[row][col];
                    }
                }
            }

            // make all 0 to the upper part of the board again
            for (int row = board.length-1; row > 0; --row) {
                for (int col = 0; col < board[row].length; ++col) {
                    if (board[row][col] == 0) {
                        int rowOfNum = 0;
                        for (int i = row; i>=0; i--) {
                            if (board[i][col] !=0) {
                                rowOfNum = i;
                                break;
                            }
                        }
                        // change the number and the empty tile
                        board[row][col] = board[rowOfNum][col];
                        board[rowOfNum][col] = 0;
                    }
                }
            }
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * a method that slide the boards in a given direction using sliding down
     * and rotateCounterClockwise()
     *
     * @param dir an object of Direction class that represents the direction
     * intended
     * @return True if sliding is done successfully
     */
    public boolean move(Direction dir) {
        if (dir == null) {
            return false;
        }

        // rotate the board
        for (int count = 0; count < dir.getRotationCount(); ++count) {
            this.rotateCounterClockwise();
        }
        // slide down the tiles
        boolean result = this.slideDown();
        this.addTile();

        // rotate the board back
        for (int count = 0; count < 4 - dir.getRotationCount(); ++count) {
            this.rotateCounterClockwise();
        }
        return result;
    }
}
