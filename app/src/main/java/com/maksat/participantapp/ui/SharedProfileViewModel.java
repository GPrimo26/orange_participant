package com.maksat.participantapp.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.maksat.participantapp.models.Profile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SharedProfileViewModel extends ViewModel {
    private final MutableLiveData<Profile> profileMutableLiveData = new MutableLiveData<>();

    public void setProfile(Profile profile){
        profileMutableLiveData.setValue(profile);
    }

    public LiveData<Profile> getProfile(){
        return profileMutableLiveData;
    }

    public String checkNull(String text) {
        if (text!=null){
            if (text.equals("")){
                return "Не указано";
            }else {
                return text;
            }
        }else {
            return "Не указано";
        }
    }

    public String getTime(String date){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", new Locale("ru"));
        try {
            if (date!=null) {
                Date date1 = format.parse(date);
                format.applyPattern("yyyy.MM.dd HH:mm");
                if (date1 != null) {
                    return format.format(date1);
                }
            }else {
                return "Не указано";
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "Не указано";
        }
        return "Не указано";
    }

}
