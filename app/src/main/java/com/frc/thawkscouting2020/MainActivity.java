package com.frc.thawkscouting2020;

import android.app.Fragment;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.*;
import androidx.lifecycle.*;

import com.frc.thawkscouting2020.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    public static DataViewModel dataViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QRActivity.updateActivity(this);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        final ViewPager VIEW_PAGER = findViewById(R.id.view_pager);
        VIEW_PAGER.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(VIEW_PAGER);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(@NonNull TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    TeleOpFragment.changeBackgroundImage(AutoFragment.s_color, (ImageView) findViewById(R.id.fieldMap));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
    }
    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    public void reset() {
        final AutoFragment autoFragment = (AutoFragment)getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + 0);
        final TeleOpFragment teleOpFragment = (TeleOpFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + 1);
        final EndgameFragment endgameFragment = (EndgameFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + 2);
        autoFragment.reset();
        teleOpFragment.reset();
        endgameFragment.reset();
    }
}
