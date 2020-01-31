package com.frc.thawkscouting2020;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.List;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRActivity extends AppCompatActivity {

    private DataViewModel dataViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        dataViewModel = MainActivity.dataViewModel;
        dataViewModel.RotationControl.setValue(EndgameFragment.CHECKBOXES[0].isChecked());
        dataViewModel.ColorControl.setValue(EndgameFragment.CHECKBOXES[1].isChecked());
        dataViewModel.Climb.setValue(EndgameFragment.CHECKBOXES[2].isChecked());
        dataViewModel.Level.setValue(EndgameFragment.CHECKBOXES[3].isChecked());
        dataViewModel.DoubleClimb.setValue(EndgameFragment.CHECKBOXES[4].isChecked());
        dataViewModel.Park.setValue(EndgameFragment.CHECKBOXES[5].isChecked());
        dataViewModel.BrownedOut.setValue(EndgameFragment.CHECKBOXES[6].isChecked());
        dataViewModel.Disabled.setValue(EndgameFragment.CHECKBOXES[7].isChecked());
        dataViewModel.YellowCard.setValue(EndgameFragment.CHECKBOXES[8].isChecked());
        dataViewModel.RedCard.setValue(EndgameFragment.CHECKBOXES[9].isChecked());
        dataViewModel.Notes.setValue(EndgameFragment.NOTES.getText().toString());

        /* GETS RID OF HEADER */
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();;
        }

        final DisplayMetrics DISPLAY_METRICS = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(DISPLAY_METRICS);
        final int HEIGHT = DISPLAY_METRICS.heightPixels;
        final int WIDTH = DISPLAY_METRICS.widthPixels;
        getWindow().setLayout((int)(WIDTH*0.6), (int)(HEIGHT*0.6));

        final Button DONE_BUTTON = findViewById(R.id.doneButton);
        DONE_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final ImageView QR_CODE = findViewById(R.id.qrCode);
        final MultiFormatWriter MULTI_FORMAT_WRITER = new MultiFormatWriter();
        try {
            /* Encode the data into a bit matrix */
            final BitMatrix BIT_MATRIX = MULTI_FORMAT_WRITER.encode(returnDataString(), BarcodeFormat.QR_CODE, 500, 500);
            final BarcodeEncoder BARCODE_ENCODER = new BarcodeEncoder();
            final Bitmap BIT_MAP = BARCODE_ENCODER.createBitmap(BIT_MATRIX);
            /* Set the QR code */
            QR_CODE.setImageBitmap(BIT_MAP);
        } catch (WriterException e) {
            /* Display the error to the user as a toast */
            Toast.makeText(QRActivity.this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private String convertArray(int[] array) {
        String finalString = "";
        for (int i = 0; i < array.length; i++) {
            finalString += array[i];
            if (i != array.length-1) {
                finalString += ",";
            }
        }
        return finalString;
    }

    private String convertCycles(int[][] cycles) {
        StringBuilder cycleString = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            for (int x = 0; x < 5; x++) {
                cycleString.append(cycles[i][x]);
                cycleString.append(",");
            }
        }
        return cycleString.toString();
    }

    private String returnDataString() {
        dataViewModel.Cycles.setValue(TeleOpFragment.CyclesWithPositions);
        final String[] DATA = new String[]{
                dataViewModel.Team.getValue(),
                dataViewModel.Match.getValue(),
                dataViewModel.Color.getValue(),
                convertArray(dataViewModel.AutoHits.getValue()),
                convertArray(dataViewModel.AutoMiss.getValue()),
                String.valueOf(dataViewModel.Station.getValue()),
                String.valueOf(dataViewModel.PlayingDefense.getValue()),
                String.valueOf(dataViewModel.DefenseOn.getValue()),
                String.valueOf(dataViewModel.Penalties.getValue()),
                convertCycles(dataViewModel.Cycles.getValue()),
                String.valueOf(dataViewModel.RotationControl.getValue()),
                String.valueOf(dataViewModel.ColorControl.getValue()),
                String.valueOf(dataViewModel.Climb.getValue()),
                String.valueOf(dataViewModel.Level.getValue()),
                String.valueOf(dataViewModel.DoubleClimb.getValue()),
                String.valueOf(dataViewModel.Park.getValue()),
                String.valueOf(dataViewModel.BrownedOut.getValue()),
                String.valueOf(dataViewModel.Disabled.getValue()),
                String.valueOf(dataViewModel.YellowCard.getValue()),
                String.valueOf(dataViewModel.RedCard.getValue()),
                String.valueOf(dataViewModel.Notes.getValue())
        };

        /* Append the data to a string builder */
        final StringBuilder WORKING_TEXT = new StringBuilder();
        for (String s : DATA) {
            WORKING_TEXT.append(s);
            WORKING_TEXT.append(",");
        }
        WORKING_TEXT.delete(WORKING_TEXT.length()-1, WORKING_TEXT.length());
        /* Return the data as a string */
        System.out.print(WORKING_TEXT.toString());
        return WORKING_TEXT.toString();
    }
}
