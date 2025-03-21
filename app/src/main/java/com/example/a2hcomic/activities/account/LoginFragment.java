package com.example.a2hcomic.activities.account;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a2hcomic.R;
import com.example.a2hcomic.activities.HomeActivity;
import com.example.a2hcomic.db.FirebaseService;
import com.example.a2hcomic.db.UserService;
import com.example.a2hcomic.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginFragment extends Fragment {

    // TODO: Rename and change types of parameters
    private EditText edt_email, edt_password;
    private Button btn_dangnhap;
    private TextView bt_dangky1;
    private FirebaseService firebaseService;

    private UserService userService;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Ánh xạ UI
        edt_email = view.findViewById(R.id.edt_email);
        edt_password = view.findViewById(R.id.edt_password);
        btn_dangnhap = view.findViewById(R.id.bt_dangnhap);
        bt_dangky1 = view.findViewById(R.id.bt_dangky1);

        // Xử lý sự kiện đăng nhập
        btn_dangnhap.setOnClickListener(v -> checkEmailExists());

        // Chuyển sang màn hình REGISTERFRAGMENT
        bt_dangky1.setOnClickListener(v -> {
            Fragment fragment = new RegisterFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_account, fragment)
                    .addToBackStack(null)
                    .commit();
        });
        return view;
    }

    private void checkEmailExists() {
        String email = edt_email.getText().toString().trim();
        String password = edt_password.getText().toString().trim();

        // Khởi tạo Service
        firebaseService = new FirebaseService();
        userService = new UserService(firebaseService);

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getActivity(), "Vui lòng nhập đầy đủ email và mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (userService.checkEmailExists(email)) {
            loginUser(email, password);
        } else {
            edt_email.setError("Email chưa được đăng ký!");
            Toast.makeText(getActivity(), "Email chưa tồn tại. Vui lòng đăng ký!", Toast.LENGTH_SHORT).show();
        }

    }
    private void loginUser(String email, String password) {
        Query query = firebaseService.getUserRef().orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        User user = userSnapshot.getValue(User.class);
                        if(CheckPassword(password, user.getPassword(), user.getStatus())) {
                            startActivity(new Intent(getActivity(), HomeActivity.class));
                            saveLoginState(user.getId(), user.getEmail(), user.getPassword());;
                        }
                        else
                            Toast.makeText(getActivity(), "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private boolean CheckPassword(String password, String pass_user, int status) {
        if(password.equals(pass_user))
            if(status == 0)
                return true;
            else
                return false;
        else
            return false;
    }

    //Luu vao local
    private void saveLoginState(String id, String email, String password) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login_state", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", id);
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();
    }

    private void loadLoginState(String id, String email, String password) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login_state", getActivity().MODE_PRIVATE);
        String userId1 = sharedPreferences.getString("id", "");
        String email1 = sharedPreferences.getString("email", "");
        String password1 = sharedPreferences.getString("password", "");


    }

}