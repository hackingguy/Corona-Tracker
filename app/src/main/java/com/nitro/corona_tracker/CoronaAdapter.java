package com.nitro.corona_tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CoronaAdapter extends ArrayAdapter<Corona> {


    private ArrayList<Corona> mlist;
    private Context mContext;
    private View v;

    public CoronaAdapter(@NonNull Context context, @NonNull ArrayList<Corona> list) {
        super(context, 0, list);
        this.mlist = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        v = convertView;

        if (v == null) v = LayoutInflater.from(mContext).inflate(R.layout.data_list_item, null);

        TextView confirmed = v.findViewById(R.id.confirmed);
        TextView active = v.findViewById(R.id.active);
        TextView deceased = v.findViewById(R.id.deceased);
        TextView cured = v.findViewById(R.id.cured);

        ((TextView) v.findViewById(R.id.state)).setText(formatNumber(mlist.get(position).getState()));
        confirmed.setText(formatNumber(mlist.get(position).getPositive()));
        active.setText(formatNumber(mlist.get(position).getActive()));
        deceased.setText(formatNumber(mlist.get(position).getDeceased()));
        cured.setText(formatNumber(mlist.get(position).getCured()));

        return v;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    public static String formatNumber(String number) {
        try {
            return NumberFormat.getNumberInstance(Locale.getDefault()).format(Integer.parseInt(number));
        } catch (Exception e) {
            return number;
        }
    }

}
