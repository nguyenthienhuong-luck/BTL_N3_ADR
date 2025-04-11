package com.example.a2hcomic.activities.personal;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a2hcomic.R;
import com.example.a2hcomic.db.FirebaseService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class EditPersonalFragment extends Fragment {
    private ImageButton btn_back;
    private TextView  btn_save;
    private EditText edt_name;
    private TextView tv_count_name;
    private String userID;




    private FirebaseService fb;

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

        fb = new FirebaseService();
        userID = getActivity().getSharedPreferences("login_state", getActivity().MODE_PRIVATE).getString("id", "");

        setViewUI();

        edt_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_count_name.setText(s.length()+"/40");
                if (s.length() >= 40 || s.length() == 0) {
                    // Nếu tên quá dài hoặc tên rỗng
                    tv_count_name.setTextColor(getResources().getColor(R.color.red, null));
                } else {
                    // Nếu tên hợp lệ
                    tv_count_name.setTextColor(getResources().getColor(R.color.black, null));
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        btn_back.setOnClickListener(v->{
            getParentFragmentManager().popBackStack();
        });

        btn_save.setOnClickListener(v->{
            String newName = edt_name.getText().toString();
            // kiểm tra có bị  không
            if (newName.isEmpty() || newName.length() > 40) {
                Toast.makeText(getActivity(), "Tên không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }
            fb.getUserRef().child(userID).child("username").setValue(newName);
            Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack();
        });



        return view;
    }

    private void setViewUI() {
        fb.getUserRef().child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("username").getValue(String.class);
                    edt_name.setText(name);
                    tv_count_name.setText(name.length()+"/40");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}