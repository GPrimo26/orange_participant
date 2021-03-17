package com.maksat.participantapp.ui.more.parts.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.maksat.participantapp.models.Profile;

public class ProfileViewModel extends AndroidViewModel {
    private MutableLiveData<Profile> profileMutableLiveData;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        profileMutableLiveData=new MutableLiveData<>();
    }

    public LiveData<Profile> getProfile(){
        /*Requests preloadInfo=new Requests(getApplication(), getApplication());
        preloadInfo.getProfile();
        preloadInfo.setOnProfileLoadedListener(() -> profileMutableLiveData.setValue(Variables.profile));*/
        return profileMutableLiveData;
    }
}