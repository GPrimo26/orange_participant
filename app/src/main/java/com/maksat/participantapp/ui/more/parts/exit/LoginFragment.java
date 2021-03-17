package com.maksat.participantapp.ui.more.parts.exit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.maksat.participantapp.MainActivity;
import com.maksat.participantapp.Requests;
import com.maksat.participantapp.R;
import com.maksat.participantapp.models.Events;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MaterialButton login_btn;
    private TextView resetPass_btn;
    private EditText login_et, password_et;
    public Events events;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        View view=inflater.inflate(R.layout.fragment_login, container, false);
        findIDs(view);
        setActions();
        return view;
    }

    private void findIDs(View view) {
        login_btn=view.findViewById(R.id.login_btn);
        resetPass_btn=view.findViewById(R.id.reset_btn);
        login_et=view.findViewById(R.id.login_et);
        password_et=view.findViewById(R.id.password_et);
    }

    private void setActions(){
        login_btn.setOnClickListener(v -> {
            if (login_et.getText().toString().equals("")){
                login_et.setError("Заполните данное поле");
                return;
            }
            if (password_et.getText().toString().equals("")){
                password_et.setError("Заполните данное поле");
                return;
            }
            Requests preloadInfo=new Requests(requireActivity());
            preloadInfo.logIn(login_et.getText().toString(), password_et.getText().toString());
            preloadInfo.setOnLoggedInListener(()->{
                    preloadInfo.getCategories();
                    preloadInfo.getProfile();
                    preloadInfo.setOnProfileLoadedListener(()->{
                        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.action_loginFragment_to_navigation_schedule);
                        ((MainActivity) requireActivity()).navView.setVisibility(View.VISIBLE);
                        View divider= requireActivity().findViewById(R.id.divider2);
                        divider.setVisibility(View.VISIBLE);
                    });
                });
            });
        resetPass_btn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_resetPasswordFragment);
        });
    }
}