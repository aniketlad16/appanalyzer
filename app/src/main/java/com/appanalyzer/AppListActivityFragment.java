package com.appanalyzer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import com.appanalyzer.R;
import com.util.DividerItemDecoration;

import java.util.ArrayList;

public class AppListActivityFragment extends Fragment {

    SwipeRecyclerViewAdapter mAdapter;
    ArrayList<App> mItems;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_list, container, false);


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.app_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        recyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider)));
        APKProcessor apkProcessor = new APKProcessor();

        mItems = (ArrayList<App>) apkProcessor.apps;

        mAdapter = new SwipeRecyclerViewAdapter(getActivity(), mItems);
        recyclerView.setAdapter(mAdapter);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.e("RecyclerView", "onScrollStateChanged");
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        return view;
    }






    }





