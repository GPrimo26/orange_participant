package com.maksat.participantapp.ui.schedule;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.maksat.participantapp.Requests;
import com.maksat.participantapp.Variables;
import com.maksat.participantapp.interfaces.Event;
import com.maksat.participantapp.interfaces.Server;
import com.maksat.participantapp.models.ErrorBody;
import com.maksat.participantapp.models.EventProgram;
import com.maksat.participantapp.models.EventProgramByDays;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleViewModel extends AndroidViewModel {

    private final MutableLiveData<List<EventProgramByDays>> mProgramByDays;
    private OnInfoLoadListener mOnInfoLoadedListener;
    private FragmentActivity activity;
    public ScheduleViewModel(@NonNull Application application) {
        super(application);
        mProgramByDays = new MutableLiveData<>();
    }

    public void setActivity(FragmentActivity activity) {
        this.activity=activity;
    }


    public interface OnInfoLoadListener {
        void isLoading();
        void infoLoaded();
    }
    public void setOnInfoLoadListener(OnInfoLoadListener listener){
        mOnInfoLoadedListener=listener;
    }

    public LiveData<List<EventProgramByDays>> getEventProgram() {
        mOnInfoLoadedListener.isLoading();
        Requests preloadInfo=new Requests(activity);
        if (Variables.token!=null) {
            preloadInfo.getCategories();
            Event eventApi = Server.GetServerWithToken(Event.class, Variables.token.access_token);
            Call<List<EventProgram>> eventProgramCall = eventApi.getEventProgram(Variables.token.eventId);
            eventProgramCall.enqueue(new Callback<List<EventProgram>>() {
                @Override
                public void onResponse(@NonNull Call<List<EventProgram>> call, @NonNull Response<List<EventProgram>> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null && response.body().size() != 0) {
                            List<Date> dates = new ArrayList<>();
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", new Locale("ru"));
                            for (int i = 0; i < response.body().size(); i++) {
                                String dateStr = response.body().get(i).getDateTimeStart();
                                try {
                                    Date date = format.parse(dateStr);
                                    dates.add(date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            Collections.sort(dates);
                            List<String> datesList = new ArrayList<>();
                            format.applyPattern("yyyy-MM-dd");
                            for (int i = 0; i < response.body().size(); i++) {
                                String dateStr;
                                if (dates.get(i) != null) {
                                    dateStr = format.format(dates.get(i));
                                    int flag = 0;
                                    for (int j = 0; j < datesList.size(); j++) {
                                        if (dateStr.equals(datesList.get(j))) {
                                            flag = 1;
                                            break;
                                        }
                                    }
                                    if (flag == 0) {
                                        datesList.add(dateStr);
                                    }
                                }
                            }
                            Variables.datesList = datesList;
                            List<EventProgramByDays> programByDays = new ArrayList<>();
                            for (int i = 0; i < datesList.size(); i++) {
                                List<EventProgram> tempEvents = new ArrayList<>();
                                for (int j = 0; j < response.body().size(); j++) {
                                    String dateStr = response.body().get(j).getDateTimeStart();
                                    format.applyPattern("yyyy-MM-dd'T'HH:mm:ss");
                                    try {
                                        Date date = format.parse(dateStr);
                                        format.applyPattern("yyyy-MM-dd");
                                        if (date != null) {
                                            dateStr = format.format(date);
                                            if (dateStr.equals(datesList.get(i))) {
                                                tempEvents.add(response.body().get(j));
                                            }
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                                programByDays.add(new EventProgramByDays(datesList.get(i), tempEvents));
                            }
                            mProgramByDays.setValue(programByDays);
                        }

                    } else {
                        try {
                            if (response.errorBody() != null) {
                                Gson gson = new Gson();
                                ErrorBody errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
                                Toast.makeText(getApplication(), errorBody.Ru, Toast.LENGTH_SHORT).show();
                                Log.d("EPROGRAM_RESP_ERROR", "" + errorBody.Ru);
                            } else {
                                Log.d("EPROGRAM_RESP_ERROR", "ErrorBody is null.");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mProgramByDays.setValue(new ArrayList<>());
                    }
                    mOnInfoLoadedListener.infoLoaded();
                }

                @Override
                public void onFailure(@NonNull Call<List<EventProgram>> call, @NonNull Throwable t) {
                    Toast.makeText(getApplication(), "Ошибка сервера.", Toast.LENGTH_SHORT).show();
                    Log.d("EPROGRAM_SERVER_ERROR", "" + t.getMessage());
                    mProgramByDays.setValue(new ArrayList<>());
                    mOnInfoLoadedListener.infoLoaded();
                }
            });
        }
        return mProgramByDays;
    }
}