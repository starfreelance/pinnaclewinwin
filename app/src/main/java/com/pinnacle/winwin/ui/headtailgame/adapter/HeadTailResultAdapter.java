package com.pinnacle.winwin.ui.headtailgame.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.network.model.HTLastResultResponse;
import com.pinnacle.winwin.utils.Utils;

import java.util.ArrayList;

public class HeadTailResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<HTLastResultResponse> htLastResults;

    public HeadTailResultAdapter(Context mContext, ArrayList<HTLastResultResponse> htLastResults) {
        this.mContext = mContext;
        this.htLastResults = htLastResults;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView textViewResult;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewResult = itemView.findViewById(R.id.textViewResult);
            textViewResult.setTypeface(Utils.getTypeFaceBodoni72(mContext));
            /*textViewResult.getPaint().setShader(Utils.getTextGradient(new int[]{mContext.getResources().getColor(R.color.colorStartGold),
                    mContext.getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));*/
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_head_tail_result, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewHolder viewHolder = (ViewHolder) holder;

        HTLastResultResponse htLastResult = htLastResults.get(position);

        viewHolder.textViewResult.setText(htLastResult.getBetType());

    }

    @Override
    public int getItemCount() {
        return htLastResults == null ? 0 : htLastResults.size();
    }

    public void updateData(ArrayList<HTLastResultResponse> htLastResults) {
        this.htLastResults = htLastResults;
        notifyDataSetChanged();
    }
}
