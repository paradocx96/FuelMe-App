package com.example.fuelme.ui.customer_dashboard.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuelme.R;

public class CustomerRefuelHistoryRecyclerViewAdapter extends RecyclerView.Adapter<CustomerRefuelHistoryRecyclerViewAdapter.MyViewHolder> {
    @NonNull
    @Override
    public CustomerRefuelHistoryRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerRefuelHistoryRecyclerViewAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
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
