package com.example.myapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.model.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MessageAdapter extends BaseAdapter {

    List<Message> messages = new ArrayList<Message>();
    Context context;

    public MessageAdapter(Context context) {
        this.context = context;
    }


    public void add(Message message) {
        this.messages.add(message);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        MessageViewHolder holder = new MessageViewHolder();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Message message = messages.get(i);

        if (message.isBelongsToCurrentUser()) {
            convertView = messageInflater.inflate(R.layout.my_message, null);
            holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(holder);
            holder.messageBody.setText(message.getText());

            //get time
            Date date = new Date(message.getTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh.mm aa");
            String formattedDate = dateFormat.format(date).toString();
            holder.time.setText(formattedDate);
        } else {
            convertView = messageInflater.inflate(R.layout.their_message, null);
            holder.avatar = (View) convertView.findViewById(R.id.avatar);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
            convertView.setTag(holder);

            holder.messageBody.setText(message.getText());
            //get time
            Date date = new Date(message.getTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh.mm aa");
            String formattedDate = dateFormat.format(date).toString();
            holder.time.setText(formattedDate);

//            GradientDrawable drawable = (GradientDrawable) holder.avatar.getBackground();
//            drawable.setColor(Color.parseColor("#000"));
        }

        return convertView;
    }

}

class MessageViewHolder {
    public View avatar;
    public TextView time;
    public TextView messageBody;
}