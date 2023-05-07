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
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.network.model.CPDetailsResponse;
import com.pinnacle.winwin.network.model.PaanaDetailsResponse;
import com.pinnacle.winwin.ui.chartgame.model.ChartDetail;
import com.pinnacle.winwin.ui.singlegame.listener.SingleGameNumberListener;
import com.pinnacle.winwin.ui.singlegame.model.SingleGameNumberModel;
import com.pinnacle.winwin.utils.Utils;

import java.util.ArrayList;

public class GenericNumberAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = GenericNumberAdapter.class.getSimpleName();

    private Context mContext;
    private ArrayList<SingleGameNumberModel> singleGameNumberList;
    private ArrayList<CPDetailsResponse> cpDetails;
    private ArrayList<PaanaDetailsResponse> paanaDetails;
    private ArrayList<ChartDetail> chartDetails;
    private SingleGameNumberListener singleGameNumberListener;
    private int selectedIndex = -1;
    private int gameId = -1;
    public boolean isItemClickable = true;

    public GenericNumberAdapter(Context mContext, /*ArrayList<SingleGameNumberModel> singleGameNumberList*/
                                ArrayList<T> dataList, int gameId) {
        this.mContext = mContext;
        if (dataList != null && !dataList.isEmpty()) {
            if (dataList.get(0) instanceof SingleGameNumberModel) {
                this.singleGameNumberList = (ArrayList<SingleGameNumberModel>) dataList;
            } else if (dataList.get(0) instanceof CPDetailsResponse) {
                this.cpDetails = (ArrayList<CPDetailsResponse>) dataList;
            } else if (dataList.get(0) instanceof PaanaDetailsResponse) {
                this.paanaDetails = (ArrayList<PaanaDetailsResponse>) dataList;
            } else if (dataList.get(0) instanceof ChartDetail) {
                chartDetails = (ArrayList<ChartDetail>) dataList;
            }
        }
        this.gameId = gameId;
        singleGameNumberListener = (SingleGameNumberListener) mContext;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView textViewNumber;
        AppCompatTextView textViewPoints;

        RelativeLayout layoutNumber;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewNumber = itemView.findViewById(R.id.textViewNumber);
            textViewNumber.setTypeface(Utils.getTypeFaceBodoni72(mContext));
            textViewNumber.getPaint().setShader(Utils.getTextGradient(new int[]{mContext.getResources().getColor(R.color.colorStartGold),
                    mContext.getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

            textViewPoints = itemView.findViewById(R.id.textViewPoints);
            textViewPoints.setTypeface(Utils.getTypeFaceBodoni72(mContext));
            /*textViewPoints.getPaint().setShader(Utils.getTextGradient(new int[]{mContext.getResources().getColor(R.color.colorStartGold),
                    mContext.getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));*/

            layoutNumber = itemView.findViewById(R.id.layoutNumber);

            layoutNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (singleGameNumberListener != null && isItemClickable) {
                        /*boolean isPointsSelected = true;
                        SingleGameNumberModel singleGameNumberModel = singleGameNumberList.get(getAdapterPosition());
                        for (SingleGameNumberModel mSingleNumberModel : singleGameNumberList) {
                            if (mSingleNumberModel.isSelected() &&
                                    mSingleNumberModel.getPointsValue() == 0) {
                                Log.e(TAG, "POINTS NOT SELECTED");
                                isPointsSelected = false;
                            }
                        }
                        if (!singleGameNumberModel.isSelected() &&
                                isPointsSelected) {
                            singleGameNumberModel.setSelected(true);
                        } else {
                            singleGameNumberModel.setSelected(false);
                        }
                        singleGameNumberModel.setSelectedIndexPosition(getAdapterPosition());
                        singleGameNumberListener.onNumberSelectedListener(singleGameNumberModel);*/



                        singleGameNumberListener.onIndexSelectedListener(getAdapterPosition());
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_game_number, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewHolder viewHolder = (ViewHolder) holder;

        switch (gameId) {
            case AppConstant.GAME_TYPE_BRACKET:
            case AppConstant.GAME_TYPE_MOTOR:
            case AppConstant.GAME_TYPE_PAANA:
            case AppConstant.GAME_TYPE_SINGLE:
            case AppConstant.GAME_TYPE_COMMON:
                SingleGameNumberModel singleGameNumberModel = singleGameNumberList.get(position);

                viewHolder.textViewNumber.setText(singleGameNumberModel.getNumber());

                if (gameId != AppConstant.GAME_TYPE_BRACKET) {
                    if (singleGameNumberModel.isSelected()) {
                        viewHolder.layoutNumber.setBackgroundResource(R.drawable.bg_gold_gradient);
                        viewHolder.textViewNumber.getPaint().setShader(null);
                        viewHolder.textViewNumber.setTextColor(mContext.getResources().getColor(android.R.color.white));

                    } else {
                        viewHolder.layoutNumber.setBackgroundResource(R.drawable.border_gold);
                        viewHolder.textViewNumber.getPaint().setShader(Utils.getTextGradient(new int[]{mContext.getResources().getColor(R.color.colorStartGold),
                                mContext.getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));
                    }
                }

                if (singleGameNumberModel.isSelected() && singleGameNumberModel.getPointsValue() > 0) {
                    viewHolder.textViewPoints.setVisibility(View.VISIBLE);
                    viewHolder.textViewPoints.setText(String.valueOf(singleGameNumberModel.getPointsValue()));
                } else {
                    viewHolder.textViewPoints.setText("");
                    viewHolder.textViewPoints.setVisibility(View.INVISIBLE);
                }
                break;
            case AppConstant.GAME_TYPE_CP:
                CPDetailsResponse cpDetail = cpDetails.get(position);

                viewHolder.textViewNumber.setText(cpDetail.getPaanaNo());

                if (cpDetail.isSelected()) {
                    viewHolder.layoutNumber.setBackgroundResource(R.drawable.bg_gold_gradient);
                    viewHolder.textViewNumber.getPaint().setShader(null);
                    viewHolder.textViewNumber.setTextColor(mContext.getResources().getColor(android.R.color.white));

                } else {
                    viewHolder.layoutNumber.setBackgroundResource(R.drawable.border_gold);
                    viewHolder.textViewNumber.getPaint().setShader(Utils.getTextGradient(new int[]{mContext.getResources().getColor(R.color.colorStartGold),
                            mContext.getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));
                }

                if (cpDetail.isSelected() && cpDetail.getPointsValue() > 0) {
                    viewHolder.textViewPoints.setVisibility(View.VISIBLE);
                    viewHolder.textViewPoints.setText(String.valueOf(cpDetail.getPointsValue()));
                } else {
                    viewHolder.textViewPoints.setText("");
                    viewHolder.textViewPoints.setVisibility(View.INVISIBLE);
                }
                break;
            case AppConstant.GAME_TYPE_PAANA_NUMBER:
                PaanaDetailsResponse paanaDetail = paanaDetails.get(position);

                viewHolder.textViewNumber.setText(paanaDetail.getPaanaNo());

                if (paanaDetail.isSelected()) {
                    viewHolder.layoutNumber.setBackgroundResource(R.drawable.bg_gold_gradient);
                    viewHolder.textViewNumber.getPaint().setShader(null);
                    viewHolder.textViewNumber.setTextColor(mContext.getResources().getColor(android.R.color.white));

                } else {
                    viewHolder.layoutNumber.setBackgroundResource(R.drawable.border_gold);
                    viewHolder.textViewNumber.getPaint().setShader(Utils.getTextGradient(new int[]{mContext.getResources().getColor(R.color.colorStartGold),
                            mContext.getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));
                }

                if (paanaDetail.isSelected() && paanaDetail.getPointsValue() > 0) {
                    viewHolder.textViewPoints.setVisibility(View.VISIBLE);
                    viewHolder.textViewPoints.setText(String.valueOf(paanaDetail.getPointsValue()));
                } else {
                    viewHolder.textViewPoints.setText("");
                    viewHolder.textViewPoints.setVisibility(View.INVISIBLE);
                }
                break;
            case AppConstant.GAME_TYPE_CHART:
                ChartDetail chartDetail = chartDetails.get(position);

                viewHolder.textViewNumber.setText(chartDetail.getSingleValue());

                if (chartDetail.isSelected()) {
                    viewHolder.layoutNumber.setBackgroundResource(R.drawable.bg_gold_gradient);
                    viewHolder.textViewNumber.getPaint().setShader(null);
                    viewHolder.textViewNumber.setTextColor(mContext.getResources().getColor(android.R.color.white));

                } else {
                    viewHolder.layoutNumber.setBackgroundResource(R.drawable.border_gold);
                    viewHolder.textViewNumber.getPaint().setShader(Utils.getTextGradient(new int[]{mContext.getResources().getColor(R.color.colorStartGold),
                            mContext.getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));
                }

                if (chartDetail.isSelected() && chartDetail.getPointsValue() > 0) {
                    viewHolder.textViewPoints.setVisibility(View.VISIBLE);
                    viewHolder.textViewPoints.setText(String.valueOf(chartDetail.getPointsValue()));
                } else {
                    viewHolder.textViewPoints.setText("");
                    viewHolder.textViewPoints.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        switch (gameId) {
            case AppConstant.GAME_TYPE_SINGLE:
            case AppConstant.GAME_TYPE_PAANA:
            case AppConstant.GAME_TYPE_MOTOR:
            case AppConstant.GAME_TYPE_BRACKET:
            case AppConstant.GAME_TYPE_COMMON:
                return singleGameNumberList.size();
            case AppConstant.GAME_TYPE_CP:
                return cpDetails.size();
            case AppConstant.GAME_TYPE_PAANA_NUMBER:
                return paanaDetails.size();
            case AppConstant.GAME_TYPE_CHART:
                return chartDetails.size();
        }
        return 0;
    }

    public void updateData(ArrayList<T> dataList) {
        switch (gameId) {
            case AppConstant.GAME_TYPE_SINGLE:
            case AppConstant.GAME_TYPE_PAANA:
            case AppConstant.GAME_TYPE_MOTOR:
            case AppConstant.GAME_TYPE_BRACKET:
            case AppConstant.GAME_TYPE_COMMON:
                this.singleGameNumberList = (ArrayList<SingleGameNumberModel>) dataList;
                break;
            case AppConstant.GAME_TYPE_CP:
                this.cpDetails = (ArrayList<CPDetailsResponse>) dataList;
                break;
            case AppConstant.GAME_TYPE_PAANA_NUMBER:
                this.paanaDetails = (ArrayList<PaanaDetailsResponse>) dataList;
                break;
            case AppConstant.GAME_TYPE_CHART:
                this.chartDetails = (ArrayList<ChartDetail>) dataList;
                break;
        }
        notifyDataSetChanged();
    }
}
