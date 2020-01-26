package com.frc.thawkscouting2020;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.ImageView;

import android.annotation.SuppressLint;
import android.widget.Toast;

public class TeleOpFragment extends Fragment {

    final float[] HORZIONTAL_LINES = {};
    final float[] VERTICAL_LINES = {};

    private long timeElapsedOn, timeElapsedAgainst;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.teleopfragment_layout, container, false);
    }

    @SuppressLint({"ClickableViewAccessibility", "DefaultLocale"})
    @Override
    public void onResume() {
        super.onResume();

        if (getView() != null) {
            final View VIEW = getView();

            final ImageView MAP = VIEW.findViewById(R.id.fieldMap);

            final Chronometer PLAYING_DEFENSE_TIMER = VIEW.findViewById(R.id.againstChronometer);
            final Chronometer ON_DEFENSE_TIMER = VIEW.findViewById(R.id.onChronometer);

            final CheckBox PLAYING_DEFENSE_BOX = VIEW.findViewById(R.id.playingDefenseTimer);
            final CheckBox ON_DEFENSE_BOX = VIEW.findViewById(R.id.onDefenseTimer);

            MAP.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        final float X_PT = motionEvent.getX() + MAP.getLeft();
                        final float Y_PT = motionEvent.getY() + MAP.getTop();
                        // check if (x,y) is on chair and do other staff
                        Toast.makeText(getContext(), String.format("%f, %f", X_PT, Y_PT), Toast.LENGTH_LONG).show();
                    }
                    ;
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
        }
    }

    private void makeToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}