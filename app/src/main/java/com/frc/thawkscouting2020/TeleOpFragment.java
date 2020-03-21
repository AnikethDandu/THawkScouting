package com.frc.thawkscouting2020;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Chronometer;

import com.frc.thawkscouting2020.databinding.TeleopfragmentLayoutBinding;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class TeleOpFragment extends Fragment {

    // **************************************************
    // Package-private fields
    // **************************************************

    /**
     * The list of actions the user has preformed
     */
    ArrayList<String> UserActions = new ArrayList<String>() {};

    /**
     * 2D Array of integers holding values of where each cycle has been scored
     */
    int[][] CyclesScored = new int[2][3];

    /**
     * 2D Array of integers holding values of shots for each position across all cycles
     */
    int[][] CyclesWithPositions = new int[6][5];

    /**
     * The list of cycles scored
     */
    List<Cycle> CyclesList = new ArrayList<>();

    /**
     * The position of each cycle scored
     */
    List<int[]> ScoringPositions = new ArrayList<>();

    /**
     * The location of the user touch on the field grid
     */
    int[] SelectedBox = {0, 0};

    // **************************************************
    // Private fields
    // **************************************************

    /**
     * The labels on the field grid
     */
    private final TextView[][] SCORING_LABELS = new TextView[2][3];

    /**
     * Array of integers representing vertical lines on the field grid
     */
    final private ArrayList<Integer> VERTICAL_LINES = new ArrayList<>();

    /**
     * Array of integers representing horizontal lines on the field grid
     */
    final private ArrayList<Integer> HORIZONTAL_LINES = new ArrayList<>();

    /**
     * The height of the field image
     */
    private int m_fieldHeight;

    /**
     * The width of the field image
     */
    private int m_fieldWidth;

    /**
     * The amount of time the timers have been running for
     */
    private long m_timeElapsedOn, m_timeElapsedAgainst;

    /**
     * The value of penalties
     */
    private int m_penalties = 0;

    /**
     * The View Model used to store data across Fragments and Activities
     */
    private DataViewModel m_dataViewModel;

    /**
     * Weak reference to the Main Activity
     */
    private WeakReference<MainActivity> m_mainWeakReference;

    /**
     * Layout binding for the current Fragment
     */
    private TeleopfragmentLayoutBinding m_binding;

    // Retains the instance of the Fragment if the device orientation is changed
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @SuppressLint({"ClickableViewAccessibility", "DefaultLocale", "SetTextI18n"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Assigns the View Model
        m_dataViewModel = m_mainWeakReference.get().DataViewModel;
        // Updates the binding object and inflates the layout
        m_binding = TeleopfragmentLayoutBinding.inflate(inflater);

        // Adds a buffer to the list of user actions
        UserActions.add("Block");

        // Binds the field labels
        SCORING_LABELS[0][0] = m_binding.label00;
        SCORING_LABELS[0][1] = m_binding.label01;
        SCORING_LABELS[0][2] = m_binding.label02;
        SCORING_LABELS[1][0] = m_binding.label10;
        SCORING_LABELS[1][1] = m_binding.label11;
        SCORING_LABELS[1][2] = m_binding.label12;

        // Resets the timers and penalties text and values
        m_dataViewModel.DefenseOn.setValue(0);
        m_dataViewModel.PlayingDefense.setValue(0);
        setPenalties();

        // Resets the values and text on-screen when the "Reset" button is pressed
        m_binding.resetButton.setOnClickListener((View view) -> resetScreen());

        // Undo the last action preformed by the user
        // Decrements the values of the last cycle and updates the labels
        // Removes the action from the list of user actions
        m_binding.teleUndoButton.setOnClickListener((View view) -> {
            final String LAST_ACTION = UserActions.get(UserActions.size()-1);
            switch (LAST_ACTION) {
                case "CYCLE":
                    if (!CyclesList.isEmpty()) {
                        final Cycle LAST_CYCLE = CyclesList.get(CyclesList.size()-1);
                        final int[] LAST_NUMBERS = {
                                LAST_CYCLE.InnerHit, LAST_CYCLE.OuterHit, LAST_CYCLE.BottomHit,
                                LAST_CYCLE.HighMiss, LAST_CYCLE.LowMiss
                        };
                        final int X = ScoringPositions.get(ScoringPositions.size()-1)[0];
                        final int Y = ScoringPositions.get(ScoringPositions.size()-1)[1];
                        CyclesScored[X][Y]--;
                        setCycleLabels(X,Y);
                        for (int i = 0; i < 5; i++) {
                            CyclesWithPositions[X+(2*Y)][i] -= LAST_NUMBERS[i];
                        }
                        ScoringPositions.remove(ScoringPositions.size()-1);
                        CyclesList.remove(CyclesList.size()-1);
                    }
                    UserActions.remove(UserActions.size()-1);
                    break;
                case "PENALTY":
                    incrementPenalties(false);
                    break;
                default:
                    break;
            }
        });

        // Assigns the gird values based on the location of the user's touch on the field
        m_binding.fieldMap.setOnTouchListener((View view, MotionEvent motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                final float X_PT = motionEvent.getX();
                final float Y_PT = motionEvent.getY();

                for (Integer line: VERTICAL_LINES) {
                    if (X_PT < line) {
                        SelectedBox[0] = VERTICAL_LINES.indexOf(line);
                        break;
                    }
                }

                for (Integer line: HORIZONTAL_LINES) {
                    if (Y_PT < line) {
                        SelectedBox[1] = HORIZONTAL_LINES.indexOf(line);
                        break;
                    }
                }
                // Starts the Cycle Activity after recording the location of the user touch
                final Intent CYCLE_INTENT = new Intent(getActivity(), CycleActivity.class);
                startActivity(CYCLE_INTENT);
            }
            return false;
        });

        // Toggle the play/pause state of the timer
        m_binding.onDefenseTimer.setOnClickListener((View view) -> {
            if (m_binding.onDefenseTimer.isChecked()) {
                m_binding.onChronometer.setBase(SystemClock.elapsedRealtime() - m_timeElapsedOn);
                m_binding.onChronometer.start();
            } else {
                m_timeElapsedOn = SystemClock.elapsedRealtime() - m_binding.onChronometer.getBase();
                m_binding.onChronometer.stop();
                m_dataViewModel.DefenseOn.setValue(returnChronometerTime(m_binding.onChronometer));
            }
        });

        // Toggle the play/pause state of the timer
        m_binding.playingDefenseTimer.setOnClickListener((View view) -> {
            if (m_binding.playingDefenseTimer.isChecked()) {
                m_binding.againstChronometer.setBase(SystemClock.elapsedRealtime()
                        - m_timeElapsedAgainst);
                m_binding.againstChronometer.start();
            } else {
                m_timeElapsedAgainst = SystemClock.elapsedRealtime() -
                        m_binding.againstChronometer.getBase();
                m_binding.againstChronometer.stop();
                m_dataViewModel.PlayingDefense.setValue(
                        returnChronometerTime(m_binding.againstChronometer));
            }
        });

        // Create the grid values based on the size of the field image
        m_binding.fieldMap.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            m_fieldWidth = m_binding.fieldMap.getWidth();
            m_fieldHeight = m_binding.fieldMap.getHeight();

            for(int i = 1; i < 3; i++) VERTICAL_LINES.add(m_fieldWidth * i / 2);
            for(int i = 1; i < 4; i++) HORIZONTAL_LINES.add(m_fieldHeight * i / 3);
        });

        m_binding.penaltiesButton.setOnClickListener((View view) -> incrementPenalties(true));
        return m_binding.getRoot();
    }

    // Assigsn the layout binding to null when the Fragment is destroyed
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        m_binding = null;
    }

    // **************************************************
    // Package-private methods
    // **************************************************

    /**
     * Swaps the background image based on the Alliance color
     *
     * @param color Color of the team
     */
    void changeBackgroundImage(String color) {
        m_binding.fieldMap.setImageResource((color.equals("BLUE"))
                ? R.drawable.blue_field2 : R.drawable.red_field2);
    }

    /**
     * Sets the field cycle labels
     *
     * @param x X grid value
     * @param y Y grid value
     */
    void setCycleLabels(int x, int y) {
        SCORING_LABELS[x][y].setText(String.valueOf(CyclesScored[x][y]));
    }

    /**
     * Resets all values and text on the current screen
     */
    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    void resetScreen() {
        UserActions.clear();
        UserActions.add("Block");
        setPenalties();
        m_dataViewModel.DefenseOn.setValue(0);
        m_dataViewModel.PlayingDefense.setValue(0);
        m_dataViewModel.LastCycle.setValue(new String[] {});
        m_dataViewModel.Cycles.setValue(new int[][] {});
        CyclesList.clear();
        ScoringPositions.clear();
        for (int x = 0; x < 2; x++) {
            for (int y  = 0; y < 3; y++) {
                CyclesScored[x][y] = 0;
                setCycleLabels(x,y);
            }
        }
        for (int x = 0; x < 6; x++) {
            for (int y  = 0; y < 5; y++) {
                CyclesWithPositions[x][y] = 0;
            }
        }
    }

    /**
     * Updates the Main Activity weak reference
     *
     * @param mainActivity Main Activity
     */
    void updateWeakReferences(MainActivity mainActivity) {
        m_mainWeakReference = new WeakReference<>(mainActivity);
    }

    // **************************************************
    // Private methods
    // **************************************************

    /**
     * Returns the time left on the timer
     *
     * @param c The chronometer
     * @return Returns the time as an integer
     */
    private int returnChronometerTime(Chronometer c) {
        String[] time = c.getText().toString().split(":");
        int minutes = Integer.parseInt(time[0]);
        int seconds = Integer.parseInt(time[1]);
        return minutes * 60 + seconds;
    }

    /**
     * Increments or decrements the penalty count and updates the label
     *
     * @param increment Boolean representing if the penalty is incremented or decremented
     */
    @SuppressLint("SetTextI18n")
    private void incrementPenalties(boolean increment) {
        if (increment) {
            m_penalties--;
            UserActions.remove(UserActions.size()-1);
        } else {
            m_penalties++;
            UserActions.add("PENALTY");
        }
        setPenalties(m_penalties);
    }

    /**
     * Sets the penalty value and label
     *
     * @param penalties Integer value of penalties
     */
    @SuppressLint("SetTextI18n")
    private void setPenalties(int penalties) {
        m_penalties = penalties;
        m_binding.penaltiesButton.setText("PENALTIES: " + m_penalties);
        m_dataViewModel.Penalties.setValue(m_penalties);
    }

    /**
     * Overrides the previous method and sets the penalty value to 0
     */
    private void setPenalties() {
        setPenalties(0);
    }
}