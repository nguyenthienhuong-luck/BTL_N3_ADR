package com.example.a2hcomic.activities.admin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.a2hcomic.R;
import com.example.a2hcomic.adapters.UserAdapter;
import com.example.a2hcomic.db.FirebaseService;
import com.example.a2hcomic.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {

    private ImageButton btnBack;
    private RecyclerView rvUserList;
    private UserAdapter userAdapter;
    private List<User> userList;

    private FirebaseService firebaseService;

    public UserFragment() {
        // Required empty public constructor
    }

    public static UserFragment newInstance() {
        return new UserFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        btnBack = view.findViewById(R.id.btnBack);
        rvUserList = view.findViewById(R.id.rvUserList);

        rvUserList.setLayoutManager(new LinearLayoutManager(getContext()));

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList);
        rvUserList.setAdapter(userAdapter);

        firebaseService = new FirebaseService();
        loadUsers();

        btnBack.setOnClickListener(v1 -> requireActivity().getSupportFragmentManager().popBackStack());


        return view;
    }

    private void loadUsers() {
        firebaseService.getUserRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot userSnap : snapshot.getChildren()) {
                    User user = userSnap.getValue(User.class);
                    if (user != null) {
                        userList.add(user);
                    }
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
    }
}
