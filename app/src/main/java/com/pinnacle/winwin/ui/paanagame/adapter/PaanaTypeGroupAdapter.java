package com.pinnacle.winwin.ui.paanagame.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.ui.paanagame.listener.PaanaTypeGroupListener;
import com.pinnacle.winwin.ui.paanagame.model.PaanaGroupModel;
import com.pinnacle.winwin.utils.Utils;

import java.util.ArrayList;

public class PaanaTypeGroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<PaanaGroupModel> paanaGroupList;
    private PaanaTypeGroupListener paanaTypeGroupListener;

    public PaanaTypeGroupAdapter(Context mContext, ArrayList<PaanaGroupModel> paanaGroupList) {
        this.mContext = mContext;
        this.paanaGroupList = paanaGroupList;
        paanaTypeGroupListener = (PaanaTypeGroupListener) mContext;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatCheckBox checkboxPaana;
        AppCompatTextView textViewPoints;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            checkboxPaana = itemView.findViewById(R.id.checkboxPaana);
            checkboxPaana.setTypeface(Utils.getTypeFaceBodoni72(mContext));
            checkboxPaana.getPaint().setShader(Utils.getTextGradient(new int[]{mContext.getResources().getColor(R.color.colorStartGold),
                    mContext.getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

            textViewPoints = itemView.findViewById(R.id.textViewPoints);
            textViewPoints.setTypeface(Utils.getTypeFaceBodoni72(mContext));

            checkboxPaana.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (paanaTypeGroupListener != null) {
                        paanaTypeGroupListener.onPaanaTypeCheckedChanged(getAdapterPosition());
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_paana_type, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewHolder viewHolder = (ViewHolder) holder;

        PaanaGroupModel paanaGroupModel = paanaGroupList.get(position);

        viewHolder.checkboxPaana.setText(paanaGroupModel.getPaanaType());

        if (paanaGroupModel.isSelected()) {
            viewHolder.checkboxPaana.setButtonDrawable(R.drawable.ic_check);
        } else {
            viewHolder.checkboxPaana.setButtonDrawable(R.drawable.ic_uncheck);
        }

        if (paanaGroupModel.isSelected() && paanaGroupModel.getPointsValue() > 0) {
            viewHolder.textViewPoints.setVisibility(View.VISIBLE);
            viewHolder.textViewPoints.setText(String.valueOf(paanaGroupModel.getPointsValue()));
        } else {
            viewHolder.textViewPoints.setText("");
            viewHolder.textViewPoints.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return paanaGroupList == null ? 0 : paanaGroupList.size();
    }

    public void updateData(ArrayList<PaanaGroupModel> paanaGroupList) {
        this.paanaGroupList = paanaGroupList;
        notifyDataSetChanged();
    }
}
