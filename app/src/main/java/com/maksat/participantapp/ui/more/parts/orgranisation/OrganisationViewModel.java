package com.maksat.participantapp.ui.more.parts.orgranisation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.maksat.participantapp.Variables;

public class OrganisationViewModel extends AndroidViewModel {
    private MutableLiveData<String> name;
    private MutableLiveData<String> position;
    private MutableLiveData<String> sphere;
    private MutableLiveData<String> adress;

    public OrganisationViewModel(@NonNull Application application) {
        super(application);
        name=new MutableLiveData<>();
        position=new MutableLiveData<>();
        sphere=new MutableLiveData<>();
        adress=new MutableLiveData<>();
    }

    /*public LiveData<String> getName(){
        if (Variables.profile.company.nameRus.equals("")){
            name.setValue(Variables.profile.company.nameEng);
        }else {
            name.setValue(Variables.profile.company.nameRus);
        }
        return name;
    }

    public LiveData<String> getPosition(){
        if (Variables.profile.company.positionRus.equals("")){
            position.setValue(Variables.profile.company.positionEng);
        }else {
            position.setValue(Variables.profile.company.positionRus);
        }
        return position;
    }

    public LiveData<String> getSphere(){
        if (Variables.profile.company.sphere.nameRus.equals("")){
            sphere.setValue(Variables.profile.company.sphere.nameEng);
        }else {
            sphere.setValue(Variables.profile.company.sphere.nameRus);
        }
        return sphere;
    }

    public LiveData<String> getAddress(){
            adress.setValue(Variables.profile.company.postAddress);
        return adress;
    }*/
}