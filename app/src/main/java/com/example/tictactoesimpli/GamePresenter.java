package com.example.tictactoesimpli;


public class GamePresenter {
    GameModel mGameModel;
    View mView;

    public GamePresenter(View view) {
        mView = view;
        mView.attachGameControlUI();
    }

    public void presentGameBoard(){
        mView.createGameBoardUI(mGameModel.getGameBoardSize());
    }

    public void updateGameBoard(){
        mView.updateGameBoardUI();
    }

    public void startGame(){
        int gameSize = 4;
        mGameModel = new GameModel(gameSize);
        mView.clearGameControlUI();
    }

    //make a move
    //detect a win
    //if no win, change player
    public void makeMove(int row, int column){
        mGameModel.makeMove(row, column);

        //update player or show winner
        if(mGameModel.getHasPlayerWon()){
            mView.updatePlayerWin();
            mView.setBoardInactive();
        }else{
          mView.updateCurrentPlayer();
        }
    }

    public interface View {
        void createGameBoardUI(int gameSize);
        void updateGameBoardUI();
        void attachGameControlUI();
        void updateCurrentPlayer();
        void updatePlayerWin();
        void clearGameControlUI();
        void setBoardInactive();
    }
}
