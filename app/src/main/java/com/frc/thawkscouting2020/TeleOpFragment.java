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

    // TODO: Clean up code

    ArrayList<String> UserActions = new ArrayList<String>() {};
    private final TextView[][] SCORING_LABELS = new TextView[2][3];

    int[][] CyclesScored = new int[2][3];
    int[][] CyclesWithPositions = new int[6][5];
    List<Cycle> CyclesList = new ArrayList<>();
    List<int[]> ScoringPositions = new ArrayList<>();
    int[] SelectedBox = {0, 0};

    final private ArrayList<Integer> VERTICAL_LINES = new ArrayList<>();
    final private ArrayList<Integer> HORIZONTAL_LINES = new ArrayList<>();
    private int m_fieldHeight;
    private int m_fieldWidth;
    private long m_timeElapsedOn, m_timeElapsedAgainst;
    private int m_penalties = 0;
    private DataViewModel m_dataViewModel;
    private WeakReference<MainActivity> m_mainWeakReference;
    private TeleopfragmentLayoutBinding m_binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @SuppressLint({"ClickableViewAccessibility", "DefaultLocale", "SetTextI18n"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        m_dataViewModel = m_mainWeakReference.get().dataViewModel;
        m_binding = TeleopfragmentLayoutBinding.inflate(inflater);

        UserActions.add("Block");

        SCORING_LABELS[0][0] = m_binding.label00;
        SCORING_LABELS[0][1] = m_binding.label01;
        SCORING_LABELS[0][2] = m_binding.label02;
        SCORING_LABELS[1][0] = m_binding.label10;
        SCORING_LABELS[1][1] = m_binding.label11;
        SCORING_LABELS[1][2] = m_binding.label12;

        m_dataViewModel.DefenseOn.setValue(0);
        m_dataViewModel.PlayingDefense.setValue(0);
        setPenalties();

        m_binding.resetButton.setOnClickListener((View view) -> reset());

        m_binding.teleUndoButton.setOnClickListener((View view) -> {
            final String LAST_ACTION = UserActions.get(UserActions.size()-1);
            switch (LAST_ACTION) {
                case "CYCLE":
                    if (!CyclesList.isEmpty()) {
                        final Cycle LAST_CYCLE = CyclesList.get(CyclesList.size()-1);
                        final int[] LAST_NUMBERS = {
                                LAST_CYCLE.innerHit, LAST_CYCLE.outerHit, LAST_CYCLE.bottomHit,
                                LAST_CYCLE.highMiss, LAST_CYCLE.lowMiss
                        };
                        final int X = ScoringPositions.get(ScoringPositions.size()-1)[0];
                        final int Y = ScoringPositions.get(ScoringPositions.size()-1)[1];
                        CyclesScored[X][Y]--;
                        setScoreLabel(X, Y);
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
                final Intent CYCLE_INTENT = new Intent(getActivity(), CycleActivity.class);
                startActivity(CYCLE_INTENT);
            }
            return false;
        });

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

        m_binding.playingDefenseTimer.setOnClickListener((View view) -> {
            if (m_binding.playingDefenseTimer.isChecked()) {
                m_binding.againstChronometer.setBase(SystemClock.elapsedRealtime() - m_timeElapsedAgainst);
                m_binding.againstChronometer.start();
            } else {
                m_timeElapsedAgainst = SystemClock.elapsedRealtime() - m_binding.againstChronometer.getBase();
                m_binding.againstChronometer.stop();
                m_dataViewModel.PlayingDefense.setValue(returnChronometerTime(m_binding.againstChronometer));
            }
        });

        m_binding.fieldMap.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            m_fieldWidth = m_binding.fieldMap.getWidth();
            m_fieldHeight = m_binding.fieldMap.getHeight();

            VERTICAL_LINES.add(m_fieldWidth / 2);
            VERTICAL_LINES.add(m_fieldWidth);

            HORIZONTAL_LINES.add(m_fieldHeight / 3);
            HORIZONTAL_LINES.add(m_fieldHeight * 2 / 3);
            HORIZONTAL_LINES.add(m_fieldHeight);
        });

        m_binding.penaltiesButton.setOnClickListener((View view) -> incrementPenalties(true));
        return m_binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        m_binding = null;
    }

    void changeBackgroundImage(String color) {
        m_binding.fieldMap.setImageResource((color.equals("BLUE"))
                ? R.drawable.blue_field2 : R.drawable.red_field2);
    }

    void setScoreLabel(int x, int y) {
        SCORING_LABELS[x][y].setText(String.valueOf(CyclesScored[x][y]));
    }

    private int returnChronometerTime( Chronometer c) {
        String[] time = c.getText().toString().split(":");
        int minutes = Integer.parseInt(time[0]);
        int seconds = Integer.parseInt(time[1]);
        return minutes * 60 + seconds;
    }

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

    @SuppressLint("SetTextI18n")
    private void setPenalties(int penalties) {
        m_penalties = penalties;
        m_binding.penaltiesButton.setText("PENALTIES: " + m_penalties);
        m_dataViewModel.Penalties.setValue(m_penalties);
    }

    @SuppressLint("SetTextI18n")
    private void setPenalties() {
        setPenalties(0);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    void reset() {
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
                setScoreLabel(x,y);
            }
        }
        for (int x = 0; x < 6; x++) {
            for (int y  = 0; y < 5; y++) {
                CyclesWithPositions[x][y] = 0;
            }
        }
    }

    void updateWeakReferences(MainActivity mainActivity) {
        m_mainWeakReference = new WeakReference<>(mainActivity);
    }
}