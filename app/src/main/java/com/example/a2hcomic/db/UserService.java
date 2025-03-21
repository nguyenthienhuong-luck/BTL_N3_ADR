package com.example.a2hcomic.db;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.a2hcomic.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserService {

    private final FirebaseService firebaseService;

    public UserService(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    public interface OnGetUserListener {
        void onGetUserResult(User user);
    }

    // up user
    public void createUser(User user) {
        String id = firebaseService.getUserRef().push().getKey();
        user.setId(id);
        firebaseService.getUserRef().child(user.getId()).setValue(user);
    }

    // check email exists
    public boolean checkEmailExists(String email) {
        // Kiểm tra xem email đã tồn tại trên Firebase hay chưa
        Query query = firebaseService.getUserRef().orderByChild("email").equalTo(email);
        return (query.get() != null); // nếu query không null thì email đã tồn tại
    }

    // get user by email

}
