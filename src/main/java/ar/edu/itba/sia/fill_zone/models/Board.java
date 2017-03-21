package ar.edu.itba.sia.fill_zone.models;

import java.util.Arrays;

public class Board {

    private int rows;

    private int columns;

    private int colors;

    private int[][] board;

    private int[] colorRegister;

    public Board(int rows, int columns, int colors, int[][] board){
        this.rows = rows;
        this.columns = columns;
        this.colors = colors;
        this.board = board;
        colorRegister = new int[colors];

        for(int row = 0; row < rows; row++){
            for(int col = 0; col < columns; col++){
                colorRegister[board[row][col]]++;
            }
        }
    }

    public int startingColor() {
        return board[0][0];
    }

    public void setColorAtLocker(int color, int row, int column){
        if(color > colors || color < 0){
            throw new IndexOutOfBoundsException();
        }

        colorRegister[board[row][column]]--;
        board[row][column] = color;
        colorRegister[color]++;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getColors() {
        return colors;
    }

    //TODO: defensive copy?
    public int[][] getBoard() {
        return board;
    }

    public int[] getColorRegister() {
        return colorRegister.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board that = (Board) o;

        return Arrays.deepEquals(board, that.board);
    }
}
