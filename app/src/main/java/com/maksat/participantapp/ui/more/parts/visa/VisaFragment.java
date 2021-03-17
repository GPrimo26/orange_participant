package com.maksat.participantapp.ui.more.parts.visa;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.maksat.participantapp.R;
import com.maksat.participantapp.Requests;
import com.maksat.participantapp.models.EditableProfile;
import com.maksat.participantapp.models.Profile;
import com.maksat.participantapp.ui.SharedProfileViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class VisaFragment extends Fragment {

    //private VisaViewModel mViewModel;
    private SharedProfileViewModel sharedProfileViewModel;

    public static VisaFragment newInstance() {
        return new VisaFragment();
    }

    private MaterialButton back_btn, accept_btn;
    private Spinner visa_spinner;
    private ArrayAdapter<String> arrayAdapter;
    private EditableProfile.Visa persVisa;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_visa, container, false);
        sharedProfileViewModel=new ViewModelProvider(requireActivity()).get(SharedProfileViewModel.class);
        if (sharedProfileViewModel.getProfile().getValue()!=null) {
            persVisa= new EditableProfile.Visa(sharedProfileViewModel.getProfile().getValue().visa);
        }else {
            persVisa= new EditableProfile.Visa(false);
        }
        findIDs(view);
        setActions();
        return view;
    }

    private void findIDs(@NotNull View view) {
        back_btn=view.findViewById(R.id.back_btn);
        visa_spinner=view.findViewById(R.id.visa_spinner);
        accept_btn=view.findViewById(R.id.accept_visa_btn);
    }

    private void setActions(){
        back_btn.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.action_visaFragment_to_navigation_more);
        });
        sharedProfileViewModel.getProfile().observe(getViewLifecycleOwner(), profile -> {
            List<String> list=new ArrayList<>();
            list.add("Да");
            list.add("Нет");
            if (arrayAdapter==null){
                arrayAdapter=new ArrayAdapter<>(requireActivity(), R.layout.spinner_dropdown, list);
            }else {
                arrayAdapter.notifyDataSetChanged();
            }
            if (visa_spinner.getAdapter()==null) {
                visa_spinner.setAdapter(arrayAdapter);
            }

            if (profile.visa){
                visa_spinner.setSelection(0);
            }else if (!profile.visa){
                visa_spinner.setSelection(1);
            }else {
                visa_spinner.setSelection(1);
            }

            visa_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position){
                        case 1:
                            persVisa.visa=true;
                            break;
                        case 2:
                            persVisa.visa=false;
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        });
        accept_btn.setOnClickListener(v -> {
            Requests request=new Requests(requireActivity());
            request.editVisa(persVisa);
        });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}