package com.example.fuelme.ui.owner_dashboard_screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.fuelme.R;
import com.example.fuelme.models.FuelStation;
import com.example.fuelme.ui.owner_dashboard_screen.adapters.OwnerStationsRecyclerViewAdapter;

import java.util.ArrayList;

public class OwnerStationsActivity extends AppCompatActivity {

    private final String TAG = "demo";
    ArrayList<FuelStation> fuelStations = new ArrayList<>(); //array list for fuel stations

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_stations);

        //setup the fuel station list
        setupFuelStations();

        Toolbar toolbar = (Toolbar) findViewById(R.id.owner_stations_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //assign recycler view
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView_ownerStations);

        OwnerStationsRecyclerViewAdapter adapter = new OwnerStationsRecyclerViewAdapter(this, fuelStations);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //method for fetching data and assigning to fuel stations array list
    public void setupFuelStations(){
        FuelStation fuelStation1 = new FuelStation("0001","l001" ,"eheliyagoda",
                "Eheliyagoda Assotiates","41/8, Sangabo Mawatha, Colombo", "01144552",
                "ehe@gmail.com","ehe.com","open",
                10, 2, "available", "available",
                0, 0 );

        FuelStation fuelStation2 = new FuelStation("0002","l089" ,"madura",
                "Madura Assotiates","78, Kandy Road, Kiribathgoda", "01144552",
                "madura@gmail.com","madura.com","open",
                100, 20, "unavailable", "available",
                0, 0 );

        FuelStation fuelStation3 = new FuelStation("0003","l052" ,"wije",
                "Wije Assotiates","65/8/9, Wihara Road, Gampaha", "01144552",
                "wije@gmail.com","wije.com","closed",
                50, 72, "available", "unavailable",
                0, 0 );

        fuelStations.add(fuelStation1);
        fuelStations.add(fuelStation2);
        fuelStations.add(fuelStation3);
    }
}