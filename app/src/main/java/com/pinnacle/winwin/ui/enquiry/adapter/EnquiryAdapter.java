package com.pinnacle.winwin.ui.enquiry.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.network.model.ChatData;
import com.pinnacle.winwin.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class EnquiryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_TYPE_ME = 0;
    private static final int ITEM_TYPE_ADMIN = 1;
    private static final int ITEM_TYPE_HEADER = 2;

    private Context mContext;
    private ArrayList<ChatData> chats;
    public static boolean isEntireListLoaded = false;

    public EnquiryAdapter(Context mContext, ArrayList<ChatData> chats) {
        this.mContext = mContext;
        this.chats = chats;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView textViewEnquiry;
        AppCompatTextView textViewTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewEnquiry = itemView.findViewById(R.id.textViewEnquiry);
            textViewTime = itemView.findViewById(R.id.textViewTime);
        }
    }

    private class AdminViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView textViewEnquiry;
        AppCompatTextView textViewTime;

        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewEnquiry = itemView.findViewById(R.id.textViewEnquiry);
            textViewTime = itemView.findViewById(R.id.textViewTime);
        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBarFooter;

        public FooterViewHolder(@NonNull View itemView) {
            super(itemView);

            progressBarFooter = itemView.findViewById(R.id.progressBarFooter);
            // Change the default color of progress bar programmatically
            progressBarFooter.getIndeterminateDrawable().setColorFilter(mContext.getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case ITEM_TYPE_ME:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_enquiry_me, parent, false);
                viewHolder = new ViewHolder(view);
                break;
            case ITEM_TYPE_ADMIN:
                View adminView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_enquiry_admin, parent, false);
                viewHolder = new AdminViewHolder(adminView);
                break;
            case ITEM_TYPE_HEADER:
                View footerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_footer_progress, parent, false);
                viewHolder = new FooterViewHolder(footerView);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ChatData chatData = chats.get(position);

        switch (getItemViewType(position)) {
            case ITEM_TYPE_ME:
                ViewHolder viewHolder = (ViewHolder) holder;

                viewHolder.textViewEnquiry.setText(chatData.getMessage());
                viewHolder.textViewTime.setText(getChatDateString(chatData));
                break;
            case ITEM_TYPE_ADMIN:
                AdminViewHolder adminViewHolder = (AdminViewHolder) holder;

                adminViewHolder.textViewEnquiry.setText(chatData.getMessage());
                adminViewHolder.textViewTime.setText(getChatDateString(chatData));

                break;
        }

    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chats.get(position).getSentBy().equalsIgnoreCase("C")) {
            return ITEM_TYPE_ME;
        } else {
            return ITEM_TYPE_ADMIN;
        }
    }

    public void updateData(ArrayList<ChatData> chats) {
        if (chats != null) {
            this.chats = chats;
            notifyDataSetChanged();
        }
    }

    private String getChatDateString(ChatData chatData) {
        SimpleDateFormat simpleDateFormat = new
                SimpleDateFormat(DateUtils.UNIX_TIME_FORMAT, Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = DateUtils.getDateFromString(chatData.getSentAt(), simpleDateFormat);
        return DateUtils.getStringFormattedDate(date, DateUtils.CHAT_DATE_FORMAT);
    }
}
