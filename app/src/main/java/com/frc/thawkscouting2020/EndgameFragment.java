package com.frc.thawkscouting2020;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class EndgameFragment extends Fragment {

    private DataViewModel dataViewModel;
    static CheckBox[] CHECKBOXES = new CheckBox[10];
    static EditText NOTES;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dataViewModel = MainActivity.dataViewModel;

        final View VIEW =  inflater.inflate(R.layout.endgamefragment_layout, container, false);

        CHECKBOXES[0] = VIEW.findViewById(R.id.rotationCheckbox);
        CHECKBOXES[1] = VIEW.findViewById(R.id.colorCheckbox);
        CHECKBOXES[2] = VIEW.findViewById(R.id.climbCheckBox);
        CHECKBOXES[3] = VIEW.findViewById(R.id.levelCheckbox);
        CHECKBOXES[4] = VIEW.findViewById(R.id.doubleCheckbox);
        CHECKBOXES[5] = VIEW.findViewById(R.id.parkCheckbox);
        CHECKBOXES[6] = VIEW.findViewById(R.id.brownedOutCheckbox);
        CHECKBOXES[7] = VIEW.findViewById(R.id.disableCheckbox);
        CHECKBOXES[8] = VIEW.findViewById(R.id.yellowCardCheckbox);
        CHECKBOXES[9] = VIEW.findViewById(R.id.redCardCheckbox);
        NOTES = VIEW.findViewById(R.id.notes);
        NOTES.setText("");

        final Button QR_BUTTON = VIEW.findViewById(R.id.qrButton);
        QR_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent QR_INTENT = new Intent(getActivity(), QRActivity.class);
                startActivity(QR_INTENT);
            }
        });

        return VIEW;
    }
}