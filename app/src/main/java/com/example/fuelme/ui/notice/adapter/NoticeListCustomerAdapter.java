package com.example.fuelme.ui.notice.adapter;

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
import com.example.fuelme.models.notice.Notice;
import com.example.fuelme.ui.notice.NoticeViewCustomerActivity;

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
        View view = LayoutInflater.from(context).inflate(R.layout.notice_list_item_customer, parent, false);

        return new NoticeListCustomerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeListCustomerAdapter.NoticeListCustomerHolder holder, @SuppressLint("RecyclerView") int position) {

        Notice notice = noticeArrayList.get(position);
        holder.setCardViewData(notice);

        holder.notice_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NoticeViewCustomerActivity.class);
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

    public static class NoticeListCustomerHolder extends RecyclerView.ViewHolder {
        TextView title, author, createdAt;
        ConstraintLayout notice_item;

        public NoticeListCustomerHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notice_item_title2);
            author = itemView.findViewById(R.id.notice_item_author2);
            createdAt = itemView.findViewById(R.id.notice_item_created2);
            notice_item = itemView.findViewById(R.id.notice_item_customer);
        }

        void setCardViewData(Notice notice) {
            title.setText(notice.getTitle());
            author.setText(notice.getAuthor());
            createdAt.setText(notice.getCreateAt());
        }
    }
}
