package com.maksat.participantapp.ui.b2b;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class B2BViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public B2BViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is B2B fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setText(String text){
        mText.setValue(text);
    }
}