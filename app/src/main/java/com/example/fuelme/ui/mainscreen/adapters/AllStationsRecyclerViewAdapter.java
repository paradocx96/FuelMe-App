package com.example.fuelme.ui.mainscreen.adapters;

import android.content.Context;
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

import java.util.ArrayList;

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

        //assigning values to the text views
        holder.txtViewStationName.setText(fuelStations.get(position).getStationName());
        holder.txtViewStationAddress.setText(fuelStations.get(position).getStationAddress());
        holder.txtViewOpenStatus.setText(fuelStations.get(position).getOpenStatus());
        holder.txtViewPetrolStatus.setText(fuelStations.get(position).getPetrolStatus());
        holder.txtViewDieselStatus.setText(fuelStations.get(position).getDieselStatus());


        //set onclick listener for card
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Card Clicked");
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
