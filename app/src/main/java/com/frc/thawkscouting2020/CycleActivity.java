package com.frc.thawkscouting2020;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.frc.thawkscouting2020.databinding.ActivityCycleBinding;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Screen that shows up for each cycle scored on Tele-Op tab
 *
 * @author Aniketh Dandu - FRC Team 1100
 */
public class CycleActivity extends AppCompatActivity {

    // **************************************************
    // Private fields
    // **************************************************

    /**
     * The list of user actions
     */
    private ArrayList<String> m_userActions = new ArrayList<String>() {};

    /**
     * Array of integers for shots scored
     */
    private final int[] CYCLE_HIT = {0, 0, 0};

    /**
     * Array of integers for shots missed
     */
    private final int[] CYCLE_MISS = {0, 0};

    /**
     * View Model used to store data cross Fragments and Activities
     */
    private DataViewModel dataViewModel;

    /**
     * A weak reference to the Main Activity
     */
    private static WeakReference<MainActivity> s_mainWeakReference;

    /**
     * A weak reference to the Tele Op Fragment
     */
    private static WeakReference<TeleOpFragment> s_teleWeakReference;

    /**
     * The layout binding for the Activity
     */
    private ActivityCycleBinding m_binding;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Assigns the bind and view
        m_binding = ActivityCycleBinding.inflate(getLayoutInflater());
        setContentView(m_binding.getRoot());

        // Adds a buffer to the user actions
        m_userActions.add("Block");
        // Assigns the Main Activity weak reference
        dataViewModel = s_mainWeakReference.get().DataViewModel;

        // Disables the header
        try
        {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        }
        catch (NullPointerException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        // Sets the default labels
        setButtonLabels();

        // Increments the shot values and button labels
        m_binding.cycleInnerButton.setOnClickListener((View view) -> addShot(0, true));
        m_binding.cycleOuterButton.setOnClickListener((View view) -> addShot(1, true));
        m_binding.cycleBottomButton.setOnClickListener((View view) -> addShot(2, true));
        m_binding.cycleHighButton.setOnClickListener((View view) -> addShot(0, false));
        m_binding.cycleLowerButton.setOnClickListener((View view) -> addShot(1, false));

        // Saves the values in the cycles and sets the labels in the Tele-Op Fragment
        // Does not allow the user to save the cycle if no shots are scored
        m_binding.acceptButton.setOnClickListener((View view) -> {
            final int SHOTS = CYCLE_HIT[0] + CYCLE_HIT[1] + CYCLE_HIT[2] + CYCLE_MISS[0] + CYCLE_MISS[1];
            if (SHOTS > 0) {
                new Thread(() -> {
                    Cycle currentCycle = new Cycle(CYCLE_HIT[0], CYCLE_HIT[1], CYCLE_HIT[2], CYCLE_MISS[0], CYCLE_MISS[1]);
                    s_teleWeakReference.get().CyclesList.add(currentCycle);
                    final String[] CYCLE = {
                            String.valueOf(currentCycle.InnerHit),
                            String.valueOf(currentCycle.OuterHit),
                            String.valueOf(currentCycle.BottomHit),
                            String.valueOf(currentCycle.HighMiss),
                            String.valueOf(currentCycle.LowMiss)
                    };
                    final int X = s_teleWeakReference.get().SelectedBox[0];
                    final int Y = s_teleWeakReference.get().SelectedBox[1];
                    for (int  i = 0; i < 5; i++) {
                        s_teleWeakReference.get().CyclesWithPositions[X+(2*Y)][i] = Integer.parseInt(CYCLE[i]);
                    }
                    s_teleWeakReference.get().CyclesScored[X][Y]++;
                    s_teleWeakReference.get().ScoringPositions.add(new int [] {X, Y});
                    s_teleWeakReference.get().UserActions.add("CYCLE");
                    m_binding.acceptButton.post(() -> runOnUiThread(() -> {
                        s_teleWeakReference.get().setCycleLabels(X, Y);
                        dataViewModel.LastCycle.setValue(CYCLE);
                    }));
                }).start();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Please score a shot for this cycle", Toast.LENGTH_SHORT).show();
            }
        });

        // Close the activity when the "Finish" button is pressed
        m_binding.CanelButton.setOnClickListener((View view) -> finish());
        // Undo the last action when the "Undo" button is pressed
        m_binding.cycleUndoButton.setOnClickListener((View view) -> undoAction());

        // Sets the window size
        final DisplayMetrics DISPLAY_METRICS = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(DISPLAY_METRICS);
        getWindow().setLayout((int)(DISPLAY_METRICS.heightPixels*0.75),
                (int)(DISPLAY_METRICS.widthPixels*0.7125));
    }

    // **************************************************
    // Static methods
    // **************************************************

    /**
     * Updates the weak references
     *
     * @param mainActivity Main Activity
     * @param teleOpFragment Tele Op Fragment
     */
    static void updateWeakReference(MainActivity mainActivity, TeleOpFragment teleOpFragment) {
        s_mainWeakReference = new WeakReference<>(mainActivity);
        s_teleWeakReference = new WeakReference<>(teleOpFragment);
    }

    // **************************************************
    // Private methods
    // **************************************************

    /**
     * Sets the labels on all of the score buttons
     */
    @SuppressLint("SetTextI18n")
    private void setButtonLabels() {
        m_binding.cycleInnerButton.setText("Inner: " + CYCLE_HIT[0]);
        m_binding.cycleOuterButton.setText("Outer: " + CYCLE_HIT[1]);
        m_binding.cycleBottomButton.setText("Bottom: " + CYCLE_HIT[2]);
        m_binding.cycleHighButton.setText("High: " + CYCLE_MISS[0]);
        m_binding.cycleLowerButton.setText("Low: " + CYCLE_MISS[1]);
    }

    /**
     * Removes the last action preformed by the user
     * Decrements the value, resets button labels, and removes the last action in the list
     */
    private void undoAction() {
        final String LAST_ACTION = m_userActions.get(m_userActions.size()-1);
        final String ACTION = LAST_ACTION.substring(0, 3);
        if (!LAST_ACTION.equals("Block")) {
            final int INDEX = Integer.parseInt(LAST_ACTION.substring(LAST_ACTION.length()-1));
            if (ACTION.equals("hit")) {
                CYCLE_HIT[INDEX]--;
            } else {
                CYCLE_MISS[INDEX]--;
            }
            setButtonLabels();
            m_userActions.remove(m_userActions.size()-1);
        }
    }

    /**
     * Increments the shot value, sets the labels on the buttons, and adds an action to the list
     *
     * @param index The index of the shot attempted
     * @param scored Boolean representing whether or not the shot was scored or not
     */
    private void addShot(int index, boolean scored) {
        if (scored) {
            CYCLE_HIT[index]++;
        } else {
            CYCLE_MISS[index]++;
        }
        m_userActions.add(((scored) ? "hit" : "miss") + index);
        setButtonLabels();
    }
}