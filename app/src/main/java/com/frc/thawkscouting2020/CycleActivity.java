package com.frc.thawkscouting2020;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.*;
import android.os.Bundle;
import java.util.ArrayList;

public class CycleActivity extends AppCompatActivity {

    private Button[] cycleHitButtons = new Button[3];
    private Button[] cycleMissButtons = new Button[2];
    private ArrayList<String> undoActions = new ArrayList<>();
    final int[] CYCLE_HIT = {0, 0, 0};
    final int[] CYCLE_MISS = {0, 0};

    private DataViewModel dataViewModel;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycle);
        dataViewModel = MainActivity.dataViewModel;

        /* GETS RID OF HEADER */
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        undoActions.add("BLOCK");

        cycleHitButtons[0] = findViewById(R.id.cycleInnerButton);
        cycleHitButtons[1] = findViewById(R.id.cycleOuterButton);
        cycleHitButtons[2] = findViewById(R.id.cycleBottomButton);
        cycleMissButtons[0] = findViewById(R.id.cycleHighButton);
        cycleMissButtons[1] = findViewById(R.id.cycleLowerButton);

        setButtonLabels();

        cycleHitButtons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CYCLE_HIT[0]++;
                undoActions.add("Hit0");
                setButtonLabels();
            }
        });

        cycleHitButtons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CYCLE_HIT[1]++;
                undoActions.add("Hit1");
                setButtonLabels();
            }
        });

        cycleHitButtons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CYCLE_HIT[2]++;
                undoActions.add("Hit2");
                setButtonLabels();
            }
        });

        cycleMissButtons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CYCLE_MISS[0]++;
                undoActions.add("Miss0");
                setButtonLabels();
            }
        });

        cycleMissButtons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CYCLE_MISS[1]++;
                undoActions.add("Miss1");
                setButtonLabels();
            }
        });

        final Button ACCEPT_BUTTON = findViewById(R.id.acceptButton);
        ACCEPT_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                final int SHOTS = CYCLE_HIT[0] + CYCLE_HIT[1] + CYCLE_HIT[2] + CYCLE_MISS[0] + CYCLE_MISS[1];
                if (SHOTS > 0) {
                    Cycle currentCycle = new Cycle(CYCLE_HIT[0], CYCLE_HIT[1], CYCLE_HIT[2], CYCLE_MISS[0], CYCLE_MISS[1]);
                    TeleOpFragment.cycles.add(currentCycle);
                    final String[] CYCLE = new String[5];
                    CYCLE[0] = String.valueOf(currentCycle.innerHit);
                    CYCLE[1] = String.valueOf(currentCycle.outerHit);
                    CYCLE[2] = String.valueOf(currentCycle.bottomHit);
                    CYCLE[3] = String.valueOf(currentCycle.highMiss);
                    CYCLE[4] = String.valueOf(currentCycle.lowMiss);
                    dataViewModel.LastCycle.setValue(CYCLE);
                    for (int  i = 0; i < 5; i++) {
                        TeleOpFragment.CyclesWithPositions[(TeleOpFragment.SCORING_POSITIONS[0] + TeleOpFragment.SCORING_POSITIONS[1]*2)][i] = Integer.valueOf(CYCLE[i]);
                    }
                    final int X = TeleOpFragment.SELECTED_BOX[0];
                    final int Y = TeleOpFragment.SELECTED_BOX[1];
                    TeleOpFragment.SCORING_POSITIONS[0] = X;
                    TeleOpFragment.SCORING_POSITIONS[1] = Y;
                    TeleOpFragment.SCORING[X][Y]++;
                    TeleOpFragment.setScoreLabel(X, Y);
                    TeleOpFragment.scoring_positions.add(new int [] {X, Y});
                    TeleOpFragment.ACTIONS.add("CYCLE");
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Please score a shot for this cycle", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final Button CANCEL_BUTTON = findViewById(R.id.CanelButton);
        CANCEL_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final Button UNDO_BUTTONS = findViewById(R.id.cycleUndoButton);
        UNDO_BUTTONS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                undoAction();
            }
        });

        final DisplayMetrics DISPLAY_METRICS = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(DISPLAY_METRICS);
        final int HEIGHT = DISPLAY_METRICS.heightPixels;
        final int WIDTH = DISPLAY_METRICS.widthPixels;

        getWindow().setLayout((int)(WIDTH*0.75), (int)(HEIGHT*0.75));
    }

    @SuppressLint("DefaultLocale")
    private void setButtonLabels() {
        cycleHitButtons[0].setText(String.format("INNER: %d", CYCLE_HIT[0]));
        cycleHitButtons[1].setText(String.format("OUTER: %d", CYCLE_HIT[1]));
        cycleHitButtons[2].setText(String.format("BOTTOM: %d", CYCLE_HIT[2]));
        cycleMissButtons[0].setText(String.format("HIGH: %d", CYCLE_MISS[0]));
        cycleMissButtons[1].setText(String.format("LOW: %d", CYCLE_MISS[1]));
    }

    private void undoAction() {
        final String LAST_ACTION = undoActions.get(undoActions.size()-1);
        final String ACTION = LAST_ACTION.substring(0, 3);
        if (!LAST_ACTION.equals("BLOCK")) {
            final int INDEX = Integer.valueOf(LAST_ACTION.substring(LAST_ACTION.length()-1));
            if (ACTION.equals("Hit")) {
                CYCLE_HIT[INDEX]--;
            } else {
                CYCLE_MISS[INDEX]--;
            }
            setButtonLabels();
            undoActions.remove(LAST_ACTION);
        }
    }
}