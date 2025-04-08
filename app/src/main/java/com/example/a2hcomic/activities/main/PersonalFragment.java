package com.example.a2hcomic.activities.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a2hcomic.R;

public class PersonalFragment extends Fragment {
    private TextView edt_user;

    public PersonalFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal, container, false);

        edt_user = view.findViewById(R.id.tv_edt_user);

        edt_user.setOnClickListener(v->{
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_main, new EditPersonalFragment());
            transaction.addToBackStack(null); // Thêm vào back stack
            transaction.commit();
        });
        return view;
    }
}