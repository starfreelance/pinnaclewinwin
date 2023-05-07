package com.pinnacle.winwin.ui.home.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.network.model.GameDetailsResponse;
import com.pinnacle.winwin.ui.home.listener.SelectGameListener;
import com.pinnacle.winwin.ui.home.model.SelectGameModel;
import com.pinnacle.winwin.utils.Utils;

import java.util.ArrayList;

public class SelectGameAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<SelectGameModel> selectGameList;
    private SelectGameListener selectGameListener;
    private ArrayList<GameDetailsResponse> gameDetails;

    public SelectGameAdapter(Context mContext, ArrayList<GameDetailsResponse> gameDetails) {
        this.mContext = mContext;
        /*this.selectGameList = selectGameList;*/
        this.gameDetails = gameDetails;
        this.selectGameListener = (SelectGameListener) mContext;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout containerLayout;
        AppCompatTextView textViewGameName;
        ImageView imageViewGame;
        Button btnPlay;

        public ViewHolder(View itemView) {
            super(itemView);

            containerLayout = itemView.findViewById(R.id.containerLayout);

            textViewGameName = itemView.findViewById(R.id.textViewGameName);
            textViewGameName.setTypeface(Utils.getTypeFaceBodoni72(mContext));
            textViewGameName.getPaint().setShader(Utils.getTextGradient(new int[]{mContext.getResources().getColor(R.color.colorStartGold),
                    mContext.getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

            imageViewGame = itemView.findViewById(R.id.imageViewGame);

            /*btnPlay = itemView.findViewById(R.id.btnPlay);
            btnPlay.setTypeface(Utils.getTypeFaceBodoni72OS(mContext));
            btnPlay.getPaint().setShader(Utils.getTextGradient(new int[]{mContext.getResources().getColor(R.color.colorStartGold),
                    mContext.getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));
            btnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectGameListener != null) {
                        selectGameListener.onGameSelectListener(selectGameList.get(getAdapterPosition()));
                    }
                }
            });*/

            containerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectGameListener != null) {
                        selectGameListener.onGameSelectListener(gameDetails.get(getAdapterPosition()));
                    }
                }
            });

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_select_game, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewHolder viewHolder = (ViewHolder) holder;

        /*SelectGameModel selectGameModel = selectGameList.get(position);*/

        GameDetailsResponse gameDetail = gameDetails.get(position);

        viewHolder.textViewGameName.setText(gameDetail.getGameName());

        int resID = -1;
        if (gameDetail.getImageResourceName() != null && !gameDetail.getImageResourceName().isEmpty()) {
            resID = mContext.getResources().getIdentifier(gameDetail.getImageResourceName(), "drawable", mContext.getPackageName());
        }
        viewHolder.imageViewGame.setImageResource(resID);

    }

    @Override
    public int getItemCount() {
        return gameDetails.size();
    }
}
