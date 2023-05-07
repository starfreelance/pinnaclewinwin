package com.pinnacle.winwin.ui.baazaar.adapter;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.network.model.BazaarDetailsResponse;
import com.pinnacle.winwin.ui.baazaar.listener.BaazaarListener;
import com.pinnacle.winwin.utils.Utils;

import java.util.ArrayList;

public class BaazaarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    ArrayList<BazaarDetailsResponse> bazaarDetails;
    private BaazaarListener baazaarListener;

    public BaazaarAdapter(Context mContext, ArrayList<BazaarDetailsResponse> bazaarDetails) {
        this.mContext = mContext;
        this.bazaarDetails = bazaarDetails;
        this.baazaarListener = (BaazaarListener) mContext;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout containerLayout;
        AppCompatTextView textViewBaazaarName;
        AppCompatTextView textViewTime;
        AppCompatTextView textViewPastResult;
        AppCompatTextView textViewBaazaarClosed;
        AppCompatTextView lblTime;
        AppCompatTextView lblPastResult;
        Button btnPlay;

        ImageView imgViewBaazaar;

        RelativeLayout layoutBaazaarClosed;

        public ViewHolder(View itemView) {
            super(itemView);

            containerLayout = itemView.findViewById(R.id.containerLayout);

            textViewBaazaarName = itemView.findViewById(R.id.textViewBaazaarName);
            textViewBaazaarName.setTypeface(Utils.getTypeFaceBodoni72(mContext));
            textViewBaazaarName.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{mContext.getResources().getColor(R.color.colorStartGold),
                    mContext.getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

            lblTime = itemView.findViewById(R.id.lblTime);
            lblTime.setTypeface(Utils.getTypeFaceBodoni72(mContext));
            lblTime.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{mContext.getResources().getColor(R.color.colorStartGold),
                    mContext.getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

            textViewTime = itemView.findViewById(R.id.textViewTime);
            textViewTime.setTypeface(Utils.getTypeFaceBodoni72(mContext));
            textViewTime.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{mContext.getResources().getColor(R.color.colorStartGold),
                    mContext.getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

            lblPastResult = itemView.findViewById(R.id.lblPastResult);
            lblPastResult.setTypeface(Utils.getTypeFaceBodoni72(mContext));
            lblPastResult.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{mContext.getResources().getColor(R.color.colorStartGold),
                    mContext.getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

            textViewPastResult = itemView.findViewById(R.id.textViewPastResult);
            textViewPastResult.setTypeface(Utils.getTypeFaceBodoni72(mContext));
            textViewPastResult.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{mContext.getResources().getColor(R.color.colorStartGold),
                    mContext.getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

            textViewBaazaarClosed = itemView.findViewById(R.id.textViewBaazaarClosed);
            textViewBaazaarClosed.setTypeface(Utils.getTypeFaceBodoni72(mContext));
            textViewBaazaarClosed.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{mContext.getResources().getColor(R.color.colorStartGold),
                    mContext.getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

            imgViewBaazaar = itemView.findViewById(R.id.imgViewBaazaar);

            layoutBaazaarClosed = itemView.findViewById(R.id.layoutBaazaarClosed);

            btnPlay = itemView.findViewById(R.id.btnPlay);

            btnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (baazaarListener != null) {
                        baazaarListener.onBaazaarSelectListener(bazaarDetails.get(getAdapterPosition()));
                    }
                }
            });

            containerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnPlay.performClick();
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_baazaar_grid, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewHolder viewHolder = (ViewHolder) holder;

        BazaarDetailsResponse bazaarDetail = bazaarDetails.get(position);

        viewHolder.textViewBaazaarName.setText(bazaarDetail.getBazaarName());
        viewHolder.imgViewBaazaar.setImageResource(bazaarDetail.getImgResource());
        /*viewHolder.textViewBaazaarName.setCompoundDrawablesWithIntrinsicBounds(baazaarModel.getImgResource(), 0, 0, 0);*/
        /*viewHolder.textViewTime.setText(String.format(
                mContext.getResources().getString(R.string.baazaar_time_value), bazaarDetail.getBazaarTiming()));*/
        viewHolder.textViewTime.setText(bazaarDetail.getBazaarTiming());
        if (bazaarDetail.getLastResult() != null &&
                !bazaarDetail.getLastResult().isEmpty()) {
            /*viewHolder.textViewPastResult.setText(String.format(
                    mContext.getResources().getString(R.string.baazaar_last_result_value), String.valueOf(bazaarDetail.getLastResult())));*/
            viewHolder.textViewPastResult.setText(String.valueOf(bazaarDetail.getLastResult()));
        } else {
            /*viewHolder.textViewPastResult.setText(String.format(
                    mContext.getResources().getString(R.string.baazaar_last_result_value), mContext.getResources().getString(R.string.final_number_empty)));*/
            viewHolder.textViewPastResult.setText(mContext.getResources().getString(R.string.final_number_empty));
        }

        if (!bazaarDetail.getIsOpenForBooking()) {
            /*viewHolder.layoutBaazaarClosed.setVisibility(View.VISIBLE);*/
            toggleBaazaarClosedView(true, viewHolder);
        } else {
            /*viewHolder.layoutBaazaarClosed.setVisibility(View.GONE);*/
            toggleBaazaarClosedView(false, viewHolder);
        }

    }

    @Override
    public int getItemCount() {
        return bazaarDetails.size();
    }

    private void toggleBaazaarClosedView(boolean isVisible, ViewHolder viewHolder) {
        if (isVisible) {
            viewHolder.layoutBaazaarClosed.setVisibility(View.VISIBLE);
            viewHolder.btnPlay.setVisibility(View.GONE);
            viewHolder.textViewBaazaarClosed.setVisibility(View.VISIBLE);
        } else {
            viewHolder.layoutBaazaarClosed.setVisibility(View.GONE);
            viewHolder.textViewBaazaarClosed.setVisibility(View.GONE);
            viewHolder.btnPlay.setVisibility(View.VISIBLE);
        }
    }
}
