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

public class NoticeListCustomerAdapter extends RecyclerView.Adapter<NoticeListCustomerAdapter.NoticeListCustomerHolder> {

    private final Context context;
    private final ArrayList<Notice> noticeArrayList;

    public NoticeListCustomerAdapter(Context context, ArrayList<Notice> noticeArrayList) {
        this.context = context;
        this.noticeArrayList = noticeArrayList;
    }

    @NonNull
    @Override
    public NoticeListCustomerAdapter.NoticeListCustomerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.notice_list_item_customer, parent, false);

        return new NoticeListCustomerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeListCustomerAdapter.NoticeListCustomerHolder holder, int position) {
        holder.title.setText(noticeArrayList.get(position).getTitle());
        holder.description.setText(noticeArrayList.get(position).getDescription());
        holder.author.setText(noticeArrayList.get(position).getAuthor());
        holder.createdAt.setText(noticeArrayList.get(position).getCreateAt());
    }

    @Override
    public int getItemCount() {
        return noticeArrayList.size();
    }


    public static class NoticeListCustomerHolder extends RecyclerView.ViewHolder {
        TextView id, title, description, author, stationId, createdAt;

        public NoticeListCustomerHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notice_item_title2);
            description = itemView.findViewById(R.id.notice_item_description2);
            author = itemView.findViewById(R.id.notice_item_author2);
            createdAt = itemView.findViewById(R.id.notice_item_created2);
        }
    }
}
