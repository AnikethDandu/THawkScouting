package com.frc.thawkscouting2020;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

/**
 * Stores information about the team, match, color, and autonomous routine
 *
 * @author Aniketh Dandu - Team 1100
 */
public class AutoFragment extends Fragment{
    /**
     * A list containing Strings of the user's actions
     */
    private final ArrayList<String>USER_ACTIONS = new ArrayList<String>() {
        {
            add("Block");
        }
    };
    /**
     * The String holding the color of the team
     */
    @NonNull
    static String s_color = "BLUE";
    /**
     * The integer array holding the number of power cells the team scores in autonomous
     */
    final private int[] POWER_CELL_HITS = {0, 0, 0};
    /**
     * The integer array holding the number of power cells the team misses in autonomous
     */
    final private int[] POWER_CELL_MISS = {0, 0};
    /**
     * The array of Buttons for autonomous hits
     */
    @NonNull
    private Button[] m_powerCellHitButtons = new Button[3];
    /**
     * The array of Buttons for autonomous misses
     */
    @NonNull
    private Button[] m_powerCellMissButtons = new Button[2];
    /**
     * The Checkbox representing the crossing of the initiation line
     */
    private CheckBox m_crossedLineCheckbox;
    /**
     * The DataViewModel used to store data from this screen
     */
    private DataViewModel m_dataViewModel;
    /**
     * The text box for the match number
     */
    private EditText m_matchEditText;
    /**
     * The text box for the team number
     */
    private EditText m_teamEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Get the view of the layout
        final View VIEW = inflater.inflate(R.layout.autofragment_layout, container, false);
        // ID the text boxes
        m_matchEditText = VIEW.findViewById(R.id.matchTextbox);
        m_teamEditText = VIEW.findViewById(R.id.teamTextbox);
        // Make the cursor invisible for the text boxes
        m_matchEditText.setCursorVisible(false);
        m_teamEditText.setCursorVisible(false);
        // return the view
        return VIEW;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (getView() != null) {
            // Get the DataViewModel across all Fragments
            m_dataViewModel = MainActivity.dataViewModel;
            // Get the layout view
            final View VIEW = getView();
            // ID Buttons, Radio Group, and Checkbox
            final ToggleButton ALLIANCE_COLOR_BUTTON = VIEW.findViewById(R.id.allianceButton);
            final Button UNDO_BUTTON = getView().findViewById(R.id.autoUndoButton);
            final RadioGroup DRIVER_STATION_GROUP = VIEW.findViewById(R.id.driverStationGroup);
            m_crossedLineCheckbox = VIEW.findViewById(R.id.crossedLineCheckbox);
            m_powerCellHitButtons[0] = VIEW.findViewById(R.id.innerAutoButton);
            m_powerCellHitButtons[1] = VIEW.findViewById(R.id.outerAutoButton);
            m_powerCellHitButtons[2] = VIEW.findViewById(R.id.bottomAutoButton);
            m_powerCellMissButtons[0] = VIEW.findViewById(R.id.autoHighMissButton);
            m_powerCellMissButtons[1] = VIEW.findViewById(R.id.autoLowMissButton);
            // Set the default driver station to one
            DRIVER_STATION_GROUP.check(R.id.stationOneButton);
            // Set the default alliance color to blue
            setAllianceColor(ALLIANCE_COLOR_BUTTON);
            // Default the button labels
            setButtonLabels();
            // Set the values in the DataViewModel
            m_dataViewModel.Team.setValue(m_teamEditText.getText().toString());
            m_dataViewModel.Color.setValue(ALLIANCE_COLOR_BUTTON.getText().toString());
            m_dataViewModel.Match.setValue(m_matchEditText.getText().toString());
            m_dataViewModel.Station.setValue(1);
            m_dataViewModel.CrossedLine.setValue(m_crossedLineCheckbox.isChecked());
            // When the checkbox is toggled, set the value in the DataViewModel
            m_crossedLineCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    m_dataViewModel.CrossedLine.setValue(m_crossedLineCheckbox.isChecked());
                }
            });
            // When the alliance color button is toggled, set the alliance color and value
            ALLIANCE_COLOR_BUTTON.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setAllianceColor(ALLIANCE_COLOR_BUTTON);
                }
            });
            /* When the match text box is edited, check if the value is empty
            If the value is empty, set the text to 0
            Otherwise, keep the text entered
             */
            m_matchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    m_matchEditText.setText(m_matchEditText.getText().toString().equals("")
                            ? "0"
                            : m_matchEditText.getText().toString());
                    MainActivity.dataViewModel.Match.setValue(m_matchEditText.getText().toString());
                    return false;
                }
            });
            /* When the team text box is edited, check if the value is empty
            If the value is empty, set the text to 9999
            Otherwise, keep the text entered
             */
            m_teamEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    m_teamEditText.setText(m_teamEditText.getText().toString().equals("")
                            ? "9999"
                            : m_teamEditText.getText().toString());
                    MainActivity.dataViewModel.Team.setValue(m_teamEditText.getText().toString());
                    return false;
                }
            });
            // When the driver station is changed, set the value in the DataViewModel
            DRIVER_STATION_GROUP.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    final int DRIVER_STATION_NUMBER = Integer.valueOf(VIEW.findViewById(DRIVER_STATION_GROUP.getCheckedRadioButtonId()).getTag().toString());
                    m_dataViewModel.Station.setValue(DRIVER_STATION_NUMBER);
                }
            });

            // When the hit and miss buttons are clicked, update the label and value

            m_powerCellHitButtons[0].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addHit(0);
                }
            });

            m_powerCellHitButtons[1].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addHit(1);
                }
            });

            m_powerCellHitButtons[2].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addHit(2);
                }
            });

            m_powerCellMissButtons[0].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addMiss(0);
                }
            });

            m_powerCellMissButtons[1].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addMiss(1);
                }
            });

            /* When the undo button is clicked, remove the last score
            Decrement the value of the score or miss
            Update the labels
            Remove the action from the list of user actions
            Update the values in the DataViewModel
             */
            UNDO_BUTTON.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String LAST_ACTION = USER_ACTIONS.get(USER_ACTIONS.size()-1);
                    if (!LAST_ACTION.equals("Block")) {
                        final int INDEX = Integer.valueOf(LAST_ACTION.substring(LAST_ACTION.length()-1));
                        if (LAST_ACTION.substring(0, 3).equals("hit")) {
                            POWER_CELL_HITS[INDEX]--;
                        } else {
                            POWER_CELL_MISS[INDEX]--;
                        }
                        setButtonLabels();
                        USER_ACTIONS.remove(USER_ACTIONS.size()-1);
                    }
                    m_dataViewModel.AutoHits.setValue(POWER_CELL_HITS);
                    m_dataViewModel.AutoMiss.setValue(POWER_CELL_MISS);
                }
            });
        }
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * Reset all the buttons and text boxes and update the labels
     */
    void reset() {
        POWER_CELL_HITS[0] = 0;
        POWER_CELL_HITS[1] = 0;
        POWER_CELL_HITS[2] = 0;
        POWER_CELL_MISS[0] = 0;
        POWER_CELL_MISS[1] = 0;
        m_matchEditText.setText("");
        m_teamEditText.setText("");
        setButtonLabels();
    }

    /**
     * Increment the hit value of the corresponding score button
     * Set the labels
     * Add an entry to the user actions
     * @param i The index of the button
     */
    @SuppressLint("DefaultLocale")
    private void addHit(int i) {
        POWER_CELL_HITS[i]++;
        setButtonLabels();
        USER_ACTIONS.add(String.format("hit%d",i));
    }

    /**
     * Increment the miss value of the corresponding score button
     * Set the labels
     * Add an entry to the user actions
     * @param i The index of the button
     */
    @SuppressLint("DefaultLocale")
    private void addMiss(int i) {
        POWER_CELL_MISS[i]++;
        setButtonLabels();
        USER_ACTIONS.add(String.format("miss%d",i));
    }

    /**
     * Set the labels of the buttons in this screen
     */
    @SuppressLint("DefaultLocale")
    private void setButtonLabels() {
        m_powerCellHitButtons[0].setText(String.format("INNER: %d", POWER_CELL_HITS[0]));
        m_powerCellHitButtons[1].setText(String.format("OUTER: %d", POWER_CELL_HITS[1]));
        m_powerCellHitButtons[2].setText(String.format("BOTTOM: %d", POWER_CELL_HITS[2]));
        m_powerCellMissButtons[0].setText(String.format("HIGH: %d", POWER_CELL_MISS[0]));
        m_powerCellMissButtons[1].setText(String.format("LOW: %d", POWER_CELL_MISS[1]));
        m_dataViewModel.AutoHits.setValue(POWER_CELL_HITS);
        m_dataViewModel.AutoMiss.setValue(POWER_CELL_MISS);
    }

    /**
     * Set the alliance color based on the state of the toggle button
     * Set the package private color variable to change the field image
     * Set the value of the color in the DataViewModel
     * @param toggleButton the alliance color button
     */
    private void setAllianceColor(@NonNull ToggleButton toggleButton) {
        toggleButton.setBackgroundColor(
                toggleButton.isChecked()
                        ? getResources().getColor(R.color.backgroundRed)
                        : getResources().getColor(R.color.backgroundBlue)
        );
        s_color = toggleButton.isChecked() ? "RED" : "BLUE";
        m_dataViewModel.Color.setValue(s_color);
    }
}