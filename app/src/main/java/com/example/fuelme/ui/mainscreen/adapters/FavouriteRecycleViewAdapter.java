/**
 * EAD - FuelMe Mobile App
 *
 * @author H.G. Malwatta - IT19240848
 * @version 1.0.0
 */

package com.example.fuelme.ui.mainscreen.adapters;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;

/**
 * H.G. Malwatta - IT19240848
 *
 * This class is used to create the recycler view for the favourite fuel stations
 */
public class FavouriteRecycleViewAdapter extends RecyclerView.Adapter<FavouriteRecycleViewAdapter.FavouriteViewHolder> {

    Context context;
    ArrayList<FuelStation> fuelStations = new ArrayList<>();

    /**
     * Constructor
     * @param context
     * @param fuelStations
     */
    public FavouriteRecycleViewAdapter(Context context, ArrayList<FuelStation> fuelStations) {
        this.context = context;
        this.fuelStations = fuelStations;
    }

    /**
     * This method is used to create the view holder
     * @param parent
     * @param viewType
     * @return FavouriteViewHolder
     */
    @NonNull
    @Override
    public FavouriteRecycleViewAdapter.FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Inflate the layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.favourite_stations_recycleview_row, parent, false);

        return new FavouriteRecycleViewAdapter.FavouriteViewHolder(view);
    }

    /**
     * This method is used to bind the view holder
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull FavouriteViewHolder holder, int position) {

        //get the fuel station for the position
        FuelStation currentFuelStation = fuelStations.get(position);

        //station open status
        String stationOpenStatus = currentFuelStation.getOpenStatus();

        //set the open status to support capitalization
        if (stationOpenStatus.equalsIgnoreCase("open")) {
            stationOpenStatus = "Open";
            holder.favouriteOpenClosedIcon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_open_status,0,0,0);
        } else if (stationOpenStatus.equalsIgnoreCase("closed")) {
            stationOpenStatus = "Closed";
            holder.favouriteOpenClosedIcon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_close_status,0,0,0);
        }

        //set the text for the text views
        holder.favouriteFuelStationName.setText(currentFuelStation.getStationName());
        holder.favouriteFuelStationAddress.setText(currentFuelStation.getStationAddress());
        holder.favouriteFuelStationOpenStatus.setText(stationOpenStatus);


        //set onclick listener for the card view
        holder.favouriteStationCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create an intent to go to the single view activity
                Intent intent = new Intent(context, StationSingleViewActivity.class);
                intent.putExtra("selected_fuel_station", currentFuelStation);
                context.startActivity(intent);
            }
        });

    }

    /**
     * This method is used to get the item count
     * @return int
     */
    @Override
    public int getItemCount() {
        return fuelStations.size();
    }

    /**
     * This class is used to create the view holder
     */
    public static class FavouriteViewHolder extends RecyclerView.ViewHolder {

        TextView favouriteFuelStationName, favouriteFuelStationAddress, favouriteFuelStationOpenStatus, favouriteOpenClosedIcon;
        CardView favouriteStationCardView;

        /**
         * Constructor
         * @param itemView
         */
        public FavouriteViewHolder(@NonNull View itemView) {
            super(itemView);

            //assign the views to the layout components
            favouriteFuelStationName = itemView.findViewById(R.id.txtView_StationName_favourite_row);
            favouriteFuelStationAddress = itemView.findViewById(R.id.txtView_stationAddress_favourite_row);
            favouriteFuelStationOpenStatus = itemView.findViewById(R.id.txtView_stationOpenStatus_favourite_row);
            favouriteOpenClosedIcon = itemView.findViewById(R.id.txtView_openClosedIcon_favourite_row);
            favouriteStationCardView = itemView.findViewById(R.id.cardView_favourite_row);

        }
    }

}
