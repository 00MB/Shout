package com.example.shout;

import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PublicAnnouncementsFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private RecyclerView mRecyclerView1;
    private RecyclerView.Adapter mAdapter1;
    private RecyclerView.LayoutManager mLayoutManager1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview= inflater.inflate(R.layout.fragment_announcements, container, false);
        ArrayList<AnnouncementItem> exampleList2= new ArrayList<>();
        exampleList2.add(new AnnouncementItem(R.drawable.ic_menu_camera , "Line 1", "Line 2","Line 3"));
        ArrayList<AnnouncementItem> exampleList1 = new ArrayList<>();
        exampleList1.add(new AnnouncementItem(R.drawable.ic_user , "Line 1", "Line 2","Line 3"));

        mRecyclerView = rootview.findViewById(R.id.announcementlist);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager=new LinearLayoutManager(getActivity());
        mAdapter = new AnnouncementAdapter(exampleList2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView1 = rootview.findViewById(R.id.announcementlist1);
        mRecyclerView1.setHasFixedSize(true);
        mLayoutManager1=new LinearLayoutManager(getActivity());
        mAdapter1 = new AnnouncementAdapter(exampleList1);
        mRecyclerView1.setLayoutManager(mLayoutManager1);
        mRecyclerView1.setAdapter(mAdapter1);

        return rootview;
    }
}
