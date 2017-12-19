package com.example.android.basketball;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    int scoreTeamA = 0;
    int scoreTeamB = 0;
    int foulA = 0;
    int foulB = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void add3pointsTeamA(View v) {
        scoreTeamA = scoreTeamA + 1;
        displayForTeamA(scoreTeamA);
    }

    public void foulTeamA(View v) {
        foulA = foulA + 1;
        displayForFoulA(foulA);
    }

    public void totalTeamA(View v) {
        int totalA;
        totalA = scoreTeamA;
        displayForTotalA(totalA);
    }

    public void add3pointsTeamB(View v) {
        scoreTeamB = scoreTeamB + 1;
        displayForTeamB(scoreTeamB);
    }


    public void foulTeamB(View v) {
        foulB = foulB + 1;
        displayForFoulB(foulB);
    }

    public void totalTeamB(View v) {
        int totalB;
        totalB = scoreTeamB;
        displayForTotalB(totalB);
    }

    public void resetButton(View v) {
        scoreTeamA = 0;
        scoreTeamB = 0;
        foulA = 0;
        foulB = 0;
        int totalA = 0;
        int totalB = 0;
        displayForTeamA(scoreTeamA);
        displayForTeamB(scoreTeamB);
        displayForFoulA(foulA);
        displayForFoulB(foulB);
        displayForTotalA(totalA);
        displayForTotalB(totalB);
    }

    /**
     * Displays the given score for Team A.
     */
    public void displayForTeamA(int score) {
        TextView scoreView = (TextView) findViewById(R.id.team_a_score);
        scoreView.setText(String.valueOf(score));
    }

    public void displayForTeamB(int score) {
        TextView scoreView = (TextView) findViewById(R.id.team_b_score);
        scoreView.setText(String.valueOf(score));
    }

    public void displayForFoulA(int score) {
        TextView scoreView = (TextView) findViewById(R.id.foulA);
        scoreView.setText(String.valueOf(score));
    }

    public void displayForFoulB(int score) {
        TextView scoreView = (TextView) findViewById(R.id.foulB);
        scoreView.setText(String.valueOf(score));
    }

    public void displayForTotalA(int score) {
        TextView scoreView = (TextView) findViewById(R.id.totalTeamA);
        scoreView.setText(String.valueOf(score));
    }

    public void displayForTotalB(int score) {
        TextView scoreView = (TextView) findViewById(R.id.totalTeamB);
        scoreView.setText(String.valueOf(score));
    }
}
