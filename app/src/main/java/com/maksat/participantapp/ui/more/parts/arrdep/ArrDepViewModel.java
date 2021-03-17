package com.maksat.participantapp.ui.more.parts.arrdep;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.maksat.participantapp.Variables;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ArrDepViewModel extends AndroidViewModel {
    private MutableLiveData<String> arrStation;
    private MutableLiveData<String> arrTime;
    private MutableLiveData<String> arrInfo;
    private MutableLiveData<String> depStation;
    private MutableLiveData<String> depTime;
    private MutableLiveData<String> depInfo;

    public ArrDepViewModel(@NonNull Application application) {
        super(application);
        arrStation=new MutableLiveData<>();
        arrTime=new MutableLiveData<>();
        arrInfo=new MutableLiveData<>();
        depStation=new MutableLiveData<>();
        depTime=new MutableLiveData<>();
        depInfo=new MutableLiveData<>();
    }

    /*public MutableLiveData<String> getArrStation() {
        arrStation.setValue(checkNull(Variables.profile.participantArrivalDeparture.arrivalStation));
        return arrStation;
    }

    public MutableLiveData<String> getArrTime() {
        arrTime.setValue(getTime(Variables.profile.participantArrivalDeparture.arrivalDateTime));
        return arrTime;
    }

    public MutableLiveData<String> getArrInfo() {
        arrInfo.setValue(checkNull(Variables.profile.participantArrivalDeparture.arrivalFlightInfo));
        return arrInfo;
    }

    public MutableLiveData<String> getDepStation() {
        depStation.setValue(checkNull(Variables.profile.participantArrivalDeparture.departureStation));
        return depStation;
    }

    public MutableLiveData<String> getDepTime() {
        depTime.setValue(getTime(Variables.profile.participantArrivalDeparture.departureDateTime));
        return depTime;
    }

    public MutableLiveData<String> getDepInfo() {
        depInfo.setValue(checkNull(Variables.profile.participantArrivalDeparture.departureFlightInfo));
        return depInfo;
    }*/

    /*private String checkNull(String text) {
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

    private String getTime(String date){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", new Locale("ru"));
        try {
            if (date!=null) {
                Date date1 = format.parse(date);
                format.applyPattern("HH:mm");
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
    }*/

}