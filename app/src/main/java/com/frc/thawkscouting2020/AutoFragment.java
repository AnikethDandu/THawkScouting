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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.EditText;
import android.widget.Button;

public class AutoFragment extends Fragment {

    private int[] powerCellHit = {0, 0, 0};
    private int[] powerCellMiss = {0, 0};

    private Button[] powerCellHitButtons = new Button[3];
    private Button[] powerCellMissButtons = new Button[2];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.autofragment_layout, container, false);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onResume() {
        super.onResume();

        // TODO: Calculate how many power cells the team picked up during autonomous

        /* KEEP ALL CODE INSIDE IF STATEMENT TO AVOID NULL POINTER EXCEPTIONS */
        if (getView() != null) {
            final View VIEW = getView();
            final ToggleButton ALLIANCE_COLOR_BUTTON = VIEW.findViewById(R.id.allianceButton);

            final EditText MATCH_EDIT_TEXT = VIEW.findViewById(R.id.matchTextbox);
            final EditText TEAM_EDIT_TEXT = VIEW.findViewById(R.id.teamTextbox);

            final RadioGroup DRIVER_STATION_GROUP = VIEW.findViewById(R.id.driverStationGroup);

            powerCellHitButtons[0] = VIEW.findViewById(R.id.innerAutoButton);
            powerCellHitButtons[1] = VIEW.findViewById(R.id.outerAutoButton);
            powerCellHitButtons[2] = VIEW.findViewById(R.id.bottomAutoButton);

            powerCellMissButtons[0] = VIEW.findViewById(R.id.autoHighMissButton);
            powerCellMissButtons[1] = VIEW.findViewById(R.id.autoLowMissButton);

            DRIVER_STATION_GROUP.check(R.id.stationOneButton);
            setAllianceColor(ALLIANCE_COLOR_BUTTON);

            ALLIANCE_COLOR_BUTTON.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    setAllianceColor(ALLIANCE_COLOR_BUTTON);
                }
            });

            MATCH_EDIT_TEXT.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    MATCH_EDIT_TEXT.setText(MATCH_EDIT_TEXT.getText().toString().equals("")
                            ? "0"
                            : MATCH_EDIT_TEXT.getText().toString());
                    return false;
                }
            });

            TEAM_EDIT_TEXT.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    TEAM_EDIT_TEXT.setText(TEAM_EDIT_TEXT.getText().toString().equals("")
                            ? "9999"
                            : TEAM_EDIT_TEXT.getText().toString());
                    return false;
                }
            });

            powerCellHitButtons[0].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    powerCellHit[0]++;
                    powerCellHitButtons[0].setText(String.format("INNER: %d", powerCellHit[0]));
                }
            });

            powerCellHitButtons[1].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    powerCellHit[1]++;
                    powerCellHitButtons[1].setText(String.format("OUTER: %d", powerCellHit[1]));
                }
            });

            powerCellHitButtons[2].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    powerCellHit[2]++;
                    powerCellHitButtons[2].setText(String.format("BOTTOM: %d", powerCellHit[2]));
                }
            });

            powerCellMissButtons[0].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    powerCellMiss[0]++;
                    powerCellMissButtons[0].setText(String.format("HIGH: %d", powerCellMiss[0]));
                }
            });

            powerCellMissButtons[1].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    powerCellMiss[1]++;
                    powerCellMissButtons[1].setText(String.format("LOW: %d", powerCellMiss[1]));
                }
            });
        }
    }

    private void setAllianceColor(ToggleButton toggleButton) {
        toggleButton.setBackgroundColor(
                toggleButton.isChecked()
                        ? getResources().getColor(R.color.backgroundRed)
                        : getResources().getColor(R.color.backgroundBlue)
        );
    }
}

