package com.frc.thawkscouting2020;

import android.os.Bundle;

import com.frc.thawkscouting2020.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import androidx.lifecycle.*;

import com.frc.thawkscouting2020.ui.main.SectionsPagerAdapter;

/**
 * Main Activity used to set weak references and Fragment references
 *
 * @author Aniketh Dandu - FRC Team 1100
 */
public class MainActivity extends AppCompatActivity {

    // **************************************************
    // Package-private fields
    // **************************************************

    /**
     * The View Model used to store data across the Fragments and Activities
     */
    DataViewModel DataViewModel;

    // **************************************************
    // Private fields
    // **************************************************

    /**
     * The Autonomous Fragment
     */
    private AutoFragment m_autoFragment;

    /**
     * The Tele-Op Fragment
     */
    private TeleOpFragment m_teleopFragment;

    /**
     * The Endgame Fragment
     */
    private EndgameFragment m_endgameFragment;

    // Creates the View Model and changes the field image depending on the Alliance color
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding BINDING = ActivityMainBinding.inflate(getLayoutInflater());
        final View VIEW = BINDING.getRoot();
        setContentView(VIEW);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        final ViewPager VIEW_PAGER = BINDING.viewPager;
        VIEW_PAGER.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = BINDING.tabs;
        tabs.setupWithViewPager(VIEW_PAGER);

        DataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(@NonNull TabLayout.Tab tab) {
                if (tab.getPosition() == 1)
                    m_teleopFragment.changeBackgroundImage(m_autoFragment.Color);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    // Updates the weak references when each Fragment is assigned to the Main Activity
    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        assert fragment.getTag() != null;
        switch (fragment.getTag()) {
            case ("android:switcher:" + R.id.view_pager + ":" + 0):
                m_autoFragment = (AutoFragment)getSupportFragmentManager().findFragmentByTag(fragment.getTag());
                assert m_autoFragment != null;
                m_autoFragment.updateWeakReferences(MainActivity.this);
                break;
            case ("android:switcher:" + R.id.view_pager + ":" + 1):
                m_teleopFragment = (TeleOpFragment) getSupportFragmentManager().findFragmentByTag(fragment.getTag());
                assert m_teleopFragment != null;
                m_teleopFragment.updateWeakReferences(MainActivity.this);
                break;
            case ("android:switcher:" + R.id.view_pager + ":" + 2):
                m_endgameFragment = (EndgameFragment) getSupportFragmentManager().findFragmentByTag(fragment.getTag());
                assert m_endgameFragment != null;
                m_endgameFragment.updateWeakReferences(MainActivity.this);
                break;
        }
        QRActivity.updateWeakReferences(MainActivity.this, m_teleopFragment);
        CycleActivity.updateWeakReference(MainActivity.this, m_teleopFragment);
    }

    // **************************************************
    // Public methods
    // **************************************************

    /**
     * Resets all values and texts of 3 tabs
     */
    public void reset() {
        m_autoFragment.resetScreen();
        m_teleopFragment.resetScreen();
        m_endgameFragment.resetScreen();
    }
}
