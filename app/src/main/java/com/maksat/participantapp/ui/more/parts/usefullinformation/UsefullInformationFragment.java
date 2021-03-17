package com.maksat.participantapp.ui.more.parts.usefullinformation;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.maksat.participantapp.R;

public class UsefullInformationFragment extends Fragment {

    private UsefullInformationViewModel mViewModel;

    public static UsefullInformationFragment newInstance() {
        return new UsefullInformationFragment();
    }

    private MaterialButton back_btn;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_usefull_information, container, false);
        findIDs(view);
        setActions();
        return view;
    }

    private void findIDs(View view) {
        back_btn=view.findViewById(R.id.back_btn);
    }

    private void setActions() {
        back_btn.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.action_usefullInformationFragment_to_navigation_more);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UsefullInformationViewModel.class);
        // TODO: Use the ViewModel
    }

}