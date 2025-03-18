package com.example.a2hcomic.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.a2hcomic.R;
import com.example.a2hcomic.models.User;

public class RegisterActivity extends AppCompatActivity {

    EditText edt_username, edt_useremail, edt_password, edt_repassword;
    Button btn_dangky;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edt_username = findViewById(R.id.edt_username);
        edt_useremail = findViewById(R.id.edt_useremail);
        edt_password = findViewById(R.id.edt_password);
        edt_repassword = findViewById(R.id.edt_repassword);
        btn_dangky = findViewById(R.id.btn_dangky);

        btn_dangky.setOnClickListener(v -> {
            String username = edt_username.getText().toString().trim();
            String useremail = edt_useremail.getText().toString().trim();
            String password = edt_password.getText().toString().trim();
            String repassword = edt_repassword.getText().toString().trim();

            if(username.isEmpty() || useremail.isEmpty() || password.isEmpty())   {
                edt_username.setError("Vui lòng nhập tên đăng nhập");
                edt_useremail.setError("Vui lòng nhập email");
                edt_password.setError("Vui lòng nhập mật khẩu");
            }
            if(!isValidEmail(useremail)){
                edt_useremail.setError("Email không hợp lệ");
            }
            //kiem tra email khong trung firebase
            if(!isValidPassword(password)){
                edt_password.setError("Mật khẩu không hợp lệ");
            }
            if(!password.equals(repassword)) {
                edt_repassword.setError("Nhập lại mật khẩu không trùng khớp");
            }
            User user = new User();
            user.setUsername(username);
            user.setEmail(useremail);
            user.setPassword(password);

            //Day dl user len firebase
        });

    }
    public boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailPattern);
    }

    public boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{4,8}$";
        return password.matches(passwordPattern);
    }

}