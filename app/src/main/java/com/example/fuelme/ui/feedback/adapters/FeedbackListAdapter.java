package com.example.fuelme.ui.feedback.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuelme.R;
import com.example.fuelme.models.Feedback;
import com.example.fuelme.ui.feedback.FeedbackList;
import com.example.fuelme.ui.feedback.ViewFeedback;

import java.util.ArrayList;

/**
 * @author H.G. Malwatta - IT19240848
 * This class is used to display the feedback(s) in recycler view
 */
public class FeedbackListAdapter extends RecyclerView.Adapter<FeedbackListAdapter.FeedbackListHolder> {

    private Context context;
    private ArrayList<Feedback> feedbackArrayList;

    /**
     * overloaded constructor
     *
     * @param context
     * @param feedbackArrayList
     */
    public FeedbackListAdapter(FeedbackList context, ArrayList<Feedback> feedbackArrayList) {
        this.context = context;
        this.feedbackArrayList = feedbackArrayList;
    }

    /**
     * This method is used to create the view holder
     *
     * @param parent
     * @param viewType
     * @return FeedbackListHolder
     */
    @NonNull
    @Override
    public FeedbackListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //inflate the layout
        View view = LayoutInflater.from(context).inflate(R.layout.feedback_single_item, parent, false);

        //return the view holder
        return new FeedbackListHolder(view);
    }

    /**
     * This method is used to bind the data to the view holder
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull FeedbackListAdapter.FeedbackListHolder holder, @SuppressLint("RecyclerView") int position) {

        //get the feedback object from the array list at the current position
        Feedback feedback = feedbackArrayList.get(position);

        //set the feedback details to the text views
        holder.setDetails(feedback);

        //set the on click listener to the constraint layout
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //create an intent to navigate to the view feedback activity
                Intent intent = new Intent(context, ViewFeedback.class);

                //pass the feedback object to the view feedback activity
                intent.putExtra("feedback_id", feedbackArrayList.get(position).getId());
                intent.putExtra("feedback_stationId", feedbackArrayList.get(position).getStationId());
                intent.putExtra("feedback_subject", feedbackArrayList.get(position).getSubject());
                intent.putExtra("feedback_description", feedbackArrayList.get(position).getDescription());
                intent.putExtra("feedback_username", feedbackArrayList.get(position).getUsername());
                intent.putExtra("feedback_dateTime", feedbackArrayList.get(position).getCreateAt());

                //start the activity
                context.startActivity(intent);
            }
        });
    }

    /**
     * This method is used to get the item count
     *
     * @return int
     */
    @Override
    public int getItemCount() {
        return feedbackArrayList.size();
    }

    /**
     * This class is used to create the view holder
     */
    class FeedbackListHolder extends RecyclerView.ViewHolder {

        private TextView txtSubject, txtDescription, txtUsername, txtTimeDate;
        ConstraintLayout parentLayout;

        /**
         * This method is used to initialize the view holder
         *
         * @param itemView
         */
        public FeedbackListHolder(@NonNull View itemView) {
            super(itemView);

            //initialize the text views
            txtSubject = itemView.findViewById(R.id.txtSubject);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtTimeDate = itemView.findViewById(R.id.txtTimeDate);
            parentLayout = itemView.findViewById(R.id.feedback_single_item);
        }

        /**
         * This method is used to set the feedback details to the text views
         *
         * @param feedback
         */
        void setDetails(Feedback feedback) {

            //set the feedback details to the text views
            txtSubject.setText(feedback.getSubject());
            txtDescription.setText(feedback.getDescription());
            txtUsername.setText(feedback.getUsername().toUpperCase());
            txtTimeDate.setText(feedback.getCreateAt());
        }
    }
}
