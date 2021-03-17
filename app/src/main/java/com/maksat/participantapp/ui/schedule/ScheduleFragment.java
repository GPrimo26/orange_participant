package com.maksat.participantapp.ui.schedule;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.maksat.participantapp.MainActivity;
import com.maksat.participantapp.R;
import com.maksat.participantapp.adapters.DaysRVAdapter;
import com.maksat.participantapp.adapters.EventsRVAdapter;
import com.maksat.participantapp.ui.customui.CenterLayoutManager;

public class ScheduleFragment extends Fragment {

    private ScheduleViewModel homeViewModel;
    private RecyclerView schedule_rv, days_rv;
    private EventsRVAdapter eventsRVAdapter;
    private DaysRVAdapter daysAdapter;
    private ProgressBar progressBar;
    private LinearLayoutManager schedule_lm;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(requireActivity()).get(ScheduleViewModel.class);
        homeViewModel.setActivity(requireActivity());
        View root = inflater.inflate(R.layout.fragment_schedule, container, false);
        Context context;
        schedule_lm=new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

        findIDs(root);
        setActions();
        return root;
    }

    private void findIDs(View root) {
        schedule_rv=root.findViewById(R.id.schedule_rv);
        days_rv=root.findViewById(R.id.days_rv);
        progressBar=root.findViewById(R.id.schedule_prb);
    }

    private void setActions(){
        CenterLayoutManager centerLayoutManager = new CenterLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        homeViewModel.setOnInfoLoadListener(new ScheduleViewModel.OnInfoLoadListener() {
            @Override
            public void isLoading() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void infoLoaded() {
                progressBar.setVisibility(View.GONE);
            }
        });
        homeViewModel.getEventProgram().observe(getViewLifecycleOwner(), eventProgramByDays -> {
            if (daysAdapter==null) {
                daysAdapter = new DaysRVAdapter((MainActivity) getActivity(), eventProgramByDays);
                daysAdapter.setOnItemClickListener(position -> {
                    days_rv.smoothScrollToPosition(position);
                    if (eventProgramByDays.size()!=0){
                        if (eventsRVAdapter==null){
                            eventsRVAdapter=new EventsRVAdapter((MainActivity)(getActivity()), eventProgramByDays.get(position).getEventProgram());
                        }else {
                            eventsRVAdapter.setEventProgram(eventProgramByDays.get(position).getEventProgram());
                        }
                        if (schedule_rv.getLayoutManager()==null) {
                            schedule_rv.setLayoutManager(schedule_lm);
                        }
                        if (schedule_rv.getAdapter()==null) {
                            schedule_rv.setAdapter(eventsRVAdapter);
                        }
                    }
                });
                if (days_rv.getLayoutManager()==null) {
                    days_rv.setLayoutManager(centerLayoutManager);
                }
                if (days_rv.getAdapter()==null) {
                    days_rv.setAdapter(daysAdapter);
                }
            }else {
                if (days_rv.getAdapter()==null) {
                    days_rv.setAdapter(daysAdapter);
                }
                if (days_rv.getLayoutManager()==null) {
                    days_rv.setLayoutManager(centerLayoutManager);
                }
                if (daysAdapter.getOnItemClickListener()==null){
                    daysAdapter.setOnItemClickListener(position -> {
                        days_rv.smoothScrollToPosition(position);
                        if (eventProgramByDays.size()!=0){
                            if (eventsRVAdapter==null){
                                eventsRVAdapter=new EventsRVAdapter((MainActivity)(getActivity()), eventProgramByDays.get(position).getEventProgram());
                            }else {
                                eventsRVAdapter.setEventProgram(eventProgramByDays.get(position).getEventProgram());
                            }
                            if (schedule_rv.getLayoutManager()==null) {
                                schedule_rv.setLayoutManager(schedule_lm);
                            }
                            if (schedule_rv.getAdapter()==null) {
                                schedule_rv.setAdapter(eventsRVAdapter);
                            }
                        }
                    });
                }
                if (schedule_rv.getLayoutManager()==null){
                    schedule_rv.setLayoutManager(schedule_lm);
                }
                if (schedule_rv.getAdapter()==null) {
                    schedule_rv.setAdapter(eventsRVAdapter);
                }
                daysAdapter.setProgramByDays(eventProgramByDays);
            }
            daysAdapter.performClick(0);
        });

    }
}