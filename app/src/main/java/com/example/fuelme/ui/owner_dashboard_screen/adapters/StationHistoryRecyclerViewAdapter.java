package com.example.fuelme.ui.owner_dashboard_screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuelme.R;
import com.example.fuelme.helpers.DateTimeHelper;
import com.example.fuelme.models.FuelStation;
import com.example.fuelme.models.FuelStationLogItem;
import com.example.fuelme.models.time.CustomDateTime;

import java.util.ArrayList;

public class StationHistoryRecyclerViewAdapter extends RecyclerView.Adapter<StationHistoryRecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<FuelStationLogItem> fuelStationLogItems = new ArrayList<>();
    String TAG = "demo";


    @NonNull
    @Override
    public StationHistoryRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate the layout
        LayoutInflater inflater  = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.station_history_recyclerview_row, parent, false);

        return new StationHistoryRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StationHistoryRecyclerViewAdapter.MyViewHolder holder, int position) {
        FuelStationLogItem currentLogItem = fuelStationLogItems.get(position); //get current item by index

        int year = currentLogItem.getYear();
        int month = currentLogItem.getMonth();
        int dayNumber = currentLogItem.getDayNumber();
        int hour = currentLogItem.getHour();
        int minute = currentLogItem.getMinute();
        int second = currentLogItem.getSecond();

        CustomDateTime customDateTimeUTC = new CustomDateTime(year, month, dayNumber, hour, minute, second);
        CustomDateTime customDateTimeSL = DateTimeHelper.convertUTCToSLTime(customDateTimeUTC);

        String dateString = DateTimeHelper.getDateInISOFormat(customDateTimeSL);
        String timeString = DateTimeHelper.getTimeInTwentyFourHourFormat(customDateTimeSL);

        String fuelTypeString = currentLogItem.getFuelType();
        String fuelStatusString = currentLogItem.getFuelStatus();

        //better support capitalization
        if (fuelTypeString.equalsIgnoreCase("petrol")){
            fuelTypeString = "Petrol";
        }
        else if (fuelTypeString.equalsIgnoreCase("diesel")){
            fuelTypeString = "Diesel";
        }

        if (fuelStatusString.equalsIgnoreCase("available")) {

            fuelStatusString = "Available";
        }
        else if (fuelStatusString.equalsIgnoreCase("unavailable")){
            fuelStatusString = "Unavailable";
        }

        //assigning values to the text views
        holder.textViewDate.setText(dateString);
        holder.textViewTime.setText(timeString);
        holder.textViewFuelType.setText(fuelTypeString);
        holder.textViewFuelStatus.setText(fuelStatusString);
    }

    @Override
    public int getItemCount() {
        return fuelStationLogItems.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textViewDate, textViewTime, textViewFuelType, textViewFuelStatus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //assign the views to the layout components
            textViewDate = itemView.findViewById(R.id.txtView_date_station_history_row);
            textViewTime = itemView.findViewById(R.id.txtView_time_station_history_row);
            textViewFuelType = itemView.findViewById(R.id.txtView_fuelType_station_history_row);
            textViewFuelStatus = itemView.findViewById(R.id.txtView_fuelStatus_station_history_row);
        }
    }
}
