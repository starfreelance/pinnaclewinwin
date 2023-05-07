package com.pinnacle.winwin.ui.headtailgame.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.network.model.CustomGameAmountResponse;
import com.pinnacle.winwin.ui.headtailgame.listener.HeadTailPointsListener;
import com.pinnacle.winwin.utils.Utils;

import java.util.ArrayList;

public class HeadTailPointsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<CustomGameAmountResponse> customGameAmounts;
    private HeadTailPointsListener headTailPointsListener;

    public HeadTailPointsAdapter(Context mContext, ArrayList<CustomGameAmountResponse> customGameAmounts) {
        this.mContext = mContext;
        this.customGameAmounts = customGameAmounts;
        headTailPointsListener = (HeadTailPointsListener) mContext;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout containerLayout;
        AppCompatTextView textViewPoints;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewPoints = itemView.findViewById(R.id.textViewPoints);
            textViewPoints.setTypeface(Utils.getTypeFaceBodoni72(mContext));
            /*textViewPoints.getPaint().setShader(Utils.getTextGradient(new int[]{mContext.getResources().getColor(R.color.colorStartGold),
                    mContext.getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));*/

            containerLayout = itemView.findViewById(R.id. containerLayout);

            containerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (headTailPointsListener != null) {
                        headTailPointsListener.onPointsSelectListener(customGameAmounts.get(getAdapterPosition()));
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_head_tail_points, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewHolder viewHolder = (ViewHolder) holder;

        CustomGameAmountResponse customGameAmount = customGameAmounts.get(position);

        viewHolder.textViewPoints.setText(customGameAmount.getAmountDisplay());

    }

    @Override
    public int getItemCount() {
        return customGameAmounts.size();
    }
}
