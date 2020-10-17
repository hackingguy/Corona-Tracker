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

public class CoronaDistrictAdapter extends ArrayAdapter<Corona> {

    private ArrayList<Corona> districts_list;
    private Context mContext;

    public CoronaDistrictAdapter(@NonNull Context context, @NonNull ArrayList<Corona> arrayList) {
        super(context, 0, arrayList);
        districts_list=arrayList;
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if(v==null) v = LayoutInflater.from(mContext).inflate(R.layout.data_list_item,null);
        final TextView district = v.findViewById(R.id.state);
        final TextView confirmed = v.findViewById(R.id.confirmed);
        final TextView active = v.findViewById(R.id.active);
        final TextView deceased = v.findViewById(R.id.deceased);
        final TextView cured = v.findViewById(R.id.cured);

        district.setText(formatNumber(districts_list.get(position).getState()));
        confirmed.setText(formatNumber(districts_list.get(position).getPositive()));
        active.setText(formatNumber(districts_list.get(position).getActive()));
        deceased.setText(formatNumber(districts_list.get(position).getDeceased()));
        cured.setText(formatNumber(districts_list.get(position).getCured()));
        v.findViewById(R.id.new_confirmed).setVisibility(View.GONE);
        v.findViewById(R.id.new_deceased).setVisibility(View.GONE);
        v.findViewById(R.id.new_cured).setVisibility(View.GONE);
        return v;
    }

    @Override
    public int getCount() {
        return districts_list.size();
    }

    private static String formatNumber(String number) {
        try {
            return NumberFormat.getNumberInstance(Locale.getDefault()).format(Integer.parseInt(number));
        } catch (Exception e) {
            return number;
        }
    }
}
