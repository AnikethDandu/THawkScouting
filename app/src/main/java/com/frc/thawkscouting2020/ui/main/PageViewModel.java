package com.frc.thawkscouting2020.ui.main;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class PageViewModel extends ViewModel {

    @NonNull
    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    @NonNull
    private LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {
        @NonNull
        @Override
        public String apply(Integer input) {
            return "Hello world from section: " + input;
        }
    });

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    @NonNull
    public LiveData<String> getText() {
        return mText;
    }
}