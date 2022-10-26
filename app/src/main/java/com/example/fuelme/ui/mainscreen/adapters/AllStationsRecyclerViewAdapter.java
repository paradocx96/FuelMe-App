package com.example.fuelme.ui.mainscreen.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuelme.R; //imported separately. Fragment issue?
import com.example.fuelme.models.FuelStation;
import com.example.fuelme.ui.mainscreen.StationSingleViewActivity;

import java.util.ArrayList;

/**
 *  IT19014128
 *  A.M.W.W.R.L. Wataketiya
 *
 * Recyclerview adapter for all stations
 *
 * References:
 *  https://developer.android.com/docs
 *  https://square.github.io/okhttp/
 *  https://youtu.be/Mc0XT58A1Z4
 * */

public class AllStationsRecyclerViewAdapter extends RecyclerView.Adapter<AllStationsRecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<FuelStation> fuelStations = new ArrayList<>();
    String TAG = "demo";

    public AllStationsRecyclerViewAdapter(Context context, ArrayList<FuelStation> fuelStations){
        this.context = context;
        this.fuelStations = fuelStations;
    }

    @NonNull
    @Override
    public AllStationsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate the layout
        LayoutInflater inflater  = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.all_stations_recyclerview_row, parent, false);

        return new AllStationsRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllStationsRecyclerViewAdapter.MyViewHolder holder, int position) {

        //get the fuel station for the position
        FuelStation currentFuelStation = fuelStations.get(position);

        //station open status
        String stationOpenStatus = currentFuelStation.getOpenStatus();

        //set the open status to support capitalization
        if (stationOpenStatus.equalsIgnoreCase("open")){
            stationOpenStatus = "Open";
        }
        else if (stationOpenStatus.equalsIgnoreCase("closed")){
            stationOpenStatus = "Closed";
        }

        //current fuel station fuel availability in strings
        String petrolAvailabilityString = currentFuelStation.getPetrolStatus();
        String dieselAvailabilityString = currentFuelStation.getDieselStatus();

        //set the availability strings to support capitalization
        if (petrolAvailabilityString.equalsIgnoreCase("available")){
            petrolAvailabilityString = "Available";
        }
        else if(petrolAvailabilityString.equalsIgnoreCase("unavailable")) {
            petrolAvailabilityString = "Unavailable";
        }


        if (dieselAvailabilityString.equalsIgnoreCase("available")){
            dieselAvailabilityString = "Available";
        }
        else if(dieselAvailabilityString.equalsIgnoreCase("unavailable")) {
            dieselAvailabilityString = "Unavailable";
        }

        //assigning values to the text views
        holder.txtViewStationName.setText(fuelStations.get(position).getStationName());
        holder.txtViewStationAddress.setText(fuelStations.get(position).getStationAddress());
        holder.txtViewOpenStatus.setText(stationOpenStatus);
        holder.txtViewPetrolStatus.setText(petrolAvailabilityString);
        holder.txtViewDieselStatus.setText(dieselAvailabilityString);

        //set the text color based on station open status
        if (stationOpenStatus.equalsIgnoreCase("open")){
           holder.txtViewOpenStatus.setTextColor(Color.parseColor("#0E8921"));
        }
        else if (stationOpenStatus.equalsIgnoreCase("closed")){
            holder.txtViewOpenStatus.setTextColor(Color.parseColor("#FF0000"));
        }

        //set text color based on fuel availability
        if (petrolAvailabilityString.equalsIgnoreCase("available")){
           holder.txtViewPetrolStatus.setTextColor(Color.parseColor("#0E8921"));
        }
        else if(petrolAvailabilityString.equalsIgnoreCase("unavailable")) {
            holder.txtViewPetrolStatus.setTextColor(Color.parseColor("#FF0000"));
        }


        if (dieselAvailabilityString.equalsIgnoreCase("available")){
            holder.txtViewDieselStatus.setTextColor(Color.parseColor("#0E8921"));
        }
        else if(dieselAvailabilityString.equalsIgnoreCase("unavailable")) {
            holder.txtViewDieselStatus.setTextColor(Color.parseColor("#FF0000"));
        }

        //set onclick listener for card
        //this navigates to the single view of the fuel station
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Card Clicked");
                Intent intent = new Intent(context, StationSingleViewActivity.class);
                intent.putExtra("selected_fuel_station", currentFuelStation); // put the selected fuel station as an extra into the intent
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fuelStations.size();
    }

    //inner class
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        //the text views in the row
        TextView txtViewStationName, txtViewStationAddress, txtViewOpenStatus, txtViewPetrolStatus, txtViewDieselStatus;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //assign the views to the layout components
            txtViewStationName = itemView.findViewById(R.id.txtView_StationName_asrv_row);
            txtViewStationAddress = itemView.findViewById(R.id.txtView_stationAddress_asrv_row);
            txtViewOpenStatus = itemView.findViewById(R.id.txtView_stationOpenStatus_asrv_row);
            txtViewPetrolStatus = itemView.findViewById(R.id.txtView_petrolStatus_asrv_row);
            txtViewDieselStatus = itemView.findViewById(R.id.txtView_dieselStatus_asrv_row);
            cardView = itemView.findViewById(R.id.cardView_asrv_row);

        }
    }
}
