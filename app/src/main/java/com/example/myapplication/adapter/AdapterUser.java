//package com.example.myapplication.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.myapplication.R;
//import com.example.myapplication.model.LeaveModel;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class AdapterUser extends RecyclerView.Adapter<AdapterUser.ViewHolder> {
//
//    public ArrayList<LeaveModel> userList = new ArrayList<>();
//    private Context context;
//
//    public AdapterUser(List<LeaveModel> feedList, Context context) {
//        this.userList.addAll(feedList);
//        this.context = context;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.list_item_leave, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        final LeaveModel userModel = userList.get(position);
//        try {
//            if (userModel != null) {
//                holder.ivThumb.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        try {
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return userList.size();
//    }
//
//    public void refresh(@NotNull Object userModelList) {
//
//    }
//
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//
//        private ImageView ivThumb;
//        private TextView tvName;
//        private View iv_circle;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            ivThumb = itemView.findViewById(R.id.ivThumb);
//            tvName = itemView.findViewById(R.id.tvName);
//            iv_circle = itemView.findViewById(R.id.iv_circle);
//        }
//    }
//}