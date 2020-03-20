package com.frc.thawkscouting2020;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.Toast;

import com.frc.thawkscouting2020.databinding.ActivityQrBinding;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.lang.ref.WeakReference;
import java.util.Objects;

public class QRActivity extends AppCompatActivity {

    // TODO: Change view binding
    // TODO: DATA BINDING

    private DataViewModel m_dataViewModel;
    private static WeakReference<MainActivity> s_mainWeakReference;
    private static WeakReference<TeleOpFragment> s_teleOpWeakReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityQrBinding BINDING = ActivityQrBinding.inflate(getLayoutInflater());
        final View VIEW = BINDING.getRoot();
        setContentView(VIEW);

        m_dataViewModel = s_mainWeakReference.get().dataViewModel;

        try
        {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        }
        catch (NullPointerException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        final DisplayMetrics DISPLAY_METRICS = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(DISPLAY_METRICS);
        getWindow().setLayout((int)(DISPLAY_METRICS.widthPixels*0.6), (int)(DISPLAY_METRICS.heightPixels*0.6));

        final Button DONE_BUTTON = BINDING.doneButton;
        DONE_BUTTON.setOnClickListener((View v) -> finish());

        final ImageView QR_CODE = BINDING.qrCode;
        final MultiFormatWriter MULTI_FORMAT_WRITER = new MultiFormatWriter();

        final Bitmap[] BIT_MAP = new Bitmap[1];
        new Thread(() -> {
            try {
                final BitMatrix BIT_MATRIX = MULTI_FORMAT_WRITER.encode(returnDataString(), BarcodeFormat.QR_CODE, 500, 500);
                final BarcodeEncoder BARCODE_ENCODER = new BarcodeEncoder();
                BIT_MAP[0] = BARCODE_ENCODER.createBitmap(BIT_MATRIX);

            } catch (WriterException e) {
                Toast.makeText(QRActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
            QR_CODE.post(() -> runOnUiThread(() -> QR_CODE.setImageBitmap(BIT_MAP[0])));
        }).start();
        s_mainWeakReference.get().reset();
    }

    private String convertArrayToCSVString(int[] array) {
        StringBuilder finalString = new StringBuilder();
        for(int arrayItem: array) {
            finalString.append(arrayItem).append(",");
        }
        finalString.delete(finalString.length()-1, finalString.length());
        return finalString.toString();
    }

    private String convertCyclesToCSVString(int[][] cycles) {
        StringBuilder cycleString = new StringBuilder();
        for (int[] position: cycles) {
            for(int score: position) {
                cycleString.append(score).append(",");
            }
        }
        cycleString.delete(cycleString.length()-1, cycleString.length());
        return cycleString.toString();
    }

    private String returnDataString() {
        m_dataViewModel.Cycles.setValue(s_teleOpWeakReference.get().CyclesWithPositions);
        final String[] DATA = {
                m_dataViewModel.Team.getValue(),
                m_dataViewModel.Match.getValue(),
                m_dataViewModel.Color.getValue(),
                String.valueOf(m_dataViewModel.Station.getValue()),
                String.valueOf(m_dataViewModel.CrossedLine.getValue()),
                convertArrayToCSVString(Objects.requireNonNull(m_dataViewModel.AutoHits.getValue())),
                convertArrayToCSVString(Objects.requireNonNull(m_dataViewModel.AutoMiss.getValue())),
                String.valueOf(m_dataViewModel.PlayingDefense.getValue()),
                String.valueOf(m_dataViewModel.DefenseOn.getValue()),
                String.valueOf(m_dataViewModel.Penalties.getValue()),
                convertCyclesToCSVString(Objects.requireNonNull(m_dataViewModel.Cycles.getValue())),
                String.valueOf(m_dataViewModel.RotationControl.getValue()),
                String.valueOf(m_dataViewModel.ColorControl.getValue()),
                String.valueOf(m_dataViewModel.AttemptedClimb.getValue()),
                String.valueOf(m_dataViewModel.Climb.getValue()),
                String.valueOf(m_dataViewModel.Level.getValue()),
                String.valueOf(m_dataViewModel.AttemptedDoubleClimb.getValue()),
                String.valueOf(m_dataViewModel.DoubleClimb.getValue()),
                String.valueOf(m_dataViewModel.BrownedOut.getValue()),
                String.valueOf(m_dataViewModel.Disabled.getValue()),
                String.valueOf(m_dataViewModel.YellowCard.getValue()),
                String.valueOf(m_dataViewModel.RedCard.getValue()),
                String.valueOf(m_dataViewModel.ScouterName.getValue()),
                String.valueOf(m_dataViewModel.Notes.getValue())
        };

        final StringBuilder WORKING_TEXT = new StringBuilder();
        for (String value : DATA) {
            WORKING_TEXT.append(value).append(",");
        }
        WORKING_TEXT.delete(WORKING_TEXT.length()-1, WORKING_TEXT.length());
        return WORKING_TEXT.toString();
    }

    static void updateWeakReferences(MainActivity activity, TeleOpFragment teleOpFragment) {
        s_mainWeakReference = new WeakReference<>(activity);
        s_teleOpWeakReference = new WeakReference<>(teleOpFragment);
    }
}