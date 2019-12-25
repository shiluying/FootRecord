package com.shiluying.footrecord.ui.home;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shiluying.footrecord.activity.MainActivity;
import com.shiluying.footrecord.database.DBHelper;
import com.shiluying.footrecord.database.RecordProvider;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Welcome to footrecord!");
    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData setRecord(MainActivity mainActivity, ContentValues contentValues){
        Uri uri = mainActivity.getContentResolver().insert(
                RecordProvider.CONTENT_URI, contentValues);
        return mText;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getRecord(MainActivity mainActivity){
        mainActivity.getContentResolver().query(RecordProvider.CONTENT_URI, DBHelper.RECORD_TABLE_COLUMNS,null,null);
    }
}