package com.kodimstudio.myapplication.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.kodimstudio.myapplication.R;
import com.kodimstudio.myapplication.model.Stat;

import java.util.List;

public class StatsListAdapter extends BaseAdapter {

    private final int layout;
    private final List<Stat> statsList;
    private final LayoutInflater inflater;

    public StatsListAdapter(@NonNull Context context, int layout, @NonNull List<Stat> statsList) {
        this.inflater = LayoutInflater.from(context);
        this.layout = layout;
        this.statsList = statsList;
    }

    @Override
    public int getCount() {
        return statsList.size();
    }

    @Override
    public Object getItem(int position) {
        return statsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        @SuppressLint("ViewHolder") View view = inflater.inflate(this.layout, parent, false);

        TextView name = (TextView) view.findViewById(R.id.stat_list_item_name);
        TextView value = (TextView) view.findViewById(R.id.stat_list_item_value);
        ConstraintLayout constraintLayout = (ConstraintLayout) view.findViewById(R.id.constraintLayout);

        Stat stat = statsList.get(position);

        name.setText(stat.name);
        value.setText(stat.value);

        return view;
    }
}
