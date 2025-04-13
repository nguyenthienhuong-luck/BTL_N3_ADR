package com.example.a2hcomic.activities.main;

import android.app.Notification;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a2hcomic.R;
import com.example.a2hcomic.adapters.NotificationAdapter;

import java.util.ArrayList;


public class NotiFragment extends Fragment {

    private RecyclerView recyclerNotification;

    private NotificationAdapter notiAdapter;

    private ArrayList<Notification> notifications;
    public NotiFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_noti, container, false);

        recyclerNotification = view.findViewById(R.id.recyclerNotification);

        notifications = new ArrayList<>();

        notiAdapter = new NotificationAdapter(notifications);

        return view;
    }
}