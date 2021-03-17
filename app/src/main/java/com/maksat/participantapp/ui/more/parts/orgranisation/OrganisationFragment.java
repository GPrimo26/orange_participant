package com.maksat.participantapp.ui.more.parts.orgranisation;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
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

import com.google.android.material.button.MaterialButton;
import com.maksat.participantapp.R;
import com.maksat.participantapp.Requests;
import com.maksat.participantapp.Variables;
import com.maksat.participantapp.models.EditableProfile;
import com.maksat.participantapp.models.Profile;
import com.maksat.participantapp.ui.SharedProfileViewModel;
import com.maksat.participantapp.ui.more.parts.profile.ProfileFragment;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OrganisationFragment extends Fragment {

    //private OrganisationViewModel mViewModel;
    private SharedProfileViewModel sharedProfileViewModel;
    private EditableProfile.Company company;


    @NotNull
    @Contract(" -> new")
    public static OrganisationFragment newInstance() {
        return new OrganisationFragment();
    }

    private MaterialButton back_btn, accept_btn;
    private EditText name_et, position_et, adress_et;
    private Spinner sphere_spinner;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_organisation, container, false);
        findIDs(view);
        sharedProfileViewModel=new ViewModelProvider(requireActivity()).get(SharedProfileViewModel.class);
        if (sharedProfileViewModel.getProfile().getValue()!=null) {
            company= new EditableProfile.Company(sharedProfileViewModel.getProfile().getValue());
        }else {
            company= new EditableProfile.Company(null, null, null, null, null, null, null);
        }

        setActions();
        return view;
    }

    private void findIDs(View view) {
        back_btn=view.findViewById(R.id.back_btn);
        name_et=view.findViewById(R.id.name_org_et);
        position_et=view.findViewById(R.id.position_org_et);
        sphere_spinner =view.findViewById(R.id.spheres_spinner);
        adress_et=view.findViewById(R.id.adress_org_et);
        accept_btn=view.findViewById(R.id.accept_org_btn);
    }

    private void setActions(){
        back_btn.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_organisationFragment_to_navigation_more));
        sharedProfileViewModel.getProfile().observe(getViewLifecycleOwner(), profile -> {
            if (!profile.company.nameRus.equals("")){
                name_et.setText(profile.company.nameRus);
            }else if (!profile.company.nameEng.equals("")){
                name_et.setText(profile.company.nameEng);
            }else {
                name_et.setText("Не указано");
            }

            if (!profile.company.positionRus.equals("")){
                position_et.setText(profile.company.positionRus);
            }else if (!profile.company.positionEng.equals("")){
                position_et.setText(profile.company.positionEng);
            }else {
                position_et.setText("Не указано");
            }
            setSpinner(profile);

            if (profile.company.postAddress.equals("")){
                adress_et.setText("Не указано");
            }else {
                adress_et.setText(profile.company.postAddress);
            }
        });
        accept_btn.setOnClickListener(v -> {
            List<EditText> editTexts=new ArrayList<>();
            editTexts.add(name_et);
            editTexts.add(position_et);
            editTexts.add(adress_et);
            boolean hasError= ProfileFragment.checkEmpty(editTexts);
            if (!hasError){
                company.organizationRus=name_et.getText().toString();
                company.positionRus=position_et.getText().toString();
                company.postAddress=adress_et.getText().toString();
                Requests request=new Requests(requireActivity());
                request.editCompany(company);
            }

        });

    }

    private void setSpinner(Profile profile) {
        List<String> spheres = new ArrayList<>();
        String name = "Не указано";
        spheres.add("Не указано");
        for (Profile.Company.Sphere sphere:Variables.spheres){
            spheres.add(sphere.nameRus);
            if (sphere.id.equals(profile.company.sphereId)){
                name=sphere.nameRus;
            }
        }
        if (arrayAdapter==null) {
            arrayAdapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_dropdown, spheres);
        }else {
            arrayAdapter.notifyDataSetChanged();
        }
        if (sphere_spinner.getAdapter()==null){
            sphere_spinner.setAdapter(arrayAdapter);
        }
        sphere_spinner.setSelection(arrayAdapter.getPosition(name));
        if (sphere_spinner.getOnItemSelectedListener()==null){
            sphere_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position!=0) {
                        company.sphereId = Variables.spheres.get(position - 1).id;
                    }else {
                        company.sphereId=null;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

}