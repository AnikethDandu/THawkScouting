package com.frc.thawkscouting2020;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;
import java.util.ArrayList;

public class DataViewModel extends ViewModel {
    MutableLiveData<String>Team = new MutableLiveData<>();
    MutableLiveData<String>Match = new MutableLiveData<>();
    MutableLiveData<String>Color = new MutableLiveData<>();
    MutableLiveData<int[]>AutoHits = new MutableLiveData<>();
    MutableLiveData<int[]>AutoMiss = new MutableLiveData<>();
    MutableLiveData<Integer>Station = new MutableLiveData<>();
    MutableLiveData<Integer>DefenseOn = new MutableLiveData<>();
    MutableLiveData<Integer>PlayingDefense = new MutableLiveData<>();
    MutableLiveData<Integer>Penalties = new MutableLiveData<>();
    MutableLiveData<int[][]>Cycles = new MutableLiveData<>();
    MutableLiveData<Boolean>RotationControl = new MutableLiveData<>();
    MutableLiveData<Boolean>ColorControl = new MutableLiveData<>();
    MutableLiveData<Boolean>AttemptedClimb = new MutableLiveData<>();
    MutableLiveData<Boolean>Climb = new MutableLiveData<>();
    MutableLiveData<Boolean>Level = new MutableLiveData<>();
    MutableLiveData<Boolean>AttemptedDoubleClimb = new MutableLiveData<>();
    MutableLiveData<Boolean>DoubleClimb = new MutableLiveData<>();
    MutableLiveData<Boolean>BrownedOut = new MutableLiveData<>();
    MutableLiveData<Boolean>Disabled = new MutableLiveData<>();
    MutableLiveData<Boolean>YellowCard = new MutableLiveData<>();
    MutableLiveData<Boolean>RedCard = new MutableLiveData<>();
    MutableLiveData<String>Notes = new MutableLiveData<>();
    MutableLiveData<String[]>LastCycle = new MutableLiveData<>();
}
