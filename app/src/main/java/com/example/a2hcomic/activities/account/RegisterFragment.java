package com.example.a2hcomic.activities.account;

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
import com.example.a2hcomic.db.FirebaseService;
import com.example.a2hcomic.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class RegisterFragment extends Fragment {

    //TAG LOG
    private static final String TAG = "RegisterFragment";

    private EditText edt_username, edt_useremail, edt_password, edt_repassword;
    private Button btn_dangky;
    private  TextView btn_dangnhap1;
    private FirebaseService fb;

    public RegisterFragment() {
        // Required empty public constructo
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        // Ánh xạ UI
        edt_username = view.findViewById(R.id.edt_username);
        edt_useremail = view.findViewById(R.id.edt_useremail);
        edt_password = view.findViewById(R.id.edt_password);
        edt_repassword = view.findViewById(R.id.edt_repassword);
        btn_dangky = view.findViewById(R.id.btn_dangky);
        btn_dangnhap1 =view.findViewById(R.id.bt_dangnhap1);

        // Khởi tạo Service
        fb = new FirebaseService();

        // Xử lý sự kiện
        btn_dangky.setOnClickListener(v -> {
            register();
        });
        btn_dangnhap1.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        return view;
    }

    // chuc nang dang ky
    private void register() {
        // lay dl tu edt
        String username = edt_username.getText().toString().trim();
        String useremail = edt_useremail.getText().toString().trim();
        String password = edt_password.getText().toString().trim();
        String repassword = edt_repassword.getText().toString().trim();

        // kiem tra dieu kien
        if (!checkCondition(username, useremail, password, repassword))
            return;

        // kiem tra email có tồn tại hay chưa
        checkEmailExists(username, useremail, password);
    }

    private void upUser(User user) {
        //Day dl user len firebase
        try {
            fb.createUser(user);
            Toast.makeText(getActivity(), "Đăng ký thành công! Vui lòng đăng nhập lại!", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkEmailExists(String username, String useremail, String password) {
        fb.getUserRef()
                .orderByChild("email")
                .equalTo(useremail)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            edt_useremail.setError("Email đã tồn tại");
                        } else {
                            User user = new User();
                            user.setUsername(username);
                            user.setEmail(useremail);
                            user.setPassword(password);
                            upUser(user);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
    }

    private boolean checkCondition(String username, String useremail, String password, String repassword) {
        if(username.isEmpty() || useremail.isEmpty() || password.isEmpty() || repassword.isEmpty())   {
            edt_username.setError("Vui lòng nhập tên đăng nhập");
            edt_useremail.setError("Vui lòng nhập email");
            edt_password.setError("Vui lòng nhập mật khẩu");
            edt_repassword.setError("Vui lòng nhập lại mật khẩu");
            return false;
        }
        else if(!isValidEmail(useremail)){
            edt_useremail.setError("Email không hợp lệ");
            return false;
        }
        else if(!isValidPassword(password)){
            edt_password.setError("Mật khẩu không hợp lệ");
            return false;
        }
        else if(!password.equals(repassword)) {
            edt_repassword.setError("Nhập lại mật khẩu không trùng khớp");
            return false;
        }
        return true;

    }

    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailPattern);
    }

    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{4,8}$";
        return password.matches(passwordPattern);
    }

}


