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

public class adapter_history extends ArrayAdapter<dataclass_history> {

    Context context;
    List<dataclass_history> dataclassHistory;

    public adapter_history(@NonNull Context context, List<dataclass_history>dataclass_HistoryList) {
        super(context, R.layout.data_item_history_record, dataclass_HistoryList);

        this.context = context;
        this.dataclassHistory = dataclass_HistoryList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_item_history_record, null, true);

        TextView tvHistoryName = view.findViewById(R.id.tvHistoryName);
        TextView tvHistoryCost = view.findViewById(R.id.tvHistoryCost);
        TextView tvHistoryDate = view.findViewById(R.id.tvHistoryDate);

        tvHistoryName.setText(dataclassHistory.get(position).getName());
        tvHistoryCost.setText(dataclassHistory.get(position).getCost());
        tvHistoryDate.setText(dataclassHistory.get(position).getHdate());

        return view;
    }
}