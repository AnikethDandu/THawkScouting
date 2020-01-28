package com.frc.thawkscouting2020;

import android.media.Image;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.widget.*;
import android.view.ViewTreeObserver;
import android.content.Intent;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.List;

public class TeleOpFragment extends Fragment {

    final TextView[][] SCORING_LABLES = new TextView[2][3];
    final int[][] SCORING = new int[2][3];

    static List<Cycle> cycles = new ArrayList<>();
    private List<int[]> scoring_positions = new ArrayList<>();

    final private ArrayList<Integer> VERTICAL_LINES = new ArrayList<>();
    final private ArrayList<Integer> HORIZONTAL_LINES = new ArrayList<>();

    final private int[] SELECTED_BOX  = {0, 0};

    private int FIELD_HEIGHT;
    private int FIELD_WIDTH;

    private long timeElapsedOn, timeElapsedAgainst;

    @SuppressLint({"ClickableViewAccessibility", "DefaultLocale"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View VIEW = inflater.inflate(R.layout.teleopfragment_layout, container, false);

        final ImageView MAP = VIEW.findViewById(R.id.fieldMap);
        final ViewTreeObserver TREE = MAP.getViewTreeObserver();

        final Chronometer PLAYING_DEFENSE_TIMER = VIEW.findViewById(R.id.againstChronometer);
        final Chronometer ON_DEFENSE_TIMER = VIEW.findViewById(R.id.onChronometer);

        final CheckBox PLAYING_DEFENSE_BOX = VIEW.findViewById(R.id.playingDefenseTimer);
        final CheckBox ON_DEFENSE_BOX = VIEW.findViewById(R.id.onDefenseTimer);

        final Button UNDO_BUTTON = VIEW.findViewById(R.id.teleUndoButton);

        SCORING_LABLES[0][0] = VIEW.findViewById(R.id.label00);
        SCORING_LABLES[0][1] = VIEW.findViewById(R.id.label01);
        SCORING_LABLES[0][2] = VIEW.findViewById(R.id.label02);
        SCORING_LABLES[1][0] = VIEW.findViewById(R.id.label10);
        SCORING_LABLES[1][1] = VIEW.findViewById(R.id.label11);
        SCORING_LABLES[1][2] = VIEW.findViewById(R.id.label12);

        UNDO_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cycles.isEmpty()) {
                    final int X = scoring_positions.get(scoring_positions.size()-1)[0];
                    final int Y = scoring_positions.get(scoring_positions.size()-1)[1];
                    System.out.println(String.format("%d, %d", X, Y));
                    SCORING[X][Y]--;
                    setScoreLabel(X, Y);
                    scoring_positions.remove(scoring_positions.get(scoring_positions.size()-1));
                    cycles.remove(cycles.size()-1);
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
                    SCORING[SELECTED_BOX[0]][SELECTED_BOX[1]]++;
                    setScoreLabel(SELECTED_BOX[0], SELECTED_BOX[1]);
                    final Intent CYCLE_INTENT = new Intent(getActivity(), CycleActivity.class);
                    startActivity(CYCLE_INTENT);
                    final int[] SCORING_POSITIONS = {SELECTED_BOX[0], SELECTED_BOX[1]};
                    scoring_positions.add(SCORING_POSITIONS);
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

        return VIEW;
    }

    static void changeBackgroundImage(String color, ImageView map) {
        if (color.equals("BLUE")) {
            map.setImageResource(R.drawable.blue_field2);
        } else {
            map.setImageResource(R.drawable.red_field2);
        }
    }

    private void setScoreLabel(int x, int y) {
        SCORING_LABLES[x][y].setText(String.valueOf(SCORING[x][y]));
    }
}