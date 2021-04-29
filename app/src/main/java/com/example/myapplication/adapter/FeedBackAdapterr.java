package com.example.myapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.hsalf.smilerating.SmileRating;

import java.util.ArrayList;

public class FeedBackAdapterr extends BaseAdapter {

    private Context context_;
    private ArrayList<String> productitems;

    private int quantity = 1;


    public FeedBackAdapterr(Context context, ArrayList<String> productitems) {
        this.productitems = productitems;
        this.context_ = context;
    }

    @Override
    public int getCount() {
        return productitems.size();
    }

    @Override
    public Object getItem(int position) {
        return productitems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context_.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.layout_feedback, null);
        }
        TextView titleText = convertView.findViewById(R.id.tvQ1);
        SmileRating subtitleText = convertView.findViewById(R.id.ratingViewQ1);
        titleText.setText("Q. " + productitems.get(position));
        return convertView;
    }
}