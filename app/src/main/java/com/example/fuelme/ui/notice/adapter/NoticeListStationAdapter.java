/**
 * FuelMe APP
 * Enterprise Application Development - SE4040
 *
 * @author IT19180526 - S.A.N.L.D. Chandrasiri
 * @version 1.0
 */

package com.example.fuelme.ui.notice.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuelme.R;
import com.example.fuelme.models.notice.Notice;
import com.example.fuelme.ui.notice.NoticeViewStationActivity;

import java.util.ArrayList;

/**
 * Station owner Notice List Adapter class for FuelMe Application
 *
 * @author IT19180526 - S.A.N.L.D. Chandrasiri
 * @version 1.0
 */
public class NoticeListStationAdapter extends RecyclerView.Adapter<NoticeListStationAdapter.NoticeListStationHolder> {

    // Defined object and variables
    private final Context context;
    private final ArrayList<Notice> noticeArrayList;

    // Defined Constructor
    public NoticeListStationAdapter(Context context, ArrayList<Notice> noticeArrayList) {
        this.context = context;
        this.noticeArrayList = noticeArrayList;
    }

    /**
     * This method used to defined and binding view object with list view layout.
     *
     * @param parent   - @NonNull ViewGroup
     * @param viewType - Integer
     * @return NoticeListStationAdapter.NoticeListStationHolder
     * @see #onCreateViewHolder(ViewGroup, int)
     */
    @NonNull
    @Override
    public NoticeListStationAdapter.NoticeListStationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Defined and assign layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.notice_list_item_station, parent, false);

        return new NoticeListStationAdapter.NoticeListStationHolder(view);
    }

    /**
     * This method used to make recycle list view in List view UI
     *
     * @param holder   - @NonNull NoticeListCustomerAdapter.NoticeListCustomerHolder
     * @param position - Integer
     * @see #onBindViewHolder(NoticeListStationHolder, int)
     */
    @Override
    public void onBindViewHolder(@NonNull NoticeListStationAdapter.NoticeListStationHolder holder, @SuppressLint("RecyclerView") int position) {
        // Create notice item using arraylist position
        Notice notice = noticeArrayList.get(position);

        // Setup one card view data to visualize in List view
        holder.setCardViewData(notice);

        // Make list item onclick listener and assign intent values to brings data to single view UI
        holder.notice_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NoticeViewStationActivity.class);
                intent.putExtra("notice_id", noticeArrayList.get(position).getId());
                intent.putExtra("notice_station_id", noticeArrayList.get(position).getStationId());
                intent.putExtra("notice_title", noticeArrayList.get(position).getTitle());
                intent.putExtra("notice_description", noticeArrayList.get(position).getDescription());
                intent.putExtra("notice_author", noticeArrayList.get(position).getAuthor());
                intent.putExtra("notice_created", noticeArrayList.get(position).getCreateAt());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    /**
     * This method used to return size of arraylist that show in List view
     *
     * @return Integer
     * @see #getItemCount()
     */
    @Override
    public int getItemCount() {
        return noticeArrayList.size();
    }

    /**
     * This class used to defined local variables and assign that with List view objects
     *
     * @see NoticeListStationHolder
     */
    public static class NoticeListStationHolder extends RecyclerView.ViewHolder {
        // Defined object and variables
        TextView title, author, createdAt;
        ConstraintLayout notice_item;
        LinearLayout notice_item_linear;

        // Defined Constructor
        public NoticeListStationHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notice_item_title3);
            author = itemView.findViewById(R.id.notice_item_author3);
            createdAt = itemView.findViewById(R.id.notice_item_created3);
            notice_item = itemView.findViewById(R.id.notice_item_station);
            notice_item_linear = itemView.findViewById(R.id.notice_list_item_sta);
        }

        /**
         * This method used to set the local variable with list data
         *
         * @param notice - Notice
         * @see #setCardViewData(Notice)
         */
        void setCardViewData(Notice notice) {
            title.setText(notice.getTitle());
            author.setText(notice.getAuthor());
            createdAt.setText(notice.getCreateAt());
        }
    }
}