package com.example.tictactoesimpli;

import android.graphics.Point;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

public class GameModel {

    private char[][] gameBoard;
    private int gameBoardSize;
    private Point lastMoveLocation;
    private ArrayList<Pair<String, Point>> winningPlayerAndLocation = new ArrayList<>();
    private boolean hasPlayerWon = false;
    private char currentPlayer = 'X';

    public GameModel(int gameSize) {
        gameBoardSize = gameSize;
        gameBoard = createGameBoard(gameSize);
        winningPlayerAndLocation.clear();
    }

    private char[][] createGameBoard(int gameBoardSize){

        char[][] gameBoard = new char[gameBoardSize][gameBoardSize];

        for (int i = 0; i < gameBoardSize; i++){
            for (int k = 0; k < gameBoardSize; k++){
                gameBoard[i][k] = '-';
            }
        }

        logGameBoard(gameBoard);
        return gameBoard;
    }

    public void makeMove(int row, int column){

        gameBoard[row][column] = getCurrentPlayer();
        setLastMove(new Point(row, column));
        logGameBoard(gameBoard);
        detectWin();

        //currentPlayer has won
        if (!getWinningPlayerAndLocation().isEmpty()){
            hasPlayerWon = true;
        }
        //no win yet, change currentPlayer
        else {
            if (getCurrentPlayer() == 'X'){
                setCurrentPlayerO();
            }
            else if (getCurrentPlayer() == 'O'){
                setCurrentPlayerX();
            }
            hasPlayerWon = false;
        }
    }

    public char getBoardLocationPiece(int row, int column) {
        return gameBoard[row][column];
    }


    private void logGameBoard(char[][] gameBoard){

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Last move by:" + getCurrentPlayer() + "\n");

        for (char[] row : gameBoard) {
            stringBuilder.append(Arrays.toString(row) + "\n");
        }
        Log.d("gameboard", stringBuilder.toString());
    }

    public void detectWin() {
        check2x2Box();
        checkCorners();
        checkRow();
        checkColumn();
        checkDiagonal();
        checkReverseDiagonal();
    }

    //2x2 box case
    //in an N gameBoardSize, only check cases where 2x2 box is possible
    private void check2x2Box(){

        for (int i = 0; i < gameBoardSize; i++) {
            for (int k = 0; k < gameBoardSize; k++) {

                //only check cases where 2x2 box is possible
                if (i + 1 != gameBoardSize && k + 1 != gameBoardSize) {

                    //check 2x2 Box -> (x,y)(x+1,y),(x, y+1),(x+1,y+1)
                    if (gameBoard[i][k] == currentPlayer &&
                            gameBoard[i + 1][k] == currentPlayer &&
                            gameBoard[i][k + 1] == currentPlayer &&
                            gameBoard[i + 1][k + 1] == currentPlayer) {
                        Log.d("gamewin 2x2", "winning player" + currentPlayer);

                        Pair<String, Point> winningPair = new Pair("2x2Box", getLastMove());
                        setWinningPlayerAndLocation(winningPair);
                        break;
                    }
                }
            }
        }
    }

    //4 corners case
    private void checkCorners(){

        //in N gameboardSize only check 4 corners if last move was in a corner spot
        if (gameBoard[getLastMove().x][getLastMove().y] == gameBoard[0][0] ||
                gameBoard[getLastMove().x][getLastMove().y] == gameBoard[0][gameBoardSize - 1] ||
                gameBoard[getLastMove().x][getLastMove().y] == gameBoard[gameBoardSize - 1][0] ||
                gameBoard[getLastMove().x][getLastMove().y] == gameBoard[gameBoardSize - 1][gameBoardSize - 1]) {

            //check if 4 corners match
            if (gameBoard[0][0] == currentPlayer &&
                    gameBoard[0][gameBoardSize - 1] == currentPlayer &&
                    gameBoard[gameBoardSize - 1][0] == currentPlayer &&
                    gameBoard[gameBoardSize - 1][gameBoardSize - 1] == currentPlayer) {
                Log.d("gamewin 4corners", "winning player" + currentPlayer);
                Pair<String, Point> winningPair = new Pair("4Corners", getLastMove());
                setWinningPlayerAndLocation(winningPair);
            }
        }
    }

    //Check win by N in a row(horizontal)
    private void checkRow(){

        boolean isRowWin = false;
        int i, k;

        for (i = 0; i < gameBoardSize && !isRowWin; i++) {
            for (k = 0; k < gameBoard[0].length; k++) {
                if (gameBoard[i][k] != currentPlayer)
                    break;
            }
            if (k == gameBoard[0].length) {
                isRowWin = true;
                Log.d("gamewin Row", "winning player" + currentPlayer);
                Pair<String, Point> winningPair = new Pair("Row", getLastMove());
                setWinningPlayerAndLocation(winningPair);
            }
        }
    }

    //Check win by N in a row(vertical)
    private void checkColumn(){

        int i,k;
        boolean isColumnWin = false;

        for (i = 0; i < gameBoard[0].length && !isColumnWin; i++) {
            for (k = 0; k < gameBoard.length; k++) {
                if (gameBoard[k][i] != currentPlayer)
                    break;
            }
            if (k == gameBoard.length){
                isColumnWin = true;
                Log.d("gamewin Column", "winning player" + currentPlayer);
                Pair<String, Point> winningPair = new Pair("Column", getLastMove());
                setWinningPlayerAndLocation(winningPair);
            }
        }
    }

    //Check win by N in a row(diagonal)
    private void checkDiagonal(){

        int i;
        for (i = 0; i < gameBoard.length; i++) {
            if (gameBoard[i][i] != currentPlayer)
                break;
        }
        if (i == gameBoard.length){
            Log.d("gamewin Diagonal", "winning player" + currentPlayer);
            Pair<String, Point> winningPair = new Pair("Diagonal", getLastMove());
            setWinningPlayerAndLocation(winningPair);
        }
    }

    //Check win by N in a row(negative-diagonal)
    private void checkReverseDiagonal(){

        int i;
        for (i = 0; i < gameBoard.length; i++) {
            if (gameBoard[i][gameBoard.length - 1 - i] != currentPlayer)
                break;
        }
        if (i == gameBoard.length){
            Log.d("gamewin ReverseDiagonal", "winning player" + currentPlayer);
            Pair<String, Point> winningPair = new Pair("Reverse Diagonal", getLastMove());
            setWinningPlayerAndLocation(winningPair);
        }
    }


    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayerX() {
        this.currentPlayer = 'X';
    }
    public void setCurrentPlayerO() {
        this.currentPlayer = 'O';
    }

    public Point getLastMove() {
        return lastMoveLocation;
    }

    public void setLastMove(Point lastMoveLocation) {
        this.lastMoveLocation = lastMoveLocation;
    }

    public ArrayList<Pair<String, Point>> getWinningPlayerAndLocation() {
        return winningPlayerAndLocation;
    }

    public void setWinningPlayerAndLocation(Pair<String, Point> winningPlayerAndLocationPair) {
        this.winningPlayerAndLocation.add(winningPlayerAndLocationPair);
    }

    public boolean getHasPlayerWon() {
        return hasPlayerWon;
    }

    public int getGameBoardSize() {
        return gameBoardSize;
    }
}
