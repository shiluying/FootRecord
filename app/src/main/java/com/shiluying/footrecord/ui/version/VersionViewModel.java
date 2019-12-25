package com.shiluying.footrecord.ui.version;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VersionViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public VersionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("FootRecord\n" +
                "Version:1.0.0");
    }

    public LiveData<String> getText() {
        return mText;
    }
}