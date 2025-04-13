package com.example.a2hcomic.db;



import androidx.annotation.NonNull;

import com.example.a2hcomic.models.Author;
import com.example.a2hcomic.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FirebaseService {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDBRef, mUserRef, mAuthorRef, mGenreRef, mComicRef, mComicGenre;
    private StorageReference mStorageRef, mComicStorageRef;

    // cấu trúc trong storage -
    // comicDB
    //    |___comics
    //           |___id-comic1
    //           |      |___img-title.png
    //           |      |___img-banner.png
    //           |      |___url-pdf.pdf
    //           |___id-comic2
    //           |___id-comic3
    //           |___...

    public FirebaseService() {
        mDatabase = FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage
                .getInstance("gs://comic4t.appspot.com")
                .getReference("comicDB");
        mDBRef = mDatabase.getReference("2HComic"); // đg dẫn tới DB

        mUserRef = mDBRef.child("users");
        mAuthorRef = mDBRef.child("authors");
        mGenreRef = mDBRef.child("genres");
        mComicRef = mDBRef.child("comics");
        mComicGenre = mDBRef.child("comic_genre");

        mComicStorageRef = mStorageRef.child("comics");
    }

    // Các phương thức getter cho các đối tượng Firebase
    public DatabaseReference getDBRef() {
        return mDBRef;
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

    public StorageReference getStorageComicRef() {
        return mComicStorageRef;
    }
    public DatabaseReference getGenreRef() {
        return mGenreRef;
    }

    public void createUser(User user) {
        String id = mUserRef.push().getKey();
        user.setId(id);
        mUserRef.child(user.getId()).setValue(user);
    }

}