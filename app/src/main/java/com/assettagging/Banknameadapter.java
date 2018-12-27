package com.assettagging;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.assettagging.model.user_tracking.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Banknameadapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    private List<Location> banklist = null;
    private ArrayList<Location> arraylist;

    public Banknameadapter(Context context, List<Location> banklist) {

        mContext = context;
        this.banklist = banklist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Location>();
        this.arraylist.addAll(banklist);
    }

    public class ViewHolder {
        TextView bankname;
    }

    @Override
    public int getCount() {
        return banklist.size();
    }

    @Override
    public Location getItem(int position) {
        return banklist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;

            holder = new ViewHolder();
            view = inflater.inflate(R.layout.bankname_adapter, null);
            // Locate the TextViews in listview_item.xml
            holder.bankname = view.findViewById(R.id.tv_banknameadapter);
            view.setTag(holder);
         if (banklist.get(position).getLocationName().length() > 0)
                holder.bankname.setText(banklist.get(position).getLocationName());

        return view;
    }

    String searchString = "";

    public void filter(String charText) {
        searchString = charText;

        charText = charText.toLowerCase(Locale.getDefault());
        banklist.clear();
        if (charText.length() == 0) {
            banklist.addAll(arraylist);
        } else {
            for (Location wp : arraylist) {
                if (wp.getLocationName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    banklist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
