package com.frc.thawkscouting2020;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CheckBox;

/**
 * Final screen with Checkboxes for climb, control panel, disabling, and cards
 * Display the QR Code
 *
 * @author Aniketh Dandu - Team 1100
 */
public class EndgameFragment extends Fragment {
    /**
     * The array of Checkboxes in the final screen
     */
    @NonNull
    static CheckBox[] CHECKBOXES = new CheckBox[11];
    /**
     * The String value of the notes
     */
    static String s_Notes;
    /**
     * The String value of the scouter name
     */
    static String s_Name;
    /**
     * The text box for the notes
     */
    private EditText m_notes;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    // Method for when the view is created
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the view corresponding to the layout
        final View VIEW =  inflater.inflate(R.layout.endgamefragment_layout, container, false);
        // ID the Checkboxes and name text box
        CHECKBOXES[0] = VIEW.findViewById(R.id.rotationCheckbox);
        CHECKBOXES[1] = VIEW.findViewById(R.id.colorCheckbox);
        CHECKBOXES[2] = VIEW.findViewById(R.id.attemptedClimb);
        CHECKBOXES[3] = VIEW.findViewById(R.id.climbCheckBox);
        CHECKBOXES[4] = VIEW.findViewById(R.id.levelCheckbox);
        CHECKBOXES[5] = VIEW.findViewById(R.id.attemptedDoubleCheckbox);
        CHECKBOXES[6] = VIEW.findViewById(R.id.doubleCheckbox);
        CHECKBOXES[7] = VIEW.findViewById(R.id.brownedOutCheckbox);
        CHECKBOXES[8] = VIEW.findViewById(R.id.disableCheckbox);
        CHECKBOXES[9] = VIEW.findViewById(R.id.yellowCardCheckbox);
        CHECKBOXES[10] = VIEW.findViewById(R.id.redCardCheckbox);
        m_notes = VIEW.findViewById(R.id.notes);
        final EditText NAME = VIEW.findViewById(R.id.scouterNameBox);
        // Make the cursor for the notes text box invisible and clear the notes text box
        m_notes.setCursorVisible(false);
        m_notes.setText("");

        // When the QR Button is clicked, set the notes and name fields and load the QR screen
        final Button QR_BUTTON = VIEW.findViewById(R.id.qrButton);
        QR_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s_Name = NAME.getText().toString();
                s_Notes = m_notes.getText().toString();
                final Intent QR_INTENT = new Intent(getActivity(), QRActivity.class);
                startActivity(QR_INTENT);
            }
        });
        return VIEW;
    }

    /**
     * Reset the checkboxes and notes
     */
    void reset() {
        for(int i = 0; i < 11; i++) {
            CHECKBOXES[i].setChecked(false);
        }
        m_notes.setText("");
    }
}