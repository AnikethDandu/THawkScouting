package com.frc.thawkscouting2020;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Screen that shows up for each cycle scored on Tele-Op tab
 *
 * @author Aniketh Dandu - Team 1100
 */
public class CycleActivity extends AppCompatActivity {

    /**
     * Array of buttons for hit buttons
     */
    @NonNull
    private Button[] m_cycleHitButtons = new Button[3];
    /**
     * Array of buttons for miss buttons
     */
    @NonNull
    private Button[] m_cycleMissButtons = new Button[2];
    /**
     * Array of Strings holding user actions
     */
    @NonNull
    private ArrayList<String> m_userActions = new ArrayList<>();
    /**
     * Array of integer values for hits
     */
    final int[] CYCLE_HIT = {0, 0, 0};
    /**
     * Array of integer values for misses
     */
    final int[] CYCLE_MISS = {0, 0};
    /**
     * TThe DataViewModel used to store data from this screen
     */
    private DataViewModel dataViewModel;

    // The method called when the view is created
    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycle);
        dataViewModel = MainActivity.dataViewModel;

        // Gets rid of header
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // Add a block to the user actions
        m_userActions.add("BLOCK");
        // ID the hit and miss buttons
        m_cycleHitButtons[0] = findViewById(R.id.cycleInnerButton);
        m_cycleHitButtons[1] = findViewById(R.id.cycleOuterButton);
        m_cycleHitButtons[2] = findViewById(R.id.cycleBottomButton);
        m_cycleMissButtons[0] = findViewById(R.id.cycleHighButton);
        m_cycleMissButtons[1] = findViewById(R.id.cycleLowerButton);
        // Default the button labels
        setButtonLabels();

        // When a hit or miss button is clicked, increment the value, update the DataViewModel, and update the labels
        m_cycleHitButtons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addHit(0);
            }
        });

        m_cycleHitButtons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addHit(1);
            }
        });

        m_cycleHitButtons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addHit(2);
            }
        });

        m_cycleMissButtons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMiss(0);
            }
        });

        m_cycleMissButtons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMiss(1);
            }
        });

        // When the accept button is pressed, send the data to the Tele-Op screen
        final Button ACCEPT_BUTTON = findViewById(R.id.acceptButton);
        ACCEPT_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                final int SHOTS = CYCLE_HIT[0] + CYCLE_HIT[1] + CYCLE_HIT[2] + CYCLE_MISS[0] + CYCLE_MISS[1];
                if (SHOTS > 0) {
                    Cycle currentCycle = new Cycle(CYCLE_HIT[0], CYCLE_HIT[1], CYCLE_HIT[2], CYCLE_MISS[0], CYCLE_MISS[1]);
                    TeleOpFragment.s_Cycles.add(currentCycle);
                    final String[] CYCLE = new String[5];
                    CYCLE[0] = String.valueOf(currentCycle.innerHit);
                    CYCLE[1] = String.valueOf(currentCycle.outerHit);
                    CYCLE[2] = String.valueOf(currentCycle.bottomHit);
                    CYCLE[3] = String.valueOf(currentCycle.highMiss);
                    CYCLE[4] = String.valueOf(currentCycle.lowMiss);
                    dataViewModel.LastCycle.setValue(CYCLE);
                    for (int  i = 0; i < 5; i++) {
                        TeleOpFragment.s_CyclesWithPositions[(TeleOpFragment.s_scoringPositions[0] + TeleOpFragment.s_scoringPositions[1]*2)][i] = Integer.valueOf(CYCLE[i]);
                    }
                    final int X = TeleOpFragment.s_SelectedBox[0];
                    final int Y = TeleOpFragment.s_SelectedBox[1];
                    TeleOpFragment.s_scoringPositions[0] = X;
                    TeleOpFragment.s_scoringPositions[1] = Y;
                    TeleOpFragment.s_Scoring[X][Y]++;
                    TeleOpFragment.setScoreLabel(X, Y);
                    TeleOpFragment.s_ScoringPositions.add(new int [] {X, Y});
                    TeleOpFragment.s_actions.add("CYCLE");
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

        getWindow().setLayout((int)(WIDTH*0.75), (int)(HEIGHT*0.7125));
    }

    @SuppressLint("DefaultLocale")
    private void setButtonLabels() {
        m_cycleHitButtons[0].setText(String.format("INNER: %d", CYCLE_HIT[0]));
        m_cycleHitButtons[1].setText(String.format("OUTER: %d", CYCLE_HIT[1]));
        m_cycleHitButtons[2].setText(String.format("BOTTOM: %d", CYCLE_HIT[2]));
        m_cycleMissButtons[0].setText(String.format("HIGH: %d", CYCLE_MISS[0]));
        m_cycleMissButtons[1].setText(String.format("LOW: %d", CYCLE_MISS[1]));
    }

    private void undoAction() {
        final String LAST_ACTION = m_userActions.get(m_userActions.size()-1);
        final String ACTION = LAST_ACTION.substring(0, 3);
        if (!LAST_ACTION.equals("BLOCK")) {
            final int INDEX = Integer.valueOf(LAST_ACTION.substring(LAST_ACTION.length()-1));
            if (ACTION.equals("hit")) {
                CYCLE_HIT[INDEX]--;
            } else {
                CYCLE_MISS[INDEX]--;
            }
            setButtonLabels();
            m_userActions.remove(LAST_ACTION);
        }
    }

    /**
     * Increment the hit value of the corresponding score button
     * Set the labels
     * Add an entry to the user actions
     * @param i The index of the button
     */
    @SuppressLint("DefaultLocale")
    private void addHit(int i) {
        CYCLE_HIT[i]++;
        setButtonLabels();
        m_userActions.add(String.format("hit%d",i));
    }

    /**
     * Increment the miss value of the corresponding score button
     * Set the labels
     * Add an entry to the user actions
     * @param i The index of the button
     */
    @SuppressLint("DefaultLocale")
    private void addMiss(int i) {
        CYCLE_HIT[i]++;
        setButtonLabels();
        m_userActions.add(String.format("miss%d",i));
    }
}