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

public class NoticeListStationAdapter extends RecyclerView.Adapter<NoticeListStationAdapter.NoticeListStationHolder> {

    private final Context context;
    private final ArrayList<Notice> noticeArrayList;

    public NoticeListStationAdapter(Context context, ArrayList<Notice> noticeArrayList) {
        this.context = context;
        this.noticeArrayList = noticeArrayList;
    }

    @NonNull
    @Override
    public NoticeListStationAdapter.NoticeListStationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.notice_list_item_station, parent, false);

        return new NoticeListStationAdapter.NoticeListStationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeListStationAdapter.NoticeListStationHolder holder, @SuppressLint("RecyclerView") int position) {

        Notice notice = noticeArrayList.get(position);
        holder.setCardViewData(notice);

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

    @Override
    public int getItemCount() {
        return noticeArrayList.size();
    }

    public static class NoticeListStationHolder extends RecyclerView.ViewHolder {
        TextView title, author, createdAt;
        ConstraintLayout notice_item;
        LinearLayout notice_item_linear;

        public NoticeListStationHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notice_item_title3);
            author = itemView.findViewById(R.id.notice_item_author3);
            createdAt = itemView.findViewById(R.id.notice_item_created3);
            notice_item = itemView.findViewById(R.id.notice_item_station);
            notice_item_linear = itemView.findViewById(R.id.notice_list_item_sta);
        }

        void setCardViewData(Notice notice) {
            title.setText(notice.getTitle());
            author.setText(notice.getAuthor());
            createdAt.setText(notice.getCreateAt());
        }
    }
}