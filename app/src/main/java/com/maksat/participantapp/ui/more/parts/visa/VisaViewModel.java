package com.maksat.participantapp.ui.more.parts.visa;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.maksat.participantapp.Variables;

public class VisaViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> hasVisa;

    public VisaViewModel(Application application) {
        super(application);
        hasVisa=new MutableLiveData<>();
    }

    public LiveData<Boolean> hasVisa(){
        //hasVisa.setValue(Variables.profile.visa);
        return hasVisa;
    }
}