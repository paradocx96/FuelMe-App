package com.example.fuelme.ui.owner_dashboard_screen.adapters;

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

import com.example.fuelme.R;
import com.example.fuelme.models.FuelStation;
import com.example.fuelme.ui.mainscreen.StationSingleViewActivity;
import com.example.fuelme.ui.mainscreen.adapters.AllStationsRecyclerViewAdapter;
import com.example.fuelme.ui.update_station_screen.UpdateStationActivity;

import java.util.ArrayList;

public class OwnerStationsRecyclerViewAdapter extends RecyclerView.Adapter<OwnerStationsRecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<FuelStation> fuelStations = new ArrayList<>();
    String TAG = "demo";

    public OwnerStationsRecyclerViewAdapter(Context context, ArrayList<FuelStation> fuelStations){
        this.context = context;
        this.fuelStations = fuelStations;
    }

    @NonNull
    @Override
    public OwnerStationsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate the layout
        LayoutInflater inflater  = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.owner_stations_recyclerview_row, parent, false);

        return new OwnerStationsRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OwnerStationsRecyclerViewAdapter.MyViewHolder holder, int position) {
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
                Intent intent = new Intent(context, UpdateStationActivity.class);
                intent.putExtra("selected_fuel_station", currentFuelStation); // put the selected fuel station as an extra into the intent
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fuelStations.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        //the text views in the row
        TextView txtViewStationName, txtViewStationAddress, txtViewOpenStatus, txtViewPetrolStatus, txtViewDieselStatus;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //assign the views to the layout components
            txtViewStationName = itemView.findViewById(R.id.txtView_StationName_osrv_row);
            txtViewStationAddress = itemView.findViewById(R.id.txtView_stationAddress_osrv_row);
            txtViewOpenStatus = itemView.findViewById(R.id.txtView_stationOpenStatus_osrv_row);
            txtViewPetrolStatus = itemView.findViewById(R.id.txtView_petrolStatus_osrv_row);
            txtViewDieselStatus = itemView.findViewById(R.id.txtView_dieselStatus_osrv_row);
            cardView = itemView.findViewById(R.id.cardView_owner_stations_recycler_view_row);
        }
    }
}
