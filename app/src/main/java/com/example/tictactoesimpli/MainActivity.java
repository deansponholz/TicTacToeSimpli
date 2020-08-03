package com.example.tictactoesimpli;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements GamePresenter.View {

    GamePresenter mGamePresenter;
    public TextView mStartGame, mCurrentPlayer;
    public TableLayout mTableLayout;
    private String playerWinString, currentPlayerString, winningLocationString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStartGame = (TextView) findViewById(R.id.startGame);
        mCurrentPlayer = (TextView) findViewById(R.id.currentPlayerTitle);
        mTableLayout = (TableLayout) findViewById(R.id.tableLayout);

        playerWinString = getString(R.string.playerWinTitle);
        currentPlayerString = getString(R.string.currentPlayerTitle);
        winningLocationString = getString(R.string.playerWinLocation);

        mGamePresenter = new GamePresenter(this);
    }

    private View.OnClickListener moveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TableRow row = (TableRow) v.getParent();
            mGamePresenter.makeMove((int) row.getTag(), (int) v.getTag());
            mGamePresenter.updateGameBoard();
            v.setClickable(false);
        }
    };

    private View.OnClickListener startListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mGamePresenter.startGame();
            mGamePresenter.presentGameBoard();
        }
    };

    @Override
    public void createGameBoardUI(int gameSize) {
        for (int i = 0; i < mTableLayout.getChildCount(); i++) {
            TableRow row = (TableRow) mTableLayout.getChildAt(i);
            row.setTag(i);

            for (int k = 0; k < mTableLayout.getChildCount(); k++) {
                Button rowButton = (Button) row.getChildAt(k);
                rowButton.setTag(k);
                rowButton.setOnClickListener(moveListener);
                rowButton.setText(String.valueOf(mGamePresenter.mGameModel.getBoardLocationPiece(i, k)));
            }

        }
    }

    @Override
    public void updateGameBoardUI() {
        for (int i = 0; i < mTableLayout.getChildCount(); i++) {
            TableRow row = (TableRow) mTableLayout.getChildAt(i);
            for (int k = 0; k < mTableLayout.getChildCount(); k++) {
                Button rowButton = (Button) row.getChildAt(k);
                rowButton.setText(String.valueOf(mGamePresenter.mGameModel.getBoardLocationPiece(i, k)));
            }

        }
    }

    @Override
    public void attachGameControlUI() {
        mStartGame.setOnClickListener(startListener);
    }

    @Override
    public void updatePlayerWin() {

        ArrayList<Pair<String, Point>> winningRecordArrayList = mGamePresenter.mGameModel.getWinningPlayerAndLocation();
        StringBuilder x = new StringBuilder();

        //show winning player
        x.append(playerWinString);
        x.append(mGamePresenter.mGameModel.getCurrentPlayer());
        x.append("\n");

        //show winning strategy
        for (int i = 0; i < winningRecordArrayList.size(); i++){
            x.append(winningRecordArrayList.get(i).first);
            x.append("\n");
        }

        //show winning location
        //reverse coord values for standard x,y plane view
        x.append(winningLocationString);
        x.append(winningRecordArrayList.iterator().next().second.y);
        x.append(",");
        x.append(winningRecordArrayList.iterator().next().second.x);

        mCurrentPlayer.setText(x);
    }

    @Override
    public void updateCurrentPlayer() {
        StringBuilder x = new StringBuilder();
        x.append(currentPlayerString);
        x.append(mGamePresenter.mGameModel.getCurrentPlayer());
        mCurrentPlayer.setText(x);
    }

    @Override
    public void clearGameControlUI() {
        mCurrentPlayer.setText(currentPlayerString);

    }

    @Override
    public void setBoardInactive() {
        for (int i = 0; i < mTableLayout.getChildCount(); i++) {
            TableRow row = (TableRow) mTableLayout.getChildAt(i);
            for (int k = 0; k < mTableLayout.getChildCount(); k++) {
                Button rowButton = (Button) row.getChildAt(k);
                rowButton.setClickable(false);
            }
        }
    }
}