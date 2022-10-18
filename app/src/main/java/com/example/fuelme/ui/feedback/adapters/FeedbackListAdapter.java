package com.example.fuelme.ui.feedback.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuelme.R;
import com.example.fuelme.models.Feedback;
import com.example.fuelme.ui.feedback.FeedbackList;

import java.util.ArrayList;

public class FeedbackListAdapter extends RecyclerView.Adapter<FeedbackListAdapter.FeedbackListHolder> {
    //1 - Feedback Adapter
    private Context context;
    private ArrayList<Feedback> feedbackArrayList;

    public FeedbackListAdapter(FeedbackList context, ArrayList<Feedback> feedbackArrayList) {
        this.context = context;
        this.feedbackArrayList = feedbackArrayList;
    }

    @NonNull
    @Override
    public FeedbackListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.feedback_single_item, parent, false);

        return new FeedbackListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackListAdapter.FeedbackListHolder holder, int position) {

        Feedback feedback = feedbackArrayList.get(position);
        holder.setDetails(feedback);
    }

    @Override
    public int getItemCount() {
        return feedbackArrayList.size();
    }

    //2 - FeedbackList Holder
    class FeedbackListHolder extends RecyclerView.ViewHolder {

        private TextView txtSubject, txtDescription, txtUsername, txtTimeDate;

        public FeedbackListHolder(@NonNull View itemView) {
            super(itemView);

            txtSubject = itemView.findViewById(R.id.txtSubject);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtTimeDate = itemView.findViewById(R.id.txtTimeDate);
        }

        void setDetails(Feedback feedback) {
            txtSubject.setText(feedback.getSubject());
            txtDescription.setText(feedback.getDescription());
            txtUsername.setText(feedback.getUsername());
            txtTimeDate.setText(feedback.getCreateAt());
        }
    }
}
