package com.example.a2hcomic.activities.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a2hcomic.R;
import com.example.a2hcomic.activities.account.AccountActivity;
import com.example.a2hcomic.activities.account.LoginFragment;
import com.example.a2hcomic.activities.admin.AdminActivity;
import com.example.a2hcomic.activities.admin.AdminFragment;
import com.example.a2hcomic.activities.personal.EditAcountFragment;
import com.example.a2hcomic.activities.personal.EditPersonalFragment;
import com.example.a2hcomic.db.FirebaseService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class PersonalFragment extends Fragment {
    private TextView edt_user;

    private TextView name_user;

    private TextView account, logout, admin;
    private BottomNavigationView nav_main;

    private FirebaseService fb;

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
        nav_main = getActivity().findViewById(R.id.nav_main);
        name_user = view.findViewById(R.id.name);
        account = view.findViewById(R.id.tv_account);
        logout = view.findViewById(R.id.tv_logout);
        admin = view.findViewById(R.id.tv_admin);

        fb = new FirebaseService();

        edt_user.setOnClickListener(v->{
            changeFragment(new EditPersonalFragment());
        });

        account.setOnClickListener(v-> {
            changeFragment(new EditAcountFragment());
        });

        admin.setOnClickListener(v->{
            // chuyển sang AdminActivity
            Intent intent = new Intent(getActivity(), AdminActivity.class);
            startActivity(intent);
        });

        logout.setOnClickListener(v->showDialog());

        return view;
    }


    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Thông báo");
        builder.setMessage("Đăng xuất khỏi tài khoản của bạn?");

        builder.setPositiveButton("Hủy", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.setNeutralButton("Đăng xuất", (dialog, which) -> {
            deleteAccount();
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button neutralButton = dialog.getButton(DialogInterface.BUTTON_NEUTRAL);

        // Set text color and shadow for buttons
        positiveButton.setTextColor(Color.parseColor("#50CAFF"));
        positiveButton.setTextSize(16.0f);
        positiveButton.setShadowLayer(7.0f, 0.0f, 8.0f, Color.parseColor("#80808080"));

        neutralButton.setTextColor(Color.parseColor("#FFA5BB"));
        neutralButton.setTextSize(16.0f);
        neutralButton.setShadowLayer(7.0f, 0.0f, 8.0f, Color.parseColor("#80808080"));
    }

    private void deleteAccount() {
        getActivity().getSharedPreferences("login_state", getActivity().MODE_PRIVATE).edit().clear().apply();
        Toast.makeText(getActivity(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getActivity(), AccountActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void changeFragment(Fragment newFragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main, newFragment);
        transaction.addToBackStack(null); // Thêm vào back stack
        transaction.commit();
        nav_main.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        nav_main.setVisibility(View.VISIBLE);
        String userId = getActivity().getSharedPreferences("login_state", getActivity().MODE_PRIVATE).getString("id", "");
        fb.getUserRef().child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("username").getValue(String.class);
                    name_user.setText(name);
                    if (snapshot.child("role").getValue(Integer.class) == 1) {
                        admin.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}