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
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.frc.thawkscouting2020.databinding.AutofragmentLayoutBinding;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Stores information about the team, match, color, and autonomous routine
 *
 * @author Aniketh Dandu - FRC Team 1100
 */
public class AutoFragment extends Fragment {

    // TODO: Clean up code

    private final ArrayList<String> USER_ACTIONS = new ArrayList<String>() {};
    private int[] m_powerCellHits = {0, 0, 0};
    private int[] m_powerCellMiss = {0, 0};
    private DataViewModel m_dataViewModel;
    private WeakReference<MainActivity> m_mainWeakReference;
    private AutofragmentLayoutBinding m_binding;
    String Color = "BLUE";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        m_binding = AutofragmentLayoutBinding.inflate(inflater);
        m_dataViewModel = m_mainWeakReference.get().dataViewModel;

        USER_ACTIONS.add("Block");

        m_binding.driverStationGroup.check(m_binding.stationOneButton.getId());
        m_binding.allianceButton.setChecked(false);
        setAllianceColor(m_binding.allianceButton);
        m_dataViewModel.Team.setValue(m_binding.teamTextbox.getText().toString());
        m_dataViewModel.Color.setValue(m_binding.allianceButton.getText().toString());
        m_dataViewModel.Match.setValue(m_binding.matchTextbox.getText().toString());
        m_dataViewModel.Station.setValue(1);
        m_dataViewModel.CrossedLine.setValue(m_binding.crossedLineCheckbox.isChecked());
        setButtonLabels();

        m_binding.teamTextbox.setOnEditorActionListener((TextView textView, int actionId, KeyEvent keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                m_dataViewModel.Team.setValue(textView.getText().toString());
                return true;
            }
            return false;
        });

        m_binding.matchTextbox.setOnEditorActionListener((TextView textView, int actionId, KeyEvent keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                m_dataViewModel.Match.setValue(textView.getText().toString());
                return true;
            }
            return false;
        });

        m_binding.crossedLineCheckbox.setOnClickListener((View view) ->  m_dataViewModel.CrossedLine.setValue(m_binding.crossedLineCheckbox.isChecked()));

        m_binding.allianceButton.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> setAllianceColor(m_binding.allianceButton));

        m_binding.matchTextbox.setOnEditorActionListener((TextView textView, int actionId, KeyEvent keyEvent) -> {
            m_binding.matchTextbox.setText(textView.getText().toString().equals("")
                    ? "0"
                    : textView.getText().toString());
            m_dataViewModel.Match.setValue(textView.getText().toString());
            return false;
        });

        m_binding.teamTextbox.setOnEditorActionListener((TextView textView, int actionId, KeyEvent keyevent) -> {
            m_binding.teamTextbox.setText(textView.getText().toString().equals("")
                    ? "9999"
                    : textView.getText().toString());
            m_dataViewModel.Team.setValue(textView.getText().toString());
            return false;
        });

        m_binding.driverStationGroup.setOnCheckedChangeListener((RadioGroup radioGroup, int actionId) -> {
            final int DRIVER_STATION_NUMBER = Integer.parseInt(m_binding.getRoot().findViewById(actionId).getTag().toString());
            m_dataViewModel.Station.setValue(DRIVER_STATION_NUMBER);
        });

        m_binding.innerAutoButton.setOnClickListener((View view) -> addShot(0, true));
        m_binding.outerAutoButton.setOnClickListener((View view) -> addShot(1, true));
        m_binding.bottomAutoButton.setOnClickListener((View view) -> addShot(2, true));
        m_binding.autoHighMissButton.setOnClickListener((View view) -> addShot(0, false));
        m_binding.autoLowMissButton.setOnClickListener((View view) -> addShot(1, false));

        m_binding.allianceButton.setOnClickListener((View view) -> {
            final String LAST_ACTION = USER_ACTIONS.get(USER_ACTIONS.size()-1);
            if (!LAST_ACTION.equals("Block")) {
                final int INDEX = Integer.parseInt(LAST_ACTION.substring(LAST_ACTION.length()-1));
                if (LAST_ACTION.substring(0, 3).equals("hit")) {
                    m_powerCellHits[INDEX]--;
                } else {
                    m_powerCellMiss[INDEX]--;
                }
                setButtonLabels();
                USER_ACTIONS.remove(USER_ACTIONS.size()-1);
            }
        });

        return m_binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        m_binding = null;
    }

    void reset() {
        m_powerCellHits = new int[]{0, 0, 0};
        m_powerCellMiss = new int[]{0, 0, 0};
        m_binding.matchTextbox.setText("");
        m_binding.teamTextbox.setText("");
        setButtonLabels();
    }

    private void addShot(int index, boolean scored) {
        if (scored) {
            m_powerCellHits[index]++;
        } else {
            m_powerCellMiss[index]++;
        }
        USER_ACTIONS.add(((scored) ? "hit" : "miss") + index);
        setButtonLabels();
    }

    private void setDataViewModel() {
        m_dataViewModel.AutoHits.setValue(m_powerCellHits);
        m_dataViewModel.AutoMiss.setValue(m_powerCellMiss);
    }

    @SuppressLint("SetTextI18n")
    private void setButtonLabels() {
        m_binding.innerAutoButton.setText("INNER: " + m_powerCellHits[0]);
        m_binding.outerAutoButton.setText("OUTER: " + m_powerCellHits[1]);
        m_binding.bottomAutoButton.setText("BOTTOM: " + m_powerCellHits[2]);
        m_binding.autoHighMissButton.setText("HIGH: " + m_powerCellMiss[0]);
        m_binding.autoLowMissButton.setText("LOW: " + m_powerCellMiss[1]);
        setDataViewModel();
    }

    private void setAllianceColor(ToggleButton toggleButton) {
        toggleButton.setBackgroundColor(toggleButton.isChecked()
                        ? getResources().getColor(R.color.backgroundRed)
                        : getResources().getColor(R.color.backgroundBlue));
        Color = toggleButton.isChecked() ? "RED" : "BLUE";
        m_dataViewModel.Color.setValue(Color);
    }

    void updateWeakReferences(MainActivity mainActivity) {
        m_mainWeakReference = new WeakReference<>(mainActivity);
    }
}