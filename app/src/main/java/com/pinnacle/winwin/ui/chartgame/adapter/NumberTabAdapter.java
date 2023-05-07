package com.pinnacle.winwin.ui.chartgame.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.ui.chartgame.listener.ChartTabNumberListener;
import com.pinnacle.winwin.ui.chartgame.model.ChartTabNumber;
import com.pinnacle.winwin.utils.Utils;

import java.util.ArrayList;

public class NumberTabAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<ChartTabNumber> numberList;
    private ChartTabNumberListener chartTabNumberListener;

    public NumberTabAdapter(Context context, ArrayList<ChartTabNumber> numberList) {
        mContext = context;
        this.numberList = numberList;
        chartTabNumberListener = (ChartTabNumberListener) context;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView textViewNumber;

        RelativeLayout layoutNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewNumber = itemView.findViewById(R.id.textViewNumber);
            textViewNumber.setTypeface(Utils.getTypeFaceBodoni72(mContext));
            textViewNumber.getPaint().setShader(Utils.getTextGradient(new int[]{mContext.getResources().getColor(R.color.colorStartGold),
                    mContext.getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

            layoutNumber = itemView.findViewById(R.id.layoutNumber);

            layoutNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (chartTabNumberListener != null) {
                        chartTabNumberListener.onTabSelected(getAdapterPosition());
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_tab_number, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewHolder viewHolder = (ViewHolder) holder;

        ChartTabNumber gameNumber = numberList.get(position);

        viewHolder.textViewNumber.setText(gameNumber.getGameNo());

        if (gameNumber.isSelected()) {
            viewHolder.layoutNumber.setBackgroundResource(R.drawable.bg_gold_gradient);
            viewHolder.textViewNumber.getPaint().setShader(null);
            viewHolder.textViewNumber.setTextColor(mContext.getResources().getColor(android.R.color.white));

        } else {
            viewHolder.layoutNumber.setBackgroundResource(R.drawable.border_gold);
            viewHolder.textViewNumber.getPaint().setShader(Utils.getTextGradient(new int[]{mContext.getResources().getColor(R.color.colorStartGold),
                    mContext.getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));
        }
    }

    @Override
    public int getItemCount() {
        return numberList.size();
    }

    public void updateData(ArrayList<ChartTabNumber> chartTabNumbers) {
        numberList = chartTabNumbers;
        notifyDataSetChanged();
    }
}
