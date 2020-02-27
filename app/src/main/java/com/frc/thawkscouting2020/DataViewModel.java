package com.frc.thawkscouting2020;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;
import java.util.ArrayList;

public class DataViewModel extends ViewModel {
    @NonNull
    MutableLiveData<String>Team = new MutableLiveData<>();
    @NonNull
    MutableLiveData<String>Match = new MutableLiveData<>();
    @NonNull
    MutableLiveData<String>Color = new MutableLiveData<>();
    @NonNull
    MutableLiveData<Boolean>CrossedLine = new MutableLiveData<>();
    @NonNull
    MutableLiveData<int[]>AutoHits = new MutableLiveData<>();
    @NonNull
    MutableLiveData<int[]>AutoMiss = new MutableLiveData<>();
    @NonNull
    MutableLiveData<Integer>Station = new MutableLiveData<>();
    @NonNull
    MutableLiveData<Integer>DefenseOn = new MutableLiveData<>();
    @NonNull
    MutableLiveData<Integer>PlayingDefense = new MutableLiveData<>();
    @NonNull
    MutableLiveData<Integer>Penalties = new MutableLiveData<>();
    @NonNull
    MutableLiveData<int[][]>Cycles = new MutableLiveData<>();
    @NonNull
    MutableLiveData<Boolean>RotationControl = new MutableLiveData<>();
    @NonNull
    MutableLiveData<Boolean>ColorControl = new MutableLiveData<>();
    @NonNull
    MutableLiveData<Boolean>AttemptedClimb = new MutableLiveData<>();
    @NonNull
    MutableLiveData<Boolean>Climb = new MutableLiveData<>();
    @NonNull
    MutableLiveData<Boolean>Level = new MutableLiveData<>();
    @NonNull
    MutableLiveData<Boolean>AttemptedDoubleClimb = new MutableLiveData<>();
    @NonNull
    MutableLiveData<Boolean>DoubleClimb = new MutableLiveData<>();
    @NonNull
    MutableLiveData<Boolean>BrownedOut = new MutableLiveData<>();
    @NonNull
    MutableLiveData<Boolean>Disabled = new MutableLiveData<>();
    @NonNull
    MutableLiveData<Boolean>YellowCard = new MutableLiveData<>();
    @NonNull
    MutableLiveData<Boolean>RedCard = new MutableLiveData<>();
    @NonNull
    MutableLiveData<String>ScouterName = new MutableLiveData<>();
    @NonNull
    MutableLiveData<String>Notes = new MutableLiveData<>();
    @NonNull
    MutableLiveData<String[]>LastCycle = new MutableLiveData<>();
}
