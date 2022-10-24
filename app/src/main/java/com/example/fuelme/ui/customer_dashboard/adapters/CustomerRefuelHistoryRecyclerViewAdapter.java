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
import com.example.fuelme.models.QueueLogItem;

import java.util.ArrayList;

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
