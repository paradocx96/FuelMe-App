package com.example.fuelme.ui.customer_dashboard.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuelme.R;
import com.example.fuelme.helpers.DateTimeHelper;
import com.example.fuelme.models.QueueLogItem;
import com.example.fuelme.models.time.CustomDateTime;

import java.util.ArrayList;

/*
* IT19014128
* A.M.W.W.R.L. Wataketiya
* Adapter for customer history recycler view row
* */

public class CustomerRefuelHistoryRecyclerViewAdapter extends RecyclerView.Adapter<CustomerRefuelHistoryRecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<QueueLogItem> queueLogItems = new ArrayList<>();
    String TAG = "demo";

    //constructor
    public CustomerRefuelHistoryRecyclerViewAdapter(Context context, ArrayList<QueueLogItem> queueLogItems){
        this.context = context;
        this.queueLogItems = queueLogItems;
    }

    @NonNull
    @Override
    public CustomerRefuelHistoryRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate the layout

        LayoutInflater inflater  = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.customer_refuel_history_recyclerview_row, parent, false);
        return new CustomerRefuelHistoryRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerRefuelHistoryRecyclerViewAdapter.MyViewHolder holder, int position) {

        //get the queue log item for the position
        QueueLogItem queueLogItem = queueLogItems.get(position);

        //get the date and time integers
        int year = queueLogItem.getYear();
        int month = queueLogItem.getMonth();
        int dayNumber = queueLogItem.getDayNumber();
        int hour = queueLogItem.getHour();
        int minute = queueLogItem.getMinute();
        int second = queueLogItem.getSecond();

        //instantiate custom date time
        CustomDateTime customDateTimeUTC = new  CustomDateTime(year, month,dayNumber, hour, minute, second);
        //get the time converted Sri Lankan time zone
        CustomDateTime customDateTimeSL = DateTimeHelper.convertUTCToSLTime(customDateTimeUTC);

        //get the date and time into strings
        String dateString = DateTimeHelper.getDateInISOFormat(customDateTimeSL);
        String timeString = DateTimeHelper.getTimeInTwentyFourHourFormat(customDateTimeSL);

        //get the queue, action and refuel status
        String queueString = queueLogItem.getQueue();
        String actionString = queueLogItem.getAction();
        String refuelStatusString = queueLogItem.getRefuelStatus();


        //change strings to better support capitalization
        if (queueString.equalsIgnoreCase("petrol")){
            queueString = "Petrol";
        }
        else if (queueString.equalsIgnoreCase("diesel")){
            queueString = "Diesel";
        }

        if (actionString.equalsIgnoreCase("join")){
            actionString = "Join";
        }
        else if (actionString.equalsIgnoreCase("leave")){
            actionString = "Leave";
        }

        if (refuelStatusString.equalsIgnoreCase("refueled")){
            refuelStatusString = "Refueled";
        }
        else if (refuelStatusString.equalsIgnoreCase("not-refueled")){
            refuelStatusString = "Not Refueled";
        }
        else if (refuelStatusString.equalsIgnoreCase("not-appliable")){
            refuelStatusString = "Not Applicable";
        }

        //assigning values to the text views
        holder.textViewDate.setText(dateString);
        holder.textViewTime.setText(timeString);
        holder.textViewStationId.setText(queueLogItem.getStationId());
        holder.textViewStationName.setText(queueLogItem.getStationName());
        holder.textViewQueue.setText(queueString);
        holder.textViewAction.setText(actionString);
        holder.textViewRefuelStatus.setText(refuelStatusString);

    }

    @Override
    public int getItemCount() {
        return queueLogItems.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        //views in the row
        TextView textViewDate, textViewTime, textViewStationId, textViewStationName, textViewQueue, textViewAction, textViewRefuelStatus;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //assign the views to the layout components
            textViewDate = itemView.findViewById(R.id.txtView_date_customer_refuel_history_row);
            textViewTime = itemView.findViewById(R.id.txtView_time_customer_refuel_history_row);
            textViewStationId = itemView.findViewById(R.id.txtView_stationId_customer_refuel_history_row);
            textViewStationName = itemView.findViewById(R.id.txtView_stationName_customer_refuel_history_row);
            textViewQueue = itemView.findViewById(R.id.txtView_queue_customer_refuel_history_row);
            textViewAction = itemView.findViewById(R.id.txtView_action_customer_refuel_history_row);
            textViewRefuelStatus = itemView.findViewById(R.id.txtView_refuelStatus_customer_refuel_history_row);

        }
    }
}
