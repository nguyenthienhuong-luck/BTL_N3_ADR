package com.example.a2hcomic.db;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.a2hcomic.models.Author;
import com.example.a2hcomic.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FirebaseService {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDBRef, mUserRef, mAuthorRef, mGenreRef, mComicRef, mComicGenre, mSliderRef;
    private StorageReference mStorageRef, mComicStorageRef, mSliderStorageRef;

    public FirebaseService() {
        mDatabase = FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage
                .getInstance("gs://comic4t.appspot.com")
                .getReference("comicDB");
        mDBRef = mDatabase.getReference("2HComic");

        mUserRef = mDBRef.child("users");
        mAuthorRef = mDBRef.child("authors");
        mGenreRef = mDBRef.child("genres");
        mComicRef = mDBRef.child("comics");
        mComicGenre = mDBRef.child("comic_genre");
        mSliderRef = mDBRef.child("sliders");

        mComicStorageRef = mStorageRef.child("comics");
        mSliderStorageRef = mStorageRef.child("sliders");
    }

    // Getter
    public StorageReference getSliderStorageRef() {
        return mSliderStorageRef;
    }

    public DatabaseReference getSliderRef() {
        return mSliderRef;
    }

    public DatabaseReference getComicGenreRef() {
        return mComicGenre;
    }

    public DatabaseReference getComicRef() {
        return mComicRef;
    }

    public DatabaseReference getUserRef() {
        return mUserRef;
    }

    public DatabaseReference getAuthorRef() {
        return mAuthorRef;
    }

    public DatabaseReference getGenreRef() {
        return mGenreRef;
    }

    public StorageReference getStorageComicRef() {
        return mComicStorageRef;
    }

    public void createUser(User user) {
        String id = mUserRef.push().getKey();
        user.setId(id);
        mUserRef.child(user.getId()).setValue(user);
    }

    // ✅ Tăng view count cho truyện
    public void increaseComicViewCount(String comicId) {
        DatabaseReference comicRef = mComicRef.child(comicId).child("viewCount");
        comicRef.runTransaction(new com.google.firebase.database.Transaction.Handler() {
            @NonNull
            @Override
            public com.google.firebase.database.Transaction.Result doTransaction(@NonNull MutableData currentData) {
                Long currentCount = currentData.getValue(Long.class);
                if (currentCount == null) {
                    currentData.setValue(1);
                } else {
                    currentData.setValue(currentCount + 1);
                }
                return com.google.firebase.database.Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                // Có thể log hoặc xử lý sau khi tăng view
            }
        });
    }
}
