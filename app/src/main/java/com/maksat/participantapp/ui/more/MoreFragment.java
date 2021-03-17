package com.maksat.participantapp.ui.more;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.maksat.participantapp.MainActivity;
import com.maksat.participantapp.R;
import com.maksat.participantapp.Variables;
import com.maksat.participantapp.models.Profile;
import com.maksat.participantapp.ui.SharedProfileViewModel;

import java.util.Objects;

public class MoreFragment extends Fragment {

    //private MoreViewModel mViewModel;

    public static MoreFragment newInstance() {
        return new MoreFragment();
    }

    private LinearLayout profile_ll;
    private LinearLayout exit_ll;
    private LinearLayout organisation_ll;
    private LinearLayout visa_ll;
    private LinearLayout AD_ll;
    private LinearLayout uInformation_ll;
    private LinearLayout settings_ll;
    private LinearLayout about_ll;
    private LinearLayout feedback_ll;
    private TextView name_tv, organisation_tv;
    private SharedProfileViewModel sharedProfileViewModel;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_more, container, false);
        sharedProfileViewModel=new ViewModelProvider(requireActivity()).get(SharedProfileViewModel.class);
        findsIDs(view);
        setActions();
        return view;
    }

    private void findsIDs(View view) {
        profile_ll=view.findViewById(R.id.profile_ll);
        organisation_ll=view.findViewById(R.id.organisation_ll);
        visa_ll=view.findViewById(R.id.visa_ll);
        AD_ll=view.findViewById(R.id.arr_dep_ll);
        uInformation_ll =view.findViewById(R.id.usefullinfo_ll);
        settings_ll=view.findViewById(R.id.settings_ll);
        about_ll=view.findViewById(R.id.about_ll);
        feedback_ll=view.findViewById(R.id.feedback_ll);
        exit_ll=view.findViewById(R.id.exit_ll);
        name_tv=view.findViewById(R.id.name_tv);
        organisation_tv=view.findViewById(R.id.organisation_tv);

    }

    private void setActions(){
        profile_ll.setOnClickListener(v -> navigateTo(R.id.action_navigation_more_to_profileFragment, v, -1));
        organisation_ll.setOnClickListener(v-> navigateTo(R.id.action_navigation_more_to_organisationFragment, v, -1));
        visa_ll.setOnClickListener(v-> navigateTo(R.id.action_navigation_more_to_visaFragment, v, -1));
        AD_ll.setOnClickListener(v-> navigateTo(R.id.action_navigation_more_to_arrDepFragment, v, -1));
        uInformation_ll.setOnClickListener(v-> navigateTo(R.id.action_navigation_more_to_usefullInformationFragment, v, -1));
        settings_ll.setOnClickListener(v-> navigateTo(R.id.action_navigation_more_to_settingsFragment, v, -1));
        about_ll.setOnClickListener(v-> navigateTo(R.id.action_navigation_more_to_aboutFragment, v,-1));
        feedback_ll.setOnClickListener(v-> navigateTo(R.id.action_navigation_more_to_feedbackFragment, v, -1));
        exit_ll.setOnClickListener(v -> {
            Variables.token=null;
            ((MainActivity)requireActivity()).preferences.edit().putString("tokenToken", "").apply();
            navigateTo(R.id.action_navigation_more_to_loginFragment, v, View.GONE);
        });
        sharedProfileViewModel.getProfile().observe(getViewLifecycleOwner(), profile -> {
            String name=profile.firstNameRus+" "+profile.lastNameRus;
            name_tv.setText(name);
            if (profile.company.nameRus.equals("")){
                organisation_tv.setText(profile.company.nameEng);
            }else {
                organisation_tv.setText(profile.company.nameRus);
            }
        });
    }

    private void navigateTo(int destination, View v, int hideType) {
        BottomNavigationView navView= requireActivity().findViewById(R.id.nav_view);
        if (hideType!=-1) {
            if (navView != null) {
                navView.setVisibility(hideType);
            }
            View divider = requireActivity().findViewById(R.id.divider2);
            if (divider != null) {
                divider.setVisibility(hideType);
            }
        }
        if (destination==R.id.action_navigation_more_to_loginFragment) {
            NavOptions.Builder navBuilder = new NavOptions.Builder();
            NavOptions navOptions = navBuilder.setLaunchSingleTop(true)
                    .setPopUpTo(R.id.navigation_more, true)
                    .setPopUpTo(R.id.navigation_schedule, true)
                    .build();
            Navigation.findNavController(v).navigate(destination, null, navOptions);
        }else {
            Navigation.findNavController(v).navigate(destination);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mViewModel = new ViewModelProvider(this).get(MoreViewModel.class);
        // TODO: Use the ViewModel
    }

}