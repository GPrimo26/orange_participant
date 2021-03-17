package com.maksat.participantapp.ui.more.parts.exit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.maksat.participantapp.R;
import com.maksat.participantapp.interfaces.Profile;
import com.maksat.participantapp.interfaces.Server;
import com.maksat.participantapp.models.ErrorBody;
import com.maksat.participantapp.models.ForgotPassword;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResetPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResetPasswordFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView login_btn;
    private EditText email_et, login_et;
    private MaterialButton sendCode_btn;
    private ProgressBar progressBar;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ResetPasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ResetPasswordFragment newInstance(String param1, String param2) {
        ResetPasswordFragment fragment = new ResetPasswordFragment();
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
        View view=inflater.inflate(R.layout.fragment_reset_password, container, false);
        findIDs(view);
        setActions();
        return view;
    }

    private void findIDs(View view){
        login_btn=view.findViewById(R.id.login_btn);
        email_et=view.findViewById(R.id.email_et);
        login_et=view.findViewById(R.id.login_et);
        sendCode_btn=view.findViewById(R.id.reset_btn);
        progressBar=view.findViewById(R.id.resetpass_pb);
    }

    private void setActions(){
        login_btn.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_resetPasswordFragment_to_loginFragment));
        sendCode_btn.setOnClickListener(v->{
            List<EditText> editTexts = new ArrayList<>();
            editTexts.add(login_et);
            editTexts.add(email_et);
            boolean flag = false;
            for (EditText editText: editTexts) {
                if (editText.getText().toString().equals("")) {
                    editText.setError("Заполните данное поле");
                    flag =true;
                }
            }
            if (flag){
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            doCall();
        });
    }
    private void doCall() {
        Profile recoverPassApi = Server.GetServer(Profile.class);
        ForgotPassword fpBody = new ForgotPassword(email_et.getText().toString(), login_et.getText().toString(), 1);
        Call<ResponseBody> call = recoverPassApi.sendCode(fpBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    if (response.body()!=null){
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Мы выслали вам письмо на электронную почту.", Toast.LENGTH_LONG).show();
                        login_btn.performClick();
                    }
                }else {
                    try {
                        if (response.errorBody() != null) {
                            Gson gson = new Gson();
                            ErrorBody errorBody=gson.fromJson(response.errorBody().string(), ErrorBody.class);
                            Toast.makeText(getContext(), errorBody.Ru, Toast.LENGTH_SHORT).show();
                            Log.d("FPASS_RESP_ERROR", ""+errorBody.Ru);
                        }else {
                            Log.d("FPASS_RESP_ERROR", "ErrorBody is null.");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Ошибка сервера.", Toast.LENGTH_SHORT).show();
                Log.d("FPASS_SERV_ERROR", ""+t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

}