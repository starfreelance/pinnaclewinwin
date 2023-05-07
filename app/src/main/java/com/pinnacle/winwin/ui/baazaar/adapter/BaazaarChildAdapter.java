package com.pinnacle.winwin.ui.baazaar.adapter;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.RecyclerView;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.network.model.BazaarDetailsResponse;
import com.pinnacle.winwin.network.model.CustomGamesResponse;
import com.pinnacle.winwin.ui.baazaar.listener.BaazaarListener;
import com.pinnacle.winwin.utils.Utils;

import java.util.ArrayList;

public class BaazaarChildAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    ArrayList<BazaarDetailsResponse> bazaarDetails;
    ArrayList<CustomGamesResponse> customGames;
    private BaazaarListener baazaarListener;
    private int genericId = -1;
    private boolean inApproval = false;

    public BaazaarChildAdapter(Context mContext, /*ArrayList<BazaarDetailsResponse> bazaarDetails*/
                               ArrayList<T> genericGames, int genericId, boolean inApproval) {
        this.mContext = mContext;
        if (genericGames != null && !genericGames.isEmpty()) {
            if (genericGames.get(0) instanceof CustomGamesResponse) {
                this.customGames = (ArrayList<CustomGamesResponse>) genericGames;
            } else if (genericGames.get(0) instanceof BazaarDetailsResponse) {
                this.bazaarDetails = (ArrayList<BazaarDetailsResponse>) genericGames;
            }
        }
        this.genericId = genericId;
//        this.bazaarDetails = bazaarDetails;
        this.baazaarListener = (BaazaarListener) mContext;
        this.inApproval = inApproval;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView textViewBaazaarName;
        AppCompatTextView textViewTime;
        AppCompatTextView textViewPastResult;
        AppCompatTextView textViewBaazaarClosed;
        Group gameInfoGroup;

        AppCompatImageView imgViewPlay;

        RelativeLayout layoutBaazaarClosed;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewBaazaarName = itemView.findViewById(R.id.textViewBaazaarName);
            textViewBaazaarName.setTypeface(Utils.getTypeFaceBodoni72(mContext));
            textViewBaazaarName.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{mContext.getResources().getColor(R.color.colorStartGold),
                    mContext.getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

            textViewTime = itemView.findViewById(R.id.textViewTime);
            textViewTime.setTypeface(Utils.getTypeFaceBodoni72(mContext));
            textViewTime.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{mContext.getResources().getColor(R.color.colorStartGold),
                    mContext.getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

            textViewPastResult = itemView.findViewById(R.id.textViewPastResult);
            textViewPastResult.setTypeface(Utils.getTypeFaceBodoni72(mContext));
            textViewPastResult.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{mContext.getResources().getColor(R.color.colorStartGold),
                    mContext.getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

            textViewBaazaarClosed = itemView.findViewById(R.id.textViewBaazaarClosed);
            textViewBaazaarClosed.setTypeface(Utils.getTypeFaceBodoni72(mContext));

            imgViewPlay = itemView.findViewById(R.id.imgViewPlay);

            gameInfoGroup = itemView.findViewById(R.id.gameInfoGroup);

            layoutBaazaarClosed = itemView.findViewById(R.id.layoutBaazaarClosed);

            if (!inApproval) {
                imgViewPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (genericId == AppConstant.GENERIC_TYPE_GAMES) {
                            if (baazaarListener != null) {
                                baazaarListener.onCustomGameSelectListener(customGames.get(getAdapterPosition()));
                            }
                        } else if (genericId == AppConstant.GENERIC_TYPE_BAAZAAR) {
                            if (baazaarListener != null) {
                                baazaarListener.onBaazaarSelectListener(bazaarDetails.get(getAdapterPosition()));
                            }
                        }
                    }
                });

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imgViewPlay.performClick();
                    }
                });
            }

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_baazaar, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewHolder viewHolder = (ViewHolder) holder;

        switch (genericId) {
            case AppConstant.GENERIC_TYPE_GAMES:
                CustomGamesResponse customGame = customGames.get(position);

                viewHolder.textViewBaazaarName.setText(customGame.getGameName());
                viewHolder.textViewBaazaarName.setCompoundDrawablesWithIntrinsicBounds(customGame.getImgResource(), 0, 0, 0);

                viewHolder.gameInfoGroup.setVisibility(View.GONE);

                /*viewHolder.textViewTime.setText(String.format(
                        mContext.getResources().getString(R.string.baazaar_time_value), bazaarDetail.getBazaarTiming()));

                if (bazaarDetail.getLastResult() != null &&
                        !bazaarDetail.getLastResult().isEmpty()) {
                    viewHolder.textViewPastResult.setText(String.format(
                            mContext.getResources().getString(R.string.baazaar_last_result_value), bazaarDetail.getLastResult()));
                } else {
                    viewHolder.textViewPastResult.setText(String.format(
                            mContext.getResources().getString(R.string.baazaar_last_result_value), mContext.getResources().getString(R.string.final_number_empty)));
                }*/
                break;
            case AppConstant.GENERIC_TYPE_BAAZAAR:
                BazaarDetailsResponse bazaarDetail = bazaarDetails.get(position);

                viewHolder.textViewBaazaarName.setText(bazaarDetail.getBazaarName());
                viewHolder.textViewBaazaarName.setCompoundDrawablesWithIntrinsicBounds(bazaarDetail.getImgResource(), 0, 0, 0);

                viewHolder.gameInfoGroup.setVisibility(View.VISIBLE);

                viewHolder.textViewTime.setText(String.format(
                        mContext.getResources().getString(R.string.baazaar_time_value), bazaarDetail.getBazaarTiming()));

                if (bazaarDetail.getLastResult() != null &&
                        !bazaarDetail.getLastResult().isEmpty()) {
                    viewHolder.textViewPastResult.setText(String.format(
                            mContext.getResources().getString(R.string.baazaar_last_result_value), bazaarDetail.getLastResult()));
                } else {
                    viewHolder.textViewPastResult.setText(String.format(
                            mContext.getResources().getString(R.string.baazaar_last_result_value), mContext.getResources().getString(R.string.final_number_empty)));
                }

                if (!bazaarDetail.getIsOpenForBooking()) {
                    toggleBaazaarClosedView(true, viewHolder);
                } else {
                    toggleBaazaarClosedView(false, viewHolder);
                }
                break;
        }



    }

    @Override
    public int getItemCount() {
        switch (genericId) {
            case AppConstant.GENERIC_TYPE_GAMES:
                return customGames.size();
            case AppConstant.GENERIC_TYPE_BAAZAAR:
                return bazaarDetails.size();
        }
        return 0;
    }

    private void toggleBaazaarClosedView(boolean isVisible, ViewHolder viewHolder) {
        if (isVisible) {
            viewHolder.layoutBaazaarClosed.setVisibility(View.VISIBLE);
            viewHolder.textViewBaazaarClosed.setVisibility(View.VISIBLE);
        } else {
            viewHolder.layoutBaazaarClosed.setVisibility(View.GONE);
            viewHolder.textViewBaazaarClosed.setVisibility(View.GONE);
        }
    }
}
