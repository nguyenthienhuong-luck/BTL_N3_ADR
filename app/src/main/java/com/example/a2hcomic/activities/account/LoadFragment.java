package com.example.a2hcomic.activities.account;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a2hcomic.R;
import com.example.a2hcomic.activities.main.MainActivity;
import com.example.a2hcomic.db.FirebaseService;
import com.example.a2hcomic.models.User;

public class LoadFragment extends Fragment {

    public LoadFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_load, container, false);
        loadLoginState();
        return v;
    }

    private void loadLoginState() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login_state", getActivity().MODE_PRIVATE);
        String userId = sharedPreferences.getString("id", "");
        String emai = sharedPreferences.getString("email", "");
        String password = sharedPreferences.getString("password", "");

        if (!userId.isEmpty())
        {
            // get user by id in firebase
            FirebaseService fb = new FirebaseService();

            fb.getUserRef().child(userId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null) {

                    User user = task.getResult().getValue(User.class);
                    if (user != null) {
                        if (user.getEmail().equals(emai) &&
                                user.getPassword().equals(password) &&
                                user.getStatus() == 0) {
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    }
                }
            });
        } else {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_account, new LoginFragment());
            transaction.addToBackStack(null); // Thêm vào back stack
            transaction.commit();
        }

    }
}