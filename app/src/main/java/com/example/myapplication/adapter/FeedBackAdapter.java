package com.example.myapplication.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.hsalf.smilerating.SmileRating;

import java.util.ArrayList;

public class FeedBackAdapter extends ArrayAdapter {

    public static ArrayList dataSet, product_ratings;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView name;
        SmileRating rating;
    }

    public FeedBackAdapter(Context context, ArrayList data, ArrayList product_ratings) {
        super(context, R.layout.layout_feedback, data);
        this.dataSet = data;
        this.product_ratings = product_ratings;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        EntityResult dataModel = (EntityResult) getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.layout_feedback, parent, false);
            viewHolder.name = convertView.findViewById(R.id.tvQ1);
            viewHolder.rating = convertView.findViewById(R.id.ratingViewQ1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.rating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(int smiley, boolean reselected) {
                product_ratings.set(position, smiley);
                switch (smiley) {
                    case SmileRating.TERRIBLE:
                        Log.i("TAG", "Terrible");
                        break;
                    case SmileRating.BAD:
                        Log.i("TAG", "Bad");
                        break;
                    case SmileRating.NONE:
                        Log.i("TAG", "NONE");
                        break;
                    case SmileRating.OKAY:
                        Log.i("TAG", "Okay");
                        break;
                    case SmileRating.GOOD:
                        Log.i("TAG", "Good");
                        break;
                    case SmileRating.GREAT:
                        Log.i("TAG", "Great");
                        break;
                    case SmileRating.GREATT:
                        Log.i("TAG", "Greattt");
                        break;
                }
            }
        });

        viewHolder.name.setText("Q. " + dataSet.get(position));
        // int smiley_question_one = viewHolder.rating.getSelectedSmile();
        try {
//            viewHolder.rating.setSelectedSmile((Integer) product_ratings.get(position));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }
}