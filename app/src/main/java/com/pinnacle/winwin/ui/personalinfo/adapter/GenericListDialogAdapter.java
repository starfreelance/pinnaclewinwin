package com.pinnacle.winwin.ui.personalinfo.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.ui.kyc.model.AccountType;
import com.pinnacle.winwin.ui.personalinfo.listener.GenericListAdapterListener;
import com.pinnacle.winwin.ui.personalinfo.model.SelectImageOption;
import com.pinnacle.winwin.utils.Utils;

import java.util.ArrayList;

public class GenericListDialogAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = GenericListDialogAdapter.class.getSimpleName();

    private Context mContext;
    private ArrayList<SelectImageOption> selectImageOptions;
    private ArrayList<AccountType> accountTypes;
    private GenericListAdapterListener adapterListener;
    private int dialogType = -1;

    public GenericListAdapterListener getAdapterListener() {
        return adapterListener;
    }

    public void setAdapterListener(GenericListAdapterListener adapterListener) {
        this.adapterListener = adapterListener;
    }

    public GenericListDialogAdapter(Context mContext, ArrayList<T> dataList, int dialogType) {
        this.mContext = mContext;
        if (dataList != null && !dataList.isEmpty()) {
            if (dataList.get(0) instanceof SelectImageOption) {
                selectImageOptions = (ArrayList<SelectImageOption>) dataList;
            } else if (dataList.get(0) instanceof AccountType) {
                accountTypes = (ArrayList<AccountType>) dataList;
            }
        }
        this.dialogType = dialogType;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        private AppCompatRadioButton radioBtnGeneric;

        private AppCompatTextView textViewTitle;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            radioBtnGeneric = itemView.findViewById(R.id.radioBtnGeneric);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewTitle.setTypeface(Utils.getTypeFaceBodoni72(mContext));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getAdapterPosition() != -1) {
                        if (adapterListener != null) {
                            adapterListener.onAdapterItemSelected(getAdapterPosition());
                        }
                    }
                }
            });

            radioBtnGeneric.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemView.performClick();
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_title_with_radio_btn, viewGroup, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        ViewHolder holder = (ViewHolder) viewHolder;

        switch (dialogType) {
            case AppConstant.SELECT_IMAGE_TYPE_DIALOG:
                SelectImageOption selectImageOption = selectImageOptions.get(position);

                holder.textViewTitle.setText(selectImageOption.getSelectImageTitle());

                if (selectImageOption.isSelected()) {
                    holder.radioBtnGeneric.setChecked(true);
                } else {
                    holder.radioBtnGeneric.setChecked(false);
                }
                break;
            case AppConstant.SELECT_ACCOUNT_TYPE_DIALOG:
                AccountType accountType = accountTypes.get(position);

                holder.textViewTitle.setText(accountType.getTitle());

                if (accountType.isSelected()) {
                    holder.radioBtnGeneric.setChecked(true);
                } else {
                    holder.radioBtnGeneric.setChecked(false);
                }
                break;
        }

    }

    @Override
    public int getItemCount() {
        switch (dialogType) {
            case AppConstant.SELECT_IMAGE_TYPE_DIALOG:
                return selectImageOptions.size();
            case AppConstant.SELECT_ACCOUNT_TYPE_DIALOG:
                return accountTypes.size();
        }
        return 0;
    }

    public void updateData(ArrayList<T> dataList) {
        switch (dialogType) {
            case AppConstant.SELECT_IMAGE_TYPE_DIALOG:
                this.selectImageOptions = (ArrayList<SelectImageOption>) dataList;
                break;
            case AppConstant.SELECT_ACCOUNT_TYPE_DIALOG:
                this.accountTypes = (ArrayList<AccountType>) dataList;
                break;
        }
        notifyDataSetChanged();

    }
}
