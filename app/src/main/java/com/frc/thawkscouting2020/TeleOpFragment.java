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
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.CheckBox;
import android.widget.Chronometer;
import java.util.ArrayList;
import java.util.List;

public class TeleOpFragment extends Fragment {

    private Button m_penaltiesButton;

    static ArrayList<String> s_actions = new ArrayList<>();

    private static TextView[][] s_scoringLabels = new TextView[2][3];
    static int[][] s_scoring = new int[2][3];
    static int[][] s_cyclesWithPositions = new int[6][5];

    static List<Cycle> cycles = new ArrayList<>();
    static List<int[]> scoring_positions = new ArrayList<>();

    final private ArrayList<Integer> VERTICAL_LINES = new ArrayList<>();
    final private ArrayList<Integer> HORIZONTAL_LINES = new ArrayList<>();

    static int[] SELECTED_BOX  = {0, 0};

    private int FIELD_HEIGHT;
    private int FIELD_WIDTH;

    private long timeElapsedOn, timeElapsedAgainst;

    private int penalties = 0;

    static int[] SCORING_POSITIONS = {0, 0};

    private DataViewModel dataViewModel;

    @SuppressLint({"ClickableViewAccessibility", "DefaultLocale"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dataViewModel = MainActivity.dataViewModel;
        final View VIEW = inflater.inflate(R.layout.teleopfragment_layout, container, false);

        final ImageView MAP = VIEW.findViewById(R.id.fieldMap);
        final ViewTreeObserver TREE = MAP.getViewTreeObserver();

        final Chronometer PLAYING_DEFENSE_TIMER = VIEW.findViewById(R.id.againstChronometer);
        final Chronometer ON_DEFENSE_TIMER = VIEW.findViewById(R.id.onChronometer);

        final CheckBox PLAYING_DEFENSE_BOX = VIEW.findViewById(R.id.playingDefenseTimer);
        final CheckBox ON_DEFENSE_BOX = VIEW.findViewById(R.id.onDefenseTimer);

        final Button UNDO_BUTTON = VIEW.findViewById(R.id.teleUndoButton);

        final Button RESET_BUTTON = VIEW.findViewById(R.id.resetButton);

        m_penaltiesButton = VIEW.findViewById(R.id.penaltiesButton);

        s_actions.add("BLOCK");

        s_scoringLabels[0][0] = VIEW.findViewById(R.id.label00);
        s_scoringLabels[0][1] = VIEW.findViewById(R.id.label01);
        s_scoringLabels[0][2] = VIEW.findViewById(R.id.label02);
        s_scoringLabels[1][0] = VIEW.findViewById(R.id.label10);
        s_scoringLabels[1][1] = VIEW.findViewById(R.id.label11);
        s_scoringLabels[1][2] = VIEW.findViewById(R.id.label12);

        dataViewModel.DefenseOn.setValue(0);
        dataViewModel.PlayingDefense.setValue(0);
        dataViewModel.Penalties.setValue(penalties);

        RESET_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });

        UNDO_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String LAST_ACTION = s_actions.get(s_actions.size()-1);
                switch (LAST_ACTION) {
                    case "CYCLE":
                        if (!cycles.isEmpty()) {
                            final Cycle LAST_CYCLE = cycles.get(cycles.size()-1);
                            final int[] LAST_NUMBERS = new int[5];
                            LAST_NUMBERS[0] = LAST_CYCLE.innerHit;
                            LAST_NUMBERS[1] = LAST_CYCLE.outerHit;
                            LAST_NUMBERS[2] = LAST_CYCLE.bottomHit;
                            LAST_NUMBERS[3] = LAST_CYCLE.highMiss;
                            LAST_NUMBERS[4] = LAST_CYCLE.lowMiss;
                            final int[] last_pos = scoring_positions.get(scoring_positions.size()-1);
                            final int[] pos= {last_pos[0], last_pos[1]};
                            final int X = scoring_positions.get(scoring_positions.size()-1)[0];
                            final int Y = scoring_positions.get(scoring_positions.size()-1)[1];
                            s_scoring[X][Y]--;
                            setScoreLabel(X, Y);
                            for (int i = 0; i < 5; i++) {
                                s_cyclesWithPositions[X+2*Y][i] -= LAST_NUMBERS[i];
                            }
                            scoring_positions.remove(scoring_positions.size()-1);
                            cycles.remove(cycles.size()-1);
                        }
                        s_actions.remove(LAST_ACTION);
                        break;
                    case "PENALTY":
                        penalties--;
                        s_actions.remove(LAST_ACTION);
                        m_penaltiesButton.setText(String.format("PENALTIES: %d", penalties));
                        dataViewModel.Penalties.setValue(penalties);
                        break;
                    default:
                        break;
                }
            }
        });

        MAP.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    final float X_PT = motionEvent.getX();
                    final float Y_PT = motionEvent.getY();

                    for (Integer line: VERTICAL_LINES) {
                        if (X_PT < line) {
                            SELECTED_BOX[0] = VERTICAL_LINES.indexOf(line);
                            break;
                        }
                    }

                    for (Integer line: HORIZONTAL_LINES) {
                        if (Y_PT < line) {
                            SELECTED_BOX[1] = HORIZONTAL_LINES.indexOf(line);
                            break;
                        }
                    }
                    final Intent CYCLE_INTENT = new Intent(getActivity(), CycleActivity.class);
                    startActivity(CYCLE_INTENT);
                }
                return false;
            }
        });

        ON_DEFENSE_BOX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ON_DEFENSE_BOX.isChecked()) {
                    ON_DEFENSE_TIMER.setBase(SystemClock.elapsedRealtime() - timeElapsedOn);
                    ON_DEFENSE_TIMER.start();
                } else {
                    timeElapsedOn = SystemClock.elapsedRealtime() - ON_DEFENSE_TIMER.getBase();
                    ON_DEFENSE_TIMER.stop();
                    dataViewModel.DefenseOn.setValue(returnChronometerTime(ON_DEFENSE_TIMER));
                }
            }
        });

        PLAYING_DEFENSE_BOX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PLAYING_DEFENSE_BOX.isChecked()) {
                    PLAYING_DEFENSE_TIMER.setBase(SystemClock.elapsedRealtime() - timeElapsedAgainst);
                    PLAYING_DEFENSE_TIMER.start();
                } else {
                    timeElapsedAgainst = SystemClock.elapsedRealtime() - PLAYING_DEFENSE_TIMER.getBase();
                    PLAYING_DEFENSE_TIMER.stop();
                    dataViewModel.PlayingDefense.setValue(returnChronometerTime(PLAYING_DEFENSE_TIMER));
                }
            }
        });

        TREE.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                // TODO: ADD WAY TO CUSTOMIZE GRID LAYOUT + ADDING GRID LINES
                FIELD_WIDTH = MAP.getWidth();
                FIELD_HEIGHT = MAP.getHeight();

                VERTICAL_LINES.add(FIELD_WIDTH / 2);
                VERTICAL_LINES.add(FIELD_WIDTH);

                HORIZONTAL_LINES.add(FIELD_HEIGHT / 3);
                HORIZONTAL_LINES.add(FIELD_HEIGHT * 2 / 3);
                HORIZONTAL_LINES.add(FIELD_HEIGHT);
            }
        });

        m_penaltiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                penalties++;
                s_actions.add("PENALTY");
                m_penaltiesButton.setText(String.format("PENALTIES: %d", penalties));
                dataViewModel.Penalties.setValue(penalties);
            }
        });

        return VIEW;
    }

    static void changeBackgroundImage(String color, ImageView map) {
        if (color.equals("BLUE")) {
            map.setImageResource(R.drawable.blue_field2);
        } else {
            map.setImageResource(R.drawable.red_field2);
        }
    }

    static void setScoreLabel(int x, int y) {
        s_scoringLabels[x][y].setText(String.valueOf(s_scoring[x][y]));
    }

    private int returnChronometerTime(Chronometer c) {
        String[] time = c.getText().toString().split(":");
        int minutes = Integer.parseInt(time[0]);
        int seconds = Integer.parseInt(time[1]);
        return minutes * 60 + seconds;
    }

    @SuppressLint("DefaultLocale")
    void reset() {
        s_actions.clear();
        s_actions.add("Block");
        penalties = 0;
        dataViewModel.DefenseOn.setValue(0);
        m_penaltiesButton.setText(String.format("PENALTIES: %d", penalties));
        dataViewModel.Penalties.setValue(penalties);
        dataViewModel.PlayingDefense.setValue(0);
        dataViewModel.LastCycle.setValue(new String[] {});
        dataViewModel.Cycles.setValue(new int[][] {});
        cycles.clear();
        scoring_positions.clear();
        for (int x = 0; x < 2; x++) {
            for (int y  = 0; y < 3; y++) {
                s_scoring[x][y] = 0;
                setScoreLabel(x, y);
            }
        }
        for (int x = 0; x < 6; x++) {
            for (int y  = 0; y < 5; y++) {
                s_cyclesWithPositions[x][y] = 0;
            }
        }
    }
}