package com.example.tipid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class adapter_dueshistory extends ArrayAdapter<dataclass_dueshistory> {

    Context context;
    List<dataclass_dueshistory> dataclassDuesHistory;

    public adapter_dueshistory(@NonNull Context context, List<dataclass_dueshistory>dataclass_DuesHistoryList) {
        super(context, R.layout.data_item_dueshistory_record, dataclass_DuesHistoryList);

        this.context = context;
        this.dataclassDuesHistory = dataclass_DuesHistoryList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_item_dueshistory_record, null, true);

        TextView tvDuesHistoryName = view.findViewById(R.id.tvDuesHistoryName);
        TextView tvDuesHistoryCost = view.findViewById(R.id.tvDuesHistoryCost);
        TextView tvDuesHistoryDate = view.findViewById(R.id.tvDuesHistoryDate);

        tvDuesHistoryName.setText(dataclassDuesHistory.get(position).getName());
        tvDuesHistoryCost.setText(dataclassDuesHistory.get(position).getCost());
        tvDuesHistoryDate.setText(dataclassDuesHistory.get(position).getDhdate());

        return view;
    }
}