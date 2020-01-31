package com.frc.thawkscouting2020;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.EditText;
import android.widget.Button;

import java.util.ArrayList;

public class AutoFragment extends Fragment {
    static String color = "BLUE";

    private DataViewModel dataViewModel;

    private int[] powerCellHit = {0, 0, 0};
    private int[] powerCellMiss = {0, 0};

    private Button[] powerCellHitButtons = new Button[3];
    private Button[] powerCellMissButtons = new Button[2];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.autofragment_layout, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        // TODO: Calculate how many power cells the team picked up during autonomous

        /* KEEP ALL CODE INSIDE IF STATEMENT TO AVOID NULL POINTER EXCEPTIONS */
        if (getView() != null) {
            dataViewModel = MainActivity.dataViewModel;

            final View VIEW = getView();
            final ToggleButton ALLIANCE_COLOR_BUTTON = VIEW.findViewById(R.id.allianceButton);

            final EditText MATCH_EDIT_TEXT = VIEW.findViewById(R.id.matchTextbox);
            final EditText TEAM_EDIT_TEXT = VIEW.findViewById(R.id.teamTextbox);

            final RadioGroup DRIVER_STATION_GROUP = VIEW.findViewById(R.id.driverStationGroup);

            final Button UNDO_BUTTON = VIEW.findViewById(R.id.autoUndoButton);

            final ArrayList<String>USER_ACTIONS = new ArrayList<String>();
            USER_ACTIONS.add("Block");

            powerCellHitButtons[0] = VIEW.findViewById(R.id.innerAutoButton);
            powerCellHitButtons[1] = VIEW.findViewById(R.id.outerAutoButton);
            powerCellHitButtons[2] = VIEW.findViewById(R.id.bottomAutoButton);

            powerCellMissButtons[0] = VIEW.findViewById(R.id.autoHighMissButton);
            powerCellMissButtons[1] = VIEW.findViewById(R.id.autoLowMissButton);

            DRIVER_STATION_GROUP.check(R.id.stationOneButton);
            setAllianceColor(ALLIANCE_COLOR_BUTTON);
            setButtonLabels();

            MATCH_EDIT_TEXT.setText("0");
            TEAM_EDIT_TEXT.setText("9999");

            dataViewModel.Team.setValue(TEAM_EDIT_TEXT.getText().toString());
            dataViewModel.Color.setValue(ALLIANCE_COLOR_BUTTON.getText().toString());
            dataViewModel.Match.setValue(MATCH_EDIT_TEXT.getText().toString());
            dataViewModel.Station.setValue(1);

            ALLIANCE_COLOR_BUTTON.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    setAllianceColor(ALLIANCE_COLOR_BUTTON);
                    MainActivity.dataViewModel.Color.setValue(ALLIANCE_COLOR_BUTTON.getText().toString());
                }
            });

            MATCH_EDIT_TEXT.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    MATCH_EDIT_TEXT.setText(MATCH_EDIT_TEXT.getText().toString().equals("")
                            ? "0"
                            : MATCH_EDIT_TEXT.getText().toString());
                    MainActivity.dataViewModel.Match.setValue(MATCH_EDIT_TEXT.getText().toString());
                    return false;
                }
            });

            TEAM_EDIT_TEXT.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    TEAM_EDIT_TEXT.setText(TEAM_EDIT_TEXT.getText().toString().equals("")
                            ? "9999"
                            : TEAM_EDIT_TEXT.getText().toString());
                    MainActivity.dataViewModel.Team.setValue(TEAM_EDIT_TEXT.getText().toString());
                    return false;
                }
            });

            DRIVER_STATION_GROUP.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    dataViewModel.Station.setValue(i+1);
                }
            });

            powerCellHitButtons[0].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    powerCellHit[0]++;
                    setButtonLabels();
                    USER_ACTIONS.add("hit0");
                }
            });

            powerCellHitButtons[1].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    powerCellHit[1]++;
                    setButtonLabels();
                    USER_ACTIONS.add("hit1");
                }
            });

            powerCellHitButtons[2].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    powerCellHit[2]++;
                    setButtonLabels();
                    USER_ACTIONS.add("hit2");
                }
            });

            powerCellMissButtons[0].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    powerCellMiss[0]++;
                    setButtonLabels();
                    USER_ACTIONS.add("miss0");
                }
            });

            powerCellMissButtons[1].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    powerCellMiss[1]++;
                    setButtonLabels();
                    USER_ACTIONS.add("miss1");
                }
            });

            UNDO_BUTTON.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String LAST_ACTION = USER_ACTIONS.get(USER_ACTIONS.size()-1);
                    if (!LAST_ACTION.equals("Block")) {
                        final int INDEX = Integer.valueOf(LAST_ACTION.substring(LAST_ACTION.length()-1));
                        if (LAST_ACTION.substring(0, 3).equals("hit")) {
                            powerCellHit[INDEX]--;
                        } else {
                            powerCellMiss[INDEX]--;
                        }
                        setButtonLabels();
                        USER_ACTIONS.remove(USER_ACTIONS.size()-1);
                    }
                    dataViewModel.AutoHits.setValue(powerCellHit);
                    dataViewModel.AutoMiss.setValue(powerCellMiss);
                }
            });
        }
    }

    private void setAllianceColor(ToggleButton toggleButton) {
        // TODO: CHANGE FIELD IMAGE WHEN COLOR CHANGES
        toggleButton.setBackgroundColor(
                toggleButton.isChecked()
                        ? getResources().getColor(R.color.backgroundRed)
                        : getResources().getColor(R.color.backgroundBlue)
        );
        color = toggleButton.isChecked() ? "RED" : "BLUE";
    }

    @SuppressLint("DefaultLocale")
    private void setButtonLabels() {
        powerCellHitButtons[0].setText(String.format("INNER: %d", powerCellHit[0]));
        powerCellHitButtons[1].setText(String.format("OUTER: %d", powerCellHit[1]));
        powerCellHitButtons[2].setText(String.format("BOTTOM: %d", powerCellHit[2]));
        powerCellMissButtons[0].setText(String.format("HIGH: %d", powerCellMiss[0]));
        powerCellMissButtons[1].setText(String.format("LOW: %d", powerCellMiss[1]));
        dataViewModel.AutoHits.setValue(powerCellHit);
        dataViewModel.AutoMiss.setValue(powerCellMiss);
    }
}

