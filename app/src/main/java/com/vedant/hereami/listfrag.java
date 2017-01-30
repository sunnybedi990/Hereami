package com.vedant.hereami;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class listfrag extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout swipeLayout;
    private View view;
    ListActivity Layout = new ListActivity();

    public listfrag() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_listfrag, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipelayout);
        Layout.getdata();
        swipeLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        Layout.onRefresh();
    }


}
