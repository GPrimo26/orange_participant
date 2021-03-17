package com.maksat.participantapp.ui.more.parts.arrdep;

import androidx.core.util.Pair;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.maksat.participantapp.R;
import com.maksat.participantapp.models.EditableProfile;
import com.maksat.participantapp.models.Profile;
import com.maksat.participantapp.ui.SharedProfileViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ArrDepFragment extends Fragment {

    private ArrDepViewModel mViewModel;
    private SharedProfileViewModel sharedProfileViewModel;

    public static ArrDepFragment newInstance() {
        return new ArrDepFragment();
    }

    private MaterialButton back_btn;
    private EditText arrStation_et, arrInfo_et, depStation_et, depInfo_et;
    private TextView arrDate_tv, depDate_tv;
    private Calendar myCalendar=Calendar.getInstance();
    private Integer textFlag;
    private EditableProfile.ArrivalDeparture arrivalDeparture;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_arr_dep, container, false);
        sharedProfileViewModel=new ViewModelProvider(requireActivity()).get(SharedProfileViewModel.class);
        if (sharedProfileViewModel.getProfile().getValue()!=null) {
            arrivalDeparture= new EditableProfile.ArrivalDeparture(sharedProfileViewModel.getProfile().getValue());
        }else {
            arrivalDeparture= new EditableProfile.ArrivalDeparture(null, null, null, null, null, null);
        }
        findIDs(view);
        setActions();
        return view;
    }

    private void findIDs(View view) {
        back_btn=view.findViewById(R.id.back_btn);
        arrStation_et=view.findViewById(R.id.arr_station_et);
        arrDate_tv=view.findViewById(R.id.arr_time_et);
        arrInfo_et=view.findViewById(R.id.arr_info_et);
        depStation_et=view.findViewById(R.id.dep_station_et);
        depDate_tv=view.findViewById(R.id.dep_time_et);
        depInfo_et=view.findViewById(R.id.dep_info_et);
    }

    private void setActions(){
        back_btn.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.action_arrDepFragment_to_navigation_more);
        });
        sharedProfileViewModel.getProfile().observe(getViewLifecycleOwner(), new Observer<Profile>() {
            @Override
            public void onChanged(Profile profile) {
                arrStation_et.setText(sharedProfileViewModel.checkNull(profile.participantArrivalDeparture.arrivalStation));
                arrDate_tv.setText(sharedProfileViewModel.getTime(profile.participantArrivalDeparture.arrivalDateTime));
                arrInfo_et.setText(sharedProfileViewModel.checkNull(profile.participantArrivalDeparture.arrivalFlightInfo));
                depStation_et.setText(sharedProfileViewModel.checkNull(profile.participantArrivalDeparture.departureStation));
                depDate_tv.setText(sharedProfileViewModel.getTime(profile.participantArrivalDeparture.departureDateTime));
                depInfo_et.setText(sharedProfileViewModel.checkNull(profile.participantArrivalDeparture.departureFlightInfo));
            }
        });
        TimePickerDialog.OnTimeSetListener time= (view1, hourOfDay, minute) -> {
            myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            myCalendar.set(Calendar.MINUTE, minute);
            updateLabel();
        };
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            new TimePickerDialog(requireActivity(), time, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
        };

        arrDate_tv.setOnClickListener(v -> {
            myCalendar=Calendar.getInstance();
            textFlag=1;
            new DatePickerDialog(requireActivity(), date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        });
        depDate_tv.setOnClickListener(v -> {
            myCalendar=Calendar.getInstance();
            textFlag=2;
            new DatePickerDialog(requireActivity(), date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void updateLabel() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", new Locale("RU"));
        switch (textFlag){
            case 1:
                arrDate_tv.setText(sdf.format(myCalendar.getTime()));
                sdf.applyPattern("yyyy-MM-dd'T'HH:mm:ss");
                arrivalDeparture.arrivalDateTime=sdf.format(myCalendar.getTime());
                break;
            case 2:
                depDate_tv.setText(sdf.format(myCalendar.getTime()));
                sdf.applyPattern("yyyy-MM-dd'T'HH:mm:ss");
                arrivalDeparture.departureDateTime=sdf.format(myCalendar.getTime());
                break;
        }
    }
}