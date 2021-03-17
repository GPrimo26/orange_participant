package com.maksat.participantapp.ui.more.parts.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
import com.maksat.participantapp.models.Title;
import com.maksat.participantapp.ui.SharedProfileViewModel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private ProfileViewModel mViewModel;
    private SharedProfileViewModel sharedProfileViewModel;
    private MaterialButton back_btn, accept_btn;
    private EditText name_et, lastName_et, phone_et, email_et, site_et;
    private Spinner appeal_spinner, gender_spinner;
    private EditableProfile.PersonalData personalData;
    private EditableProfile.Contact contact;
    private ArrayAdapter<String> appealsAdapter, gendersAdapter;
    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_profile, container, false);
       //mViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
       sharedProfileViewModel=new ViewModelProvider(requireActivity()).get(SharedProfileViewModel.class);
       if (sharedProfileViewModel.getProfile().getValue()!=null) {
           personalData= new EditableProfile.PersonalData(sharedProfileViewModel.getProfile().getValue());
           contact=new EditableProfile.Contact(sharedProfileViewModel.getProfile().getValue());
       }else {
           personalData= new EditableProfile.PersonalData(null, null, null, null, null, null, null, null, null, null, null);
           contact=new EditableProfile.Contact(null, null, null);
       }

       findIDs(view);
       setActions();
        return view;
    }

    private void findIDs(View view) {
        back_btn=view.findViewById(R.id.back_btn);
        appeal_spinner=view.findViewById(R.id.adress_org_spinner);
        name_et=view.findViewById(R.id.name_et);
        lastName_et=view.findViewById(R.id.lastname_et);
        gender_spinner =view.findViewById(R.id.gender_spinner);
        phone_et=view.findViewById(R.id.phone_et);
        email_et=view.findViewById(R.id.email_et);
        site_et=view.findViewById(R.id.site_et);
        accept_btn=view.findViewById(R.id.accept_btn);
    }

    private void setActions(){
        back_btn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_navigation_more);
        });
        accept_btn.setOnClickListener(v->{
            List<EditText> editTexts=new ArrayList<>();
            editTexts.add(name_et);
            editTexts.add(lastName_et);
            editTexts.add(phone_et);
            editTexts.add(site_et);
            editTexts.add(site_et);
            boolean hasError=checkEmpty(editTexts);
            if (!hasError){
                personalData.firstNameRus=name_et.getText().toString();
                personalData.lastNameRus=lastName_et.getText().toString();
                contact.phoneNumber=phone_et.getText().toString();
                contact.email=email_et.getText().toString();
                contact.webSite=site_et.getText().toString();
                Requests request=new Requests(requireActivity());
                request.editPersonalData(personalData);
                request.editContact(contact);
            }


        });
        sharedProfileViewModel.getProfile().observe(getViewLifecycleOwner(), profile -> {
            setSpinners(profile);
            setEditText(name_et, profile.firstNameRus);
            setEditText(lastName_et, profile.lastNameRus);
            setEditText(phone_et, profile.phoneNumber);
            setEditText(email_et, profile.email);
            setEditText(site_et, profile.webSite);
        });
    }

    public static boolean checkEmpty(List<EditText> editTexts) {
        for (EditText editText:editTexts){
            if (editText.getText().toString().equals("")){
                editText.setError("Данное поле должно быть заполнено");
                return true;
            }
        }
        return false;
    }

    private void setSpinners(Profile profile) {
        List<String> titles = new ArrayList<>();
        titles.add("Не указано");
        for (Title title:Variables.titles){
            titles.add(title.nameRus);
        }
        if (appealsAdapter==null) {
            appealsAdapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_dropdown, titles);
        }else {
            appealsAdapter.notifyDataSetChanged();
        }
        if (appeal_spinner.getAdapter()==null) {
            appeal_spinner.setAdapter(appealsAdapter);
        }
        if (Variables.titles!=null){
            if (Variables.titles.size()!=0){
                for (Title title:Variables.titles){
                    if (profile.titleId.equals(title.id)){
                        int pos=appealsAdapter.getPosition(title.nameRus);
                        appeal_spinner.setSelection(pos);
                        break;
                    }else {
                        appeal_spinner.setSelection(0);
                        personalData.titleId=null;
                    }
                }
            }else {
                appeal_spinner.setSelection(0);
                personalData.titleId=null;
            }
        }else {
            appeal_spinner.setSelection(0);
            personalData.titleId=null;
        }
        if (appeal_spinner.getOnItemSelectedListener()==null) {
            appeal_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position!=0) {
                        personalData.titleId = Variables.titles.get(position - 1).id;
                    }else {
                        personalData.titleId=null;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        List<String> genders = new ArrayList<>();
        genders.add("Не указано");
        genders.add("Мужской");
        genders.add("Женский");
        if (gendersAdapter==null) {
            gendersAdapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_dropdown, genders);
        }else {
            gendersAdapter.notifyDataSetChanged();
        }
        if (gender_spinner.getAdapter()==null) {
            gender_spinner.setAdapter(gendersAdapter);
        }

        switch (profile.genderId){
            case 1:
                gender_spinner.setSelection(1);
                break;
            case 2:
                gender_spinner.setSelection(2);
                break;
        }
        if (gender_spinner.getOnItemSelectedListener()==null) {
            gender_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position){
                        case 0:
                            personalData.genderId=null;
                            break;
                        case 1:
                            personalData.genderId=1;
                            break;
                        case 2:
                            personalData.genderId=2;
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    public void setEditText(EditText editText, String text){
        if (text.equals("")){
            editText.setText("Не указано");
        }else {
            editText.setText(text);
        }
    }
}