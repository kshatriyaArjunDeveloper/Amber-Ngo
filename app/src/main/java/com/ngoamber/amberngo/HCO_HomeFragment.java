package com.ngoamber.amberngo;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Map;

import static com.ngoamber.amberngo.HC_OwnersActivity.healthcard_members;
import static com.ngoamber.amberngo.LoginActivity.USER_NO;


/**
 * A simple {@link Fragment} subclass.
 */
public class HCO_HomeFragment extends Fragment {

    ListView lv_hco;
    HCO_arrayadapter HCO_arrayadapter;
    ArrayList<Map> healthcard_members_fragments;


    public HCO_HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_hco__home, container, false);
        /* CUSTOM TOOLBAR */
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);


        // Create the adapter to convert the array to views
        healthcard_members_fragments = healthcard_members;
        HCO_arrayadapter = new HCO_arrayadapter(getActivity(), healthcard_members_fragments,USER_NO);
        // Attach the adapter to a ListView
        lv_hco = rootView.findViewById(R.id.lv_hco);
        lv_hco.setAdapter(HCO_arrayadapter);
        HCO_arrayadapter.notifyDataSetChanged();


        /* LAYOUT FOR FRAGMENT */
        return rootView;
    }




}
