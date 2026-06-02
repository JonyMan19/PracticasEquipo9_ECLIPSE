
package com.example.tetris;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TetrisView tetrisView;
    private TextView txtScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tetrisView = (TetrisView) findViewById(R.id.tetrisView);
        txtScore   = (TextView)   findViewById(R.id.txtScore);

        Button btnLeft   = (Button) findViewById(R.id.btnLeft);
        Button btnRotate = (Button) findViewById(R.id.btnRotate);
        Button btnRight  = (Button) findViewById(R.id.btnRight);
        Button btnDown   = (Button) findViewById(R.id.btnDown);

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tetrisView.moveLeft();
                tetrisView.invalidate();
                refreshScore();
            }
        });

        btnRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tetrisView.rotatePiece();
                tetrisView.invalidate();
                refreshScore();
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tetrisView.moveRight();
                tetrisView.invalidate();
                refreshScore();
            }
        });

        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tetrisView.moveDown();
                refreshScore();
            }
        });
    }

    private void refreshScore() {
        txtScore.setText("Puntuación: " + tetrisView.getScore());
    }

    @Override
    protected void onPause() {
        super.onPause();
        tetrisView.pauseGame();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tetrisView.resumeGame();
    }
}