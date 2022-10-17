package com.example.fuelme.ui.mainscreen.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fuelme.R;
import com.example.fuelme.models.FuelStation;
import com.example.fuelme.ui.mainscreen.adapters.AllStationsRecyclerViewAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllStationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllStationsFragment extends Fragment {

    private final String TAG = "demo";
    ArrayList<FuelStation> fuelStations = new ArrayList<>(); //array list for fuel stations

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AllStationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllStationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllStationsFragment newInstance(String param1, String param2) {
        AllStationsFragment fragment = new AllStationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_all_stations, container, false);

        //setup the fuel station list
        setupFuelStations();

        //assign recycler view
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_allStations);

        //get activity gets parent context (probably) or try getContext()
        AllStationsRecyclerViewAdapter adapter = new AllStationsRecyclerViewAdapter(getActivity(), fuelStations);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    //method for fetching data and assigning to fuel stations array list
    public void setupFuelStations(){
        FuelStation fuelStation1 = new FuelStation("0001","l001" ,"eheliyagoda",
                "Eheliyagoda Assotiates","address, adress road.", "01144552",
                "ehe@gmail.com","ehe.com","open",
                10, 2, "avaialble", "avaialble",
                0, 0 );

        FuelStation fuelStation2 = new FuelStation("0002","l089" ,"madura",
                "Madura Assotiates","address, adress road.", "01144552",
                "madura@gmail.com","madura.com","open",
                100, 20, "avaialble", "avaialble",
                0, 0 );

        FuelStation fuelStation3 = new FuelStation("0003","l052" ,"wije",
                "Wije Assotiates","address, adress road.", "01144552",
                "wije@gmail.com","wije.com","open",
                100, 20, "avaialble", "avaialble",
                0, 0 );

        fuelStations.add(fuelStation1);
        fuelStations.add(fuelStation2);
        fuelStations.add(fuelStation3);
    }
}