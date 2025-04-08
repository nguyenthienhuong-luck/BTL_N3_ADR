package com.example.a2hcomic.activities.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.a2hcomic.R;



public class EditPersonalFragment extends Fragment {
    private ImageButton btn_back;
    private TextView  btn_save;

    private EditText edt_name;
    private TextView tv_count_name;


    public EditPersonalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_personal, container, false);

        btn_back = view.findViewById(R.id.btn_back);
        btn_save = view.findViewById(R.id.tv_save);
        edt_name = view.findViewById(R.id.edt_name);
        tv_count_name = view.findViewById(R.id.tv_count_name);

       btn_back.setOnClickListener(v->{
           getParentFragmentManager().popBackStack();
       });
       btn_save.setOnClickListener(v->{
       });
        return view;
    }
}