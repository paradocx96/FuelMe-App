package com.example.fuelme.ui.notice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuelme.R;
import com.example.fuelme.models.notice.Notice;

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
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.notice_list_item_station, parent, false);

        return new NoticeListStationAdapter.NoticeListStationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeListStationAdapter.NoticeListStationHolder holder, int position) {
        holder.title.setText(noticeArrayList.get(position).getTitle());
        holder.description.setText(noticeArrayList.get(position).getDescription());
        holder.author.setText(noticeArrayList.get(position).getAuthor());
        holder.createdAt.setText(noticeArrayList.get(position).getCreateAt());
    }

    @Override
    public int getItemCount() {
        return noticeArrayList.size();
    }

    public static class NoticeListStationHolder extends RecyclerView.ViewHolder {
        TextView id, title, description, author, stationId, createdAt;

        public NoticeListStationHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notice_item_title3);
            description = itemView.findViewById(R.id.notice_item_description3);
            author = itemView.findViewById(R.id.notice_item_author3);
            createdAt = itemView.findViewById(R.id.notice_item_created3);
        }
    }
}