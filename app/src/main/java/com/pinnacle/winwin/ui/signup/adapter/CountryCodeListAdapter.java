package com.pinnacle.winwin.ui.signup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.network.model.CountryCodeData;
import com.pinnacle.winwin.ui.signup.listener.CountryCodeAdapterListener;
import com.pinnacle.winwin.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CountryCodeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<CountryCodeData> countryList;
    private CountryCodeAdapterListener countryCodeAdapterListener;

    public CountryCodeAdapterListener getCountryCodeAdapterListener() {
        return countryCodeAdapterListener;
    }

    public void setCountryCodeAdapterListener(CountryCodeAdapterListener countryCodeAdapterListener) {
        this.countryCodeAdapterListener = countryCodeAdapterListener;
    }

    public CountryCodeListAdapter(Context context, ArrayList<CountryCodeData> countryList) {
        this.context = context;
        this.countryList = countryList;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView textViewCountryName;
        AppCompatTextView textViewCountryCode;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            textViewCountryName = itemView.findViewById(R.id.textViewCountryName);
            textViewCountryName.setTypeface(Utils.getTypeFaceBodoni72(context));

            textViewCountryCode = itemView.findViewById(R.id.textViewCountryCode);
            textViewCountryCode.setTypeface(Utils.getTypeFaceBodoni72(context));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (countryCodeAdapterListener != null) {
                        countryCodeAdapterListener.onItemSelected(countryList.get(getAdapterPosition()));
                    }
                }
            });
        }
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_country_code, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {

        ViewHolder viewHolder = (ViewHolder) holder;

        CountryCodeData countryCodeData = countryList.get(position);

        viewHolder.textViewCountryName.setText(countryCodeData.getCountryName());
        viewHolder.textViewCountryCode.setText(countryCodeData.getCountryCode());
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public void updateData(ArrayList<CountryCodeData> countryList) {
        if (countryList != null) {
            this.countryList = countryList;
            notifyDataSetChanged();
        }
    }
}
