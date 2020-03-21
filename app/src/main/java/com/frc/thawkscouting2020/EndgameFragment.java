package com.frc.thawkscouting2020;

import com.frc.thawkscouting2020.databinding.EndgamefragmentLayoutBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.lang.ref.WeakReference;

/**
 * Final screen with Checkboxes for climb, control panel, disabling, and cards
 * Display the QR Code
 *
 * @author Aniketh Dandu - FRC Team 1100
 */
public class EndgameFragment extends Fragment {

    // **************************************************
    // Private fields
    // **************************************************

    /**
     * Array of checkboxes present in the Fragment
     */
    private final CheckBox[] CHECKBOXES = new CheckBox[11];

    /**
     * Weak reference to the Main Activity
     */
    private WeakReference<MainActivity> m_mainWeakReference;

    /**
     * Layout binding object
     */
    private EndgamefragmentLayoutBinding m_binding;

    // Retains the instance so the screen does not reset when the orientation changes
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Updates the layout binding value and inflates the layout
        m_binding = EndgamefragmentLayoutBinding.inflate(inflater);

        // Updates the checkbox array
        CHECKBOXES[0] = m_binding.rotationCheckbox;
        CHECKBOXES[1] = m_binding.colorCheckbox;
        CHECKBOXES[2] = m_binding.attemptedClimb;
        CHECKBOXES[3] = m_binding.climbCheckBox;
        CHECKBOXES[4] = m_binding.levelCheckbox;
        CHECKBOXES[5] = m_binding.attemptedDoubleCheckbox;
        CHECKBOXES[6] = m_binding.doubleCheckbox;
        CHECKBOXES[7] = m_binding.brownedOutCheckbox;
        CHECKBOXES[8] = m_binding.disableCheckbox;
        CHECKBOXES[9] = m_binding.yellowCardCheckbox;
        CHECKBOXES[10] = m_binding.redCardCheckbox;

        // Makes the notes text cursor invisible and clears the text
        m_binding.notes.setCursorVisible(false);
        m_binding.notes.setText("");

        // Starts the QR activity when the "QR" button is clicked
        m_binding.qrButton.setOnClickListener((View view) -> {
            setDataViewModel();
            final Intent QR_INTENT = new Intent(getActivity(), QRActivity.class);
            startActivity(QR_INTENT);
        });

        // Checks the attempted climb checkbox when the climb checkbox is checked
        m_binding.climbCheckBox.setOnClickListener((View view) -> {
            if (m_binding.climbCheckBox.isChecked()) m_binding.attemptedClimb.setChecked(true);
        });

        // Checks the attempted double climb checkbox when the double climb checkbox is checked
        m_binding.doubleCheckbox.setOnClickListener((View view) -> {
            if (m_binding.doubleCheckbox.isChecked())
                m_binding.attemptedDoubleCheckbox.setChecked(true);
        });

        // Return the view
        return m_binding.getRoot();
    }

    // Assigns the biding to null when the Fragment is destroyed
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        m_binding = null;
    }

    // **************************************************
    // Package-private methods
    // **************************************************

    /**
     * Resets the current Fragment values and labels
     */
    void resetScreen() {
        for (CheckBox checkBox: CHECKBOXES) checkBox.setChecked(false);
        m_binding.notes.setText("");
    }

    /**
     * Updates the weak reference to the Main Activity
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
     * Sets the values in the View Model
     */
    private void setDataViewModel() {
        final DataViewModel DATA_VIEW_MODEL = m_mainWeakReference.get().DataViewModel;
        final String NAME = m_binding.scouterNameBox.getText().toString();
        final String NOTES = m_binding.notes.getText().toString();
        DATA_VIEW_MODEL.RotationControl.setValue(CHECKBOXES[0].isChecked());
        DATA_VIEW_MODEL.ColorControl.setValue(CHECKBOXES[1].isChecked());
        DATA_VIEW_MODEL.AttemptedClimb.setValue(CHECKBOXES[2].isChecked());
        DATA_VIEW_MODEL.Climb.setValue(CHECKBOXES[3].isChecked());
        DATA_VIEW_MODEL.Level.setValue(CHECKBOXES[4].isChecked());
        DATA_VIEW_MODEL.AttemptedDoubleClimb.setValue(CHECKBOXES[5].isChecked());
        DATA_VIEW_MODEL.DoubleClimb.setValue(CHECKBOXES[6].isChecked());
        DATA_VIEW_MODEL.BrownedOut.setValue(CHECKBOXES[7].isChecked());
        DATA_VIEW_MODEL.Disabled.setValue(CHECKBOXES[8].isChecked());
        DATA_VIEW_MODEL.YellowCard.setValue(CHECKBOXES[9].isChecked());
        DATA_VIEW_MODEL.RedCard.setValue(CHECKBOXES[10].isChecked());
        DATA_VIEW_MODEL.ScouterName.setValue(NAME.equals("") ? "No Scouter Name" : NAME);
        DATA_VIEW_MODEL.Notes.setValue(NOTES.equals("") ? "No Notes" : NOTES);
    }
}