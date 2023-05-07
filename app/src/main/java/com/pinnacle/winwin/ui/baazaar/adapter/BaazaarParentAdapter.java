package com.pinnacle.winwin.ui.baazaar.adapter;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.ui.baazaar.model.AllGamesModel;
import com.pinnacle.winwin.utils.Utils;

import java.util.ArrayList;

public class BaazaarParentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
//    ArrayList<BazaarDetailsResponse> bazaarDetails;
    ArrayList<AllGamesModel> allGames;
    private boolean inApproval = false;

    public BaazaarParentAdapter(Context mContext, /*ArrayList<BazaarDetailsResponse> bazaarDetails*/
                                ArrayList<AllGamesModel> allGames, boolean inApproval) {
        this.mContext = mContext;
//        this.bazaarDetails = bazaarDetails;
        this.allGames = allGames;
        this.inApproval = inApproval;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView textViewTitle;

        RecyclerView recyclerViewChild;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewTitle.setTypeface(Utils.getTypeFaceBodoni72(mContext));
            textViewTitle.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{mContext.getResources().getColor(R.color.colorStartGold),
                    mContext.getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

            recyclerViewChild = itemView.findViewById(R.id.recyclerViewChild);
            recyclerViewChild.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_baazaar_parent, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewHolder viewHolder = (ViewHolder) holder;

        AllGamesModel allGamesModel = allGames.get(position);

        viewHolder.textViewTitle.setText(allGamesModel.getHeaderTitle());

        loadChildRecyclerView(viewHolder, allGamesModel);

    }

    @Override
    public int getItemCount() {
        return allGames.size();
    }

    private void loadChildRecyclerView(ViewHolder viewHolder, AllGamesModel allGamesModel) {

        BaazaarChildAdapter baazaarChildAdapter = new BaazaarChildAdapter(mContext,
                (ArrayList) allGamesModel.getGenericGameList(), allGamesModel.getGenericId(), inApproval);
        viewHolder.recyclerViewChild.setAdapter(baazaarChildAdapter);
    }
}
