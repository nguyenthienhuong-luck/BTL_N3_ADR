package com.example.a2hcomic.activities.personal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a2hcomic.R;
import com.example.a2hcomic.activities.account.AccountActivity;
import com.example.a2hcomic.db.FirebaseService;

public class EditAcountFragment extends Fragment {
        private TextView tv_email,title_xoa;

        private EditText edt_pass,edt_pass_new,edt_repass;

        private ImageButton btn_back;

        private Button btn_save_pass;

        private String userId;
        private FirebaseService fb;

    public EditAcountFragment() {
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
        View view = inflater.inflate(R.layout.fragment_edit_acount, container, false);

        btn_back = view.findViewById(R.id.btn_back);
        tv_email = view.findViewById(R.id.tv_email);
        title_xoa = view.findViewById(R.id.title_xoa);
        edt_pass = view.findViewById(R.id.edt_pass);
        edt_pass_new = view.findViewById(R.id.edt_pass_new);
        edt_repass = view.findViewById(R.id.edt_repass);
        btn_save_pass = view.findViewById(R.id.btn_save);

        fb = new FirebaseService();
        userId = getActivity().getSharedPreferences("login_state", getActivity().MODE_PRIVATE).getString("id", "");

        setEmail();

        btn_save_pass.setOnClickListener(v-> {
            changePass();
        });

        title_xoa.setOnClickListener(v-> showDialog() );

        btn_back.setOnClickListener(v->{
            getParentFragmentManager().popBackStack();
        });

        return view;
    }

    private void changePass() {
        String pass = edt_pass.getText().toString().trim();
        String pass_new = edt_pass_new.getText().toString().trim();
        String repass = edt_repass.getText().toString().trim();
        if (pass.isEmpty() || pass_new.isEmpty() || repass.isEmpty()) {
            Toast.makeText(getActivity(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        String current_pass = getActivity().getSharedPreferences("login_state", getActivity().MODE_PRIVATE).getString("password", "");
        if (!pass.equals(current_pass)) {
            Toast.makeText(getActivity(), "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pass_new.equals(repass)) {
            Toast.makeText(getActivity(), "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        fb.getUserRef().child(userId).child("password").setValue(pass_new);
        getActivity().getSharedPreferences("login_state", getActivity().MODE_PRIVATE).edit().putString("password", pass_new).apply();
        Toast.makeText(getActivity(), "Thay đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
        getParentFragmentManager().popBackStack();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Xác nhận xóa tài khoản");
        builder.setMessage("Bạn có chắc chắn muốn xóa tài khoản?");

        builder.setPositiveButton("Hủy", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.setNeutralButton("Xóa", (dialog, which) -> {
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
        fb.getUserRef().child(userId).removeValue();
        getActivity().getSharedPreferences("login_state", getActivity().MODE_PRIVATE).edit().clear().apply();
        Toast.makeText(getActivity(), "Xóa tài khoản thành công", Toast.LENGTH_SHORT).show();

        getActivity().finish();
        Intent intent = new Intent(getActivity(), AccountActivity.class);
        startActivity(intent);
    }

    private void setEmail() {
        String email = getActivity().getSharedPreferences("login_state", getActivity().MODE_PRIVATE).getString("email", "");
        tv_email.setText(email);
    }

}