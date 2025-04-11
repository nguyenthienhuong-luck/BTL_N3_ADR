package com.example.a2hcomic.activities.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.a2hcomic.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView nav_main;

    private FrameLayout frame_main;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        nav_main = findViewById(R.id.nav_main);
        frame_main = findViewById(R.id.frame_main);

        frame_main.setOnTouchListener((v, event) -> {
            hideSoftKeyboard();
            return false;
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_main, new HomeFragment())
                .commit();

        nav_main.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            if (item.getItemId() == R.id.home_ic) {
                fragment = new HomeFragment();
            } else if (item.getItemId() == R.id.archive_ic) {
                fragment = new ArchiveFragment();
            } else if (item.getItemId() == R.id.noti_ic) {
                fragment = new NotiFragment();
            } else if (item.getItemId() == R.id.personal_ic) {
                fragment = new PersonalFragment();
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_main, fragment)
                    .commit();
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}