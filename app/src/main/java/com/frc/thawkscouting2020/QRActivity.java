package com.frc.thawkscouting2020;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.view.View;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.frc.thawkscouting2020.databinding.ActivityQrBinding;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.lang.ref.WeakReference;
import java.util.Objects;

/**
 * Activity that creates a QR code with all the data from previous tabs
 *
 * @author Aniketh Dandu - FRC Team 1100
 */
public class QRActivity extends AppCompatActivity {

    // **************************************************
    // private fields
    // **************************************************

    /**
     * Weak reference of Main Activity
     */
    private static WeakReference<MainActivity> s_mainWeakReference;

    /**
     * Weak reference of Tele Op Fragment
     */
    private static WeakReference<TeleOpFragment> s_teleOpWeakReference;

    /**
     * View Model used to store data across screens
     */
    private DataViewModel m_dataViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityQrBinding BINDING = ActivityQrBinding.inflate(getLayoutInflater());
        final View VIEW = BINDING.getRoot();
        setContentView(VIEW);

        // Set the View Model
        m_dataViewModel = s_mainWeakReference.get().DataViewModel;

        // Disable the header
        try
        {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        }
        catch (NullPointerException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        // Set the window size
        final DisplayMetrics DISPLAY_METRICS = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(DISPLAY_METRICS);
        getWindow().setLayout((int)(DISPLAY_METRICS.widthPixels*0.6),
                (int)(DISPLAY_METRICS.heightPixels*0.6));

        // Close the activity when the "Done" button is pressed
        BINDING.doneButton.setOnClickListener((View v) -> finish());

        // Create a QR code on a separate thread using the data String and post it on the UI thread
        final MultiFormatWriter MULTI_FORMAT_WRITER = new MultiFormatWriter();
        final Bitmap[] BIT_MAP = new Bitmap[1];
        new Thread(() -> {
            try {
                final BitMatrix BIT_MATRIX = MULTI_FORMAT_WRITER.encode(returnDataString(),
                        BarcodeFormat.QR_CODE, 500, 500);
                final BarcodeEncoder BARCODE_ENCODER = new BarcodeEncoder();
                BIT_MAP[0] = BARCODE_ENCODER.createBitmap(BIT_MATRIX);

            } catch (WriterException e) {
                Toast.makeText(QRActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
            BINDING.qrCode.post(() -> runOnUiThread(() -> BINDING.qrCode.setImageBitmap(
                    BIT_MAP[0])));
        }).start();

        // Reset all of the screens
        s_mainWeakReference.get().reset();
    }

    // **************************************************
    // Private methods
    // **************************************************

    /**
     * Takes an array of autonomous shots and returns a String
     *
     * @param array Array of integers of shots
     * @return Returns String with the array values
     */
    private String convertArrayToCSVString(int[] array) {
        StringBuilder finalString = new StringBuilder();
        for(int arrayItem: array) {
            finalString.append(arrayItem).append(",");
        }
        finalString.delete(finalString.length()-1, finalString.length());
        return finalString.toString();
    }

    /**
     * Takes a 2D array of the cycles from each position on the field and returns a String
     *
     * @param cycles 2D Array of integers of cycles from every position
     * @return Returns String with 2D array values
     */
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

    /**
     * Takes all the values from all 3 screens and returns them in a String format
     *
     * @return Returns String with all values
     */
    private String returnDataString() {
        m_dataViewModel.Cycles.setValue(s_teleOpWeakReference.get().CyclesWithPositions);
        final String[] DATA = {
                m_dataViewModel.Team.getValue(),
                m_dataViewModel.Match.getValue(),
                m_dataViewModel.Color.getValue(),
                String.valueOf(m_dataViewModel.Station.getValue()),
                String.valueOf(m_dataViewModel.CrossedLine.getValue()),
                convertArrayToCSVString(Objects.requireNonNull(
                        m_dataViewModel.AutoHits.getValue())),
                convertArrayToCSVString(Objects.requireNonNull(
                        m_dataViewModel.AutoMiss.getValue())),
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

    // **************************************************
    // Static methods
    // **************************************************

    /**
     * Gets weak references to the Main Activity and Tele-Op Fragment
     *
     * @param mainActivity MainActivity
     * @param teleOpFragment TeleOpFragment
     */
    static void updateWeakReferences(MainActivity mainActivity, TeleOpFragment teleOpFragment) {
        s_mainWeakReference = new WeakReference<>(mainActivity);
        s_teleOpWeakReference = new WeakReference<>(teleOpFragment);
    }
}