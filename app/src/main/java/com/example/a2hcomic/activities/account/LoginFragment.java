package com.example.a2hcomic.activities.account;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a2hcomic.R;
import com.example.a2hcomic.activities.main.MainActivity;
import com.example.a2hcomic.db.FirebaseService;
import com.example.a2hcomic.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class LoginFragment extends Fragment {

    // TAG LOG
    private static final String TAG = "LoginFragment";

    private EditText edt_email, edt_password;
    private Button btn_dangnhap;
    private TextView bt_dangky1;
    private FirebaseService fb;

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

        fb = new FirebaseService();

        // Xử lý sự kiện đăng nhập
        btn_dangnhap.setOnClickListener(v -> checkExists());

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

    private void checkExists() {
        String email = edt_email.getText().toString().trim();
        String password = edt_password.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getActivity(), "Vui lòng nhập đầy đủ email và mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }

        loginUser(email, password);
    }

    private void loginUser(String email, String password) {
        fb.getUserRef()
                .orderByChild("email")
                .equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        User user = userSnapshot.getValue(User.class);
                        if (user.getPassword().equals(password) &&
                                user.getStatus() == 0) {
                            saveLoginState(user.getId(), user.getEmail(), user.getPassword());
                        }
                        else Toast.makeText(getActivity(), "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                } else Toast.makeText(getActivity(), "Không tìm thấy tài khoản", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Luu vao local
    private void saveLoginState(String id, String email, String password) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login_state", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", id);
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();

        Toast.makeText(getActivity(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
    }

}