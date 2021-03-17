package com.maksat.participantapp.ui.b2b;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.maksat.participantapp.R;

public class B2BFragment extends Fragment {

    private B2BViewModel b2BViewModel;
    private Button btn;
    private EditText editText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        b2BViewModel =
                new ViewModelProvider(this).get(B2BViewModel.class);
        View root = inflater.inflate(R.layout.fragment_b2b, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        btn=root.findViewById(R.id.btn);
        editText=root.findViewById(R.id.et);
        b2BViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        btn.setOnClickListener(v -> b2BViewModel.setText(editText.getText().toString()));
        return root;
    }
}