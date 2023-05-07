package com.pinnacle.winwin.ui.singlegame.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.network.model.AmountDetailsResponse;
import com.pinnacle.winwin.ui.singlegame.listener.SelectPointsListener;
import com.pinnacle.winwin.ui.singlegame.model.PointsModel;
import com.pinnacle.winwin.utils.Utils;

import java.util.ArrayList;

public class SelectPointsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<PointsModel> pointsList;
    private ArrayList<AmountDetailsResponse> amountDetails;
    private SelectPointsListener selectPointsListener;

    public SelectPointsAdapter(Context mContext, /*ArrayList<PointsModel> pointsList*/
                               ArrayList<AmountDetailsResponse> amountDetails) {
        this.mContext = mContext;
        /*this.pointsList = pointsList;*/
        this.amountDetails = amountDetails;
        this.selectPointsListener = (SelectPointsListener) mContext;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout containerLayout;
        AppCompatTextView textViewPoints;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewPoints = itemView.findViewById(R.id.textViewPoints);
            textViewPoints.setTypeface(Utils.getTypeFaceBodoni72(mContext));
            textViewPoints.getPaint().setShader(Utils.getTextGradient(new int[]{mContext.getResources().getColor(R.color.colorStartGold),
                    mContext.getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

            containerLayout = itemView.findViewById(R.id. containerLayout);

            containerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectPointsListener != null) {
                        /*selectPointsListener.onPointsSelectListener(pointsList.get(getAdapterPosition()));*/
                        selectPointsListener.onPointsSelectListener(amountDetails.get(getAdapterPosition()));
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_points, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewHolder viewHolder = (ViewHolder) holder;

        /*PointsModel pointsModel = pointsList.get(position);

        viewHolder.textViewPoints.setText(pointsModel.getPointValue());*/

        AmountDetailsResponse amountDetail = amountDetails.get(position);

        viewHolder.textViewPoints.setText(amountDetail.getAmountDisplay());

    }

    @Override
    public int getItemCount() {
        return /*pointsList.size()*/amountDetails.size();
    }
}
