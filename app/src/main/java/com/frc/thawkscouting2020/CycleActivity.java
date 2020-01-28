package com.frc.thawkscouting2020;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.*;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

public class CycleActivity extends AppCompatActivity {

    private Button[] cycleHitButtons = new Button[3];
    private Button[] cycleMissButtons = new Button[2];
    private ArrayList<String> undoActions = new ArrayList<>();
    final int[] CYCLE_HIT = {0, 0, 0};
    final int[] CYCLE_MISS = {0, 0};

    private List<Cycle> tempCycles;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycle);

        /* GETS RID OF HEADER */
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();;
        }

        undoActions.add("BLOCK");

        cycleHitButtons[0] = findViewById(R.id.cycleInnerButton);
        cycleHitButtons[1] = findViewById(R.id.cycleOuterButton);
        cycleHitButtons[2] = findViewById(R.id.cycleBottomButton);
        cycleMissButtons[0] = findViewById(R.id.cycleHighButton);
        cycleMissButtons[1] = findViewById(R.id.cycleLowerButton);

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
            public void onClick(View view) {
                // TODO: TRACK PARTS OF CYCLES
                Cycle currentCycle = new Cycle(CYCLE_HIT[0], CYCLE_HIT[1], CYCLE_HIT[2], CYCLE_MISS[0], CYCLE_MISS[1]);
                TeleOpFragment.cycles.add(currentCycle);
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

        getWindow().setLayout((int)(WIDTH*0.6), (int)(HEIGHT*0.6));
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